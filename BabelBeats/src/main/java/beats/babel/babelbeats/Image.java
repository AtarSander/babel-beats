package beats.babel.babelbeats;

public class Image {
    private final int width;
    private final int height;
    private final String imageURL;
    public Image(int width, int height, String imageURL){
        this.width = width;
        this.height = height;
        this.imageURL = imageURL;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getImageURL() {
        return imageURL;
    }

    @Override
    public String toString() {
        return "Image{width="+width+", height="+height+", imageURL='"+imageURL+"'}";
    }
}
