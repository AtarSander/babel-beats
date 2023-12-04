package beats.babel.babelbeats;

public class Main {
    public static void main(String[] args) {
        YoutubeSearcher ys = new YoutubeSearcher();
        String url =  ys.urlSearch("city morgue you can smd");
        System.out.println(url);
    }
}
