package beats.babel.babelbeats;

import beats.babel.babelbeats.controller.RestAPI;
import beats.babel.babelbeats.controller.SpotifyController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BabelBeatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BabelBeatsApplication.class, args);

		SpotifyController sc = new SpotifyController();
//		SpotifyHandler sh = new SpotifyHandler();
		while (!sc.hasLoggedUser()) {
			try {
				Thread.sleep(1000); // Add a short delay to avoid busy-waiting
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		SpotifyUser su = new SpotifyUser(sc.getUserToken(), sc.getRefreshToken());
		sh.setSpotifyUser(su);
		RestAPI restapi = new RestAPI(sh);

//		Song[] songs = sh.getPlaylistSongs(sh.getRecommendedPlaylist(13, "english"), 5);

//		String name = songs[0].toString();
//
//		YoutubeSearcher ys = new YoutubeSearcher();
//
//		String url =  ys.urlSearch(name);
//		MusicDownloader.download(url, name);
//
//		GeniusHandler gh = new GeniusHandler();
//		gh.getLyricsToFile(name, "EN", true);
////		DeepLHandler dh = new DeepLHandler();
////		dh.translate(lyrics, "PL");
//		Timestamper ts = new Timestamper();
//		List<Pair> pairs = ts.getTimestamps(name.replace(" ", "_"));
////		List<Pair> pairs = ts.getTimestamps("Post_Malone_Mourning");
//		for(Pair pair:pairs){
//			System.out.println(pair.toString());
//		}
	}
}
