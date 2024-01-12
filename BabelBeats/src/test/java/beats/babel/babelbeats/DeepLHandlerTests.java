package beats.babel.babelbeats;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class DeepLHandlerTests {

    @Test
    public void testTranslateWithValidInput() {
        String inputText = "Hello, how are you?";
        List<String> input = new ArrayList<>();
        input.add(inputText);
        String targetLang = "es";  // Spanish
        String srcLang = "en";
        DeepLHandler deepLHandler = new DeepLHandler();
        String translatedText = String.valueOf(deepLHandler.translate(input, targetLang, srcLang));

        assertNotNull(translatedText);
    }

    @Test
    public void testTranslateWithInvalidInput() {
        String inputText = null;
        List<String> input = new ArrayList<>();
        input.add(inputText);
        String targetLang = "fr";  // French
        String srcLang = "pl";
        DeepLHandler deepLHandler = new DeepLHandler();

        assertNull(deepLHandler.translate(input, targetLang, srcLang));
    }

    @Test
    public void testTranslateWithEmptyInput() {
        String inputText = "";
        List<String> input = new ArrayList<>();
        input.add(inputText);
        String targetLang = "de";  // German
        String srcLang = "pl";
        DeepLHandler deepLHandler = new DeepLHandler();

        assertNull(deepLHandler.translate(input, targetLang, srcLang));
    }
}
