package beats.babel.babelbeats.controller;
import beats.babel.babelbeats.Song;
import beats.babel.babelbeats.YoutubeSearcher;
import beats.babel.babelbeats.Timestamper;
import beats.babel.babelbeats.SpotifyHandler;
import beats.babel.babelbeats.MusicDownloader;
import beats.babel.babelbeats.GeniusHandler;
import beats.babel.babelbeats.SpotifyUser;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;



@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class RestAPI {
    private final SpotifyHandler sh;

    public RestAPI(){
        this.sh = new SpotifyHandler();
    }
    @GetMapping("/resume")
    public void resumePlayback(@RequestParam(required = true)String userToken, @RequestParam(required = true)String refreshToken){
        SpotifyUser spotifyUser = new SpotifyUser(userToken, refreshToken);
        sh.startPlayback(spotifyUser);
    }

    @GetMapping("/pause")
    public void pausePlayback(@RequestParam(required = true)String userToken, @RequestParam(required = true)String refreshToken){
        SpotifyUser spotifyUser = new SpotifyUser(userToken, refreshToken);
        sh.pausePlayback(spotifyUser);
    }

    @GetMapping("/loadRecommendedSong")
    public void loadRecommendedSong(@RequestParam(required = true)String userToken, @RequestParam(required = true)String refreshToken){
        SpotifyUser su = new SpotifyUser(userToken, refreshToken);
//        safe 3
        Song[] songs = sh.getPlaylistSongs(sh.getRecommendedPlaylist(8,"english", su), 5, su);
        String name = songs[0].toString();
        YoutubeSearcher ys = new YoutubeSearcher();
        String url =  ys.urlSearch(name);
		MusicDownloader.download(url, name);
        GeniusHandler gh = new GeniusHandler();
		gh.getLyricsToFile(name, "EN", true);
        Timestamper ts = new Timestamper();
        ts.saveTimestamps(name.replace(" ", "_"));
        saveTitles(songs);
//        List<Pair> pairs = ts.getTimestamps(name.replace(" ", "_"));
//		for(Pair pair:pairs){
//			System.out.println(pair.toString());
//		}
    }
    private void saveTitles(Song[] titles)
    {
        String[] songsTitles= new String[titles.length];
        for (int i = 0; i < titles.length; i++) {
            songsTitles[i] = titles[i].toString();
        }
        String arrayAsString = String.join(System.lineSeparator(), songsTitles);
        String filePath = "src/main/resources/lyrics/songQueue.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(arrayAsString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
