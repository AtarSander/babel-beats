package beats.babel.babelbeats;

import java.util.Arrays;

public class Artist {
    private final String name;
    private final String[] genres;
    private final String pictureURL;

    public Artist(String name, String[] genres, String pictureURL){
        this.name = name;
        this.genres = genres;
        this.pictureURL = pictureURL;
    }

    public String getName() {
        return name;
    }

    public String[] getGenres() {
        return genres;
    }

    public String getPictureURL() {
        return pictureURL;
    }


    @Override
    public String toString() {
        return "beats.babel.babelbeats.Artist{" +
                "name='" + name + '\'' +
                ", genres=" + Arrays.toString(genres) +
                ", pictureURL='" + pictureURL + '\'' +
                '}';
    }
}
