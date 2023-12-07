package beats.babel.babelbeats;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class RequestHandler {
    public static String sendHTTPRequest(String url, String[] headerName, String[] headerValue, String type, String parameters) {
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
    public String sendHTTPRequest(String url, String[] headerName, String[] headerValue) {
        return sendHTTPRequest(url, headerName, headerValue, "", "");
    }


}
