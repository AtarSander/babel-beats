package beats.babel.babelbeats;

public class SpotifyUser {
    private final String token;
    private final String refreshToken;
//    private final String country;
//    private Artist[] topArtists;

    public SpotifyUser(String userToken, String refreshToken){
        this.token = userToken;
        this.refreshToken = refreshToken;
//        SpotifyHandler sh = new SpotifyHandler();
//        sh.setSpotifyUser(this);
//        this.country = sh.getUserCountry();
//        this.topArtists = sh.getUsersFavArtists();
    }

    public String getToken(){
        return token;
    }

    public String getRefreshToken(){
        return refreshToken;
    }

//    public String getCountry() {
//        return country;
//    }
//
//    public Artist[] getTopArtists() {
//        return topArtists;
//    }
//
//    public boolean isConnected(){
//        return token != null;
//    }
}
