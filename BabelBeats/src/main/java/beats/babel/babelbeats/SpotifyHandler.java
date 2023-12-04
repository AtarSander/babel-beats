package beats.babel.babelbeats;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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


    public String fetchAccessTokenJSON() {
        String url = "https://accounts.spotify.com/api/token";
        String[] headerName = new String[]{"Content-Type"};
        String[] headerValue = new String[]{"application/x-www-form-urlencoded"};
        String parameters = "grant_type=client_credentials" +
                "&client_id=" + clientID +
                "&client_secret=" + clientSecret;
        return sendHTTPRequest(url, headerName, headerValue, "POST", parameters);
//        return sendHTTPRequest(url, "Content-Type", "application/x-www-form-urlencoded", parameters);

    }

    public void setAccessToken() {
        String fetchedData = fetchAccessTokenJSON();
        JSONExtractor JSONe = new JSONExtractor();
        accessToken = JSONe.extract(fetchedData, "access_token");
    }

    public String fetchUserProfileJSON() {
        String url = "https://api.spotify.com/v1/me";
        String[] headerName = new String[]{"Authorization"};
        String[] headerValue = new String[]{"Bearer " + spotifyUser.getToken()};
//          String headerName = "Authorization";
//          String headerValue = "Bearer " + userToken.getToken();

        return sendHTTPRequest(url, headerName, headerValue, "None", "");

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
        sendHTTPRequest(url, header, headerValues, "PUT", "");
    }

    public void pausePlayback() {
        String url = "https://api.spotify.com/v1/me/player/pause";
        String[] header = new String[]{"Authorization"};
        String[] headerValues = new String[]{"Bearer " + spotifyUser.getToken()};
        sendHTTPRequest(url, header, headerValues, "PUT", "");
    }

    private String fetchUsersFavArtistsJSON() {
        String url = "https://api.spotify.com/v1/me/top/artists?limit=30&offset=0";
        String[] headerName = new String[]{"Authorization"};
        String[] headerValues = new String[]{"Bearer " + spotifyUser.getToken()};
        return sendHTTPRequest(url, headerName, headerValues);
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

//            extract images from item
            JSONArray images = item.getJSONArray("images");

//            get first image from list
            JSONObject imageJson = images.getJSONObject(0);
            int width = imageJson.getInt("width");
            int height = imageJson.getInt("height");
            String imageUrl = imageJson.getString("url");
            Image image = new Image(width, height, imageUrl);

//            get artists name
            String name = item.getString("name");

//            get artist's genres as JSONArray from item
            JSONArray genresArray = item.getJSONArray("genres");

//            extract each genre and put it into String[] genres
            String[] genres = new String[genresArray.length()];
            for (int j = 0; j < genresArray.length(); j++){
                genres[j] = genresArray.getString(j);
            }

//            create Artist obj and add it to topArtists
            Artist artist = new Artist(name, genres, image);
            topArtists[i] = artist;
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

    private String sendHTTPRequest(String url, String[] headerName, String[] headerValue, String type, String parameters) {
        var client = HttpClient.newHttpClient();

        var builder = HttpRequest.newBuilder(
                URI.create(url));
        for (int i = 0; i < headerName.length; i++) {
            builder.header(headerName[i], headerValue[i]);
        }
        if (Objects.equals(type, "POST")) {
            builder.POST(HttpRequest.BodyPublishers.ofString(parameters));
        }
        if (Objects.equals(type, "PUT")) {
            builder.PUT(HttpRequest.BodyPublishers.ofString(parameters));
        }
        HttpRequest request = builder.build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private String sendHTTPRequest(String url, String[] headerName, String[] headerValue) {
        return sendHTTPRequest(url, headerName, headerValue, "", "");
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