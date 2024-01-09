import axios from "axios";
import React, { useState, useEffect } from 'react';
import { SlControlPlay, SlControlPause } from "react-icons/sl";
import "./PlayButton.css";

const getPlaybackState = async(userToken, refreshToken) => {
    try {
        return await axios.get(`http://localhost:8080/api/getPlaybackState?userToken=${userToken}&refreshToken=${refreshToken}`);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
};
function PlayButton({ userToken, refreshToken }) {
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
        <button className="playButton" onClick={() => setIsPlaying(prevIsPlaying => !prevIsPlaying)}>
            {isPlaying ? <SlControlPlay/> : <SlControlPause/>}
        </button>
    );
}

export default PlayButton;