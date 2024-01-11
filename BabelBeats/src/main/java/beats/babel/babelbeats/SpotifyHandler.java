package beats.babel.babelbeats;

import java.util.*;

import beats.babel.babelbeats.controller.SpotifyController;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;


@Component
public class SpotifyHandler {
    private String accessToken;
    private static String clientID;
    private static String clientSecret;
    private Artist[] artists;


    public SpotifyHandler() {
        JSONExtractor JSONe = new JSONExtractor();
        String[] keys = new String[]{"ClientId", "ClientSecret"};
        String[] credentials = JSONe.readFromFile("src/main/resources/spotifyCredentials.json", keys);
        clientID = credentials[0];
        clientSecret = credentials[1];
        setAccessToken();
    }

    public Artist[] getArtists() {
        return artists;
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

    private String fetchUserProfileJSON(SpotifyUser spotifyUser) {
        String url = "https://api.spotify.com/v1/me";
        String[] headerName = new String[]{"Authorization"};
        String[] headerValue = new String[]{"Bearer " + spotifyUser.getToken()};
        RequestHandler rh = new RequestHandler();
        return rh.sendHTTPRequest(url, headerName, headerValue, "None", "");

    }

    public String getUserCountry(SpotifyUser spotifyUser) {
        JSONExtractor je = new JSONExtractor();
        var data = fetchUserProfileJSON(spotifyUser);
        return je.extract(data, "country");
    }

    private void playerPUT(SpotifyUser spotifyUser, String action){
        String url = "https://api.spotify.com/v1/me/player/" + action;
        String[] header = new String[]{"Authorization"};
        String[] headerValues = new String[]{"Bearer " + spotifyUser.getToken()};
        RequestHandler.sendHTTPRequest(url, header, headerValues, "PUT", "");

    }
    public void startPlayback(SpotifyUser spotifyUser) {
        playerPUT(spotifyUser, "play");
    }

    public void pausePlayback(SpotifyUser spotifyUser) {
        playerPUT(spotifyUser, "pause");
    }

    public int getPosition(SpotifyUser spotifyUser) {
        String response = getPlaybackState(spotifyUser);
        JSONObject jo = new JSONObject(response);
        return jo.getInt("progress_ms");
    }

    public void seekPosition(SpotifyUser spotifyUser, int time){
        int newTime = getPosition(spotifyUser)+ time;
        newTime = Math.max(newTime, 0);
        playerPUT(spotifyUser, "seek?position_ms=" + newTime);
    }

    public String getPlaybackState(SpotifyUser spotifyUser){
        String url = "https://api.spotify.com/v1/me/player";
        String[] header = new String[]{"Authorization"};
        String[] headerValues = new String[]{"Bearer " + spotifyUser.getToken()};
        return RequestHandler.sendHTTPRequest(url, header, headerValues, "GET", "");
    }

    public boolean isPlaying(SpotifyUser spotifyUser){
        String response = getPlaybackState(spotifyUser);
        JSONObject jo = new JSONObject(response);
        return jo.getBoolean("is_playing");
    }

    public void playSongByID(SpotifyUser spotifyUser, String songID){
        String url = "https://api.spotify.com/v1/me/player/play";
        String[] header = new String[]{"Authorization"};
        String[] headerValues = new String[]{"Bearer " + spotifyUser.getToken()};
        JSONObject parameters = new JSONObject();
        JSONArray uris = new JSONArray();
        uris.put("spotify:track:" + songID);
        parameters.put("uris", uris);
        String response = RequestHandler.sendHTTPRequest(url, header, headerValues, "PUT", parameters.toString());
    }

    private String fetchUsersFavArtistsJSON(SpotifyUser spotifyUser) {
        String url = "https://api.spotify.com/v1/me/top/artists?limit=30&offset=0";
        String[] headerName = new String[]{"Authorization"};
        String[] headerValues = new String[]{"Bearer " + spotifyUser.getToken()};
        RequestHandler rh = new RequestHandler();
        return rh.sendHTTPRequest(url, headerName, headerValues);
    }


    public Artist[] getUsersFavArtists(SpotifyUser spotifyUser) {
//        fetch json from Spotify API
        String fetchedJSON = fetchUsersFavArtistsJSON(spotifyUser);

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

    public String[] countUsersGenres(SpotifyUser spotifyUser){
//        initialize a map that has genre names as keys and their count as values
        Map<String, Integer> genres = new HashMap<>();
        String[] officialGenres = {"pop", "rock", "hip hop", "r&b", "country",
                "jazz", "blues", "soul", "funk", "gospel",
                "reggae", "indie", "classical", "folk",
                "metal", "punk", "alternative", "dance", "hyperpop", "party"};
        Vector<String> userGenres = new Vector<>();
//        iterate over each of users top artists and add their genres to hashmap
        artists = getUsersFavArtists(spotifyUser);
        for (Artist a : artists){
            //                if (!genres.containsKey(g)){
            //                    genres.put(g, 1);
            //                }
            //                else{
            //                    genres.put(g, genres.get(g) + 1);
            //                }
            userGenres.addAll(Arrays.asList(a.getGenres()));
        }

        for (int i = 0; i < userGenres.size(); i++) {
            boolean changed = false;
            for (String og : officialGenres) {
                if (userGenres.get(i).contains(og)) {
                    userGenres.set(i, og);
                    break;
                }
            }
            if (!changed) {
                userGenres.set(i, "");
            }
        }

        userGenres.removeIf(String::isEmpty);

        for (String g : userGenres) {
            if (!genres.containsKey(g)) {
                genres.put(g, 1);
            }
            else {
                genres.put(g, genres.get(g) + 1);
            }
        }

        return sortByValue(genres);
    }

    private String fetchRecommendedPlaylistJSON(int categoryOffset, String language, SpotifyUser spotifyUser){
        String genre = countUsersGenres(spotifyUser)[categoryOffset];
        String query = language + "+" + genre;
        String url = ("https://api.spotify.com/v1/search?q=" +
                query.replace(" ", "+") +
                "&type=playlist&market=" + getUserCountry(spotifyUser) +
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

    public String getRecommendedPlaylist(int categoryOffset, String language, SpotifyUser spotifyUser){
        String response = fetchRecommendedPlaylistJSON(categoryOffset, language, spotifyUser);
        return extractPlaylistIdFromJson(response);
    }

    public String fetchSongsFromPlaylistJSON(String playlistID, int songCount, SpotifyUser spotifyUser){
        String[] songs = new String[songCount];
        String url = "https://api.spotify.com/v1/playlists/" +
                      playlistID + "/tracks?limit=" + songCount;
        String[] headerName = new String[]{"Authorization"};
        String[] headerValues = new String[]{"Bearer " + spotifyUser.getToken()};
        RequestHandler rh = new RequestHandler();
        return rh.sendHTTPRequest(url, headerName, headerValues);
    }

    public Song[] getPlaylistSongs(String playlistID, int songCount, SpotifyUser spotifyUser){
//        fetch json
        String fetchedData = fetchSongsFromPlaylistJSON(playlistID, songCount, spotifyUser);
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

//            extract duration
            int duration = track.getInt("duration_ms") / 1000;

//            extract artistIds
            JSONArray artistIds = track.getJSONArray("artists");

//            init Artist[]
            Artist[] artists = new Artist[artistIds.length()];

            for (int j = 0; j < artistIds.length(); j++){
//                get artistId
                String artistId = artistIds.getJSONObject(j).getString("id");
                JSONObject artistJson = new JSONObject(fetchArtistJSON(artistId, spotifyUser));
                Artist tempArtist = createArtistFromJson(artistJson);
                if (tempArtist != null)
                    artists[j] = tempArtist;
            }

//            get song image
            JSONObject album = track.getJSONObject("album");
            JSONObject imageJson = album.getJSONArray("images").getJSONObject(0);
            Image image = createImageFromJson(imageJson);

//            add song to songs
            songs[i] = new Song(artists, name, image, songId, duration);
        }
        return songs;
    }

    private Artist createArtistFromJson(JSONObject artistJson){
        //            extract images from item
        JSONArray images = artistJson.getJSONArray("images");

//            get first image from list
            if(images.isEmpty())
                return null;
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

    public String fetchArtistJSON(String artistID, SpotifyUser spotifyUser){
        String url = "https://api.spotify.com/v1/artists/" + artistID;
        String[] headerName = new String[]{"Authorization"};
        String[] headerValues = new String[]{"Bearer " + spotifyUser.getToken()};
        RequestHandler rh = new RequestHandler();
        return rh.sendHTTPRequest(url, headerName, headerValues);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public boolean isConnected() {
        return (accessToken != null);
    }
}