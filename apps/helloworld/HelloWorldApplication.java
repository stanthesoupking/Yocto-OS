package apps.helloworld;

import java.io.IOException;

import yocto.application.Application;

public class HelloWorldApplication extends Application {
    public static void main(String[] args) {
        System.out.println("Started.");
        init();

        int i = 0;
        while (true) {
            writeString(1, 1, "Total iterations: " + i);
            i++;

            // for (int x = 0; x < 128; x++) {
            //     for (int y = 0; y < 64; y++) {
            //         setPixel(x, y, true);
            //     }
            // }
            try {
                sync();
            } catch (IOException e) {
                System.out.println("Sync failed.");
                System.exit(1);
            }
        }
    }
}