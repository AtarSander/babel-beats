package beats.babel.babelbeats;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Vector;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class GeniusHandlerTests {

    @Test
    public void testGetLyricsWithValidQuery() {
        String query = "Bohemian Rhapsody";
        GeniusHandler geniusHandler = new GeniusHandler();
        Vector<String> lyrics = geniusHandler.getLyrics(query);

        assertNotNull(lyrics);
        assertFalse(lyrics.isEmpty());
    }

    @Test
    public void testGetLyricsWithInvalidQuery() {
        String query = "";
        GeniusHandler geniusHandler = new GeniusHandler();
        Vector<String> lyrics = geniusHandler.getLyrics(query);

        assertTrue(lyrics.isEmpty());
    }

    @Test
    public void testGetLyricsToFile() {
        String title = "Imagine";
        String language = "en";
        boolean formatName = true;
        GeniusHandler geniusHandler = new GeniusHandler();
        geniusHandler.getLyricsToFile(title, language, formatName);
        File file = new File("src/main/resources/lyrics/plainLyrics/Imagine.json");

        assertThat(file).exists();
        assertThat(file).isNotEmpty();
    }
}
