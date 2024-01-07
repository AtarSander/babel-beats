import React from 'react';
import BottomBar from "./BottomBar";
import LanguagePicker from "./LanguagePicker";
import "./MainApp.css"

function MainApp() {
    return (
        <div className="MainApp">
            {/*<SyncedText />*/}
            <LanguagePicker />
            <BottomBar />
        </div>
    );
}


export default MainApp;
