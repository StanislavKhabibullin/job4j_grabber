package html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.DateTimeParser;
import utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SuppressWarnings("checkstyle:WhitespaceAround")
public class SqlRuParse implements Parse {
   // public static List<Post> base = new ArrayList<>();

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> massiv = new ArrayList<>();
        Document doc = Jsoup.connect(link).get();
        Elements row = doc.select(".postslisttopic");
        Integer i = 1;
        for (Element td
                : row) {
            Element href = td.child(0);
            String addres = href.attr("href");
            massiv.add(this.detail(addres));
        }
        return massiv;
    }

    @Override
    public Post detail(String link) throws IOException {
        Document document = Jsoup.connect(link).get();
        Elements descr = document.getElementsByClass("msgBody");
        Elements titles = document.getElementsByClass("messageHeader");
        Elements timik = document.getElementsByClass("msgFooter");
        String description = descr.get(1).text();
        String title = titles.get(0).text();
        System.out.println(link);
        LocalDateTime localDateTime = new SqlRuDateTimeParser().parse(timik.get(0).text());
        System.out.println("Formatted data from Vacancy - " + localDateTime.format(DateTimeFormatter.ofPattern("d MMM yy, HH:mm")));
        Post result = new Post(0, title, link, description, localDateTime);
        System.out.println(result);
        return result;
    }

  /*  public static String parsingVacancy(String urlAdress) throws IOException {
        Document document = Jsoup.connect(urlAdress).get();
        Elements descr = document.getElementsByClass("msgBody");
        int i = 0;
        for (Element stroka
                :descr) {
            System.out.println("DESCRIPTION - " + "index = " + i++ + ": " + stroka.text());
        }
        return descr.get(1).text();
    }

    public static List<Post> parsingMethod(Document doc) throws IOException {
        Elements rtp = doc.getElementsByClass("altCol").after(".postslisttopic");
        Elements row = doc.select(".postslisttopic");
        Integer i = 1;
        for (Element td
                :row) {
            Element href = td.child(0);
            System.out.println(href.attr("href"));
            String descripton = parsingVacancy(href.attr("href"));

            System.out.println(href.text());

            Element pot = rtp.get(i);
            System.out.println("Время опубликования объявления - " + pot.text());
            LocalDateTime test = new SqlRuDateTimeParser().parse(pot.text());

            System.out.println("Formatted data - " + test.format(DateTimeFormatter.ofPattern("d MMM yy, HH:mm")));
            i = i + 2;
            base.add(new Post(0, href.text(), href.attr("href"), descripton, test));
        }
        return base;
    }

   */

    public static void main(String[] args) throws IOException {
        String urlName = "https://www.sql.ru/forum/job-offers";
        List<Post> result = new ArrayList<>();
        SqlRuParse testBase = new SqlRuParse();
        result.addAll(testBase.list(urlName));
        for (int i = 2; i < 6; i++) {
            String usrlNextPageNumber = urlName + "/" + i;
            result.addAll(testBase.list(usrlNextPageNumber));
        }
        for (Post element
                  :result) {
             System.out.println(element);
          }
    }
}
