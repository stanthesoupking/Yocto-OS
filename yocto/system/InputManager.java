package yocto.system;

import java.io.Console;
import java.io.IOException;
import java.io.Reader;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.ansi.UnixTerminal;

import yocto.logging.Logger;

public class InputManager extends Thread {
    UnixTerminal terminal;

    public InputManager() {
        terminal = null;
        try {
            terminal = new UnixTerminal();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            if (terminal != null) {
                while (true) {
                    KeyStroke key = terminal.readInput();
                    Logger.log(getClass(), "Pressed " + key);
                }
            }
        } catch (IOException e) {
            Logger.log(getClass(), "IO Error: " + e.getMessage());
        }
    }
}