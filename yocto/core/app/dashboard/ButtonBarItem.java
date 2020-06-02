package yocto.core.app.dashboard;

import yocto.util.Bitmap;

public class ButtonBarItem {
    private Bitmap icon;

    public ButtonBarItem(Bitmap icon) {
        this.icon = icon;
    }

    public Bitmap getIcon() {
        return icon;
    }
}