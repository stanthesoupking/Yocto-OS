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

            Logger.log(YoctoSystem.class, "Writing text...");
            display.writeString(0, 0, font, "Hello World!");
            display.present();
            Logger.log(YoctoSystem.class, "Complete!");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
