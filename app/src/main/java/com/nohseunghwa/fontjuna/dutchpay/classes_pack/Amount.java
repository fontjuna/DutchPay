package com.nohseunghwa.fontjuna.dutchpay.classes_pack;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.COMMA;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_EMPTY_INPUT;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_IN_AMOUNT;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_WRONG_EXPRESSION;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ITEMnITEM;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.LEFTnRIGHT;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.MINUS;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.PLUS;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.TITLEnMONEY;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.VALID_CHARACTERS_ALL;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.VALID_CHARACTERS_AMOUNT;

/**
 * Created by fontjuna on 2017-08-22.
 */

public class Amount {
    private String mSource;
    private ArrayList<Double> mAmount;
    private String mText;
    private boolean mError;
    private String mMessage;
    private double mTotal;

    public Amount(String source) {
        mError = source.isEmpty();
        mMessage = ERROR_EMPTY_INPUT + "(" + mSource + ")";
        mSource = source;
        if (!isError()) {
            mAmount = new ArrayList<>();
            mMessage = "";
            mText = "";
            mTotal = 0.0;
            parsing();
        }
    }

    private String checkAndFix(String source) {
        source = source.replace((char) 13, (char) 32);
        source = source.replace((char) 10, (char) 32);
        source = source.replace(" ", "");
        source = source.replace(MINUS + PLUS, MINUS);
        source = source.replace(PLUS + MINUS, MINUS);
        if (!Pattern.matches(VALID_CHARACTERS_ALL, source)) {
            mMessage = ERROR_WRONG_EXPRESSION + "(" + source + ")";
            mError = true;
        }
        return source;
    }

    private void parsing() {
        // 무조건 첫번째만 해당함
        String source = checkAndFix(mSource);
        if (!isError()) {
            source = source.split(ITEMnITEM)[0];
            if (!isError()) {

                if (source.contains(LEFTnRIGHT)) {
                    source = source.split(LEFTnRIGHT)[0];
                }
                if (source.contains(TITLEnMONEY)) {
                    source = source.split(TITLEnMONEY)[1];
                }
                if (source.isEmpty()) {
                    mError = true;
                    mMessage = ERROR_EMPTY_INPUT + "(" + source + ")";
                } else if (!Pattern.matches(VALID_CHARACTERS_AMOUNT, source)) {
                    mError = true;
                    mMessage = ERROR_WRONG_EXPRESSION + "(" + source + ")";
                } else {
                    extractAmount(source);
                    if (!isError()) {
                        for (double d : mAmount) {
                            mTotal += d;
                        }
                    }
                }
            }
        }
    }

    private String extractAmount(String source) {
        if (source.isEmpty()) {
            mError = true;
            mMessage = ERROR_EMPTY_INPUT;
            return "";
        }
        if (!Pattern.matches(VALID_CHARACTERS_AMOUNT, source)) {
            mError = true;
            mMessage = ERROR_IN_AMOUNT;
            return "";
        }
        source = source.replace(COMMA, "");
        String[] plus = ("0" + source).split("[" + PLUS + "]"); // + 는 []로 감싼다
        for (String i : plus) {
            if (i.contains(MINUS)) {
                parseMinus(i);
            } else {
                mAmount.add(Double.parseDouble(i));
                mText += (mText.isEmpty() ? "" : "+") + i;
            }
        }

        return source;
    }

    private void parseMinus(String s) {
        s = s.replace(MINUS, COMMA + MINUS);
        String[] minus = ("0" + s).split(COMMA);
        for (int j = 0; j < minus.length; j++) {
            mAmount.add(Double.parseDouble(minus[j]));
            mText += (mText.isEmpty() ? "" : "+") + minus[j];
        }
    }

    public boolean isError() {
        return mError;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getSource() {
        return mSource;
    }

    public double getTotal() {
        return mTotal;
    }

    public int getCount() {
        return mAmount.size();
    }

    public ArrayList<Double> getAmount() {
        return mAmount;
    }

    public String getText() {
        return mText;
    }
}
