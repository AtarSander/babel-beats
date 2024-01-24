import React from 'react';
import "./LanguagePicker.css";
import {useLocation} from "react-router-dom";
import axios from "axios";

function LanguagePicker({ setSelectedLanguage, setFlag, title, languages, langListElementCSS, imageWidth, imageHeight }) {
    return (
        <>
            <h1 className={"languageTitle"}>{title}</h1>
            <div className={"languageList"}>
                {languages.map((value, index) => (
                    <div key={index} className={langListElementCSS} onClick={async () => {
                        setFlag(true);
                        // console.log(value);
                        setSelectedLanguage(value);
                    }}>
                        <img src={`${value}.png`} width={imageWidth} height={imageHeight} alt={"not found"} style={{pointerEvents: 'none'}}/>
                        <br/>
                        {value}
                    </div>
                ))}
            </div>
        </>
    );
}

export default LanguagePicker;
