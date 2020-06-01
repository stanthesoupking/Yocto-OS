package yocto.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import yocto.logging.Logger;
import yocto.event.ApplicationEvent;
import yocto.event.ApplicationEventType;
import yocto.util.ApplicationHeartbeat;

public class ConnectedApplication extends Thread {
    // The rate at which the application thread will check if it is foregrounded (when sleeping)
    private static int FOREGROUND_POLLING_RATE = 100;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean running;
    private ApplicationServer applicationServer;
    private ApplicationContext applicationContext;

    // Is this application currently running in the foreground?
    private Boolean isForeground;

    private String applicationTitle = "Unknown Application";

    private boolean applicationRunInBackground = false;

    ArrayList<ApplicationEvent> outputEventBuffer;

    public ConnectedApplication(ApplicationServer applicationServer, ApplicationContext applicationContext,
            Socket socket) throws IOException {
        this.applicationServer = applicationServer;
        this.applicationContext = applicationContext;
        this.socket = socket;
        this.isForeground = false;

        // Create input and output streams
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.in = new ObjectInputStream(this.socket.getInputStream());

        outputEventBuffer = new ArrayList<ApplicationEvent>();
    }

    @Override
    public void run() {
        // Start heartbeat loop
        running = true;
        try {
            while (running) {
                // Logger.log(getClass(), "Waiting for heartbeat from app.");

                // Receive heartbeat from app
                ApplicationHeartbeat response = null;
                try {
                    response = (ApplicationHeartbeat) in.readObject();
                    // Logger.log(getClass(), "Received heartbeat from app.");

                    // Do events
                    applicationContext.doEvent(this, new ApplicationEvent(ApplicationEventType.CLEAR));
                    applicationContext.doEvents(this, response.events);
                    applicationContext.doEvent(this, new ApplicationEvent(ApplicationEventType.PRESENT));
                } catch (ClassNotFoundException e) {
                    Logger.log(getClass(), "Error: received invalid response from client app.");
                }

                // Flush output events
                ApplicationEvent events[] = outputEventBuffer.toArray(new ApplicationEvent[outputEventBuffer.size()]);

                ApplicationEvent outEvents[] = new ApplicationEvent[0];

                if (isForeground) {
                    // Send input events
                    outEvents = applicationContext.getInputManager().getKeyEvents();

                    // Clear input manager events
                    applicationContext.getInputManager().clearKeyEvents();
                } else if (!applicationRunInBackground) {
                    // Sleep until application is in foreground
                    while (!isForeground) {
                        Thread.sleep(FOREGROUND_POLLING_RATE);
                    }
                }

                // Create heartbeat
                ApplicationHeartbeat heartbeat = new ApplicationHeartbeat(outEvents);

                // Send heartbeat to app
                // Logger.log(getClass(), "Sending heartbeat to app.");
                out.writeObject(heartbeat);

                // Clear output events
                outputEventBuffer.clear();
            }
        } catch (IOException e) {
            Logger.log(getClass(), "IO Error: " + e.getMessage());
        } catch (InterruptedException e) {
            Logger.log(getClass(), "Interrupted Error: " + e.getMessage());
        }

        // Close application
        closeApplication();
    }
    
    public void setForeground(boolean v) {
        isForeground = v;
    }

    public boolean getForeground() {
        return isForeground;
    }

    public void setApplicationTitle(String title) {
        applicationTitle = title;
    }

    public String getApplicationTitle() {
        return applicationTitle;
    }

    public void setApplicationRunInBackground(boolean value) {
        applicationRunInBackground = value;
    }

    public void closeApplication() {
        applicationServer.removeApplication(this);
    }
}