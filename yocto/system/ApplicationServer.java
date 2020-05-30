package yocto.system;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import yocto.logging.Logger;

public class ApplicationServer extends Thread {
    public static final int APP_PORT = 9440;

    private ApplicationContext applicationContext;
    private ServerSocket server;

    private ArrayList<ConnectedApplication> connectedApplications;

    public ApplicationServer(ApplicationContext applicationContext) throws IOException {
        this.applicationContext = applicationContext;

        // Start server
        server = new ServerSocket(APP_PORT);

        connectedApplications = new ArrayList<ConnectedApplication>();
    }

    @Override
    public void run() {
        boolean running = true;
        try {
            // Accept connections
            while (running) {
                Socket socket = server.accept();
                Logger.log(getClass(), "Recieved new connection.");

                // Start new connection thread
                ConnectedApplication app = new ConnectedApplication(applicationContext, socket);
                app.start();
                connectedApplications.add(app);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getApplicationCount() {
        return connectedApplications.size();
    }

    public ConnectedApplication getApplication(int index) {
        return connectedApplications.get(index);
    }
}