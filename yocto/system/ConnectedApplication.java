package yocto.system;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import yocto.logging.Logger;
import yocto.util.ApplicationEvent;
import yocto.util.ApplicationEventType;
import yocto.util.ApplicationHeartbeat;

public class ConnectedApplication extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean running;
    private ApplicationContext applicationContext;

    ArrayList<ApplicationEvent> outputEventBuffer;

    public ConnectedApplication(ApplicationContext applicationContext, Socket socket) throws IOException {
        this.applicationContext = applicationContext;
        this.socket = socket;

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
                    applicationContext.doEvent(new ApplicationEvent(ApplicationEventType.CLEAR));
                    applicationContext.doEvents(response.events);
                    applicationContext.doEvent(new ApplicationEvent(ApplicationEventType.PRESENT));
                } catch (ClassNotFoundException e) {
                    Logger.log(getClass(), "Error: received invalid response from client app.");
                }


                // Flush output events
                ApplicationEvent events[] = outputEventBuffer.toArray(new ApplicationEvent[outputEventBuffer.size()]);

                // Create heartbeat
                ApplicationHeartbeat heartbeat = new ApplicationHeartbeat(events);

                // Send heartbeat to app
                // Logger.log(getClass(), "Sending heartbeat to app.");
                out.writeObject(heartbeat);

                // Clear output events
                outputEventBuffer.clear();
            }
        } catch (IOException e) {
            Logger.log(getClass(), "IO Error: " + e.getMessage());
        }
    }
}