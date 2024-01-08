import React from 'react';
import "./LanguagePicker.css";
import {useLocation} from "react-router-dom";
import axios from "axios";

function LanguagePicker({ selectedLanguage, setSelectedLanguage, genreImagesJSON, setGenreImagesJSON }) {
    let languages = ["spanish", "italian", "korean", "english", "portuguese", "german", "chinese", "french"]
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const userToken = searchParams.get('userToken');
    const refreshToken = searchParams.get('refreshToken');


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
                        <img src={`${value}.png`} width={300} height={200} alt={"not found"}/>
                        <br/>
                        {value}
                    </div>
                ))}
            </div>
        </>
    );
}

export default LanguagePicker;
