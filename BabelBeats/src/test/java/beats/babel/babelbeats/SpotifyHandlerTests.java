package beats.babel.babelbeats;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;


public class SpotifyHandlerTests {

    private SpotifyHandler spotifyHandler;

    @BeforeEach
    public void setUp() {
        spotifyHandler = new SpotifyHandler();
    }

    @Test
    public void testSetAccessToken() {
        assertNotNull(spotifyHandler.getAccessToken());
    }


    @Test
    public void testStartPlayback() {
        SpotifyUser spotifyUser = mock(SpotifyUser.class);
        when(spotifyUser.getToken()).thenReturn("example_token");

        assertDoesNotThrow(() -> spotifyHandler.startPlayback(spotifyUser));
    }

    @Test
    public void testPausePlayback() {
        SpotifyUser spotifyUser = mock(SpotifyUser.class);
        when(spotifyUser.getToken()).thenReturn("example_token");

        assertDoesNotThrow(() -> spotifyHandler.pausePlayback(spotifyUser));
    }

}
