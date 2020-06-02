package yocto.application;

import java.io.IOException;
import java.lang.reflect.Constructor;

import yocto.logging.Logger;
import yocto.util.Bitmap;
import yocto.event.ApplicationEvent;
import yocto.event.ApplicationEventType;
import yocto.event.DrawBitmapEvent;
import yocto.event.SetApplicationReceiveKeystrokesInBackgroundEvent;
import yocto.event.SetApplicationRunInBackgroundEvent;
import yocto.event.SetApplicationTitleEvent;
import yocto.event.WriteStringEvent;

public class Application {
    // Connection to the Yocto application server
    private ApplicationServerConnection connection;

    // Placeholder application title
    //  - This should be changed by the extending app by running
    //      setApplicationTitle(...)
    private String applicationTitle = "Unknown Application";

    // Should the application continue to run while in the background?
    //  - If set to false, the app will freeze when it is no longer in the foreground
    //  - This prevents the sync from progressing while the app is backgrounded.
    private boolean runInBackground = false;

    // Does this application recieve keystrokes while it is running in the background?
    //  - Note: The application can only act on these keystrokes in realtime if it is also set
    //     to run in background.
    private boolean applicationRecieveKeystrokesInBackground = false;

    public Application() {
        // Connect to Yocto system
        connectToSystem();
    }

    /**
     * Starts the application
     * 
     * @param appClass class of the application to start
     * @param args
     */
    public static void launch(Class<? extends Application> appClass, String... args) {
        try {
            Constructor<?> ctor = appClass.getConstructor();
            Application app = (Application) ctor.newInstance();
            app.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Connect to the yocto system
     */
    private void connectToSystem() {
        try {
            connection = new ApplicationServerConnection();
        } catch (IOException e) {
            Logger.log(Application.class, "Failed to connect to Yocto application server.");
        }
    }

    /**
     * Application entry method.
     * 
     * Called on application startup.
     */
    public void start() {
        // Override this.
    }

    /**
     * Push and pull events with the Yocto system
     */
    public ApplicationEvent[] sync() throws IOException {
        return connection.sync();
    }

    public void setPixel(int x, int y, boolean state) {
        ApplicationEvent event = new ApplicationEvent();
        event.eventType = state ? ApplicationEventType.SET_PIXEL : ApplicationEventType.UNSET_PIXEL;
        event.ix = x;
        event.iy = y;

        connection.pushEvent(event);
    }

    public void writeChar(int x, int y, char c) {
        ApplicationEvent event = new ApplicationEvent(ApplicationEventType.WRITE_CHAR);
        event.ix = x;
        event.iy = y;
        event.cx = c;

        connection.pushEvent(event);
    }

    public void writeString(int x, int y, String text) {
        WriteStringEvent event = new WriteStringEvent(x, y, text);
        connection.pushEvent(event);
    }

    public void drawBitmap(int x, int y, Bitmap bitmap) {
        DrawBitmapEvent event = new DrawBitmapEvent(x, y, bitmap);
        connection.pushEvent(event);
    }

    public ApplicationEvent[] getEvents() {
        return connection.getEvents();
    }

    public void setApplicationTitle(String title) {
        applicationTitle = title;

        // Send update to app server
        connection.pushEvent(new SetApplicationTitleEvent(applicationTitle));
    }

    public void setRunInBackground(boolean v) {
        runInBackground = v;

        // Send update to app server
        connection.pushEvent(new SetApplicationRunInBackgroundEvent(runInBackground));
    }


    public void setReceiveKeystrokesInBackground(boolean v) {
        applicationRecieveKeystrokesInBackground = v;

        // Send update to app server
        connection.pushEvent(new SetApplicationReceiveKeystrokesInBackgroundEvent(applicationRecieveKeystrokesInBackground));
    }
}