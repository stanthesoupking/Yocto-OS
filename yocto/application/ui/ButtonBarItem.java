package yocto.application.ui;

import yocto.util.Bitmap;

public class ButtonBarItem {
    private String name;
    private Bitmap icon;
    private Action selectAction = new Action();

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

    public void onSelect() {
        selectAction.doAction();
    }

    public void setOnSelect(Action action) {
        selectAction = action;
    }
}