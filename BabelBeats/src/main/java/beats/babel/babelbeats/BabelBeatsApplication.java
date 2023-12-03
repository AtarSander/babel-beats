package beats.babel.babelbeats;

import beats.babel.babelbeats.controller.spotifyUserToken;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BabelBeatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BabelBeatsApplication.class, args);
		spotifyUserToken sut = new spotifyUserToken();

	}
}
