package com.example.fontjuna.dutchpay.backing;

/**
 * Created by fontjuna on 2017-08-20.
 */

public class Member implements CommonDutchPay {
    private String mName = "";
    private double mRatio = 1.0;
    private boolean mError = true;
    private String mText = "";

    public Member(String text) {
        this.mError = false;
        this.mText = text;
        excute();
    }

    public String getName() {
        return mName;
    }

    public double getRatio() {
        return mRatio;
    }

    private void excute() {
        if (mText.isEmpty()) {
            mError = true;
        } else if (mText.length() > mText.replace(MEMBERnRATIO, "").length() + 1) {
            mError = true;
        }
        if (!isError()) {
            String[] text = mText.split(MEMBERnRATIO);
            mName = text[0];
            mRatio = text.length < 2 ? 1.0 : Double.parseDouble(text[1]);
        }
    }

    public boolean isError() {
        return mError;
    }

}
