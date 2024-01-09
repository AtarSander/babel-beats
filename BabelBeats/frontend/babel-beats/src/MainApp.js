import React, { useEffect, useState } from 'react';
import BottomBar from './BottomBar';
import LanguagePicker from './LanguagePicker';
import './MainApp.css';
import GenrePicker from "./GenrePicker";
import axios from "axios";
import {useLocation} from "react-router-dom";

function MainApp() {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const [userToken, setUserToken] = useState(searchParams.get('userToken'));
    const [refreshToken, setRefreshToken] = useState(searchParams.get('refreshToken'));
    const [isLangSiteHidden, setIsLangSiteHidden] = useState(false);
    const [appState, setAppState] = useState(0);


    const [selectedLanguage, setSelectedLanguage] = useState(null);
    useEffect(() => {
        if (selectedLanguage != null)
            console.log(`Selected language: ${selectedLanguage}`);
    }, [selectedLanguage]);

    const [genreImagesJSON, setGenreImagesJSON] = useState(null);
    useEffect(() => {
        if (genreImagesJSON != null)
            console.log(genreImagesJSON);
    }, [genreImagesJSON]);

    const [selectedGenre, setSelectedGenre] = useState(null);
    useEffect(() => {
        async function sendRequest() {
            const response = await axios.get(`http://localhost:8080/api/loadRecommendedSong?userToken=${userToken}&refreshToken=${refreshToken}&genre=${selectedGenre}&language=${selectedLanguage}`,);
            setAppState(5);
            setTimeout(() => {
                setAppState(6);
            }, 2000);
            console.log(`Recommended song json: ${response}`)
        }
        if (selectedGenre != null) {
            sendRequest();
            setAppState(3);
            setTimeout(() => {
                setAppState(4);
            }, 2000);
        }
    }, [selectedGenre]);

    const [delayed, setDelayed] = useState(false);

    const handleLanguageSelection = (language) => {
        setSelectedLanguage(language);
        setAppState(1);
        setTimeout(() => {
            setAppState(2);
        }, 2000);
    };

    return (
        <div className={`MainApp`}>
            <div className={`content-wrapper ${appState === 0 ? '' : 'hidden'}`}>
                {appState < 2 && (
                    <LanguagePicker
                        setSelectedLanguage={handleLanguageSelection}
                        setGenreImagesJSON={setGenreImagesJSON}
                        userToken={userToken}
                        refreshToken={refreshToken}
                    />
                )}
            </div>
            <div className={`genre-wrapper ${appState === 1 ? 'before' : appState >= 3 ? 'hidden' : ''}`}>
                {(appState === 2 || appState === 3) && (
                    <GenrePicker
                        genreImagesJSON={genreImagesJSON}
                        setSelectedGenre={setSelectedGenre}
                    />
                )}
            </div>
            <div className={`wait-wrapper ${appState === 3 ? 'before': appState >= 7 && appState < 3 ? 'hidden' : ''}`}>
                    <h1 className={"waitTitle"}>YOUR MUSIC IS BEING PREPARED!</h1>
                {appState === 5 || appState === 6 && <button className={"musicReadyButton"}>Ready</button>}
            </div>
            <BottomBar userToken={userToken} refreshToken={refreshToken}/>
        </div>
    );
}
export default MainApp;