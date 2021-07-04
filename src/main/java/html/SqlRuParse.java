package html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SqlRuParse {
    public static List<Post> base = new ArrayList<>();

    public static void parsingMethod(Document doc) {
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
            LocalDateTime test = new SqlRuDateTimeParser().parse(pot.text());

            System.out.println("Formatted data - " + test.format(DateTimeFormatter.ofPattern("d MMM yy, HH:mm")));
            i = i + 2;
            base.add(new Post(0, "no title", href.attr("href"), href.text(), test));
        }

    }

    public static void main(String[] args) throws IOException {
        String urlName = "https://www.sql.ru/forum/job-offers";
        Document doc = Jsoup.connect(urlName).get();
        parsingMethod(doc);
        for (int i = 2; i < 6; i++) {
            Document doc1 = Jsoup.connect(urlName + "/" + i).get();
            parsingMethod(doc1);
        }
        for (Post element
                :base) {
            System.out.println(element);
        }
    }
}
