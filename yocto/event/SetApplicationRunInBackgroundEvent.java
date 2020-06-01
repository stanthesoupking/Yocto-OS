package yocto.event;

public class SetApplicationRunInBackgroundEvent extends ApplicationEvent {
    private static final long serialVersionUID = -2427151489536813152L;
    public boolean value;
    
    public SetApplicationRunInBackgroundEvent(boolean value) {
        super(ApplicationEventType.SET_APP_RUN_IN_BACKGROUND);
        this.value = value;
    }
}