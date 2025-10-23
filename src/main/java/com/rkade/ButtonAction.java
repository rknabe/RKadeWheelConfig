package com.rkade;

public enum ButtonAction {
    UNKNOWN(255),
    NONE(0),
    PAUSE(1),
    SHUTDOWN(2),
    ESCAPE(3);

    private int code = 255;

    ButtonAction(int i) {
        this.code = i;
    }

    public static ButtonAction from(int code) {
        for (ButtonAction action : ButtonAction.values()) {
            if (action.getCode() == code) {
                return action;
            }
        }
        return UNKNOWN;
    }

    public int getCode() {
        return code;
    }
}
