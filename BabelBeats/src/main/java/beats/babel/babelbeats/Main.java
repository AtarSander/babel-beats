package beats.babel.babelbeats;

import beats.babel.babelbeats.spotifyHandler;

public class Main {
    public static void main(String[] args) {
        String clientId = "77e15947cd8a4be0aacdc89c22b18121";
        String clientSecret = "f352aa77fabb4d2984053fa31e51416c";
        spotifyHandler s = new spotifyHandler(clientId, clientSecret);
        s.setAccessToken();
        System.out.println(s.getAccessToken());
    }
}
