package beats.babel.babelbeats;

import beats.babel.babelbeats.controller.SpotifyController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BabelBeatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BabelBeatsApplication.class, args);
		SpotifyController sc = new SpotifyController();
		while(!sc.hasLoggedUser()){
		;
		}
		SpotifyUserToken sut = new SpotifyUserToken(sc.getUserToken(), sc.getRefreshToken());
		SpotifyHandler sh = new SpotifyHandler();
		sh.setUserToken(sut);
//		System.out.println(sh.getUserCountry());
		while (true) {
//			sh.startPlayback();
//			sh.pausePlayback();
			sh.getUsersFavs();
		}
	}
}
