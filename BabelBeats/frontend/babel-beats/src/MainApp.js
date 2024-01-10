import React, { useEffect, useState } from 'react';
import BottomBar from './BottomBar';
import LanguagePicker from './LanguagePicker';
import './MainApp.css';
import GenrePicker from "./GenrePicker";
import axios from "axios";
import {useLocation} from "react-router-dom";
import SyncedText from "./SyncedText";

function MainApp() {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const [userToken, setUserToken] = useState(searchParams.get('userToken'));
    const [refreshToken, setRefreshToken] = useState(searchParams.get('refreshToken'));
    const [isLangSiteHidden, setIsLangSiteHidden] = useState(false);
    const [appState, setAppState] = useState(0);
    const [isPlaying, setIsPlaying] = useState(null);
    const [songPosition, setSongPosition] = useState(0);
    const [songData, setSongData] = useState(null);

    function appStateTransition(firstValue, seconds=1) {
        setAppState(firstValue);
        setTimeout(() => {
            setAppState(firstValue + 1);
        }, seconds * 1000);
    }

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
            appStateTransition(5);
            console.log(`Recommended song json: `, response.data)
            setSongData(response.data);
        }
        if (selectedGenre != null) {
            sendRequest();
            appStateTransition(3);
        }
    }, [selectedGenre]);

    const [delayed, setDelayed] = useState(false);

    const handleLanguageSelection = (language) => {
        setSelectedLanguage(language);
        appStateTransition(1);
    };

    return (
        <div className={`MainApp`}>
            <img className={"logo"} src={"babelBeats.png"}  alt={"babelBeats"} />
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
            <div className={`wait-wrapper ${appState === 3 ? 'before' : appState >= 7 ? 'hidden' : ''}`}>
                {(appState >= 3 && appState <= 7) && (
                    <h1 className={"waitTitle"}>YOUR MUSIC IS BEING PREPARED!</h1>
                )}
                {appState >= 5 && appState <= 7 && <button className={`musicReadyButton ${appState === 5 ? 'before' : ''}`}
                                                           onClick={() => appStateTransition(7)}>Ready</button>}
            </div>
            <div className={`syncedText-wrapper ${appState === 7 ? 'before' : appState >= 9 ? 'hidden' : ''}`}>
                {appState >= 7 && <SyncedText songData={songData} isPlaying={isPlaying} songPosition={songPosition}/>}
            </div>
            <BottomBar userToken={userToken} refreshToken={refreshToken} isPlaying={isPlaying} setIsPlaying={setIsPlaying} songPosition={songPosition} setSongPosition={setSongPosition}/>
        </div>
    );
}

export default MainApp;