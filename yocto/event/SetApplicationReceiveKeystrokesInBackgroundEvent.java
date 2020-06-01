package yocto.event;

public class SetApplicationReceiveKeystrokesInBackgroundEvent extends ApplicationEvent {
    private static final long serialVersionUID = -8275981036971339383L;
    public boolean value;
    
    public SetApplicationReceiveKeystrokesInBackgroundEvent(boolean value) {
        super(ApplicationEventType.SET_APP_RECEIVE_KEYSTROKES_IN_BACKGROUND);
        this.value = value;
    }
}