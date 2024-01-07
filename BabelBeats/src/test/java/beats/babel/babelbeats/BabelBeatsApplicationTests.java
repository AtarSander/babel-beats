package beats.babel.babelbeats;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

class BabelBeatsApplicationTests {

	@Test
	void testTimestamper() {
		Timestamper ts = new Timestamper();
	}

	@Test
	void testMusicDownloader() {
		String url = "https://www.youtube.com/watch?v=DAOZJPquY_w&ab_channel=PostMaloneVEVO";
		String name = "Test Song";
		int exitCode = MusicDownloader.download(url, name);
		assertThat(exitCode).isZero();
	}



	@Test
	void testWebScraper() {
	}

	@Test
	public void testJSONExtractextract() {
		JSONExtractor jsonExtractor = new JSONExtractor();
		String json = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
		String key = "key1";
		String result = jsonExtractor.extract(json, key);
		assertThat(result).isEqualTo("value1");
	}

	@Test
	public void testJSONExtractextractList() {
		JSONExtractor jsonExtractor = new JSONExtractor();
		String json = "{\"key1\":[\"value1\",\"value2\"]}";
		String key = "key1";
		JSONArray result = jsonExtractor.extractList(json, key);
		assertThat(result).containsExactly("value1", "value2");
	}

	@Test
	void testSpotifyHandler() {
	}

	@Test
	void testGeniusHandler() {
	}

	@Test
	void testDeepLHandler() {
	}

	@Test
	void testArtist() {
		Image testImage = new Image(40, 40, "www.testimageurl.com");
		String[] testGenres = {"Genre1", "Genre2", "Genre3", "Genre4", "Genre5"};
		Artist artist = new Artist("Jan Kowalski", testGenres, testImage);
		assertThat(artist.getName()).isEqualTo("Jan Kowalski");
		assertThat(artist.getGenres()).isEqualTo(testGenres);
		assertThat(artist.getImage()).isEqualTo(testImage);
		assertThat(artist.toString()).isEqualTo("Jan Kowalski");
	}

	@Test
	void testSong() {
		Image testImage1 = new Image(40, 40, "www.testimageurl1.com");
		String[] testGenres1 = {"Genre1", "Genre2", "Genre3", "Genre4", "Genre5"};
		Artist artist1 = new Artist("Jan Kowalski", testGenres1, testImage1);
		Image testImage2 = new Image(70, 90, "www.testimageurl2.com");
		String[] testGenres2 = {"Genre1", "Genre2", "Genre3", "Genre4", "Genre5"};
		Artist artist2 = new Artist("Marek Twardoch", testGenres2, testImage2);
		Image testImage3 = new Image(150, 150, "www.testimageurl3.com");
		String[] testGenres3 = {"Genre1", "Genre2", "Genre3", "Genre4", "Genre5"};
		Artist artist3 = new Artist("Maria Kobyla", testGenres3, testImage3);
		Artist[] testArtists ={artist1, artist2, artist3};
		Song song = new Song(testArtists, "testSong", testImage1, "testID", 10);
		assertThat(song.getArtist()).isEqualTo(testArtists);
		assertThat(song.getName()).isEqualTo("testSong");
		assertThat(song.getImage()).isEqualTo(testImage1);
		assertThat(song.getId()).isEqualTo("testID");
		assertThat(song.getDuration()).isEqualTo(10);
		assertThat(song.toString()).isEqualTo(testArtists[0] + " " + "testSong");
	}

}
