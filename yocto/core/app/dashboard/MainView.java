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
import yocto.system.YoctoSystem;
import yocto.util.Bitmap;
import yocto.util.Gravity;

public class MainView extends View {
    private ButtonBar bar;

    private ButtonBarItem launcherButton;
    private ButtonBarItem runningAppsButton;
    private ButtonBarItem settingsButton;

    public MainView(Application app, String name) {
        super(app, name);

        bar = new ButtonBar(app, 32);
        try {
            launcherButton = new ButtonBarItem("Launch an App", Bitmap.loadFromFile("resources/img/launcher.bmp"));
            launcherButton.setOnSelect(new Action() {
                @Override
                public void doAction() {
                    viewManager.setCurrentView("Launch App");
                }
            });
            bar.pushItem(launcherButton);

            runningAppsButton = new ButtonBarItem("Running Apps", Bitmap.loadFromFile("resources/img/running_app.bmp"));
            runningAppsButton.setOnSelect(new Action() {
                @Override
                public void doAction() {
                    viewManager.setCurrentView("Running Apps");
                }
            });
            bar.pushItem(runningAppsButton);

            settingsButton = new ButtonBarItem("Settings", Bitmap.loadFromFile("resources/img/settings.bmp"));
            bar.pushItem(settingsButton);
        } catch (IOException e) {
            System.out.println("Error: Failed loading bitmap.");
            System.exit(1);
        }
    }

    @Override
    public void open() {

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
        app.writeString(1, 1, "Dashboard");
        app.writeString(83, 1, app.getTime());
        app.writeString(1, 64, "Yocto OS v" + YoctoSystem.YOCTO_VERSION, Gravity.BOTTOM_LEFT);

        bar.draw();
    }

}