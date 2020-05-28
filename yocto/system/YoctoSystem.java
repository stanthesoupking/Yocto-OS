package yocto.system;

import java.io.IOException;

import yocto.drivers.Display;
import yocto.logging.Logger;

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
        }
    }
}
