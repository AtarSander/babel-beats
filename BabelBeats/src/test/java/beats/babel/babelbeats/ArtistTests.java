package beats.babel.babelbeats;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ArtistTests {

    @Test
    public void testArtistProperties() {
        String name = "Example Artist";
        String[] genres = {"Pop", "Rock"};
        Image image = new Image(200, 300, "https://example.com/artist-image.jpg");
        Artist artist = new Artist(name, genres, image);

        assertThat(artist.getName()).isEqualTo(name);
        assertThat(artist.getGenres()).isEqualTo(genres);
        assertThat(artist.getImage()).isEqualTo(image);
    }

    @Test
    public void testEquality() {
        Artist artist1 = new Artist("Example Artist", new String[]{"Pop", "Rock"},
                new Image(200, 300, "https://example.com/artist-image.jpg"));
        Artist artist2 = new Artist("Example Artist", new String[]{"Pop", "Rock"},
                new Image(200, 300, "https://example.com/artist-image.jpg"));
        Artist differentArtist = new Artist("Another Artist", new String[]{"Hip Hop"},
                new Image(300, 400, "https://example.com/another-artist-image.jpg"));

        assertThat(artist1.toString()).isEqualTo(artist2.toString());
        assertThat(artist1).isNotEqualTo(differentArtist);
    }

    @Test
    public void testToString() {
        Artist artist = new Artist("Example Artist", new String[]{"Pop", "Rock"},
                new Image(200, 300, "https://example.com/artist-image.jpg"));

        assertThat(artist.toString()).isEqualTo("Example Artist");
    }
}
