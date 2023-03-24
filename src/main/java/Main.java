import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {

    final List<String> URLS = new ArrayList<>(List.of(
        "https://www.euro.com.pl/konsole-nintendo-switch/nintendo-switch-lite-niebieski.bhtml",
        "https://mediamarkt.pl/konsole-i-gry/konsola-nintendo-switch-lite-turkusowa"
    ));

    System.out.println("Nintendo Switch Lite:");

    String euroPrice = getPriceEuro(URLS.get(0));
    System.out.println("euro.com.pl -> " + euroPrice + " zł");

    String mediaMarktPrice = getPriceMediaMarkt(URLS.get(1));
    System.out.println("mediamarkt.pl -> " + mediaMarktPrice + " zł");
  }

  public static String getPriceMediaMarkt(String url) {
    try {
      Document doc = Jsoup
        .connect(url)
        .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
        .header("Accept-Encoding", "gzip, deflate")
        .header("Accept", "*/*")
        .get();

      Elements elements = doc.select("script[type=application/ld+json]");

      for (Element element : elements) {
          JsonObject jsonData = new Gson().fromJson(element.html(), JsonObject.class);
          if (!jsonData.has("offers")) continue;

          return jsonData.getAsJsonObject("offers").get("price").getAsString();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return "0";
  }

  public static String getPriceEuro(String url) {
    try {
      Document doc = Jsoup
        .connect(url)
        .userAgent("python-requests/2.28.2")
        .header("Accept-Encoding", "gzip, deflate")
        .header("Accept", "*/*")
        .get();

      Elements elements = doc.select("script[type=application/ld+json]");

      for (Element element : elements) {
        JsonObject jsonData = new Gson().fromJson(element.html(), JsonObject.class);
        if (!jsonData.has("offers")) continue;

        return jsonData.getAsJsonObject("offers").get("price").getAsString();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return "0";
  }
}
