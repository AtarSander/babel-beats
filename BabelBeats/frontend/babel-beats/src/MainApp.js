import React, { useEffect, useState } from 'react';
import BottomBar from './BottomBar';
import LanguagePicker from './LanguagePicker';
import './MainApp.css';

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

    const handleLanguageSelection = (language) => {
        setSelectedLanguage(language);
        setIsLangSiteHidden(true);
    };

    return (
        <div className="MainApp">
            <div className={`content-wrapper ${isLangSiteHidden ? 'hidden' : ''}`}>
                    <LanguagePicker
                        selectedLanguage={selectedLanguage}
                        setSelectedLanguage={handleLanguageSelection}
                        genreImagesJSON={genreImagesJSON}
                        setGenreImagesJSON={setGenreImagesJSON}
                    />
            </div>
                <BottomBar />
        </div>
    );
}

export default MainApp;
