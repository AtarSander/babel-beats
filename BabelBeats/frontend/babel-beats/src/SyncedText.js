import React, {useEffect, useState} from "react";
import './SyncedText.css';
import axios from "axios";


function SyncedText({ songData, isPlaying, songPosition }) {
    const [text, setText] = useState('');
    let songTitle = songData.title.replace(/_/g, " ");
    let timestamps = songData.timestamps;
    // console.log(timestamps);
    let translated = songData.timestampsTranslated;
    const [startTime, setStartTime] = useState(new Date().getTime());
    const [passedTime, setPassedTime] = useState(0);
    const [currLine, setCurrLine] = useState("");
    const [prevLine, setPrevLine] = useState("");
    const [prevPrevLine, setPrevPrevLine] = useState("");

    const [currTransLine, setCurrTransLine] = useState("");
    const [prevTransLine, setPrevTransLine] = useState("");
    const [prevPrevTransLine, setPrevPrevTransLine] = useState("");

    async function blacklist() {
        const sendRequest = async() => {
            try {
                let response = await axios.get(`http://localhost:8080/api/blacklist?title=${songTitle.replace(" ", "_")}`);
                console.log("Blacklisted ", songTitle.replace(" ", "_"));
            } catch (error) {
                console.log('Error blacklisting');
            }
        }

        sendRequest();
    }


    function currentLyric() {
        // console.log(songData.length);
        for (let i = 0; i < timestamps.length; i++) {
            if (songPosition + passedTime < timestamps[i].value) {
                setPrevPrevLine(i === 0 || i === 1 ? "" : timestamps[i - 2].key);
                setPrevLine((i === 0) ? "" : timestamps[i-1].key);
                setCurrLine(timestamps[i].key);

                setPrevPrevTransLine(i === 0 || i === 1 ? "" : translated[i - 2].key);
                setPrevTransLine((i === 0) ? "" : translated[i-1].key);
                setCurrTransLine(translated[i].key);
                break;
            }
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
                    <h1 className={"prevPrevLine"}>{prevPrevLine}</h1>
                    <h1 className={"prevLine"}>{prevLine}</h1>
                    <h1 className={"currLine"}>{currLine}</h1>
                </div>
                <div className={"translatedLyrics"}>
                    <h1 className={"prevPrevLine"}>{prevPrevTransLine}</h1>
                    <h1 className={"prevLine"}>{prevTransLine}</h1>
                    <h1 className={"currLine"}>{currTransLine}</h1>
                </div>
            </div>
            <button className={"blackList"} onClick={blacklist}>Blacklist this song!</button>

        </>);
}

export default SyncedText;