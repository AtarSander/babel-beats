import {useLocation} from "react-router-dom";

function RecommendButton() {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const userToken = searchParams.get('userToken');
    const refreshToken = searchParams.get('refreshToken');

    const sendRequest = async() => {
        try {

        }
    }
}