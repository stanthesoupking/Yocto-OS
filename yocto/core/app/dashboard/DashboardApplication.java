package yocto.core.app.dashboard;

import java.io.IOException;
import java.util.Calendar;

import yocto.application.Application;
import yocto.event.ApplicationEvent;
import yocto.event.ApplicationEventType;
import yocto.event.KeyEvent;
import yocto.event.KeyEventType;
import yocto.system.ApplicationServer;
import yocto.system.YoctoSystem;
import yocto.util.Bitmap;

public class DashboardApplication extends Application {
    private ApplicationServer applicationServer;
    ButtonBar bar;

    public DashboardApplication(ApplicationServer applicationServer) {
        super();
        this.applicationServer = applicationServer;
    }

    @Override
    public void start() {
        setApplicationTitle("Dashboard");
        setRunInBackground(true);
        setReceiveKeystrokesInBackground(true);

        bar = new ButtonBar(32);
        try {
            ButtonBarItem launcherButton = new ButtonBarItem(Bitmap.loadFromFile("resources/img/launcher.bmp"));
            bar.pushItem(launcherButton);

            ButtonBarItem runningAppButton = new ButtonBarItem(Bitmap.loadFromFile("resources/img/running_app.bmp"));
            bar.pushItem(runningAppButton);

            ButtonBarItem settingsButton = new ButtonBarItem(Bitmap.loadFromFile("resources/img/settings.bmp"));
            bar.pushItem(settingsButton);
        } catch (IOException e) {
            System.out.println("Error: Failed loading bitmap.");
            System.exit(1);
        }

        while (true) {
            handleEvents();

            writeString(1, 1, "Dashboard");
            writeString(83, 1, getTime());
            writeString(1, 58, "Yocto OS v" + YoctoSystem.YOCTO_VERSION);

            //writeString(1, 9, "Total running apps: " + applicationServer.getApplicationCount());

            bar.draw(this);

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
                    bar.moveSelectionRight();
                } else if (keyEventType == KeyEventType.ArrowLeft) {
                    bar.moveSelectionLeft();
                }
            }
        }
    }

    public String getTime() {
        Calendar c = Calendar.getInstance();
        String amPm = c.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
        return String.format("%02d:%02d:%02d %s", c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND),
                amPm);
    }

    public static void main(String args[]) {
        launch(DashboardApplication.class, args);
    }
}