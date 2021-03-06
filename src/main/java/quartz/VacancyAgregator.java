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
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
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

    public void web(Store store) {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(Integer.parseInt(prop.getProperty("port")))) {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try (OutputStream out = socket.getOutputStream()) {
                        out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                        for (Post post : store.getAll()) {
                            out.write(post.toString().getBytes(Charset.forName("Windows-1251")));
                            out.write(System.lineSeparator().getBytes());
                        }
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

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
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler(); //?????????????? ??????????, ?????????????????????? ??????????
        scheduler.start();                                               // ????????????????
      /*  JobDetail jobDetail = newJob(Vacant.class).build();              //quartz ???????????? ?????? ?????????????? ???????????? ?? ?????????? org.quartz.Job. ??????????, ?????????????????????? ???????? ?????????????????? Vacant.
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
                .build();              //quartz ???????????? ?????? ?????????????? ???????????? ?? ?????????? org.quartz.Job. ??????????, ?????????????????????? ???????? ?????????????????? Vacant.
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
       va.web(storeArgument);
    }

    public static class Vacant implements Job { // ??????????, ?????????????????????? ?????????????????? Job, ???????????? ?????????? ???????????? ?????????????????? ?????????????????? ????????????????
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
