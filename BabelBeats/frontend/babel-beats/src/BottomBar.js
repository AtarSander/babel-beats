import PlayButton from "./PlayButton";
import "./BottomBar.css"
import SkipButton from "./SkipButton";

function BottomBar({ userToken, refreshToken }) {
    return (
        <div className="bottomBar">
            <SkipButton direction="backward"  userToken={userToken} refreshToken={refreshToken}/>
            <PlayButton userToken={userToken} refreshToken={refreshToken}/>
            <SkipButton direction="forward" userToken={userToken} refreshToken={refreshToken}/>
        </div>
    );
}

export default BottomBar;