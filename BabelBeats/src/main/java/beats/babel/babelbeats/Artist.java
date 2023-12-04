package beats.babel.babelbeats;


import java.util.Arrays;

public class Artist {
    private final String name;
    private final String[] genres;
    private final Image image;

    public Artist(String name, String[] genres, Image image){
        this.name = name;
        this.genres = genres;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String[] getGenres() {
        return genres;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                ", genres=" + Arrays.toString(genres) +
                '}';
    }
}
