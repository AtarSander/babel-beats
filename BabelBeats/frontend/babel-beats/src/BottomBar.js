import PlayButton from "./PlayButton";
import "./BottomBar.css"

function BottomBar() {
    return (
        <div className="bottomBar">
            <PlayButton />
            <PlayButton />
        </div>
    );
}

export default BottomBar;