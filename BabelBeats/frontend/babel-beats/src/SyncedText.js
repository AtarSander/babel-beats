import React, {useEffect, useState} from "react";
import axios from "axios";

function SyncedText({ songData, isPlaying, songPosition }) {
    const [text, setText] = useState('');
    let songTitle = songData.title;
    let timestamps = songData.timestamps;
    // console.log(timestamps);
    let translated = songData.translatedTimestamps;
    const [startTime, setStartTime] = useState(new Date().getTime());
    const [passedTime, setPassedTime] = useState(0);
    const [currLine, setCurrLine] = useState("");
    const [prevLine, setPrevLine] = useState("");

    function currentLyric() {
        // console.log(songData.length);
        for (let i = 0; i < timestamps.length; i++) {
            if (songPosition + passedTime < timestamps[i].value) {
                setPrevLine((i === 0) ? "": timestamps[i-1].key);
                setCurrLine(timestamps[i].key);
                // console.log(timestamps[i].value);
                console.log(currLine);
                break;
            }
            // console.log(timestamps[i]);
        }

    }

    useEffect(() => {
        let intervalID = 0;
        if(isPlaying) {
            intervalID = setInterval(() => {
                setPassedTime(getPassedTime());
                currentLyric();
            }, 10)
            return () => clearInterval(intervalID);
        }
    }, [passedTime, isPlaying])

    useEffect(() => {
        resetTimer();
    }, [songPosition])


    function resetTimer() {
        setStartTime(new Date().getTime());
    }

    function getPassedTime(){
        return new Date().getTime() - startTime;
    }

    return (
        <div>
            <h1>{songPosition + passedTime}</h1>
            <h1>{songTitle}</h1>
            <h1>{prevLine}</h1>
            <h1>{currLine}</h1>
        </div>
    );
}

export default SyncedText;