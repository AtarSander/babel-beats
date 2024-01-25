import PlayButton from "./PlayButton";
import "./BottomBar.css"
import SkipButton from "./SkipButton";

function BottomBar({ userToken, refreshToken, isPlaying, setIsPlaying, songPosition, setSongPosition }) {

    return (
        <div className="bottomBar">
            <SkipButton direction="backward"  userToken={userToken} refreshToken={refreshToken}/>
            <PlayButton userToken={userToken} refreshToken={refreshToken} isPlaying={isPlaying} setIsPlaying={setIsPlaying} songPosition={songPosition} setSongPosition={setSongPosition}/>
            <SkipButton direction="forward" userToken={userToken} refreshToken={refreshToken}/>
        </div>
    );
}

export default BottomBar;