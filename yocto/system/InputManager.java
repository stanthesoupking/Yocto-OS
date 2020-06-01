package yocto.system;

import java.io.Console;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.ansi.UnixTerminal;

import yocto.logging.Logger;
import yocto.event.KeyEvent;
import yocto.event.KeyEventType;

public class InputManager extends Thread {
    UnixTerminal terminal;

    private ApplicationServer applicationServer;

    public InputManager(ApplicationServer applicationServer) {
        this.applicationServer = applicationServer;

        // Connect to terminal
        terminal = null;
        try {
            terminal = new UnixTerminal();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            if (terminal != null) {
                while (true) {
                    // Generate key event and push to key events collection
                    generateKeyEvent(terminal.readInput());
                }
            }
        } catch (IOException e) {
            Logger.log(getClass(), "IO Error: " + e.getMessage());
        }
    }
    
    private void generateKeyEvent(KeyStroke key) {
        KeyEvent keyEvent = null;
        Logger.log(getClass(), "Key pressed " + key);
        if(key.getKeyType() == KeyType.Character) {
            keyEvent = KeyEvent.createCharacterEvent(key.getCharacter());
        } else if (key.getKeyType() == KeyType.ArrowDown) {
            keyEvent = KeyEvent.createKeyEvent(KeyEventType.ArrowDown);
        } else if (key.getKeyType() == KeyType.ArrowUp) {
            keyEvent = KeyEvent.createKeyEvent(KeyEventType.ArrowUp);
        } else if (key.getKeyType() == KeyType.ArrowLeft) {
            keyEvent = KeyEvent.createKeyEvent(KeyEventType.ArrowLeft);
        } else if (key.getKeyType() == KeyType.ArrowRight) {
            keyEvent = KeyEvent.createKeyEvent(KeyEventType.ArrowRight);
        }

        if (keyEvent != null) {
            // Push key event to application server
            applicationServer.pushKeyEvent(keyEvent);
        }
    }
}