package quartz;

import html.Parse;
import html.SqlRuParse;
import jdbc.Store;
import org.quartz.Scheduler;

public class VacancyAgregator implements Grab {
    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) {
        SqlRuParse site = parse;

    }

    public static void main(String[] args) {

    }
}
