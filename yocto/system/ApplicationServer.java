package yocto.system;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import yocto.event.KeyEvent;
import yocto.logging.Logger;

public class ApplicationServer extends Thread {
    public static final int APP_PORT = 9440;

    private ApplicationContext applicationContext;
    private ServerSocket server;

    private ConnectedApplication foregroundApplication;
    private ArrayList<ConnectedApplication> connectedApplications;

    public ApplicationServer(ApplicationContext applicationContext) throws IOException {
        this.foregroundApplication = null;
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
                ConnectedApplication app = new ConnectedApplication(this, applicationContext, socket);
                app.start();
                connectedApplications.add(app);

                // Focus on any new apps
                //if (foregroundApplication == null) {
                    setForegroundApplication(app);
                //}
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

    public ConnectedApplication[] getApplications() {
        return connectedApplications.toArray(new ConnectedApplication[connectedApplications.size()]);
    }

    public void setForegroundApplication(int index) {
        setForegroundApplication(connectedApplications.get(index));
    }

    public void removeApplication(ConnectedApplication app) {
        if (foregroundApplication == app) {
            setForegroundApplication(0); // Go back to dashboard
        }

        synchronized (connectedApplications) {
            connectedApplications.remove(app);
        }
    }

    public void setForegroundApplication(ConnectedApplication app) {
        if (foregroundApplication != null) {
            foregroundApplication.setForeground(false);
        }

        foregroundApplication = app;

        if (foregroundApplication != null) {
            foregroundApplication.setForeground(true);
        }
    }

    public ConnectedApplication getForegroundApplication() {
        return foregroundApplication;
    }

    /**
     * Push key event to all connected applications.
     */
    public void pushKeyEvent(KeyEvent keyEvent) {
        synchronized (connectedApplications) {
            for (ConnectedApplication connectedApplication : connectedApplications) {
                connectedApplication.pushKeyEvent(keyEvent);
            }
        }
    }
}