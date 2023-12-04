//package beats.babel.babelbeats;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.IOException;
//
//public class WebScraper {
//    private final String url;
//    private int timeout;
//    public WebScraper(String url)
//    {
//        this.url = url;
//    }
//
//    public String scrapeLyrics()
//    {
//        try {
//            Document doc = Jsoup.connect(url).userAgent("Mozilla").timeout(timeout).get();
//            Elements body = doc.select();
//            System.out.println(body.select("br"));
//        }
//        catch(IOException e) {
//            e.printStackTrace();
//    }
//        return "0";
//    }
//
//}
