import React, { useEffect, useState } from 'react';
import BottomBar from './BottomBar';
import LanguagePicker from './LanguagePicker';
import './MainApp.css';
import GenrePicker from "./GenrePicker";

function MainApp() {
    const [isLangSiteHidden, setIsLangSiteHidden] = useState(false);

    const [selectedLanguage, setSelectedLanguage] = useState(null);
    useEffect(() => {
        console.log(`Selected language: ${selectedLanguage}`);
    }, [selectedLanguage]);

    const [genreImagesJSON, setGenreImagesJSON] = useState(null);
    useEffect(() => {
        console.log(genreImagesJSON);
    }, [genreImagesJSON]);

    const [delayed, setDelayed] = useState(false);

    const handleLanguageSelection = (language) => {
        setSelectedLanguage(language);
        setIsLangSiteHidden(true);
        setTimeout(() => {
            setDelayed(true);
        }, 2000);
    };

    return (
        <div className={`MainApp ${isLangSiteHidden ? 'lang-hidden' : ''}`}>
            <div className={`content-wrapper ${isLangSiteHidden ? 'hidden' : ''}`}>
                {!delayed && (
                    <LanguagePicker
                        selectedLanguage={selectedLanguage}
                        setSelectedLanguage={handleLanguageSelection}
                        genreImagesJSON={genreImagesJSON}
                        setGenreImagesJSON={setGenreImagesJSON}
                    />
                )}
            </div>
            <div className={`genre-wrapper ${delayed && isLangSiteHidden ? '' : 'before'}`}>
                {delayed && (
                    <GenrePicker genreImagesJSON={genreImagesJSON}/>
                )}
            </div>
            <BottomBar/>
        </div>
    )
}
export default MainApp;