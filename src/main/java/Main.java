import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
  public static void main(String[] args) {

    final List<String> URLS = new ArrayList<>();
    final List<String> USER_AGENTS = new ArrayList<>(List.of(
        "python-requests/2.28.2",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36"
    ));

    // get all the links from a file
    try (Scanner scan =
             new Scanner(new File("src/main/resources/data.txt"))) {
      while(scan.hasNext()) {
        URLS.add(scan.nextLine());
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    System.out.println("Nintendo Switch Lite prices:");

    List<PriceJson> pricesJson = Collections.synchronizedList(new ArrayList<>());
    ExecutorService executorService = Executors.newFixedThreadPool(4);

    for (String url : URLS) {
      if (url.contains("mediamarkt.pl") || url.contains("x-kom.pl")) {
        PriceJson priceJson = new PriceJson(url, USER_AGENTS.get(1));
        pricesJson.add(priceJson);
      } else if (url.contains("euro.com.pl")) {
        PriceJson priceJson = new PriceJson(url, USER_AGENTS.get(0));
        pricesJson.add(priceJson);
      } else if (url.contains("morele.net")) {

      } else if (url.contains("amazon.pl")) {

      }
    }

    for (PriceJson priceJson : pricesJson) {
      Future<String> result = executorService.submit(priceJson);
      try {
        System.out.println("Future result euro.com.pl: " + result.get() + " zł");

      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }


    executorService.shutdown();

//    String euroPrice = getPriceJson(URLS.get(0), USER_AGENTS.get(0));
//    System.out.println("euro.com.pl -> " + euroPrice + " zł");
//
//    String mediaMarktPrice = getPriceJson(URLS.get(1), USER_AGENTS.get(1));
//    System.out.println("mediamarkt.pl -> " + mediaMarktPrice + " zł");
//
//    String xkomPrice = getPriceJson(URLS.get(2), USER_AGENTS.get(1));
//    System.out.println("x-kom.pl -> " + xkomPrice + " zł");
//
//    String morelePrice = getPriceMorele(URLS.get(3));
//    System.out.println("morele.net -> " + morelePrice + " zł");
//
//    String amazonPrice = getPriceAmazon(URLS.get(4));
//    System.out.println("amazon.pl -> " + amazonPrice + " zł");
  }

  public static String getPriceJson(String url, String userAgent) {
    try {
      Document doc = Jsoup
          .connect(url)
          .userAgent(userAgent)
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
}
