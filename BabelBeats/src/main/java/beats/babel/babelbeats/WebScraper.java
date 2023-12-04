package beats.babel.babelbeats;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

public class WebScraper {
    private final String url;
    private int timeout;
    public WebScraper(String url)
    {
        this.url = url;
    }

    public String scrapeLyrics()
    {
        String lyrics = "";
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla").timeout(timeout).get();
            Elements lyricsContainers = doc.select("div[data-lyrics-container=true]");
            StringBuilder textWithLineBreaks = new StringBuilder();
            for (Element div : lyricsContainers) {
                textWithLineBreaks.append(div.html().replaceAll("<br>", "\n"));
            }
            Whitelist whitelist = Whitelist.none();
            lyrics = Jsoup.clean(textWithLineBreaks.toString(), "", whitelist, new Document.OutputSettings().prettyPrint(false));

        }
        catch(IOException e) {
            e.printStackTrace();
    }
        return lyrics;
    }

}
