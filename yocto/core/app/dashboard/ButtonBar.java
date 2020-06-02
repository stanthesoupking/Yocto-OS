package yocto.core.app.dashboard;

import java.util.ArrayList;

import yocto.application.Application;

public class ButtonBar {

    float floatTime = 0;
    int xFloat = 0;
    int selectedIndex = 0;
    ArrayList<ButtonBarItem> items;

    int y;
    int itemWidth = 1;
    int itemHeight = 1;

    boolean floatingAnimation;

    public ButtonBar(int y) {
        this.y = y;
        this.floatingAnimation = false;

        items = new ArrayList<ButtonBarItem>();
    }

    public ButtonBar(int y, boolean floatingAnimation) {
        this.y = y;
        this.floatingAnimation = floatingAnimation;

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

    public void draw(Application app) {

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
    }
}