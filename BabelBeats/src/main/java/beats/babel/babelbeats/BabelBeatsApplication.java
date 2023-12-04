package beats.babel.babelbeats;

import beats.babel.babelbeats.controller.SpotifyController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BabelBeatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BabelBeatsApplication.class, args);
		WebScraper ws = new WebScraper("https://genius.com/Travis-scott-thank-god-lyrics");
//		SpotifyHandler sh = new SpotifyHandler();
//		sh.getUsersFavs();
		ws.scrapeLyrics();

//		SpotifyController sc = new SpotifyController();
//		while(!sc.hasLoggedUser()){
//		;
//		}
//		SpotifyUserToken sut = new SpotifyUserToken(sc.getUserToken(), sc.getRefreshToken());
//		sh.setUserToken(sut);
////		System.out.println(sh.getUserCountry());
//		while (true) {
////			sh.startPlayback();
////			sh.pausePlayback();
//		}

	}
}
