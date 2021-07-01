package html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

public class SqlRuParse {
    @SuppressWarnings("checkstyle:OperatorWrap")
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements rtp = doc.getElementsByClass("altCol").after(".postslisttopic");
        Elements row = doc.select(".postslisttopic");
        Integer i = 1;
       for (Element td
                :row) {
            Element href = td.child(0);

            System.out.println(href.attr("href"));
            System.out.println(href.text());
           Element pot = rtp.get(i);
           System.out.println("Время опубликования объявления - " + pot.text());
           i = i + 2;
        }

    }
}
