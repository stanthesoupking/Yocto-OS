package yocto.util.bdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import yocto.logging.Logger;

/**
 * BDF Font
 */
public class Font {
    private int fontWidth;
    private int fontHeight;

    private char[][] bitmapData;

    public Font(String path) throws FileNotFoundException, Exception {
        // Load font from path
        loadFromFile(path);
    }

    public char[] getBitmapData(char c) {
        return bitmapData[c];
    }

    public int getFontWidth() {
        return fontWidth;
    }

    public int getFontHeight() {
        return fontHeight;
    }

    public char[] getCharacter(char c) {
        return bitmapData[c];
    }

    private void loadFromFile(String path) throws FileNotFoundException, Exception {
        Scanner in = new Scanner(new FileReader(path));

        // Get STARTFONT
        String line = in.nextLine();
        if (!line.equals("STARTFONT 2.1")) {
            Logger.log(getClass(), "Error: Unsupported BDF format.");
            throw new Exception("Unsupported BDF format.");
        }

        FontBoundingBox boundingBox = new FontBoundingBox(0, 0, 0, 0);
        FontBoundingBox bbx = new FontBoundingBox(0, 0, 0, 0);
        int currentCharacter = 0;

        try {
            while (in.hasNextLine()) {
                // Get line key
                String key = in.next();

                // Check if start of bitmap data
                if (key.equals("BITMAP")) {
                    // Skip non-ASCII characters
                    if ((currentCharacter < 0) || (currentCharacter > 255)) {
                        continue;
                    }
                    // Allocate bitmap data (if not already allocated)
                    if (bitmapData == null) {
                        bitmapData = new char[256][boundingBox.height];
                    }

                    // Read bitmap data
                    // Add y-offset lines
                    int aboveRowCount = ((boundingBox.height - bbx.height) - bbx.offsetY + boundingBox.offsetY);
                    for (int i = 0; i < aboveRowCount; i++) {
                        bitmapData[currentCharacter][i] = 0;
                    }

                    for (int i = aboveRowCount; i < aboveRowCount + bbx.height; i++) {
                        String hexString = in.next();
                        char row = (char) Integer.parseInt(hexString, 16);

                        row = (char) ((int) row >> bbx.offsetX);
                        bitmapData[currentCharacter][i] = row;
                    }

                    // Append remaining lines
                    for (int i = aboveRowCount + bbx.height; i < boundingBox.height; i++) {
                        bitmapData[currentCharacter][i] = 0;
                    }

                    // Read ENDCHAR
                    in.nextLine();
                }

                boolean isFontBoundingBox = key.equals("FONTBOUNDINGBOX");
                boolean isBBX = key.equals("BBX");

                if (isFontBoundingBox || isBBX) {
                    FontBoundingBox tBox = new FontBoundingBox(0, 0, 0, 0);

                    // Read bbx data
                    tBox.width = in.nextInt();
                    tBox.height = in.nextInt();
                    tBox.offsetX = in.nextInt();
                    tBox.offsetY = in.nextInt();

                    // Only allow fonts with a width of <= 8 (byte)
                    if (tBox.width > 8) {
                        Logger.log(getClass(), "Error: Only fonts with a width of 8 or less pixels are supported.");
                        throw new Exception("Font too wide");
                    }

                    if (isFontBoundingBox) {
                        boundingBox = tBox;
                        fontHeight = boundingBox.height;
                        fontWidth = boundingBox.width;
                    } else {
                        bbx = tBox;
                    }
                } else if (key.equals("ENCODING")) {
                    // Get current character
                    currentCharacter = in.nextInt();
                }
            }
        } catch (Exception e) {
            // Do nothing
        }
    }
}