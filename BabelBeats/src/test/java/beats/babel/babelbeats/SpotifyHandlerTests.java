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
    public void testGetUserCountry() {
        SpotifyUser spotifyUser = mock(SpotifyUser.class);
        when(spotifyUser.getToken()).thenReturn("example_token");
        String country = spotifyHandler.getUserCountry(spotifyUser);

        assertNotNull(country);
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

    @Test
    public void testSeekPosition() {
        SpotifyUser spotifyUser = mock(SpotifyUser.class);
        when(spotifyUser.getToken()).thenReturn("example_token");

        assertDoesNotThrow(() -> spotifyHandler.seekPosition(spotifyUser, 30));
    }

    @Test
    public void testGetUsersFavArtists() {
        SpotifyUser spotifyUser = mock(SpotifyUser.class);
        when(spotifyUser.getToken()).thenReturn("example_token");
        Artist[] artists = spotifyHandler.getUsersFavArtists(spotifyUser);

        assertNotNull(artists);
    }

    @Test
    public void testCountUsersGenres() {
        SpotifyUser spotifyUser = mock(SpotifyUser.class);
        when(spotifyUser.getToken()).thenReturn("example_token");

        String[] genres = spotifyHandler.countUsersGenres(spotifyUser);

        assertNotNull(genres);
    }

    @Test
    public void testGetRecommendedPlaylist() {
        SpotifyUser spotifyUser = mock(SpotifyUser.class);
        when(spotifyUser.getToken()).thenReturn("example_token");
        String playlistId = spotifyHandler.getRecommendedPlaylist(0, "pop", spotifyUser);

        assertNotNull(playlistId);
    }

    @Test
    public void testGetPlaylistSongs() {
        SpotifyUser spotifyUser = mock(SpotifyUser.class);
        when(spotifyUser.getToken()).thenReturn("example_token");
        Song[] songs = spotifyHandler.getPlaylistSongs("example_playlist_id", 10, spotifyUser);

        assertNotNull(songs);
    }
}
