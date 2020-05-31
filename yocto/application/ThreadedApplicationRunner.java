package yocto.application;

public class ThreadedApplicationRunner extends Thread {
    private Application app;

    public ThreadedApplicationRunner(Application app) {
        this.app = app;
    }

    @Override
    public void run() {
        // Start app
        app.start();
    }
}