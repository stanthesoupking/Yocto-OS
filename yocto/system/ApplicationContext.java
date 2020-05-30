package yocto.system;

import java.io.IOException;

import yocto.drivers.Display;
import yocto.util.ApplicationEvent;
import yocto.util.ApplicationEventType;
import yocto.util.bdf.Font;

public class ApplicationContext {
    private Display display;
    private Font font;

    public ApplicationContext(Display display, Font font) {
        this.display = display;
        this.font = font;
    }

    public Display getDisplay() {
        return display;
    }

    public void doEvents(ConnectedApplication app, ApplicationEvent[] events) {
        for (ApplicationEvent event : events) {
            doEvent(app, event);
        }
    }

    public void doEvent(ConnectedApplication app, ApplicationEvent event) {
        // Filter events that are foreground-only
        if (app.getForeground() || !isForegroundOnly(event)) {
            switch (event.eventType) {
                case SET_PIXEL:
                    doSetPixel(event);
                    break;
                case UNSET_PIXEL:
                    doUnsetPixel(event);
                    break;
                case WRITE_CHAR:
                    doWriteChar(event);
                    break;
                case WRITE_STRING:
                    doWriteString(event);
                    break;
                case CLEAR:
                    doClear(event);
                    break;
                case PRESENT:
                    doPresent(event);
                    break;
                default:
                    // Do nothing
                    break;
            }
        }
    }

    private void doSetPixel(ApplicationEvent event) {
        try {
            synchronized (display) {
                display.setPixel(event.ix, event.iy, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doUnsetPixel(ApplicationEvent event) {
        try {
            synchronized (display) {
                display.setPixel(event.ix, event.iy, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doClear(ApplicationEvent event) {
        try {
            synchronized (display) {
                display.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doPresent(ApplicationEvent event) {
        try {
            synchronized (display) {
                display.present();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWriteChar(ApplicationEvent event) {
        try {
            synchronized (display) {
                display.writeChar(event.ix, event.iy, font, event.cx);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWriteString(ApplicationEvent event) {
        try {
            synchronized (display) {
                display.writeString(event.ix, event.iy, font, event.sx);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isForegroundOnly(ApplicationEvent event) {
        return ((event.eventType == ApplicationEventType.CLEAR) || (event.eventType == ApplicationEventType.PRESENT)
                || (event.eventType == ApplicationEventType.SET_PIXEL)
                || (event.eventType == ApplicationEventType.UNSET_PIXEL)
                || (event.eventType == ApplicationEventType.WRITE_CHAR)
                || (event.eventType == ApplicationEventType.WRITE_STRING));
    }
}