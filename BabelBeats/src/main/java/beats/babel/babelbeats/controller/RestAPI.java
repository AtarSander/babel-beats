package beats.babel.babelbeats.controller;
import beats.babel.babelbeats.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class RestAPI {
    private final SpotifyHandler sh;
    private final SongDBService songDBService;

    public RestAPI(SongDBService songDBService){
        this.sh = new SpotifyHandler();
        this.songDBService = songDBService;
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
        return sh.isPlaying(spotifyUser);
    }

    @GetMapping("/recommendGenres")
    public String recommendGenre(@RequestParam(required = true)String userToken, @RequestParam(required = true)String refreshToken){
        SpotifyUser spotifyUser = new SpotifyUser(userToken, refreshToken);
        JSONArray ja = new JSONArray();
        String[] genres = Arrays.copyOfRange(sh.countUsersGenres(spotifyUser), 0, 8);
        JSONObject jo = new JSONObject();
        for (String genre : genres) {
            jo.put("genre", genre);
            Vector<String> icons = new Vector<String>();
            for (Artist artist : sh.getArtists()) {
                if (Arrays.asList(artist.getGenres()).contains(genre)) {
                    icons.add(artist.getImage().getImageURL());
                }
            }
            jo.put("artistIcon", icons);
            ja.put(new JSONObject(jo.toString()));
            jo.clear();
        }

        return ja.toString();
    }

    @GetMapping("/loadRecommendedSong")
    public void loadRecommendedSong(@RequestParam(required = true)String userToken, @RequestParam(required = true)String refreshToken, String genre, String Language){
        SpotifyUser su = new SpotifyUser(userToken, refreshToken);
        YoutubeSearcher ys = new YoutubeSearcher();
        GeniusHandler gh = new GeniusHandler();
        Timestamper ts = new Timestamper();
        String videoID = "";
        int songIndex = 0;
        JSONObject songData;
        SongRecord newRecord;
        long size;
        Song[] songs = sh.getPlaylistSongs(sh.getRecommendedPlaylist(4,"english", su), 15, su);

        for (int i = 0; i < songs.length; i++) {
            videoID = ys.videoID(songs[i].toString());

            if (songs[i].getDuration() < 5 * 60 && ys.isLenCompatible(videoID, songs[i].getDuration(), 5)) {
                songIndex = i;
                break;
            }
        }
        String name = songs[songIndex].toString();
        if (isSongInDatabase(name.replace(" ", "_")))
        {
            newRecord = getRecordByTitle(name.replace(" ", "_"));
        }
        else
        {
            gh.getLyricsToFile(name, "EN", true);
            MusicDownloader.download("https://www.youtube.com/watch?v=" + videoID, name);
            size = getNumberOfRecords();
            songData = ts.saveTimestamps(name.replace(" ", "_"), size);
            newRecord = new SongRecord(songData);
            addRecord(newRecord);
        }
//        saveTitles(songs);
    }

    @GetMapping("/seekPosition")
    public void seekPosition(@RequestParam(required = true)String userToken, @RequestParam(required = true)String refreshToken, @RequestParam(required = true)int timeDiff){
        SpotifyUser spotifyUser = new SpotifyUser(userToken, refreshToken);
        sh.seekPosition(spotifyUser, timeDiff);
    }

    @PostMapping("/add")
    public void addRecord(@RequestBody SongRecord newRecord) {
        songDBService.addRecord(newRecord);
    }

    @GetMapping("/checkSong/{title}")
    public boolean isSongInDatabase(@PathVariable String title) {
        return songDBService.isSongInDatabase(title);
    }

    @GetMapping("/byTitle/{title}")
    public SongRecord getRecordByTitle(@PathVariable String title) {
        return songDBService.getRecordByTitle(title);
    }

    @GetMapping("/count")
    public long getNumberOfRecords() {
        return songDBService.getNumberOfRecords();
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
