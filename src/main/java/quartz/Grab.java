package quartz;

import html.Parse;
import jdbc.Store;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import java.io.IOException;
import java.sql.SQLException;

public interface Grab {
    void init(Parse parse, Store store, Scheduler scheduler) throws IOException, SQLException, ClassNotFoundException, SchedulerException;
}
