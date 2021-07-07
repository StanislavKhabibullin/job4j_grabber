package quartz;

import html.Parse;
import jdbc.Store;
import org.quartz.Scheduler;

public interface Grab {
    void init(Parse parse, Store store, Scheduler scheduler);
}
