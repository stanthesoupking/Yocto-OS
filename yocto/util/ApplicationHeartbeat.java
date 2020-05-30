package yocto.util;

import java.io.Serializable;

public class ApplicationHeartbeat implements Serializable {
    private static final long serialVersionUID = 2154307396912188875L;

    public ApplicationEvent[] events;

    public ApplicationHeartbeat(ApplicationEvent[] events) {
        this.events = events;
    }
}