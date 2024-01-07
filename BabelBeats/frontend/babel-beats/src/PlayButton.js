import axios from "axios";
import React, { useState, useEffect } from 'react';
import {useLocation} from 'react-router-dom';
import "./PlayButton.css"

const getPlaybackState = async(userToken, refreshToken) => {
    try {
        return await axios.get(`http://localhost:8080/api/getPlaybackState?userToken=${userToken}&refreshToken=${refreshToken}`);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
};
function PlayButton() {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const userToken = searchParams.get('userToken');
    const refreshToken = searchParams.get('refreshToken');
    const [isPlaying, setIsPlaying] = useState(getPlaybackState(userToken, refreshToken));
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

    useEffect(() => {
        play();
    }, [isPlaying]);

    return (
            <button className="playButton" onClick={() => setIsPlaying(prevIsPlaying => !prevIsPlaying)}>Play/Pause</button>
    );
}

export default PlayButton;