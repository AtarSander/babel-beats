package beats.babel.babelbeats;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class SpotifyUserTests {
    @Test
    public void testConstructor() {
        String userToken = "example_token";
        String refreshToken = "example_refresh_token";
        SpotifyUser spotifyUser = new SpotifyUser(userToken, refreshToken);

        assertThat(spotifyUser.getToken()).isEqualTo(userToken);
        assertThat(spotifyUser.getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test
    public void isConnectedShouldReturnTrueWhenTokenIsNotNull() {
        SpotifyUser spotifyUser = new SpotifyUser("example_token", "example_refresh_token");
        boolean isConnected = spotifyUser.isConnected();

        assertThat(isConnected).isTrue();
    }

    @Test
    public void isConnectedShouldReturnFalseWhenTokenIsNull() {
        SpotifyUser spotifyUser = new SpotifyUser(null, "example_refresh_token");
        boolean isConnected = spotifyUser.isConnected();

        assertThat(isConnected).isFalse();
    }
}
