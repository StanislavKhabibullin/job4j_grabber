package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SqlRuDateTimeParser implements DateTimeParser {
    @Override
    public LocalDateTime parse(String parse) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yy, HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(parse, formatter);
        return localDateTime;
    }
}
