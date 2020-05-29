package yocto.drivers;

import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import com.pi4j.io.spi.SpiMode;

import yocto.logging.Logger;
import yocto.util.IntegerPair;
import yocto.util.bdf.Font;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Display {
    private final int GDRAM_ROWS = 32;
    private final int GDRAM_COLS = 16;
    private final int GDRAM_SIZE = GDRAM_ROWS * GDRAM_COLS;

    private final int SCREEN_WIDTH = 128;
    private final int SCREEN_HEIGHT = 64;

    protected char[] gdramBuffer;
    protected SpiDevice spi = null;

    private Set<IntegerPair> dirtyGDRamBytes;

    public Display() throws IOException {
        spi = SpiFactory.getInstance(SpiChannel.CS1, 540000, SpiMode.MODE_0);

        // Create GD RAM buffer
        gdramBuffer = new char[GDRAM_SIZE * 2];

        // Create GD RAM dirty bytes set
        dirtyGDRamBytes = new HashSet<IntegerPair>(GDRAM_SIZE);

        Logger.log(getClass(), "Setting up display...");
        setupDisplay(true, false, false);
        setGraphicDisplay(true);

        Logger.log(getClass(), "Clearing GD RAM...");
        clear();
        present();
    }

    private void writeData(byte[] data) throws IOException {
        //Logger.log(getClass(), "Writing out " + data.length + " bytes...");
        // StringBuilder sb = new StringBuilder();
        // for (byte b : data) {
        // sb.append(String.format("%02X ", b));
        // }
        // Logger.log(getClass(), "Bytes: " + sb.toString());
        spi.write(data);
    }

    private void sendInstruction(char instruction) throws IOException {
        sendInstruction(instruction, false, false);
    }

    private void sendInstruction(char instruction, boolean rs, boolean rw) throws IOException {
        byte output[] = { 0, 0, 0 };

        // Write synchronisation string
        output[0] = (byte) (0b11111000 | ((rs ? 1 : 0) << 1) | ((rw ? 1 : 0) << 2));

        // Write higher data (first 4 bits)
        output[1] = (byte) (instruction & 0b11110000);

        // Write lower data
        output[2] = (byte) (instruction << 4);

        writeData(output);
    }

    private void sendInstructions(char[] instructions) throws IOException {
        sendInstructions(instructions, false, false);
    }

    private void sendInstructions(char[] instructions, boolean rs, boolean rw) throws IOException {
        byte output[] = new byte[2 * instructions.length + 1];

        int outPos = 0;

        // Write synchronisation string
        output[outPos++] = (byte) (0b11111000 | ((rs ? 1 : 0) << 1) | ((rw ? 1 : 0) << 2));
        for (int i = 0; i < instructions.length; i++) {
            
            // Write higher data (first 4 bits)
            output[outPos++] = (byte) (instructions[i] & 0b11110000);

            // Write lower data
            output[outPos++] = (byte) (instructions[i] << 4);
        }

        writeData(output);
    }

    private void setupDisplay(boolean displayOn, boolean cursor, boolean characterBlink) throws IOException {
        char data = 0b00001000;

        if (displayOn) {
            data = (char) (data | 0b00000100);
        }

        if (cursor) {
            data = (char) (data | 0b00000010);
        }

        if (characterBlink) {
            data = (char) (data | 0b00000001);
        }

        sendInstruction(data);
    }

    private void setExtendedFunctionSet(boolean v) throws IOException {
        char data = 0b00100000;

        if (v) {
            data = (char) (data | 0b00000100);
        }

        sendInstruction(data);
    }

    private void setGraphicDisplay(boolean v) throws IOException {
        char data = 0b00100100;

        if (v) {
            data = (char) (data | 0b00000010);
        }

        sendInstruction(data);
    }

    private void setGDRamAddress(int x, int y) throws IOException {
        char data[] = { (char) (0b10000000 | ((char) y & 0b00111111)), (char) (0b10000000 | ((char) x & 0b00001111)) };

        sendInstructions(data);
    }

    private void setGDRamBytes(int x, int y, char bytes[]) throws IOException {
        setGDRamAddress(x, y);

        sendInstructions(bytes, true, false);
    }

    private void clearGDRam() throws IOException {
        char gbytes[] = { 0, 0 };

        int pos;
        for (int y = 0; y < GDRAM_ROWS; y++) {
            for (int x = 0; x < GDRAM_COLS; x++) {
                // Update GD Ram Buffer
                pos = getGDRamBufferPosition(x, y);
                gdramBuffer[pos] = 0;
                gdramBuffer[pos + 1] = 0;

                setGDRamBytes(x, y, gbytes);
            }
        }
    }

    private int getGDRamBufferPosition(int x, int y) {
        return x * 2 + y * 8 * 4;
    }

    private void setPixel(int x, int y, boolean state) throws IOException {
        // Constrain X and Y
        if ((x < 0) || (x >= SCREEN_WIDTH) || (y < 0) || (y >= SCREEN_HEIGHT)) {
            return;
        }

        int byte_x;
        int byte_y;
        if (y < 32) {
            byte_x = x / 16;
            byte_y = y;
        } else {
            byte_x = 8 + (x / 16);
            byte_y = y - 32;
        }

        char byte_mod = (char) (128 >> (x % 8));

        int bufferAddress = getGDRamBufferPosition(byte_x, byte_y);
        int offset = (x % 16) / 8;
        char oldByte = gdramBuffer[bufferAddress + offset];
        if (state) {
            gdramBuffer[bufferAddress + offset] |= byte_mod;
        } else {
            gdramBuffer[bufferAddress + offset] &= ~byte_mod;
        }

        if (oldByte != gdramBuffer[bufferAddress + offset]) {
            // Push dirty byte
            dirtyGDRamBytes.add(new IntegerPair(byte_x, byte_y));
        }

        // char bytes[] = { gdramBuffer[bufferAddress], gdramBuffer[bufferAddress + 1] };
        // setGDRamBytes(byte_x, byte_y, bytes);
    }

    public void clear() throws IOException {
        clearGDRam();
    }

    public void present() throws IOException {
        int addr;
        char bytes[] = { 0, 0 };
        Logger.log(getClass(), dirtyGDRamBytes.size() + " dirty bytes to update.");
        for (IntegerPair dirtyPos : dirtyGDRamBytes) {
            // Update dirty byte
            addr = getGDRamBufferPosition(dirtyPos.a, dirtyPos.b);
            bytes[0] = gdramBuffer[addr];
            bytes[1] = gdramBuffer[addr + 1];

            setGDRamBytes(dirtyPos.a, dirtyPos.b, bytes);
        }
        dirtyGDRamBytes.clear();
    }

    public void writeChar(int x, int y, Font font, char c) throws IOException {
        int charWidth = font.getFontWidth();
        int charHeight = font.getFontHeight();

        // Print character
        char bmp[] = font.getBitmapData(c);
        for (int sy = 0; sy < charHeight; sy++) {
            for (int sx = 0; sx < charWidth; sx++) {
                boolean draw = ((bmp[sy] << sx) & 128) == 128;
                setPixel(x + sx, y + sy, draw);
            }
        }
    }

    public void writeString(int x, int y, Font font, String text) throws IOException {
        int charWidth = font.getFontWidth();
        int charHeight = font.getFontHeight();

        int cx = x;
        int totalChars = text.length();
        for (int i = 0; i < totalChars; i++) {
            writeChar(cx, y, font, text.charAt(i));
            cx += charWidth;
        }
    }
}