package apps.helloworld;

import java.io.IOException;
import java.security.Key;
import java.util.Calendar;

import yocto.application.Application;
import yocto.event.ApplicationEvent;
import yocto.event.ApplicationEventType;
import yocto.event.KeyEvent;
import yocto.event.KeyEventType;

public class HelloWorldApplication extends Application {

    private int posX = 0;

    @Override
    public void start() {
        setApplicationTitle("Hello World");

        Calendar c;
        while (true) {
            handleEvents();

            c = Calendar.getInstance();
            String amPm = c.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
            String timeText = String.format("%02d:%02d:%02d %s", c.get(Calendar.HOUR), c.get(Calendar.MINUTE),
                    c.get(Calendar.SECOND), amPm);
            writeString(posX, 0, timeText);

            for (int x = 0; x < 128; x++) {
                setPixel(x, 6, true);
            }

            try {
                sync();
            } catch (IOException e) {
                System.out.println("Sync failed.");
                System.exit(1);
            }
        }
    }

    public void handleEvents() {
        for (ApplicationEvent event : getEvents()) {
            if (event.eventType == ApplicationEventType.KEY) {
                KeyEvent keyEvent = (KeyEvent) event;
                KeyEventType keyEventType = keyEvent.getKeyType();
                if (keyEventType == KeyEventType.ArrowRight) {
                    posX += 5;
                } else if (keyEventType == KeyEventType.ArrowLeft) {
                    posX -= 5;
                }
            }
        }
    }

    public static void main(String args[]) {
        launch(HelloWorldApplication.class, args);
    }
}