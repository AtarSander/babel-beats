import axios from "axios";
import React, { useState, useEffect } from 'react';

const PlayButton = () => {
    const [isPlaying, setIsPlaying] = useState(false);
    const play = async() => {
            try {
                if(!isPlaying) {
                    const response = await axios.get('http://localhost:8080/api/resume');
                    console.log("Resume playback");
                }
                else {
                    const response = await axios.get('http://localhost:8080/api/pause');
                    console.log("Pause playback");
                }
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

    useEffect(() => {
        play();
    }, [isPlaying]);

    return(
    <div>
        <button className="playButton" onClick={e => {
        setIsPlaying(!isPlaying);
        play();
    }}></button>
    </div>
    );
};

export default PlayButton;