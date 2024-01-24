package beats.babel.babelbeats;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class YoutubeSearcherTests {

    @Test
    public void testVideoID() {
        YoutubeSearcher youtubeSearcher = new YoutubeSearcher();
        String result = youtubeSearcher.videoID("rockstar");

        assertThat(result).isNotEmpty();
    }

    @Test
    public void testVideoLen() {
        YoutubeSearcher youtubeSearcher = new YoutubeSearcher();
        String result = youtubeSearcher.videoID("rockstar");
        int len = youtubeSearcher.videoLen(result);

        assertThat(len).isEqualTo(218000);
    }

    @Test
    public void testIsLenCompatible() {
        YoutubeSearcher youtubeSearcher = new YoutubeSearcher();
        String id = youtubeSearcher.videoID("rockstar");
        boolean result = youtubeSearcher.isLenCompatible(id, 216000, 3000);

        assertThat(result).isTrue();
    }

}