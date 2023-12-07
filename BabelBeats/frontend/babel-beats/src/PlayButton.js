import axios from "axios";
import React, { useState, useEffect } from 'react';
import {useLocation} from 'react-router-dom';
import "./PlayButton.css"
const PlayButton = () => {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);

    const userToken = searchParams.get('userToken');
    const refreshToken = searchParams.get('refreshToken');
    const [isPlaying, setIsPlaying] = useState(false);
    const play = async() => {
        try {
                if(!isPlaying) {
                    const response = await axios.get(`http://localhost:8080/api/resume?userToken=${userToken}&refreshToken=${refreshToken}`);
                    console.log("Resume playback");
                }
                else {
                    const response = await axios.get(`http://localhost:8080/api/pause?userToken=${userToken}&refreshToken=${refreshToken}`);
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
        <div className="bottomBar">
            <button className="playButton" onClick={() => setIsPlaying(prevIsPlaying => !prevIsPlaying)}></button>
        </div>
    );
};

export default PlayButton;