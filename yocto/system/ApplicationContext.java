package yocto.system;

import java.io.IOException;

import yocto.drivers.Display;
import yocto.util.ApplicationEvent;
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

    public void doEvents(ApplicationEvent[] events) {
        for (ApplicationEvent event : events) {
            doEvent(event);
        }
    }

    public void doEvent(ApplicationEvent event) {
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

    private void doSetPixel(ApplicationEvent event) {
        try {
            display.setPixel(event.ix, event.iy, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doUnsetPixel(ApplicationEvent event) {
        try {
            display.setPixel(event.ix, event.iy, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doClear(ApplicationEvent event) {
        try {
            display.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doPresent(ApplicationEvent event) {
        try {
            display.present();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWriteChar(ApplicationEvent event) {
        try {
            display.writeChar(event.ix, event.iy, font, event.cx);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWriteString(ApplicationEvent event) {
        try {
            display.writeString(event.ix, event.iy, font, event.sx);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}