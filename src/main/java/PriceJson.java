import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.Callable;

public class PriceJson implements Callable<String> {

  private final String url;
  private final String userAgent;

  public PriceJson(String url, String userAgent) {
    this.url = url;
    this.userAgent = userAgent;
  }
  @Override
  public String call() throws RuntimeException {
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
}
