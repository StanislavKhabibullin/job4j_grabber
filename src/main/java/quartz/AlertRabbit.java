package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.sql.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {
/*
    private static String pathtoproperties
            = "C:\\projects\\job4j_grabber\\src\\main\\resources\\rabbit.properties";
    private static Connection connect;
    Integer seq;

    private Integer init() throws IOException, SQLException, ClassNotFoundException {

        try (FileInputStream in = new FileInputStream(pathtoproperties)) {
            Properties prop = new Properties();
            prop.load(in);
            seq = Integer.valueOf(prop.getProperty("rabbit.interval"));
            Class.forName(prop.getProperty("driver-class-name"));
            connect = DriverManager.getConnection(
                    prop.getProperty("url"),
                    prop.getProperty("username"),
                    prop.getProperty("password")
            );
        }
        return seq;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException, SchedulerException, IOException {
            AlertRabbit rabbit = new AlertRabbit();

        int requestTime = rabbit.init();
            List<Long> store = new ArrayList<>();
            try (Statement statement = connect.createStatement()) {
                String sql = String.format(
                        "Create table if not exists "
                                + "rabbit"
                                + "(id serial primary key, created_date DATE);");
                statement.execute(sql);
                System.out.println(Rabbit.getTableScheme(connect, "rabbit"));
            }
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("store", store);
            data.put("con", connect);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(requestTime)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
            System.out.println(store);
        }
}

   public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            List<Long> store = (List<Long>) context.getJobDetail().getJobDataMap().get("store");
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("con");
            store.add(System.currentTimeMillis());
            Date date = new Date();
            try (PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO rabbit(created_date) VALUES(?);")) {

                statement.setDate(1, date);
                statement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        public static String getTableScheme(Connection connection, String tableName) throws SQLException {
            StringBuilder scheme = new StringBuilder();
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet columns = metaData.getColumns(null, null,
                    tableName, null)) {
                scheme.append(String.format("%-15s %-15s%n", "column", "type"));
                while (columns.next()) {
                    scheme.append(String.format("%-15s %-15s%n",
                            columns.getString("COLUMN_NAME"),
                            columns.getString("TYPE_NAME")));
                }
            }
            return scheme.toString();
        }

 */
    }



