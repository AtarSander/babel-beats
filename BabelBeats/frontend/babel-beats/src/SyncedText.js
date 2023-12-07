import React, { useEffect, useState } from "react";

async function extractFromQueue() {
    try {
        const response = await fetch('/src/main/resources/lyrics/songsQueue.txt');
        const text = await response.text();
        const lines = text.split('\n');
        return lines[0].trim();
    } catch (error) {
        console.error('Error fetching data:', error);
        throw error;
    }
}

async function readFromFile(name) {
    let path = '../../src/main/resources/lyrics/processedLyrics/';

    try {
        const response = await fetch(path + name + '.json');
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    } catch (error) {
        console.error('Error fetching data:', error);
        throw error;
    }
}

const SyncedText = () => {
    const [text, setText] = useState('');
    let data = "";
    useEffect(() => {
        const fetchData = async () => {
            try {
                const title = await extractFromQueue();
                data = await readFromFile(title);
                console.log(data);
                setText(data);
            } catch (error) {
                // Handle errors here
                console.error('Error:', error);
            }
        };

        fetchData();
    }, []);

    return (
        <div>
            <h1>{data}</h1>
        </div>
    );
}

export default SyncedText;