import React from 'react';
import PlayButton from './PlayButton';
import SyncedText from './SyncedText';

function MainApp() {
    return (
        <div className="MainApp">
            <SyncedText />
            <PlayButton />
        </div>
    );
}


export default MainApp;
