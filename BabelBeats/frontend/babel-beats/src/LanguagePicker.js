import React from 'react';
import "./LanguagePicker.css";
import {useLocation} from "react-router-dom";
import axios from "axios";

function LanguagePicker({setSelectedLanguage, setGenreImagesJSON, userToken, refreshToken }) {
    let languages = ["Spanish", "Italian", "Korean", "English", "Portuguese", "German", "Chinese", "French"];

    const sendRequest = async (userToken, refreshToken) => {
        try {
            const response = await axios.get(`http://localhost:8080/api/recommendGenres?userToken=${userToken}&refreshToken=${refreshToken}`, );
            return response.data;
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    }

    return (
        <>
            <h1 className={"languageTitle"}>Pick a language:</h1>
            <div className={"languageList"}>
                {languages.map((value, index) => (
                    <div key={index} className={`languageListElement`} onClick={async () => {
                        setGenreImagesJSON(await sendRequest(userToken, refreshToken, value));
                        // console.log(response);
                        setSelectedLanguage(value);
                    }}>
                        <img src={`${value}.png`} width={300} height={200} alt={"not found"} style={{pointerEvents: 'none'}}/>
                        <br/>
                        {value}
                    </div>
                ))}
            </div>
        </>
    );
}

export default LanguagePicker;
