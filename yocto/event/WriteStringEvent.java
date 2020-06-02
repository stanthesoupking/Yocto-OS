package yocto.event;

import yocto.util.Gravity;

public class WriteStringEvent extends ApplicationEvent {
    private static final long serialVersionUID = 3765052095383303413L;
    public String text;
    public Gravity gravity;
    public int x;
    public int y;
    
    public WriteStringEvent(int x, int y, String text) {
        super(ApplicationEventType.WRITE_STRING);
        this.x = x;
        this.y = y;
        this.text = text;
        this.gravity = Gravity.TOP_LEFT;
    }

    public WriteStringEvent(int x, int y, String text, Gravity gravity) {
        super(ApplicationEventType.WRITE_STRING);
        this.x = x;
        this.y = y;
        this.text = text;
        this.gravity = gravity;
    }
}