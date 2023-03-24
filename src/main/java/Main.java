import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        final String URL = "https://www.euro.com.pl/konsole-nintendo-switch/nintendo-switch-lite-niebieski.bhtml";
        final String USER_AGENT = "python-requests/2.28.2";
        Document doc;

        try {
            System.out.println("Sending request...");
            doc = Jsoup
                    .connect(URL)
                    .userAgent(USER_AGENT)
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Accept", "*/*")
                    .get();
            System.out.println("Success!");

            Elements elements = doc.select("script[type=application/ld+json]");

            for (Element element : elements) {
                try {
                    JSONObject jsonObject = new JSON
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
