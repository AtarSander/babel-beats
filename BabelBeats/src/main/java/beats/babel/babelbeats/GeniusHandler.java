package beats.babel.babelbeats;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.util.Objects;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class GeniusHandler {
    private static String accessToken;

    public GeniusHandler() {
        accessToken = "tLh3CHyVeq43Q0fL1p3h3ckxvwCd8x6mAlUgvoQLWTfYvjB2Lq3t-IWvMylju31A";
    }
    public String searchGenius(String query)
    {
        try
        {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String uri = "https://api.genius.com/search?q=" + encodedQuery;
            String[] header = new String[]{"Authorization"};
            String[] headerValues = new String[]{"Bearer " + accessToken};
            return sendHTTPRequest(uri, header, headerValues, "GET", "");
        }
        catch ( IOException e) {
            // Handle exceptions
            e.printStackTrace();
            return null;
        }
    }

    private String formatText(String text, char startSign, char stopSign, int maxEndl) {
        int newLineCount = 0;
        StringBuilder formattedText = new StringBuilder();
        boolean removeCycle = false;
        for (char character : text.toCharArray()) {
            if (!removeCycle) {
                if (character == startSign)
                    removeCycle = true;
                if (character == '\n')
                    newLineCount += 1;
                else
                    newLineCount = 0;
                if (newLineCount < maxEndl)
                    formattedText.append(character);
            }
        }
    }

    public String getLyrics(String JSON_file)
    {
        JSONExtractor file = new JSONExtractor();
        String first = file.extract(JSON_file, "response");
        String second = file.extract(JSON_file, "song");
    }

    public String searchSong(String id)
    {
            String uri = "https://api.genius.com/songs/" + id;
            String[] header = new String[]{"Authorization"};
            String[] headerValues = new String[]{"Bearer " + accessToken};
            return sendHTTPRequest(uri, header, headerValues, "GET", "");

    }
    private String sendHTTPRequest(String url, String[] headerName, String[] headerValue, String type, String parameters) {
        var client = HttpClient.newHttpClient();

        var builder = HttpRequest.newBuilder(
                URI.create(url));
        for (int i = 0; i < headerName.length; i++){
            builder.header(headerName[i], headerValue[i]);
        }
        if (Objects.equals(type, "POST")) {
            builder.POST(HttpRequest.BodyPublishers.ofString(parameters));
        }
        if (Objects.equals(type, "PUT")){
            builder.PUT(HttpRequest.BodyPublishers.ofString(parameters));
        }
        if (Objects.equals(type, "GET")){
            builder.GET();
        }
        HttpRequest request = builder.build();

        try{
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
