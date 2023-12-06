package beats.babel.babelbeats;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;


public class SpotifyHandler {
    private SpotifyUser spotifyUser;
    private String accessToken;
    private String clientID;
    private String clientSecret;

    public SpotifyHandler() {
        if (clientID == null || clientSecret == null) {
            JSONExtractor JSONe = new JSONExtractor();
            String[] keys = new String[]{"ClientId", "ClientSecret"};
            String[] credentials = JSONe.readFromFile("src/main/resources/spotifyCredentials.json", keys);
            clientID = credentials[0];
            clientSecret = credentials[1];
        }
        setAccessToken();
    }


    private String fetchAccessTokenJSON() {
        String url = "https://accounts.spotify.com/api/token";
        String[] headerName = new String[]{"Content-Type"};
        String[] headerValue = new String[]{"application/x-www-form-urlencoded"};
        String parameters = "grant_type=client_credentials" +
                "&client_id=" + clientID +
                "&client_secret=" + clientSecret;
        RequestHandler rh = new RequestHandler();
        return rh.sendHTTPRequest(url, headerName, headerValue, "POST", parameters);
    }

    public void setAccessToken() {
        String fetchedData = fetchAccessTokenJSON();
        JSONExtractor JSONe = new JSONExtractor();
        accessToken = JSONe.extract(fetchedData, "access_token");
    }

    private String fetchUserProfileJSON() {
        String url = "https://api.spotify.com/v1/me";
        String[] headerName = new String[]{"Authorization"};
        String[] headerValue = new String[]{"Bearer " + spotifyUser.getToken()};
        RequestHandler rh = new RequestHandler();
        return rh.sendHTTPRequest(url, headerName, headerValue, "None", "");

    }


    public String getUserCountry() {
        JSONExtractor je = new JSONExtractor();
        var data = fetchUserProfileJSON();
        return je.extract(data, "country");
    }

    public void startPlayback() {
        String url = "https://api.spotify.com/v1/me/player/play";
        String[] header = new String[]{"Authorization"};
        String[] headerValues = new String[]{"Bearer " + spotifyUser.getToken()};
        RequestHandler rh = new RequestHandler();
        rh.sendHTTPRequest(url, header, headerValues, "PUT", "");
    }

    public void pausePlayback() {
        String url = "https://api.spotify.com/v1/me/player/pause";
        String[] header = new String[]{"Authorization"};
        String[] headerValues = new String[]{"Bearer " + spotifyUser.getToken()};
        RequestHandler rh = new RequestHandler();
        rh.sendHTTPRequest(url, header, headerValues, "PUT", "");
    }

    private String fetchUsersFavArtistsJSON() {
        String url = "https://api.spotify.com/v1/me/top/artists?limit=30&offset=0";
        String[] headerName = new String[]{"Authorization"};
        String[] headerValues = new String[]{"Bearer " + spotifyUser.getToken()};
        RequestHandler rh = new RequestHandler();
        return rh.sendHTTPRequest(url, headerName, headerValues);
    }


    public Artist[] getUsersFavArtists() {
//        fetch json from Spotify API
        String fetchedJSON = fetchUsersFavArtistsJSON();

//        extract artist info from fetched json
        JSONExtractor je = new JSONExtractor();
        JSONArray items = je.extractList(fetchedJSON, "items");

//        initialize Artist[]
        Artist[] topArtists = new Artist[30];

        for (int i = 0; i < items.length(); i++){
//            extract current item from JSONArray items
            JSONObject item = items.getJSONObject(i);
//            add artist to topArtists
            topArtists[i] = createArtistFromJson(item);
        }
        return topArtists;
    }

    public Map<String, Integer> countUsersGenres(){
//        initialize a map that has genre names as keys and their count as values
        Map<String, Integer> genres = new HashMap<>();

//        iterate over each of users top artists and add their genres to hashmap
        for (Artist a : spotifyUser.getTopArtists()){
            for (String g : a.getGenres()){
                if (!genres.containsKey(g)){
                    genres.put(g, 1);
                }
                else{
                    genres.put(g, genres.get(g) + 1);
                }
            }
        }
        return genres;
    }

    private String fetchRecommendedPlaylistJSON(int categoryOffset, String language){
        String genre = sortByValue(countUsersGenres())[categoryOffset];
        String query = genre + "+" + language;
        String url = ("https://api.spotify.com/v1/search?q=" +
                query.replace(" ", "+") +
                "&type=playlist&market=" + spotifyUser.getCountry() +
                "&limit=1");
        String[] headerName = new String[]{"Authorization"};
        String[] headerValues = new String[]{"Bearer " + spotifyUser.getToken()};
        RequestHandler rh = new RequestHandler();
        return rh.sendHTTPRequest(url, headerName, headerValues);
    }

    private String extractPlaylistIdFromJson(String json){
        JSONObject jo = new JSONObject(json);
        JSONObject playlists = jo.getJSONObject("playlists");
        JSONObject item = playlists.getJSONArray("items").getJSONObject(0);
        return item.getString("id");
    }

    public String getRecommendedPlaylist(int categoryOffset, String language){
        String response = fetchRecommendedPlaylistJSON(categoryOffset, language);
        return extractPlaylistIdFromJson(response);
    }

    public String fetchSongsFromPlaylistJSON(String playlistID, int songCount){
        String[] songs = new String[songCount];
        String url = "https://api.spotify.com/v1/playlists/" +
                      playlistID + "/tracks?limit=" + songCount;
        String[] headerName = new String[]{"Authorization"};
        String[] headerValues = new String[]{"Bearer " + spotifyUser.getToken()};
        RequestHandler rh = new RequestHandler();
        return rh.sendHTTPRequest(url, headerName, headerValues);
    }

    public Song[] getPlaylistSongs(String playlistID, int songCount){
//        fetch json
        String fetchedData = fetchSongsFromPlaylistJSON(playlistID, songCount);
        JSONObject jo = new JSONObject(fetchedData);

//        extract items
        JSONArray items = jo.getJSONArray("items");

//        init sons table
        Song[] songs = new Song[items.length()];
        for (int i = 0; i < items.length(); i++){
//            get current song json
            JSONObject item = items.getJSONObject(i);
            JSONObject track = item.getJSONObject("track");
//            extract songs name
            String name = track.getString("name");

//            extract songId
            String songId = track.getString("id");

//            extract artistIds
            JSONArray artistIds = track.getJSONArray("artists");

//            init Artist[]
            Artist[] artists = new Artist[artistIds.length()];

            for (int j = 0; j < artistIds.length(); j++){
//                get artistId
                String artistId = artistIds.getJSONObject(j).getString("id");
                JSONObject artistJson = new JSONObject(fetchArtistJSON(artistId));
                artists[j] = createArtistFromJson(artistJson);
            }

//            get song image
            JSONObject album = track.getJSONObject("album");
            JSONObject imageJson = album.getJSONArray("images").getJSONObject(0);
            Image image = createImageFromJson(imageJson);

//            add song to songs
            songs[i] = new Song(artists, name, image, songId);
        }
        return songs;
    }

    private Artist createArtistFromJson(JSONObject artistJson){
        //            extract images from item
        JSONArray images = artistJson.getJSONArray("images");

//            get first image from list
        JSONObject imageJson = images.getJSONObject(0);

//        create image
        Image image = createImageFromJson(imageJson);

//            get artists name
        String name = artistJson.getString("name");

//            get artist's genres as JSONArray from item
        JSONArray genresArray = artistJson.getJSONArray("genres");

//            extract each genre and put it into String[] genres
        String[] genres = new String[genresArray.length()];
        for (int j = 0; j < genresArray.length(); j++){
            genres[j] = genresArray.getString(j);
        }

//            create Artist obj and add it to topArtists
        return new Artist(name, genres, image);
    }

    private Image createImageFromJson(JSONObject imageJson){
        int width = imageJson.getInt("width");
        int height = imageJson.getInt("height");
        String imageUrl = imageJson.getString("url");
        return new Image(width, height, imageUrl);
    }


    private String[] sortByValue(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        String[] keys = new String[list.size()];

        for (int i =0; i < list.size(); i++){
            keys[i] = list.get(i).getKey();
        }
        return keys;
    }

    public String fetchArtistJSON(String artistID){
        String url = "https://api.spotify.com/v1/artists/" + artistID;
        String[] headerName = new String[]{"Authorization"};
        String[] headerValues = new String[]{"Bearer " + spotifyUser.getToken()};
        RequestHandler rh = new RequestHandler();
        return rh.sendHTTPRequest(url, headerName, headerValues);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setSpotifyUser(SpotifyUser sut) {
        spotifyUser = sut;
    }

    public boolean isConnected() {
        return (accessToken != null);
    }
}