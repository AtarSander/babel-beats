import React from 'react';
import "./LanguagePicker.css";
import {useLocation, useNavigate} from "react-router-dom";
import axios from "axios";

function LanguagePicker() {
    let languages = ["spanish", "italian", "korean", "english", "portuguese", "german", "chinese", "french"]
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const userToken = searchParams.get('userToken');
    const refreshToken = searchParams.get('refreshToken');
    const sendRequest = async (userToken, refreshToken, language) => {
        try {
            return await axios.get(`http://localhost:8080/api/reccomendGenres?userToken=${userToken}&refreshToken=${refreshToken}`, );
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
                        let response = await sendRequest(userToken, refreshToken, value);
                        console.log(response);
                    }}>
                        <img src={`${value}.png`} width={300} height={200} alt={"no image found"}/>
                        <br/>
                        {value}
                    </div>
                ))}
            </div>
        </>
    );
}

export default LanguagePicker;
