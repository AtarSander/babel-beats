import React, {useEffect} from 'react';
import axios from "axios";
import React, { useState, useEffect } from 'react';

const PlayButton = () => {
    const play = () => {
        const [isPlaying, setIsPlaying] = useState(false);
        var isPlaying = false;
        setIsPlaying(prevState => !prevState);
        useEffect(() => {
            const fetchData = async () =>{
            try {
                if(!isPlaying)
                    const response = await axios.get('http://localhost:8080/api/resume');
                else
                    const response = await axios.get('http://localhost:8080/api/pause');
                // isPlaying = !isPlaying;
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
        }, []);
    }
}