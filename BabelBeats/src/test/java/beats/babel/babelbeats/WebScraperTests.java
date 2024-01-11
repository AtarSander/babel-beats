package beats.babel.babelbeats;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;



public class WebScraperTests {

    @Test
    public void testScrapeLyrics() {
        String url = "https://genius.com/Kendrick-lamar-humble-lyrics";
        WebScraper webScraper = new WebScraper(6000);
        String result = webScraper.scrapeLyrics(url);
        String expected = "[Intro]\n" +
                "\n" +
                "Nobody pray for me\n" +
                "It been that day for me\n" +
                "Way (Yeah, yeah)";
        result = result.substring(0, Math.min(result.length(), 68));
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testScrapeLyricsWithIOException(){
        String url = "https://example.com";
        WebScraper webScraper = new WebScraper(5000);
        String result = webScraper.scrapeLyrics(url);

        assertThat(result).isEmpty();
    }


}