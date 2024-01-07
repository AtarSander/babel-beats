import React from 'react';
import './LoginSite.css'

const LoginSite = () => {
    const handleRedirect = () => {
        window.location.href = "http://localhost:8080/login";
    };


    return (
            <div className="container">
                <h1 className={"loginHeader"}>Press to sign in to Spotify</h1>
                <button className={"loginButton"} onClick={handleRedirect}>Login to Spotify</button>
            </div>
    );
};

export default LoginSite;
