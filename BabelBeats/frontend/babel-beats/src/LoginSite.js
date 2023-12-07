import React from 'react';
import './LoginSite.css'

const LoginSite = () => {
    const handleRedirect = () => {
        window.location.href = "http://localhost:8080/login";
    };
    const gradientStyle = {
        background: 'linear-gradient(to bottom, #121212, #000000)',
        height: '85vh',
};


    return (
            <div style={gradientStyle} className={'container'}>
                <h1>Press to sign in to Spotify</h1>
                <button onClick={handleRedirect}>Login to Spotify</button>
            </div>
    );
};

export default LoginSite;
