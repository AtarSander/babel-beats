package beats.babel.babelbeats.controller;
import beats.babel.babelbeats.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class RestAPI {
    private final SpotifyHandler sh;
    private final SongDBService songDBService;
    private Map<String, String> mapLanguages;
    public RestAPI(SongDBService songDBService){
        this.sh = new SpotifyHandler();
        this.songDBService = songDBService;
        try{
            ObjectMapper om = new ObjectMapper();
            this.mapLanguages = om.readValue(new File("src/main/resources/languageMap.json"), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        String[] usersGenres = sh.countUsersGenres(spotifyUser);
        String[] genres = Arrays.copyOfRange(usersGenres, 0, Math.min(usersGenres.length, 8));
        JSONObject jo = new JSONObject();
        for (String genre : genres) {
            jo.put("genre", genre);
            Vector<String> icons = new Vector<String>();
            for (Artist artist : sh.getArtists()) {
                for (String g : artist.getGenres()) {
                    if (g.contains(genre) || icons.size() >= 4) {
                        icons.add(artist.getImage().getImageURL());
                        break;
                    }
                }
            }
            jo.put("artistIcon", icons);
            ja.put(new JSONObject(jo.toString()));
            jo.clear();
        }

        return ja.toString();
    }

    @GetMapping("/loadRecommendedSong")
    public SongRecord loadRecommendedSong(@RequestParam(required = true)String userToken, @RequestParam(required = true)String refreshToken, @RequestParam(required = true)int genre, @RequestParam(required = true)String targetLang, @RequestParam(required = true)String userLang){
        SpotifyUser su = new SpotifyUser(userToken, refreshToken);
        YoutubeSearcher ys = new YoutubeSearcher();
        GeniusHandler gh = new GeniusHandler();
        Timestamper ts = new Timestamper();
        List<String> blacklist = new ArrayList<>();
        String videoID = "";
        int songIndex = 4;
        JSONObject songData;
        SongRecord newRecord;
        long size;
        Song[] preShuffleSongs = sh.getPlaylistSongs(sh.getRecommendedPlaylist(genre, targetLang, su), 30, su);
        List<Song> songs = Arrays.asList(preShuffleSongs);
//        List<Song> temp = new ArrayList<>(songs);
//        temp.removeIf(Objects::isNull);
//        songs = temp;
//        Collections.shuffle(temp);

        while(true) {
//            for (int i = 0; i < songs.size(); i++) {
//                Song song = songs.get(i);
//                if (song.getDuration() < 5 * 60000 && !blacklist.contains(song.toString())) {
//                    videoID = ys.videoID(song.toString());
//                    if(ys.isLenCompatible(videoID, song.getDuration(), 5000))
//                        songIndex = i;
//                    break;
//                }
//            }

//        songIndex = 6;
        videoID = ys.videoID(songs.get(songIndex).toString());

            String name = songs.get(songIndex).toString();
            if (isSongInDatabase(name.replace(" ", "_"))) {
                newRecord = getRecordByTitle(name.replace(" ", "_"));
            }
            else
            {
                gh.getLyricsToFile(name, mapLanguages.get(targetLang), true);
                MusicDownloader.download("https://www.youtube.com/watch?v=" + videoID, name);
                size = getNumberOfRecords();
                try {
                    songData = ts.saveTimestamps(name.replace(" ", "_"), size, mapLanguages.get(userLang), mapLanguages.get(targetLang));
                }
                catch(Exception e){
                    blacklist.add(name);
                    continue;
                }
                if (songData.isEmpty()) {
                    blacklist.add(name);
                    continue;
                }
                newRecord = new SongRecord();
                newRecord.loadFromJSON(songData);
                addRecord(newRecord);
            }
            sh.playSongByID(su, songs.get(songIndex).getId());
            sh.pausePlayback(su);
//        saveTitles(songs);
            return newRecord;
        }
    }

    @GetMapping("/seekPosition")
    public void seekPosition(@RequestParam(required = true)String userToken, @RequestParam(required = true)String refreshToken, @RequestParam(required = true)int timeDiff){
        SpotifyUser spotifyUser = new SpotifyUser(userToken, refreshToken);
        sh.seekPosition(spotifyUser, timeDiff);
    }

    @GetMapping("/getSongPosition")
    public int getPosition(@RequestParam(required = true)String userToken, @RequestParam(required = true)String refreshToken){
        SpotifyUser spotifyUser = new SpotifyUser(userToken, refreshToken);
        return sh.getPosition(spotifyUser);
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
