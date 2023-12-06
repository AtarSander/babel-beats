package beats.babel.babelbeats;

import beats.babel.babelbeats.controller.SpotifyController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class BabelBeatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BabelBeatsApplication.class, args);

		SpotifyController sc = new SpotifyController();
		SpotifyHandler sh = new SpotifyHandler();

		while(!sc.hasLoggedUser()){
			;
		}
		SpotifyUser su = new SpotifyUser(sc.getUserToken(), sc.getRefreshToken());
		sh.setSpotifyUser(su);
		Song[] songs = sh.getPlaylistSongs(sh.getRecommendedPlaylist(0, "german"), 5);
//		for (Song s : songs){
//			System.out.println(s.toString());
//		}
		String name = songs[0].toString();

		YoutubeSearcher ys = new YoutubeSearcher();

		String url =  ys.urlSearch(name);
		MusicDownloader.download(url, name);

		GeniusHandler gh = new GeniusHandler();
		String lyrics = gh.getLyrics(name);
//		DeepLHandler dh = new DeepLHandler();
//		dh.translate(lyrics, "PL");
		Timestamper ts = new Timestamper();
		List<Pair> pairs = ts.getTimestamps(name);

//		String playlistId = sh.getRecommendedPlaylist(0, "Polish");
	}
}
