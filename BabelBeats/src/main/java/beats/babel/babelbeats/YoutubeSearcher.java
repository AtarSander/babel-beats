package beats.babel.babelbeats;

import org.json.JSONArray;
import org.json.JSONObject;


public class YoutubeSearcher {
    private final String authKey;
    private final String url;

    public YoutubeSearcher(){
        JSONExtractor je = new JSONExtractor();
        String[] keys = new String[]{"authKey", "url"};
        String[] credentials = je.readFromFile("src/main/resources/youtubeCredentials.json", keys);
        this.authKey = credentials[0];
        this.url = credentials[1];
    }

    private String formatQuery(String query){
        return query.replace(" ", "%20") + "%20audio";
    }

    private String formatURL(String maxResults, String type, String videoCategoryID, String query){
        String newUrl = (url +
                "?part=snippet&maxResults=" + maxResults +
                "&q=" + formatQuery(query) +
                "&type=" + type + "&videoCategoryId=" + videoCategoryID +
                "&key=" + authKey);

        return newUrl;
    }

    private String extractVideoId(String responseJSON){
        JSONExtractor je = new JSONExtractor();
        JSONArray items = je.extractList(responseJSON, "items");
        JSONObject item = items.getJSONObject(0);
        JSONObject id = item.getJSONObject("id");
        return id.getString("videoId");
    }

    public String urlSearch(String query){
        String formattedUrl = formatURL("1", "video", "10", query);
        RequestHandler rh = new RequestHandler();
        String response = rh.sendHTTPRequest(formattedUrl, new String[]{}, new String[]{}, "GET", "");

        return "https://www.youtube.com/watch?v=" + extractVideoId(response);
    }
}
