package quartz;

import html.Parse;
import html.Post;
import html.SqlRuParse;
import jdbc.Store;
import jdbc.StoreSqlBase;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import utils.SqlRuDateTimeParser;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;

public class VacancyAgregator implements Grab {
    private String urlName = "https://www.sql.ru/forum/job-offers";

    private Scheduler timeTasks() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler(); //создаем класс, управляющий всеми
        scheduler.start();                                               // работами
        JobDetail jobDetail = newJob(Vacant.class).build();              //quartz каждый раз создает объект с типом org.quartz.Job. класс, реализующий этот интерфейс Vacant.
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInHours(12)
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        return scheduler;
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws IOException, SQLException, ClassNotFoundException, SchedulerException {
        List<Post> mas = new ArrayList<>();
        mas = parse.list(urlName);
        for (Post element
                :mas) {
            store.save(element);
        }
        scheduler.start();
    }

    public static void main(String[] args) throws SchedulerException, SQLException, IOException, ClassNotFoundException {
        VacancyAgregator va = new VacancyAgregator();
       // va.timeTasks();
        SqlRuParse parseArgument = new SqlRuParse(new SqlRuDateTimeParser());
        StoreSqlBase storeArgument = new StoreSqlBase();
        va.init(parseArgument, storeArgument, va.timeTasks());
    }

    public static class Vacant implements Job { // класс, реализующий интерфейс Job, внутри этого класса описываем требуемые действия
        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            System.out.println("Something need to execute ....");
        }
    }
}
