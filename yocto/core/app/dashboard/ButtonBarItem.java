package yocto.core.app.dashboard;

import yocto.util.Bitmap;

public class ButtonBarItem {
    private String name;
    private Bitmap icon;

    public ButtonBarItem(String name, Bitmap icon) {
        this.name = name;
        this.icon = icon;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}