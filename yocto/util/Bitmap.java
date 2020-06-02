package yocto.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class Bitmap implements Serializable {
    private static final long serialVersionUID = -6392300794855573118L;
    private char[][] data;
    private int width;
    private int height;

    public Bitmap(int width, int height) {
        this.width = width;
        this.height = height;

        data = new char[height][(int) Math.ceil(width / 8.0)];
    }

    public static Bitmap loadFromFile(String path) throws IOException {
        BufferedImage bImg = ImageIO.read(new File(path));
        int w = bImg.getWidth();
        int h = bImg.getHeight();

        Bitmap bitmap = new Bitmap(w, h);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int p = bImg.getRGB(x, y) * -1;
                bitmap.setPixel(x, y, p == 1);
            }
        }

        return bitmap;
    }

    public boolean getPixel(int x, int y) {
        return ((data[y][x / 8] << (x % 8)) & 128) == 128;
    }

    public void setPixel(int x, int y, boolean state) {
        if (state) {
            data[y][x / 8] |= 128 >> (x % 8);
        } else {
            data[y][x / 8] &= ~128 >> (x % 8);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}