package yocto.system;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import yocto.drivers.Display;
import yocto.logging.Logger;
import yocto.util.bdf.Font;

class YoctoSystem {

    private static Display display;

    public static void main(String args[]) {
        // Setup logger
        Logger.showTime(true);

        // Open display
        Logger.log(YoctoSystem.class, "Opening display driver...");
        try {
            display = new Display();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Open font
        Logger.log(YoctoSystem.class, "Opening font...");
        Font font = null;
        try {
            font = new Font("font.bdf");
            display.present();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Start input manager
        InputManager inputManager = new InputManager();
        inputManager.start();

        ApplicationContext applicationContext = new ApplicationContext(display, font);

        // Start application server
        ApplicationServer applicationServer = null;
        try {
            applicationServer = new ApplicationServer(applicationContext);
            applicationServer.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        Logger.log(YoctoSystem.class, "System ready.");
    }
}
