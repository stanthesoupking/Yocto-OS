package yocto.core.app.dashboard;

import java.io.IOException;

import yocto.application.Application;
import yocto.application.ui.Action;
import yocto.application.ui.ButtonBar;
import yocto.application.ui.ButtonBarItem;
import yocto.application.ui.View;
import yocto.event.ApplicationEvent;
import yocto.event.ApplicationEventType;
import yocto.event.KeyEvent;
import yocto.event.KeyEventType;
import yocto.system.ApplicationServer;
import yocto.system.ConnectedApplication;
import yocto.system.YoctoSystem;
import yocto.util.Bitmap;
import yocto.util.Gravity;

public class RunningAppsView extends View {
    private ApplicationServer applicationServer;

    private ButtonBar bar;
    private ButtonBarItem backButton;

    public RunningAppsView(Application app, ApplicationServer applicationServer, String name) {
        super(app, name);

        this.applicationServer = applicationServer;
    }

    @Override
    public void open() {
        createButtonBar();
    }

    @Override
    public void handleEvents() {
        bar.handleEvents();

        for (ApplicationEvent event : app.getEvents()) {
            // Do nothing
        }
    }

    @Override
    public void draw() {
        app.writeString(1, 1, "Running Apps");
        app.writeString(83, 1, app.getTime());
        app.writeString(1, 64, "Yocto OS v" + YoctoSystem.YOCTO_VERSION, Gravity.BOTTOM_LEFT);

        bar.draw();
    }

    private void createButtonBar() {
        bar = new ButtonBar(app, 32);
        try {
            backButton = new ButtonBarItem("Back", Bitmap.loadFromFile("resources/img/back.bmp"));
            backButton.setOnSelect(new Action() {
                @Override
                public void doAction() {
                    viewManager.setCurrentView("Main");
                }
            });
            bar.pushItem(backButton);

            Bitmap unknownAppBmp = Bitmap.loadFromFile("resources/img/unknown_app.bmp");

            for (ConnectedApplication connectedApplication : applicationServer.getApplications()) {
                ButtonBarItem appItem = new ButtonBarItem(connectedApplication.getApplicationTitle(), unknownAppBmp);
                appItem.setOnSelect(new Action() {
                    @Override
                    public void doAction() {
                        applicationServer.setForegroundApplication(connectedApplication);
                    }
                });
                bar.pushItem(appItem);
            }
        } catch (IOException e) {
            System.out.println("Error: Failed loading bitmap.");
            System.exit(1);
        }
    }

}