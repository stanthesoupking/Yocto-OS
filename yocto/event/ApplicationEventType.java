package yocto.event;

public enum ApplicationEventType {
    SET_PIXEL,
    UNSET_PIXEL,
    WRITE_CHAR,
    WRITE_STRING,
    DRAW_BITMAP,
    SET_APP_TITLE,
    SET_APP_RUN_IN_BACKGROUND,
    SET_APP_RECEIVE_KEYSTROKES_IN_BACKGROUND,
    CLEAR,
    KEY,
    PRESENT
};