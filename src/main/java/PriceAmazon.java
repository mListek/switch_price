import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.concurrent.Callable;

public class PriceAmazon implements Callable<String> {
  private final String url;
  private final String userAgent;

  public PriceAmazon(String url, String userAgent) {
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

      Element element = doc.selectFirst("span.a-offscreen");

      assert element != null;
      return element.html().replace("&nbsp;", "");

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
