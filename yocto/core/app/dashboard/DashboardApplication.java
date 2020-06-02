package yocto.core.app.dashboard;

import java.io.IOException;

import yocto.application.Application;
import yocto.application.ui.ViewManager;
import yocto.event.ApplicationEvent;
import yocto.event.ApplicationEventType;
import yocto.event.KeyEvent;
import yocto.event.KeyEventType;
import yocto.system.ApplicationServer;

public class DashboardApplication extends Application {
    private ApplicationServer applicationServer;
    private ViewManager viewManager;

    public DashboardApplication(ApplicationServer applicationServer) {
        super();
        this.applicationServer = applicationServer;
    }

    @Override
    public void start() {
        setApplicationTitle("Dashboard");
        setRunInBackground(true);
        setReceiveKeystrokesInBackground(true);
        
        viewManager = new ViewManager();
        viewManager.pushView(new MainView(this, "Main"));
        viewManager.pushView(new LaunchAppView(this, "Launch App"));
        viewManager.pushView(new RunningAppsView(this, applicationServer, "Running Apps"));

        viewManager.setCurrentView("Main");

        while (true) {
            // Handle global events
            handleEvents();

            // Update current view
            viewManager.handleEvents();

            // Draw the current view
            viewManager.draw();

            // Sync with the server
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
                if (keyEventType == KeyEventType.Escape) {
                    // Switch focus back to the dashboard
                    applicationServer.setForegroundApplication(0);
                }
            }
        }
    }

    public static void main(String args[]) {
        launch(DashboardApplication.class, args);
    }
}