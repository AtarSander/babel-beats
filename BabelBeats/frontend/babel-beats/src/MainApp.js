import React, { useEffect, useState } from 'react';
import BottomBar from './BottomBar';
import LanguagePicker from './LanguagePicker';
import './MainApp.css';
import GenrePicker from "./GenrePicker";
import axios from "axios";
import {useLocation} from "react-router-dom";
import SyncedText from "./SyncedText";
import Logo from "./Logo";

function MainApp() {
    let languages = ["Spanish", "Italian", "Korean", "English", "Portuguese", "German", "Chinese", "French"];
    let userLanguages = ["Spanish", "Italian", "Korean", "English", "Portuguese", "German", "Chinese", "French", "Ukrainian", "Slovak", "Slovenian", "Polish", "Turkish", "Swedish", "Greek", "Czech", "Bulgarian", "Danish", "Estonian", "Latvian", "Lithuanian", "Dutch", "Romanian", "Hungarian", "Norwegian", "Indonesian", "Japanese"];

    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const [userToken, setUserToken] = useState(searchParams.get('userToken'));
    const [refreshToken, setRefreshToken] = useState(searchParams.get('refreshToken'));
    const [isLangSiteHidden, setIsLangSiteHidden] = useState(false);
    const [appState, setAppState] = useState(0);
    const [isPlaying, setIsPlaying] = useState(null);
    const [songPosition, setSongPosition] = useState(0);
    const [songData, setSongData] = useState(null);

    const [selectedTargetLanguage, setSelectedTargetLanguage] = useState(null);
    const [selectedUserLanguage, setSelectedUserLanguage] = useState(null);

    const [targetLangChosen, setTargetLangChosen] = useState(null);
    const [userLangChosen, setUserLangChosen] = useState(null);

    function appStateTransition(firstValue, seconds=1) {
        setAppState(firstValue);
        setTimeout(() => {
            setAppState(firstValue + 1);
        }, seconds * 1000);
    }

    useEffect(() => {
        if (selectedTargetLanguage != null)
            console.log(`Selected target language: ${selectedTargetLanguage}`);
    }, [selectedTargetLanguage]);

    const [genreImagesJSON, setGenreImagesJSON] = useState(null);
    useEffect(() => {
        if (genreImagesJSON != null)
            console.log("Genre images JSON: ", genreImagesJSON);
    }, [genreImagesJSON]);

    const [selectedGenre, setSelectedGenre] = useState(null);
    useEffect(() => {
        async function sendRequest() {
            const response = await axios.get(`http://localhost:8080/api/loadRecommendedSong?userToken=${userToken}&refreshToken=${refreshToken}&genre=${selectedGenre}&targetLang=${selectedTargetLanguage}&userLang=${selectedUserLanguage}`);
            appStateTransition(7);

            console.log(`Recommended song json: `, response.data)
            setSongData(response.data);
        }
        if (selectedGenre != null) {
            sendRequest();
            appStateTransition(5);
        }
    }, [selectedGenre]);

    const handleTargetLangSelection = (language) => {
        setSelectedTargetLanguage(language);
        appStateTransition(3);
    };

    useEffect(() => {
        const sendGenreRequest = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/recommendGenres?userToken=${userToken}&refreshToken=${refreshToken}`);
                return response.data;
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        const fetchData = async () => {
            const genreImagesData = await sendGenreRequest();
            setGenreImagesJSON(genreImagesData);
        };

        if (targetLangChosen != null) {
            fetchData();
        }
    }, [targetLangChosen]);

    useEffect(() => {
        if (selectedUserLanguage != null)
            console.log(`Selected user language: ${selectedUserLanguage}`);
    }, [selectedUserLanguage]);

    const handleUserLangSelection = (language) => {
        setSelectedUserLanguage(language);
        appStateTransition(1);
    }

    console.log(appState);

    return (
        <div className={`MainApp`}>
            <Logo isPlaying={isPlaying}/>
            <div className={`content-wrapper ${appState === 0 ? '' : 'hidden'}`}>
                {appState < 2 && (
                    <LanguagePicker
                        setSelectedLanguage={handleUserLangSelection}
                        setFlag={setUserLangChosen}
                        title={"What language do you speak?"}
                        languages={userLanguages}
                        langListElementCSS={"userLangListElement"}
                        imageWidth={150}
                        imageHeight={100}
                    />
                )}
            </div>

            <div className={`content-wrapper ${appState === 1 ? 'before' : appState >= 3 ? 'hidden' : ''}`}>
                {appState >= 2 && appState <= 3 && (
                    <LanguagePicker
                        setSelectedLanguage={handleTargetLangSelection}
                        setFlag={setTargetLangChosen}
                        title={"What language do you want to learn?"}
                        languages={languages}
                        langListElementCSS={"targetLangListElement"}
                        imageWidth={300}
                        imageHeight={200}
                    />
                )}
            </div>

            <div className={`genre-wrapper ${appState === 3 ? 'before' : appState >= 5 ? 'hidden' : ''}`}>
                {(appState === 4 || appState === 5) && (
                    <GenrePicker
                        genreImagesJSON={genreImagesJSON}
                        setSelectedGenre={setSelectedGenre}
                    />
                )}
            </div>
            <div className={`wait-wrapper ${appState === 5 ? 'before' : appState >= 9 ? 'hidden' : ''}`}>
                {(appState >= 5 && appState <= 9) && (
                    <h1 className={"waitTitle"}>YOUR MUSIC IS BEING PREPARED!</h1>
                )}
                {appState >= 7 && appState <= 9 &&
                    <button className={`musicReadyButton ${appState === 7 ? 'before' : ''}`}
                            onClick={() => appStateTransition(9)}>Ready</button>}
            </div>
            <div className={`syncedText-wrapper ${appState === 9 ? 'before' : appState >= 11 ? 'hidden' : ''}`}>
                {appState >= 9 && <SyncedText songData={songData} isPlaying={isPlaying} songPosition={songPosition}/>}
            </div>
            <BottomBar userToken={userToken} refreshToken={refreshToken} isPlaying={isPlaying}
                       setIsPlaying={setIsPlaying} songPosition={songPosition} setSongPosition={setSongPosition}/>
        </div>
    );
}

export default MainApp;