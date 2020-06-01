package yocto.event;

public class WriteStringEvent extends ApplicationEvent {
    private static final long serialVersionUID = 3765052095383303413L;
    public String text;
    public int x;
    public int y;
    
    public WriteStringEvent(int x, int y, String text) {
        super(ApplicationEventType.WRITE_STRING);
        this.x = x;
        this.y = y;
        this.text = text;
    }
}