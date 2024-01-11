import React, {useEffect, useState} from "react";
import "./Logo.css";

function Logo() {
    const [blinkStatus, setBlinkStatus] = useState(false);
    const [logoSrc, setLogoSrc] = useState("babelBeats.png");

    useEffect(() => {
        const blinkInterval = setInterval(() => {
            setLogoSrc("babelBlink.png");
            setTimeout(() => {
                setLogoSrc("babelBeats.png");
            }, 1000);
        }, 5000);

        return () => {
            clearInterval(blinkInterval);
        };
    }, []);

    return (
        <div className={"logo-container"}>
            <img className={"logo"} src={logoSrc} alt={"babelBeats"}/>
        </div>
    );
}

export default Logo;