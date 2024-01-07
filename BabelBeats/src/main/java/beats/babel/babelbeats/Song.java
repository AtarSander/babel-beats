package beats.babel.babelbeats;

public class Song {
    private final Artist[] artists;
    private final String name;
    private final Image image;
    private final String id;
    private final int duration;

    public Song(Artist[] artists, String name, Image image, String id, int duration)
    {
        this.artists = artists;
        this.name = name;
        this.image = image;
        this.id = id;
        this.duration = duration;
    }

    public Artist[] getArtist() {
        return artists;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        StringBuilder stringOutput = new StringBuilder();
//        for (Artist artist : artists)
//        {
        stringOutput.append(artists[0]);
        stringOutput.append(" ");
//        }
        stringOutput.append(name);
        return stringOutput.toString();
    }
}
