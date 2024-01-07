import PlayButton from "./PlayButton";
import "./BottomBar.css"
import SkipButton from "./SkipButton";

function BottomBar() {
    return (
        <div className="bottomBar">
            <SkipButton direction="backward" />
            <PlayButton />
            <SkipButton direction="forward" />
        </div>
    );
}

export default BottomBar;