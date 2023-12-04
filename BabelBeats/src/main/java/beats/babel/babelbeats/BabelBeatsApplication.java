package beats.babel.babelbeats;

import beats.babel.babelbeats.controller.SpotifyController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
		for (Artist a : su.getTopArtists()){
			System.out.printf(a.toString() + '\n');
		}

		Map<String, Integer> genres = sh.countUsersGenres();
		genres.forEach((genre, count) ->{
			System.out.println("Genre: " + genre + ", Count: " + count);
		});

		}

}
