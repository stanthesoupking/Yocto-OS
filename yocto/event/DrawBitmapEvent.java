package yocto.event;

import yocto.util.Bitmap;

public class DrawBitmapEvent extends ApplicationEvent {
    private static final long serialVersionUID = 3765052095383303413L;
    public int x;
    public int y;
    public Bitmap bitmap;
    
    public DrawBitmapEvent(int x, int y, Bitmap bitmap) {
        super(ApplicationEventType.DRAW_BITMAP);
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
    }
}