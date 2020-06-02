package yocto.system;

public class InstalledApplication {
    private String path;
    private String title;
    private String launchCommand;

    public InstalledApplication(String path, String title, String launchCommand) {
        this.path = path;
        this.title = title;
        this.launchCommand = launchCommand;
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

    public String toString() {
        return "InstalledApplication(" + title + ")";
    }
}