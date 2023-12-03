package beats.babel.babelbeats;

public class SpotifyUserToken {
    private final String token;
    private final String refreshToken;

    public SpotifyUserToken(String userToken, String refreshToken){
        this.token = userToken;
        this.refreshToken = refreshToken;
    }

    public String getToken(){
        return token;
    }

    public String getRefreshToken(){
        return refreshToken;
    }


    public boolean isConnected(){
        return token != null;
    }
}
