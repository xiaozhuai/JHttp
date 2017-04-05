package io.xiaozhuai.github.jhttp;

import java.util.Date;

/**
 * Created by xiaozhuai on 17/4/5.
 */
public class HttpLog {
    private static final String ANSI_COLOR_RED = "\033[31m";
    private static final String ANSI_COLOR_ORANGE = "\033[33m";
    private static final String ANSI_COLOR_NORMAL = "\033[0m";
    private static final String ANSI_COLOR_GREEN = "\033[32m";
    private static final String ANSI_COLOR_BLUE = "\033[34m";

    public static final int LOG_LEVEL_DEBUG = 0;
    public static final int LOG_LEVEL_INFO = 1;
    public static final int LOG_LEVEL_WARNING = 2;
    public static final int LOG_LEVEL_ERROR = 3;

    public interface LogCallback {
        void onLog(String log);
    }

    private static LogCallback sLogCallback;
    private static boolean sAnsiColorEnable = true;
    private static int sLogLevel = LOG_LEVEL_INFO;

    private static void log(String color, int level, String levelStr, String format, Object... args) {
        String logFrame = String.format(format, args);
        String date = new Date().toString();
        if (sAnsiColorEnable) logFrame = color + "[" + date + "] " + levelStr + logFrame + ANSI_COLOR_NORMAL;
        else logFrame = "[" + new Date().toString() + "] " + levelStr + logFrame;
        if (sLogCallback != null) sLogCallback.onLog(logFrame);
        else System.out.println(logFrame);
    }

    public static void D(String format, Object... args) {
        if (LOG_LEVEL_DEBUG >= sLogLevel)
            log(ANSI_COLOR_GREEN, LOG_LEVEL_DEBUG, "D/ ", format, args);
    }

    public static void I(String format, Object... args) {
        if (LOG_LEVEL_INFO >= sLogLevel)
            log(ANSI_COLOR_NORMAL, LOG_LEVEL_INFO, "I/ ", format, args);
    }

    public static void W(String format, Object... args) {
        if (LOG_LEVEL_WARNING >= sLogLevel)
            log(ANSI_COLOR_ORANGE, LOG_LEVEL_WARNING, "W/ ", format, args);
    }

    public static void E(String format, Object... args) {
        if (LOG_LEVEL_ERROR >= sLogLevel)
            log(ANSI_COLOR_RED, LOG_LEVEL_ERROR, "E/ ", format, args);
    }

    public static void setLogCallback(LogCallback cb) {
        sLogCallback = cb;
    }

    public static void enableAnsiColor(boolean enable) {
        sAnsiColorEnable = enable;
    }

    public static void setLogLevel(int logLevel) {
        sLogLevel = logLevel;
    }

}
