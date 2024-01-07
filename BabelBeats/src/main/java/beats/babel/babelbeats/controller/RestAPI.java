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

    @GetMapping("/getPlaybackState")
    public boolean getPlaybackState(@RequestParam(required = true)String userToken, @RequestParam(required = true)String refreshToken){
        SpotifyUser spotifyUser = new SpotifyUser(userToken, refreshToken);
        return sh.getPlaybackState(spotifyUser);
    }

    @GetMapping("/loadRecommendedSong")
    public void loadRecommendedSong(@RequestParam(required = true)String userToken, @RequestParam(required = true)String refreshToken){
        SpotifyUser su = new SpotifyUser(userToken, refreshToken);
        YoutubeSearcher ys = new YoutubeSearcher();
        GeniusHandler gh = new GeniusHandler();
        Timestamper ts = new Timestamper();
        String videoID = "";
        int songIndex = 0;
        Song[] songs = sh.getPlaylistSongs(sh.getRecommendedPlaylist(0,"english", su), 15, su);

        for (int i = 0; i < songs.length; i++) {
            videoID = ys.videoID(songs[i].toString());

            if (songs[i].getDuration() < 5 * 60 && ys.isLenCompatible(videoID, songs[i].getDuration(), 5)) {
                songIndex = i;
                break;
            }
        }
        String name = songs[songIndex].toString();
        gh.getLyricsToFile(name, "EN", true);
        MusicDownloader.download("https://www.youtube.com/watch?v=" + videoID, name);
        ts.saveTimestamps(name.replace(" ", "_"));
//        saveTitles(songs);
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
