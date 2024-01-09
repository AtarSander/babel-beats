import React, {useEffect } from 'react';
import { SlActionUndo, SlActionRedo } from "react-icons/sl";
import axios from "axios";
import "./SkipButton.css";

function SkipButton({direction, userToken, refreshToken}) {
    let isForward = direction === "forward";
    let timeDiff = 15000;

    const sendRequest = async() => {
        try {
            if (isForward) {
                await axios.get(`http://localhost:8080/api/seekPosition?userToken=${userToken}&refreshToken=${refreshToken}&timeDiff=${timeDiff.toString()}`);
                console.log('Forwards 15s')
            } else {
                await axios.get(`http://localhost:8080/api/seekPosition?userToken=${userToken}&refreshToken=${refreshToken}&timeDiff=-${timeDiff.toString()}`);
                console.log('Backwards 15s')
            }
        } catch (e) {
            console.error('Error seeking audio position: ', e)
        }
    }

    useEffect(() => {
        sendRequest();
    }, [isForward]);

    return (
        <>
            <button className="skipButton" onClick={() => sendRequest()}>{isForward ? <SlActionRedo/> : <SlActionUndo/>}</button>
        </>

    )
}

export default SkipButton;