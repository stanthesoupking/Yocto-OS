package apps.helloworld;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import yocto.application.Application;

public class HelloWorldApplication extends Application {
    public static void main(String[] args) {
        System.out.println("Started.");
        init();

        Calendar c;
        while (true) {
            c = Calendar.getInstance();
            String amPm = c.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
            String timeText = String.format("%02d:%02d:%02d %s", c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), amPm);
            writeString(1, 1, timeText);

            try {
                sync();
            } catch (IOException e) {
                System.out.println("Sync failed.");
                System.exit(1);
            }
        }
    }
}