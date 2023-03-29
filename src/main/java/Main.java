import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
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

    System.out.println("Checking prices...");

    ExecutorService executorService = Executors.newFixedThreadPool(5);
    Map<String, Future<String>> prices = new HashMap<>();

    for (String url : URLS) {
      if (url.contains("mediamarkt.pl")) {
        PriceJson priceJson = new PriceJson(url, USER_AGENTS.get(1));
        prices.put("mediamarkt.pl", executorService.submit(priceJson));
      } else if (url.contains("x-kom.pl")) {
        PriceJson priceJson = new PriceJson(url, USER_AGENTS.get(1));
        prices.put("x-kom.pl", executorService.submit(priceJson));
      } else if (url.contains("euro.com.pl")) {
        PriceJson priceJson = new PriceJson(url, USER_AGENTS.get(0));
        prices.put("euro.com.pl", executorService.submit(priceJson));
      } else if (url.contains("morele.net")) {
        PriceMorele priceMorele = new PriceMorele(url, USER_AGENTS.get(1));
        prices.put("morele.net", executorService.submit(priceMorele));
      } else if (url.contains("amazon.pl")) {
        PriceAmazon priceAmazon = new PriceAmazon(url, USER_AGENTS.get(1));
        prices.put("amazon.pl", executorService.submit(priceAmazon));
      }
    }

    for (String storeName : prices.keySet()) {
      Future<String> price = prices.get(storeName);
      try {
        System.out.println(storeName + ": " + price.get() + " zł");

      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }

    executorService.shutdown();
  }
}
