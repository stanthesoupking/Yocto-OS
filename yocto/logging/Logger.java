package yocto.logging;

public class Logger {
    public static boolean showFullClassNames = false;
    public static boolean showTime = false;
    public static long startMilli = System.currentTimeMillis();

    public static void log(Class<?> c, String message) {
        String className = showFullClassNames ? c.getName() : c.getSimpleName();
        
        String source = className;
        if (showTime) {
            source = getCurrentTime() + "ms:" + source;
        }

        String output = String.format("[%s]: %s", source, message);
        System.out.println(output);
    }

    public static void showFullClassNames(boolean v) {
        showFullClassNames = v;
    }

    public static void showTime(boolean v) {
        showTime = v;
    }

    private static long getCurrentTime() {
        return System.currentTimeMillis() - startMilli;
    }
}