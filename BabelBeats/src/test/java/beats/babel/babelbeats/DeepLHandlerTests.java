package beats.babel.babelbeats;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class DeepLHandlerTests {

    @Test
    public void testTranslateWithValidInput() {
        String inputText = "Hello, how are you?";
        String targetLang = "es";  // Spanish
        DeepLHandler deepLHandler = new DeepLHandler();
        String translatedText = deepLHandler.translate(inputText, targetLang);

        assertNotNull(translatedText);
    }

    @Test
    public void testTranslateWithInvalidInput() {
        String inputText = null;
        String targetLang = "fr";  // French
        DeepLHandler deepLHandler = new DeepLHandler();
        String translatedText = deepLHandler.translate(inputText, targetLang);

        assertNull(translatedText);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        String inputText = "";
        String targetLang = "de";  // German
        DeepLHandler deepLHandler = new DeepLHandler();
        String translatedText = deepLHandler.translate(inputText, targetLang);

        assertEquals("", translatedText);
    }

}
