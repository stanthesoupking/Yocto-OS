package yocto.application.ui;

import yocto.application.Application;

public class View {
    private String name;
    protected Application app;
    protected ViewManager viewManager;

    public View(Application app, String name) {
        this.name = name;
        this.app = app;
    }

    public void open() {
        // Override this
    }

    public void handleEvents() {
        // Override this
    }

    public void draw() {
        // Override this
    }

    public String getName() {
        return name;
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }
}