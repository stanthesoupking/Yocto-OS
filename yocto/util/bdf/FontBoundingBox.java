package yocto.util.bdf;

public class FontBoundingBox {
    public int width;
    public int height;
    public int offsetX;
    public int offsetY;

    public FontBoundingBox(int width, int height, int offsetX, int offsetY) {
        this.width = width;
        this.height= height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}