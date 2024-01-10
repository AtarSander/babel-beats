import React, {useEffect, useState} from "react";
import './SyncedText.css';


function SyncedText({ songData, isPlaying, songPosition }) {
    const [text, setText] = useState('');
    let songTitle = songData.title.replace(/_/g, " ");
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

    function formattedTime(){
        let currTime = songPosition + passedTime;
        let seconds = Math.floor(currTime/1000) % 60
        return Math.floor(currTime/60000) + (seconds < 10 ? ":0" : ":") + seconds;
    }
    return (
        <>
            <div className={"title-wrapper"}>
                <h1 className={"timer"}>{formattedTime()}</h1>
                <h1 className={"songTitle"}>{songTitle}</h1>
            </div>
            <div className={"lyricsWrapper"}>
                <div className={"originalLyrics"}>
                    <h1 className={"prevLine"}>{prevLine}</h1>
                    <h1 className={"currLine"}>{currLine}</h1>
                </div>
                <div className={"translatedLyrics"}>
                    <h1 className={"prevLine"}>{prevLine}</h1>
                    <h1 className={"currLine"}>{currLine}</h1>
                </div>
            </div>
        </>);
}

                export default SyncedText;