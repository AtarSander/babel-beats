package beats.babel.babelbeats.controller;

import beats.babel.babelbeats.JSONExtractor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Random;
import java.util.Base64;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/")
public class SpotifyController {
    private static String userToken;
    private static String refreshToken;
    private static String generatedState;
    private static String clientID;
    private static String clientSecret;
    private static final String redirect_uri = "http://localhost:8080/callback";
    private static final String redirect_callback = "http://localhost:3000/home";

    public SpotifyController(){
        if (clientID == null || clientSecret == null)
        {
            JSONExtractor JSONe = new JSONExtractor();
            String[] keys = new String[]{"ClientId", "ClientSecret"};
            String[] credentials = JSONe.readFromFile("src/main/resources/spotifyCredentials.json", keys);
            clientID = credentials[0];
            clientSecret = credentials[1];
        }
    }

    public String getUserToken(){
        return userToken;
    }

    public String getRefreshToken(){
        return refreshToken;
    }

    private static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < length; i++) {
            // Generate a random character in the range ['A', 'Z']
            char randomChar = (char) (random.nextInt(26) + 'A');
            str.append(randomChar);
        }

        return str.toString();
    }


    @GetMapping("login")
    @ResponseBody
    public RedirectView requestAuth() {
        RedirectView redirectedView = new RedirectView();
        generatedState = generateRandomString(16);
        String scope = "user-read-playback-state user-modify-playback-state user-top-read streaming user-read-private user-read-email";
        String redirectUrl = "https://accounts.spotify.com/authorize?" +
                "response_type=code" +
                "&client_id=" + clientID +
                "&scope=" + scope +
                "&redirect_uri=" + redirect_uri +
                "&state=" + generatedState;
        redirectedView.setUrl(redirectUrl);
        return redirectedView;
    }

    @GetMapping("callback")
    public RedirectView requestUserToken(@RequestParam(required = true)String code, @RequestParam(required = true)String state){
        RedirectView redirectedView = new RedirectView();
        if (Objects.equals(state, generatedState)) {
            String url = "https://accounts.spotify.com/api/token";
            String parameters = "code=" + code +
                    "&redirect_uri=" + redirect_uri +
                    "&grant_type=" + "authorization_code";

            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(
                            URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic " + base64Encode(clientID + ":" + clientSecret))
                    .POST(HttpRequest.BodyPublishers.ofString(parameters))
                    .build();

            try {
                var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JSONExtractor JSONe = new JSONExtractor();
                userToken = JSONe.extract(response.body(), "access_token");
                refreshToken = JSONe.extract(response.body(), "refresh_token");
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else {
            // ADD BETTER ERROR MESSAGE HANDLING
            System.out.println("User denied access!");
        }
        redirectedView.setUrl(redirect_callback);
        return redirectedView;

    }

    public static String base64Encode(String originalString) {
        byte[] encodedBytes = Base64.getEncoder().encode(originalString.getBytes());
        return new String(encodedBytes);
    }

    public boolean hasLoggedUser(){
        return userToken != null;
    }
}
