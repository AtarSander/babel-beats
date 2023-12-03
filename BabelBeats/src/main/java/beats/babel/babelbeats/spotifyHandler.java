package beats.babel.babelbeats;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import org.json.JSONObject;


public class spotifyHandler {
    private String userToken;
    private String accessToken;

    // CHANGE IT LATER TO BE TAKEN FROM FILE
    private String clientId = "77e15947cd8a4be0aacdc89c22b18121";
    private String clientSecret = "f352aa77fabb4d2984053fa31e51416c";
    
    public spotifyHandler(String ClientId, String ClientSecret){
        clientId = ClientId;
        clientSecret = ClientSecret;
    }

    private String extractAccessToken(String fetchedJSON)
    {
        JSONObject parser = new JSONObject(fetchedJSON);
        return parser.getString("access_token");
    }
    
    public String fetchAccessTokenJSON(){
        String url = "https://accounts.spotify.com/api/token";
        String headerName = "Content-Type";
        String headerValue = "application/x-www-form-urlencoded";
        String parameters = "grant_type=client_credentials" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret;

        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                URI.create(url))
            .header(headerName, headerValue)
            .POST(HttpRequest.BodyPublishers.ofString(parameters))
            .build();
        
        try{
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public void setAccessToken(){
        String fetchedData = fetchAccessTokenJSON();
        accessToken = extractAccessToken(fetchedData);
    }

    public String getAccessToken(){
        return accessToken;
    }

    public void setUserToken(String UserToken){
        userToken = UserToken;
    }

    public boolean isConnected(){
        return (accessToken != null);
    }
}