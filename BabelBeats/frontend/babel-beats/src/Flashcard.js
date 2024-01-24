import './Flashcard.css'
import flashCards from './flashCards.json';
import {AiOutlineLeft, AiOutlineRight} from "react-icons/ai";
import {useEffect, useState} from "react";

function Flashcard({ srcLang, targetLang }) {
    const [currWord, setCurrWord] = useState(0);
    const [words, setWords] = useState([]);
    const [wordsTrans, setWordsTrans] = useState([]);
    const [ready, setReady] = useState(false);
    useEffect(() => {
        let shuffled = flashCards
            .map(value => ({ value, sort: Math.random() }))
            .sort((a, b) => a.sort - b.sort)
            .map(({ value }) => value);
        setWords(shuffled.map(card => card[targetLang]));
        setWordsTrans(shuffled.map(card => card[srcLang]));
        setReady(true);
    }, []);

    return (
        <div className={"FlashcardWrapper"}>
            <div className={"lrButton"} onClick={() => setCurrWord((currWord - 1 + words.length) % words.length)}>
                <AiOutlineLeft/>
            </div>
            <div className={"Flashcard"}>
                <div className={"FlashcardInner"}>
                    <div className={"FlashcardFront"}>
                        {ready && <h1>{words[currWord]}</h1>}
                    </div>
                    <div className={"FlashcardBack"}>
                        {ready && <h1>{wordsTrans[currWord]}</h1>}
                    </div>
                </div>
            </div>
            <div  className={"lrButton"} onClick={() => setCurrWord((currWord + 1) % words.length)}>
                <AiOutlineRight/>
            </div>
        </div>
    )
}

export default Flashcard;