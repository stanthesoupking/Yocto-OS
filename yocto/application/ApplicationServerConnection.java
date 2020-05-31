package yocto.application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import yocto.logging.Logger;
import yocto.system.ApplicationServer;
import yocto.event.ApplicationEvent;
import yocto.util.ApplicationHeartbeat;

public class ApplicationServerConnection {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    ApplicationEvent[] inputEventBuffer;
    ArrayList<ApplicationEvent> outputEventBuffer;

    public ApplicationServerConnection() throws IOException {
        socket = new Socket("localhost", ApplicationServer.APP_PORT);

        // Create input and output streams
        this.out = new ObjectOutputStream(this.socket.getOutputStream());
        this.in = new ObjectInputStream(this.socket.getInputStream());

        inputEventBuffer = new ApplicationEvent[0];
        outputEventBuffer = new ArrayList<ApplicationEvent>();
    }

    public ApplicationEvent[] sync() throws IOException {
        // Flush output events
        ApplicationEvent events[] = outputEventBuffer.toArray(new ApplicationEvent[outputEventBuffer.size()]);

        // Create heartbeat
        ApplicationHeartbeat heartbeat = new ApplicationHeartbeat(events);

        // Send heartbeat to server
        out.writeObject(heartbeat);

        // Clear output events
        outputEventBuffer.clear();

        // Receive heartbeat from server
        ApplicationHeartbeat response = null;
        try {
            response = (ApplicationHeartbeat) in.readObject();
            inputEventBuffer = response.events;
        } catch (ClassNotFoundException e) {
            Logger.log(getClass(), "Error: received invalid response from server.");
            return new ApplicationEvent[0];
        }

        // Return response events
        return response.events;
    }

    public void pushEvent(ApplicationEvent event) {
        outputEventBuffer.add(event);
    }
    
    public ApplicationEvent[] getEvents() {
        return inputEventBuffer;
    }
}