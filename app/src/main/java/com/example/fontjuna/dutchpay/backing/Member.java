package com.example.fontjuna.dutchpay.backing;

import java.util.regex.Pattern;

/**
 * Created by fontjuna on 2017-08-20.
 */

public class Member implements CommonDutchPay {
    private boolean mError = true;
    private String mErrorMessage = "";
    private String mName = "";
    private double mRatio = 1.0;
    private String mSource = "";

    public Member(String source) {
        this.mError = false;
        this.mSource = source;
        parsing();
    }

    private void parsing() {
        if (mSource.isEmpty()) {
            mErrorMessage = ERROR_EMPTY_INPUT;
            mError = true;
        } else if (!Pattern.matches("^[" + TEXT + MEMBERnRATIO + DOT + "]*$", mSource)) {
            mError = true;
            mErrorMessage = ERROR_WRONG_EXPRESSION;
        } else if (mSource.length() > mSource.replace(MEMBERnRATIO, "").length() + 1) {
            mError = true;
            mErrorMessage = ERROR_DELIMITER;
        }
        if (!isError()) {
            String[] text = mSource.split(MEMBERnRATIO);
            mName = text[0];
            mRatio = text.length < 2 ? 1.0 : Double.parseDouble(text[1]);
        }
    }

    public String getName() {
        return mName;
    }

    public double getRatio() {
        return mRatio;
    }

    public boolean isError() {
        return mError;
    }

    public String getError() {
        return mErrorMessage;
    }

}
