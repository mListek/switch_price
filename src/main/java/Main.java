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
        "https://mediamarkt.pl/konsole-i-gry/konsola-nintendo-switch-lite-turkusowa",
        "https://www.x-kom.pl/p/649842-konsola-nintendo-nintendo-switch-lite-niebieski.html",
        "https://www.morele.net/nintendo-switch-lite-blue-5947533/",
        "https://www.amazon.pl/Konsola-Nintendo-Switch-kolorze-niebieskim/dp/B092JL5NS2/",
        "https://www.neonet.pl/konsole-i-gry/nintendo-switch-lite-grey.html"
    ));

    System.out.println("Nintendo Switch Lite:");

//    String euroPrice = getPriceEuro(URLS.get(0));
//    System.out.println("euro.com.pl -> " + euroPrice + " zł");
//
//    String mediaMarktPrice = getPriceMediaMarkt(URLS.get(1));
//    System.out.println("mediamarkt.pl -> " + mediaMarktPrice + " zł");
//
//    String xkomPrice = getPriceXkom(URLS.get(2));
//    System.out.println("x-kom.pl -> " + xkomPrice + " zł");
//
//    String morelePrice = getPriceMorele(URLS.get(3));
//    System.out.println("morele.net -> " + morelePrice + " zł");

//    String amazonPrice = getPriceAmazon(URLS.get(4));
//    System.out.println("amazon.pl -> " + amazonPrice + " zł");

  }

  public static String getPriceAmazon(String url) {
    try {
      Document doc = Jsoup
          .connect(url)
          .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
          .header("Accept-Encoding", "gzip, deflate")
          .header("Accept", "*/*")
          .get();

      Element element = doc.selectFirst("span.a-offscreen");

      return element.html().replace("&nbsp;", "");

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getPriceMorele(String url) {
    try {
      Document doc = Jsoup
          .connect(url)
          .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
          .header("Accept-Encoding", "gzip, deflate")
          .header("Accept", "*/*")
          .get();

      Element element = doc.selectFirst("div#product_price_brutto");

      return element.attr("content");

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getPriceXkom(String url) {
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
