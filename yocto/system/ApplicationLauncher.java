package yocto.system;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import yocto.logging.Logger;
import yocto.util.Bitmap;

public class ApplicationLauncher {
    private static final String APPLICATION_INSTALL_PATH = "yocto/applications";
    public static final String APPLICATION_METADATA_FILE = "app.yocto";

    private InstalledApplication[] installedApplications;

    public ApplicationLauncher() {
        scanForApplications();
    }

    public InstalledApplication[] getInstalledApplications() {
        return installedApplications;
    }

    public void scanForApplications() {
        // Make sure that application install path exists
        createPaths();

        File[] files = new File(getApplicationInstallPath()).listFiles();

        ArrayList<InstalledApplication> appList = new ArrayList<InstalledApplication>();
        for (File file : files) {
            if (file.isDirectory()) {
                // Add application
                InstalledApplication app = getApplicationEntry(file);
                if (app != null) {
                    appList.add(app);
                    Logger.log(getClass(), "Found app: " + app.toString());
                }

            }
        }

        installedApplications = appList.toArray(new InstalledApplication[appList.size()]);
    }

    private void createPaths() {
        File appPath = new File(getApplicationInstallPath());
        appPath.mkdirs();
    }

    public String getApplicationInstallPath() {
        return System.getProperty("user.home") + File.separatorChar + APPLICATION_INSTALL_PATH;
    }

    public void launchApplication(InstalledApplication app) {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(app.getLaunchCommand());
            pb.directory(new File(app.getPath()));
            pb.start();
        } catch (IOException e) {
            Logger.log(getClass(), "IO Error: " + e.getMessage());
        }

    }

    private InstalledApplication getApplicationEntry(File basePath) {
        JSONParser parser = new JSONParser();
        try {
            List<String> lines = Files
                    .readAllLines(Paths.get(basePath.getPath() + File.separatorChar + APPLICATION_METADATA_FILE));

            String jsonData = "";

            for (String line : lines) {
                jsonData += line;
            }

            JSONObject object = (JSONObject) parser.parse(jsonData);
            String path = basePath.getPath();
            String name = (String) object.get("name");
            String launchCommand = (String) object.get("launchCommand");
            String iconPath = (String) object.get("icon");

            Bitmap icon;
            if (iconPath != null) {
                icon = Bitmap.loadFromFile(path + File.separatorChar + iconPath);
            } else {
                icon = Bitmap.loadFromFile("resources/img/unknown_app.bmp");
            }

            InstalledApplication app = new InstalledApplication(path, name, launchCommand, icon);
            return app;
        } catch (IOException e) {
            Logger.log(getClass(), "IO Error: " + e.getMessage());
            return null;
        } catch (ParseException e) {
            Logger.log(getClass(), "Parsing Error: " + e.getMessage());
            return null;
        }
    }
}