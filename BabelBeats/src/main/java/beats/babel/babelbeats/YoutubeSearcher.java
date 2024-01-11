package beats.babel.babelbeats;

import org.json.JSONArray;
import org.json.JSONObject;

import static java.lang.Math.abs;


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

    private String formatQuery(String query) {
        if (query == null) {
            throw new IllegalArgumentException("Query cannot be null");
        }
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

    protected String extractVideoId(String responseJSON){
        JSONExtractor je = new JSONExtractor();
        JSONArray items = je.extractList(responseJSON, "items");
        JSONObject item = items.getJSONObject(0);
        JSONObject id = item.getJSONObject("id");
        return id.getString("videoId");
    }

    public String videoID(String query){
        String formattedUrl = formatURL("1", "video", "10", query);
        String response = RequestHandler.sendHTTPRequest(formattedUrl, new String[]{}, new String[]{}, "GET", "");

        return  extractVideoId(response);
    }

    private int extractVideoLen(String responseJSON){
        JSONObject jsonObject = new JSONObject(responseJSON);
        JSONArray items = jsonObject.getJSONArray("items");
        JSONObject item = items.getJSONObject(0);
        JSONObject contentDetails = item.getJSONObject("contentDetails");
        String durationString = contentDetails.getString("duration");

        int indexT = durationString.indexOf('T');
        int indexM = durationString.indexOf('M');
        int indexS = durationString.indexOf('S');
        int time = (durationString.charAt(indexT + 1) - '0') * 60000;

        if(indexS != -1) {
            if (indexS - indexM == 2)
                time += (durationString.charAt(indexM + 1) - '0') * 1000;
            else {
                time += (durationString.charAt(indexM + 1) - '0') * 10000 + (durationString.charAt(indexM + 2) - '0') * 1000;
            }
        }
        return time;
    }

    public int videoLen(String id){
        RequestHandler rh = new RequestHandler();
        String[] headerName = {"Accept"};
        String[] headerValue = {"application/json"};
        String response = rh.sendHTTPRequest("https://www.googleapis.com/youtube/v3/videos?part=contentDetails&id=" + id + "&key=" + authKey, headerName , headerValue);
        return extractVideoLen(response);
    }

    public boolean isLenCompatible(String id, int otherVidLen, int maxDelta){
        return abs(videoLen(id) - otherVidLen) < maxDelta;
    }
}
