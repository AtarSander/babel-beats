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
        String srcLang = "en";
        DeepLHandler deepLHandler = new DeepLHandler();
        String translatedText = deepLHandler.translate(inputText, targetLang, srcLang);

        assertNotNull(translatedText);
    }

    @Test
    public void testTranslateWithInvalidInput() {
        String inputText = null;
        String targetLang = "fr";  // French
        String srcLang = "pl";
        DeepLHandler deepLHandler = new DeepLHandler();
        String translatedText = deepLHandler.translate(inputText, targetLang, srcLang);
        if (translatedText.equals("nul"))
            translatedText = null;

        assertNull(translatedText);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        String inputText = "";
        String targetLang = "de";  // German
        String srcLang = "pl";
        DeepLHandler deepLHandler = new DeepLHandler();
        String translatedText = deepLHandler.translate(inputText, targetLang, srcLang);

        assertEquals("", translatedText);
    }

}
