package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import static java.time.LocalDate.now;

public class SqlRuDateTimeParser implements DateTimeParser {
    @Override
    public LocalDateTime parse(String parse) {
        if (parse.contains("вчера") || parse.contains("сегодня")) {
            String[] mas = parse.split(" ");
            for (String element
                    :mas) {
                System.out.println(element);
            }
            LocalDate currentData = now();
            if (mas[0].equals("вчера")) {
                currentData = now().minusDays(1);
            }
            String stroka = currentData.toString() + " " + mas[1];
            System.out.println("yesterday - " + stroka);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-LL-dd HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(stroka, dateTimeFormatter);
            return localDateTime;
        } else {
            String[] mas = parse.split(",");
            for (String element
                    :mas) {
                System.out.println(element);
            }
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yy, HH:mm");
                String[] mas1 = parse.split(" ");
                String stroka = mas1[0];
                for (int i = 1; i < mas1.length; i++) {
                    if (i == 1) {
                        stroka = stroka + " " + mas1[i] + ".";
                    } else {
                        stroka = stroka + " " + mas1[i];
                    }
                }
                System.out.println(stroka);
                LocalDateTime localDateTime = LocalDateTime.parse(stroka, formatter);
                return localDateTime;
            } catch (DateTimeParseException exc) {
                System.out.println("Is not parseble date - " + parse);
                throw exc;
            }
        }
    }
}
