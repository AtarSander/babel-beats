package beats.babel.babelbeats.controller;
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
@RequestMapping("/")
public class spotifyUserToken {
    public String userToken;
    private String generatedState;
    private final String clientID = "77e15947cd8a4be0aacdc89c22b18121";
    private final String redirect_uri = "http://localhost:8080/callback";
    private String clientSecret = "f352aa77fabb4d2984053fa31e51416c";

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
        String scope = "user-read-playback-state user-modify-playback-state user-top-read streaming";
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
    public void requestUserToken(@RequestParam(required = true)String code, @RequestParam(required = true)String state){
        if (Objects.equals(state, generatedState)) {
            String url = "https://accounts.spotify.com/api/token";
            String parameters = "code:" + code +
                    "&redirect_uri=" + redirect_uri +
                    "&grant_type=" + "authorization_code";

            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(
                            URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic" + base64Encode(clientID + ":" + clientSecret))
                    .POST(HttpRequest.BodyPublishers.ofString(parameters))
                    .build();

            try {
                var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                userToken = response.body();
                System.out.println(userToken);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else {
            System.out.println("User denied access!");
        }
    }
    public static String base64Encode(String originalString) {
        byte[] encodedBytes = Base64.getEncoder().encode(originalString.getBytes());
        return new String(encodedBytes);
    }
}
