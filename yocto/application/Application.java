package yocto.application;

import java.io.IOException;
import java.lang.reflect.Constructor;

import yocto.logging.Logger;
import yocto.event.ApplicationEvent;
import yocto.event.ApplicationEventType;

public class Application {
    private ApplicationServerConnection connection;

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
        ApplicationEvent event = new ApplicationEvent(ApplicationEventType.WRITE_STRING);
        event.ix = x;
        event.iy = y;
        event.sx = text;

        connection.pushEvent(event);
    }

    public ApplicationEvent[] getEvents() {
        return connection.getEvents();
    }
}