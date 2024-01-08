package beats.babel.babelbeats;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

class BabelBeatsApplicationTests {

	@Test
	void testTimestamper() {
		String testName = "okudakun_oeoe";
		Timestamper ts = new Timestamper();
		JSONObject testData = ts.saveTimestamps(testName, 0);
		try {
			String returnName = (String) testData.get("title");
			Long returnId = (Long) testData.get("_id");
			List<SongRecord.Timestamp> returnTimestamps = (List<SongRecord.Timestamp>) testData.get("timestamps");

			assertThat(returnName).isEqualTo(testName);
			assertThat(returnId).isEqualTo(0);
			assertThat(returnTimestamps.size()).isNotEqualTo(0);
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
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
		WebScraper scrape = new WebScraper(6000);
		String returnLyrics = scrape.scrapeLyrics("https://genius.com/The-weeknd-save-your-tears-lyrics");

		assertThat(returnLyrics.length()).isNotEqualTo(0);
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
		List<String> resultList = new ArrayList<>();
		for (int i = 0; i < result.length(); i++) {
			try
			{
				resultList.add(result.getString(i));
			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
		}

		assertThat(resultList).containsExactlyInAnyOrder("value1", "value2");
	}

	@Test
	void testSpotifyHandler() {
		SpotifyHandler sh = new SpotifyHandler();

	}

	@Test
	void testGeniusHandler() {
		GeniusHandler gh = new GeniusHandler();
		String name = "ed sheeran shape of you";
		gh.getLyricsToFile(name, "EN", true);
		File file = new File("src/main/resources/lyrics/plainLyrics/Shape_of_You.json");

		assertThat(file).exists();
		assertThat(file).isNotEmpty();
	}

	@Test
	void testDeepLHandler() {
	}

	@Test
	void testYoutubeSearcher() {
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

	@Test
	void testSongRecord() {
		JSONObject songData = new JSONObject();
		JSONArray timestampsArray = new JSONArray();
		JSONObject timestampJson = new JSONObject();
		SongRecord songRecord = new SongRecord(songData);
		try {
			songData.put("_id", 1);
			songData.put("title", "Test Song");
			timestampJson.put("value", 30.5);
			timestampJson.put("key", "Test Key");
			timestampsArray.put(timestampJson);
			songData.put("timestamps", timestampsArray);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		assertThat(songRecord).isNotNull();
		assertThat(songRecord.getTitle()).isEqualTo("Test Song");
		assertThat(songRecord.get_id()).isEqualTo(1);
		List<SongRecord.Timestamp> timestamps = songRecord.getTimestamps();
		assertThat(timestamps).hasSize(1);
		SongRecord.Timestamp firstTimestamp = timestamps.get(0);
		assertThat(firstTimestamp.getValue()).isEqualTo(30.5);
		assertThat(firstTimestamp.getKey()).isEqualTo("Test Key");
	}

}
