package quartz;

import html.Parse;
import html.Post;
import html.SqlRuParse;
import jdbc.Store;
import jdbc.StoreSqlBase;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import utils.SqlRuDateTimeParser;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;

public class VacancyAgregator implements Grab {
    private static String pathproperties =
            "C:\\projects\\job4j_grabber\\src\\main\\resources\\storeSqlBase.properties";
    private String urlName = "https://www.sql.ru/forum/job-offers";

    private Properties prop = new Properties();

    public Store store() throws IOException {
        setProp();
        StoreSqlBase base = new StoreSqlBase(prop);
        return base;
    }

    public void setProp() throws IOException {
        try (FileInputStream in = new FileInputStream(pathproperties)) {
            prop.load(in);
        }
    }

    private Scheduler timeTasks() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler(); //создаем класс, управляющий всеми
        scheduler.start();                                               // работами
      /*  JobDetail jobDetail = newJob(Vacant.class).build();              //quartz каждый раз создает объект с типом org.quartz.Job. класс, реализующий этот интерфейс Vacant.
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(20)
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(jobDetail, trigger);

       */
        return scheduler;
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws IOException, SQLException, ClassNotFoundException, SchedulerException {
        JobDataMap map = new JobDataMap();
        map.put("store", store);
        map.put("parse", parse);
        map.put("url", urlName);
        JobDetail jobDetail = newJob(Vacant.class)
                .usingJobData(map)
                .build();              //quartz каждый раз создает объект с типом org.quartz.Job. класс, реализующий этот интерфейс Vacant.
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(20)
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
      /*  List<Post> mas = new ArrayList<>();
        mas = parse.list(urlName);
        for (Post element
                :mas) {
            store.save(element);
        }
        scheduler.start();

       */
    }

    public static void main(String[] args) throws SchedulerException, SQLException, IOException, ClassNotFoundException {
        VacancyAgregator va = new VacancyAgregator();
       Scheduler scheduler =  va.timeTasks();
       SqlRuParse parseArgument = new SqlRuParse(new SqlRuDateTimeParser());
       Store storeArgument = va.store();
       va.init(parseArgument, storeArgument, scheduler);
    }

    public static class Vacant implements Job { // класс, реализующий интерфейс Job, внутри этого класса описываем требуемые действия
        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            System.out.println("Something need to execute ....");
            JobDataMap map = jobExecutionContext.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            String url = (String) map.get("url");
            List<Post> mas = new ArrayList<>();
            try {
                mas = parse.list(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Post element
                    :mas) {
                try {
                    store.save(element);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
