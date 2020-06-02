package yocto.system;

import yocto.util.Bitmap;

public class InstalledApplication {
    private String path;
    private String title;
    private String launchCommand;
    private Bitmap icon;

    public InstalledApplication(String path, String title, String launchCommand, Bitmap icon) {
        this.path = path;
        this.title = title;
        this.launchCommand = launchCommand;
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }
    
    public String getLaunchCommand() {
        return launchCommand;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String toString() {
        return "InstalledApplication(" + title + ")";
    }
}