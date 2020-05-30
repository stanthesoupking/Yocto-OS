package yocto.application;

import java.io.IOException;

import yocto.logging.Logger;
import yocto.util.ApplicationEvent;
import yocto.util.ApplicationEventType;

public class Application {
    private static ApplicationServerConnection connection;

    public static void init() {
        // Connect to Yocto system
        connectToSystem();
    }

    /**
     * Connect to the yocto system
     */
    private static void connectToSystem() {
        try {
            connection = new ApplicationServerConnection();
        } catch (IOException e) {
            Logger.log(Application.class, "Failed to connect to Yocto application server.");
        }
    }

    /**
     * Push and pull events with the Yocto system
     */
    public static ApplicationEvent[] sync() throws IOException {
        return connection.sync();
    }

    public static void setPixel(int x, int y, boolean state) {
        ApplicationEvent event = new ApplicationEvent();
        event.eventType = state ? ApplicationEventType.SET_PIXEL : ApplicationEventType.UNSET_PIXEL;
        event.ix = x;
        event.iy = y;

        connection.pushEvent(event);
    }

    public static void writeChar(int x, int y, char c) {
        ApplicationEvent event = new ApplicationEvent(ApplicationEventType.WRITE_CHAR);
        event.ix = x;
        event.iy = y;
        event.cx = c;

        connection.pushEvent(event);
    }

    public static void writeString(int x, int y, String text) {
        ApplicationEvent event = new ApplicationEvent(ApplicationEventType.WRITE_STRING);
        event.ix = x;
        event.iy = y;
        event.sx = text;

        connection.pushEvent(event);
    }
}