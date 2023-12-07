package beats.babel.babelbeats;

public class Song {
    private final Artist[] artists;
    private final String name;
    private final Image image;
    private final String id;

    public Song(Artist[] artists, String name, Image image, String id)
    {
        this.artists = artists;
        this.name = name;
        this.image = image;
        this.id = id;
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
