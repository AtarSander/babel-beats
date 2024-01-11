import React from 'react';
import "./LanguagePicker.css";
import {useLocation} from "react-router-dom";
import axios from "axios";

function LanguagePicker({ setSelectedLanguage, setFlag, title, languages }) {
    return (
        <>
            <h1 className={"languageTitle"}>{title}</h1>
            <div className={"languageList"}>
                {languages.map((value, index) => (
                    <div key={index} className={`languageListElement`} onClick={async () => {
                        // setGenreImagesJSON(await sendRequest(userToken, refreshToken, value));
                        // console.log(response);
                        setFlag(true);
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
