package yocto.core.app;

import java.io.IOException;
import java.util.Calendar;

import yocto.application.Application;
import yocto.system.YoctoSystem;

public class DashboardApplication extends Application {
    @Override
    public void start() {
        setApplicationTitle("Dashboard");

        while (true) {
            writeString(1, 1, "Dashboard");
            writeString(83, 1, getTime());
            writeString(1, 58, "Yocto OS v" + YoctoSystem.YOCTO_VERSION);

            try {
                sync();
            } catch (IOException e) {
                System.out.println("Sync failed.");
                System.exit(1);
            }
        }
    }

    public String getTime() {
        Calendar c = Calendar.getInstance();
        String amPm = c.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
        return String.format("%02d:%02d:%02d %s", c.get(Calendar.HOUR), c.get(Calendar.MINUTE),
                c.get(Calendar.SECOND), amPm);
    }

    public static void main(String args[]) {
        launch(DashboardApplication.class, args);
    }
}