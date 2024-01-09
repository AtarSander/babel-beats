package beats.babel.babelbeats;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class YoutubeSearcherTests {

    @Test
    public void testVideoID() {
        YoutubeSearcher youtubeSearcher = createMockedYoutubeSearcher("videoId123");
        String result = youtubeSearcher.videoID("testQuery");

        assertThat(result).isEqualTo("videoId123");
    }

    @Test
    public void testVideoLen() {
        YoutubeSearcher youtubeSearcher = createMockedYoutubeSearcherWithLen("5");
        int result = youtubeSearcher.videoLen("testVideoId");

        assertThat(result).isEqualTo(5);
    }

    @Test
    public void testIsLenCompatible() {
        YoutubeSearcher youtubeSearcher = createMockedYoutubeSearcherWithLen("10");
        boolean result = youtubeSearcher.isLenCompatible("testVideoId", 8, 3);

        assertThat(result).isTrue();
    }

    private YoutubeSearcher createMockedYoutubeSearcher(String videoId) {
        YoutubeSearcher youtubeSearcher = Mockito.spy(new YoutubeSearcher());
        when(youtubeSearcher.videoID(any())).thenCallRealMethod();
        when(youtubeSearcher.extractVideoId(any())).thenReturn(videoId);
        when(youtubeSearcher.videoLen(any())).thenReturn(0);
        return youtubeSearcher;
    }

    private YoutubeSearcher createMockedYoutubeSearcherWithLen(String videoLen) {
        YoutubeSearcher youtubeSearcher = Mockito.spy(new YoutubeSearcher());
        when(youtubeSearcher.videoLen(any())).thenReturn(Integer.parseInt(videoLen));
        return youtubeSearcher;
    }
}