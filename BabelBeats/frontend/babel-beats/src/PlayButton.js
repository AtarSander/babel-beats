import axios from "axios";
import React, {useEffect, useRef} from 'react';
import {SlControlPause, SlControlPlay} from "react-icons/sl";
import "./PlayButton.css";


function PlayButton({ userToken, refreshToken, isPlaying, setIsPlaying, setSongPosition}) {
    const isFirstRender = useRef(true);
    async function handleSongPosition() {
        const sendRequest = async() => {
            try {
                let response = await axios.get(`http://localhost:8080/api/getSongPosition?userToken=${userToken}&refreshToken=${refreshToken}`);
                console.log(response.data);
                return response.data;
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        }
        setSongPosition(await sendRequest());
    }

    const play = async() => {
        try {
            if(!isPlaying) {
                await axios.get(`http://localhost:8080/api/resume?userToken=${userToken}&refreshToken=${refreshToken}`);
                console.log("Resume playback");
            }
            else {
                await axios.get(`http://localhost:8080/api/pause?userToken=${userToken}&refreshToken=${refreshToken}`);
                console.log("Pause playback");
            }
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    async function getPlaybackState() {
        const playbackState = async (userToken, refreshToken) => {
            try {
                let response = await axios.get(`http://localhost:8080/api/getPlaybackState?userToken=${userToken}&refreshToken=${refreshToken}`);
                return response.data;
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        return await playbackState(userToken, refreshToken);
    }

    const handleClick = async () => {
        await handleSongPosition();
        await play();
        let newState = await getPlaybackState(userToken, refreshToken);
        setIsPlaying(newState);
    };

    useEffect(() => {
        const fetchData = async () => {
            if (isPlaying === null && isFirstRender.current) {
                let newState = await getPlaybackState(userToken, refreshToken);
                setIsPlaying(newState);
                isFirstRender.current = false;
            }
        };
        fetchData();
    }, []);

    return (
        <button className="playButton" onClick={() => handleClick()}>
            {!isPlaying ? <SlControlPlay/> : <SlControlPause/>}
        </button>
    );
}

export default PlayButton;