package yocto.event;

public class KeyEvent extends ApplicationEvent {
    private static final long serialVersionUID = 543778023073294347L;

    protected KeyEventType keyType;
    protected char c = '\0';

    public KeyEvent() {
        super(ApplicationEventType.KEY);
    }

    public static KeyEvent createCharacterEvent(char c) {
        KeyEvent keyEvent = new KeyEvent();
        keyEvent.c = c;
        keyEvent.keyType = KeyEventType.Character;
        return keyEvent;
    }

    public static KeyEvent createKeyEvent(KeyEventType type) {
        KeyEvent keyEvent = new KeyEvent();
        keyEvent.keyType = type;
        return keyEvent;
    }

    public char getCharacter() {
        return c;
    }

    public KeyEventType getKeyType() {
        return keyType;
    }
}