package beats.babel.babelbeats;

public class Main {
    public static void main(String[] args) {
        String clientId = "77e15947cd8a4be0aacdc89c22b18121";
        String clientSecret = "f352aa77fabb4d2984053fa31e51416c";
        SpotifyHandler s = new SpotifyHandler();
        s.setAccessToken();
        System.out.println(s.getAccessToken());
    }
}
