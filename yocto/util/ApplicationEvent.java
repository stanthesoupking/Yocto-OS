package yocto.util;

import java.io.Serializable;

public class ApplicationEvent implements Serializable {
    private static final long serialVersionUID = 7585589170978364063L;

    public ApplicationEventType eventType;
    public int ix, iy, iz;
    public String sx, sy, sz;
    public char cx, cy, cz;

    public ApplicationEvent() {}

    public ApplicationEvent(ApplicationEventType eventType) {
        this.eventType = eventType;
    }

}