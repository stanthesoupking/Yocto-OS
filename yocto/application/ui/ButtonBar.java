package yocto.application.ui;

import java.util.ArrayList;

import yocto.application.Application;
import yocto.event.ApplicationEvent;
import yocto.event.ApplicationEventType;
import yocto.event.KeyEvent;
import yocto.event.KeyEventType;
import yocto.util.Gravity;

public class ButtonBar {
    private Application app;

    float floatTime = 0;
    int xFloat = 0;
    int selectedIndex = 0;
    ArrayList<ButtonBarItem> items;

    int y;
    int itemWidth = 1;
    int itemHeight = 1;

    public boolean floatingAnimation = true;
    public boolean showSelectedName = true;

    public ButtonBar(Application app, int y) {
        this.app = app;
        this.y = y;

        items = new ArrayList<ButtonBarItem>();
    }

    public void pushItem(ButtonBarItem item) {
        items.add(item);

        // Update button width
        int w = item.getIcon().getWidth();
        if (w > itemWidth) {
            itemWidth = w;
        }

        // Update button height
        int h = item.getIcon().getHeight();
        if (h > itemHeight) {
            itemHeight = h;
        }
    }

    public void setSelectedIndex(int index) {
        selectedIndex = index;
    }

    public void moveSelectionLeft() {
        selectedIndex--;
        if (selectedIndex < 0) {
            selectedIndex = items.size() - 1;
        }
    }

    public void moveSelectionRight() {
        selectedIndex = (selectedIndex + 1) % items.size();
    }

    public ButtonBarItem getSelectedItem() {
        return items.get(selectedIndex);
    }

    public void handleEvents() {
        for (ApplicationEvent event : app.getEvents()) {
            if (event.eventType == ApplicationEventType.KEY) {
                KeyEvent keyEvent = (KeyEvent) event;
                KeyEventType keyEventType = keyEvent.getKeyType();
                if (keyEventType == KeyEventType.ArrowRight) {
                    moveSelectionRight();
                } else if (keyEventType == KeyEventType.ArrowLeft) {
                    moveSelectionLeft();
                } else if (keyEventType == KeyEventType.Enter) {
                    getSelectedItem().onSelect();
                }
            }
        }
    }

    public void draw() {

        int finalWidth = itemWidth + 6;

        int i = 0;
        floatTime += 0.5;
        for (ButtonBarItem item : items) {
            int x = (Application.SCREEN_WIDTH / 2) + ((i - selectedIndex) * finalWidth) - (finalWidth / 2);
            int bY = y - (itemHeight / 2);
            if (floatingAnimation && (i == selectedIndex)) {
                bY -= (int) (Math.pow(1 + Math.sin(floatTime), 2) * 0.75);
            }

            app.drawBitmap(x, bY, item.getIcon());
            i++;
        }

        if (showSelectedName) {
            // Draw selected name
            app.writeString((Application.SCREEN_WIDTH / 2), y - (itemHeight / 2) - 4,
                    items.get(selectedIndex).getName(), Gravity.BOTTOM_CENTER);
        }
    }
}