package beats.babel.babelbeats;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SongTests {

    @Test
    public void testSongProperties() {
        Artist artist = new Artist("Example Artist", new String[]{"Pop", "Rock"},
                new Image(200, 300, "https://example.com/artist-image.jpg"));
        Song song = new Song(new Artist[]{artist}, "Example Song",
                new Image(300, 400, "https://example.com/song-image.jpg"),
                "123456", 240);

        assertThat(song.getArtist()).containsExactly(artist);
        assertThat(song.getName()).isEqualTo("Example Song");
        assertThat(song.getImage().toString()).isEqualTo(new Image(300, 400, "https://example.com/song-image.jpg").toString());
        assertThat(song.getId()).isEqualTo("123456");
        assertThat(song.getDuration()).isEqualTo(240000);
    }

    @Test
    public void testToString() {
        Artist artist = new Artist("Example Artist", new String[]{"Pop", "Rock"},
                new Image(200, 300, "https://example.com/artist-image.jpg"));
        Song song = new Song(new Artist[]{artist}, "Example Song",
                new Image(300, 400, "https://example.com/song-image.jpg"),
                "123456", 240);

        assertThat(song.toString()).isEqualTo("Example Artist Example Song");
    }
}
