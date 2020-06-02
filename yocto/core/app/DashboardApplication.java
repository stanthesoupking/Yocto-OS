package yocto.core.app;

import java.io.IOException;
import java.util.Calendar;

import yocto.application.Application;
import yocto.system.ApplicationServer;
import yocto.system.ConnectedApplication;
import yocto.system.YoctoSystem;
import yocto.util.Bitmap;

public class DashboardApplication extends Application {
    private ApplicationServer applicationServer;

    public DashboardApplication(ApplicationServer applicationServer) {
        super();
        this.applicationServer = applicationServer;
    }

    @Override
    public void start() {
        setApplicationTitle("Dashboard");
        setRunInBackground(true);
        setReceiveKeystrokesInBackground(true);

        Bitmap logoBmp = null;
        try {
            logoBmp = Bitmap.loadFromFile("resources/img/logo.bmp");
        } catch (IOException e) {
            System.out.println("Error: Failed loading bitmap.");
            System.exit(1);
        }

        int x = -logoBmp.getWidth();
        while (true) {
            writeString(1, 1, "Dashboard");
            writeString(83, 1, getTime());
            writeString(1, 58, "Yocto OS v" + YoctoSystem.YOCTO_VERSION);

            writeString(1, 9, "Total running apps: " + applicationServer.getApplicationCount());

            // Draw bitmap
            drawBitmap(x++, 18, logoBmp);

            if (x > 128) {
                x = -logoBmp.getWidth();
            }

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
        return String.format("%02d:%02d:%02d %s", c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND),
                amPm);
    }

    public static void main(String args[]) {
        launch(DashboardApplication.class, args);
    }
}