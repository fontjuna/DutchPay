package com.nohseunghwa.fontjuna.dutchpay.backing;

/**
 * Created by fontjuna on 2017-08-18.
 */

public class MessageEvent {
    private String mMessage;

    public MessageEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}
