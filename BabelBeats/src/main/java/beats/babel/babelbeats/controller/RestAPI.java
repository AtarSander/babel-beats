package beats.babel.babelbeats.controller;
import beats.babel.babelbeats.Song;
import beats.babel.babelbeats.YoutubeSearcher;
import beats.babel.babelbeats.Timestamper;
import beats.babel.babelbeats.SpotifyHandler;
import beats.babel.babelbeats.MusicDownloader;
import beats.babel.babelbeats.GeniusHandler;
import beats.babel.babelbeats.SpotifyUser;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



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
        Song[] songs = sh.getPlaylistSongs(sh.getRecommendedPlaylist(3,"english", su), 5, su);
        String name = songs[0].toString();
        YoutubeSearcher ys = new YoutubeSearcher();
        String url =  ys.urlSearch(name);
		MusicDownloader.download(url, name);
        GeniusHandler gh = new GeniusHandler();
		gh.getLyricsToFile(name, "EN", true);
        Timestamper ts = new Timestamper();
        ts.saveTimestamps(name.replace(" ", "_"));
        saveTitles(songs);

    }
    private void saveTitles(Song[] titles)
    {
        Map<String, String> titleMap = new HashMap<>();
        for (int i = 0; i < titles.length; i++) {
            titleMap.put(String.valueOf(i + 1), titles[i].toString());
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(titleMap);
            File file = new File("src/main/java/resources/lyrics/songQueue.json");
            objectMapper.writeValue(file, titleMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
