package beats.babel.babelbeats;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        YoutubeSearcher ys = new YoutubeSearcher();
        String name = "city morgue you can smd";
        String url =  ys.urlSearch(name);
        name = name.replace(" ", "_");
        MusicDownloader.download(url, name);

    }
}
