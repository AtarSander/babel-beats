package beats.babel.babelbeats;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class WebScraperTests {

    @Test
    public void testScrapeLyrics() throws IOException {
        String url = "https://example.com";
        String mockHtml = "<div data-lyrics-container=\"true\">Lyrics line 1<br>Lyrics line 2<br></div>";
        WebScraper webScraper = createMockedWebScraper(mockHtml);
        String result = webScraper.scrapeLyrics(url);

        assertThat(result).isEqualTo("Lyrics line 1\nLyrics line 2\n");
    }

    @Test
    public void testScrapeLyricsWithIOException() throws IOException {
        String url = "https://example.com";
        WebScraper webScraper = createMockedWebScraperWithIOException();
        String result = webScraper.scrapeLyrics(url);

        assertThat(result).isEmpty();
    }

    private WebScraper createMockedWebScraper(String mockHtml) throws IOException {
        Document mockDocument = Jsoup.parse(mockHtml);
        WebScraper webScraper = mock(WebScraper.class);
        when(webScraper.scrapeLyrics(any())).thenCallRealMethod();
        when(Jsoup.connect(any()).userAgent(any()).timeout(any()).get()).thenReturn(mockDocument);
        return webScraper;
    }

    private WebScraper createMockedWebScraperWithIOException() throws IOException {
        WebScraper webScraper = mock(WebScraper.class);
        when(webScraper.scrapeLyrics(any())).thenCallRealMethod();
        when(Jsoup.connect(any()).userAgent(any()).timeout(any()).get()).thenThrow(IOException.class);
        return webScraper;
    }
}