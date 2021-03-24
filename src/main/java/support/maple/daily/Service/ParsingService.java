package support.maple.daily.Service;


import java.io.IOException;
import java.net.URLEncoder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParsingService {

  public void getData(String name) {
    String url = "https://maplestory.nexon.com/Ranking/World/Total?c=";
    try {
      String urlname = URLEncoder.encode(name, "UTF-8");
      url = url + urlname;
      System.out.println(url);

      Document doc = Jsoup.connect(url).get();
      // System.out.println(doc.html());
      Elements elem = doc.select("div.rank_table_wrap");
      System.out.println(elem.first().select("tr").first().text());
      Elements e = elem.select("tbody");
      System.out.println(e.first().select("tr").first().text());


    } catch (IOException e) {
      e.printStackTrace();
    }


  }
}
