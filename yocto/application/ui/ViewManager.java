package yocto.application.ui;

import java.util.HashMap;

public class ViewManager {

    private HashMap<String, View> views;
    private View currentView = null;

    public ViewManager() {
        views = new HashMap<String, View>();
    }

    public void pushView(View view) {
        // Give the view access to it's view manager
        view.setViewManager(this);

        views.put(view.getName(), view);
    }

    public void handleEvents() {
        if (currentView != null) {
            currentView.handleEvents();
        }
    }

    public void draw() {
        if (currentView != null) {
            currentView.draw();
        }
    }

    public void setCurrentView(String viewName) {
        currentView = views.get(viewName);

        // Trigger the view's open event
        if (currentView != null) {
            currentView.open();
        }
    }

    public View getCurrentView(View view) {
        return currentView;
    }
}