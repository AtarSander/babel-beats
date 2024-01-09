package beats.babel.babelbeats;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


public class MusicDownloaderTests {

    @Test
    public void testDownloadWithValidUrl() {
        String validUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        String name = "TestSong";
        int exitCode = MusicDownloader.download(validUrl, name);

        assertEquals(0, exitCode);
    }

    @Test
    public void testDownloadWithInvalidUrl() {
        String invalidUrl = "https://www.youtube.com/watch_invalid_url";
        String name = "TestSong";
        int exitCode = MusicDownloader.download(invalidUrl, name);

        assertEquals(1, exitCode);
    }

    @Test
    public void testDownloadWithEmptyUrl() {
        String emptyUrl = "";
        String name = "TestSong";
        int exitCode = MusicDownloader.download(emptyUrl, name);

        assertEquals(1, exitCode);
    }
}