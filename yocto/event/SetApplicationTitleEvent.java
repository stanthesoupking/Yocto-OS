package yocto.event;

public class SetApplicationTitleEvent extends ApplicationEvent {
    private static final long serialVersionUID = -6826067321051211478L;
    private String title;
    
    public SetApplicationTitleEvent(String title) {
        super(ApplicationEventType.SET_APP_TITLE);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}