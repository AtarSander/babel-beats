package beats.babel.babelbeats;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;


public class ImageTests {

    @Test
    public void testImageProperties() {

        int width = 100;
        int height = 150;
        String imageURL = "https://example.com/image.jpg";
        Image image = new Image(width, height, imageURL);

        assertThat(image.getWidth()).isEqualTo(width);
        assertThat(image.getHeight()).isEqualTo(height);
        assertThat(image.getImageURL()).isEqualTo(imageURL);
    }

    @Test
    public void testEquality() {
        Image image1 = new Image(100, 150, "https://example.com/image1.jpg");
        Image image2 = new Image(100, 150, "https://example.com/image1.jpg");
        Image differentImage = new Image(200, 300, "https://example.com/image2.jpg");

        assertThat(image1).isEqualTo(image2);
        assertThat(image1).isNotEqualTo(differentImage);
    }

    @Test
    public void testToString() {
        Image image = new Image(100, 150, "https://example.com/image.jpg");

        assertThat(image.toString()).isEqualTo("Image{width=100, height=150, imageURL='https://example.com/image.jpg'}");
    }
}
