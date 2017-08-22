package com.nohseunghwa.fontjuna.dutchpay.classes_pack;

import java.util.regex.Pattern;

import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.DOT;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.MEMBER2MEMBER;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.TEXT;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_DELIMITER;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_EMPTY_INPUT;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_WRONG_EXPRESSION;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.MEMBERnRATIO;

/**
 * Created by fontjuna on 2017-08-21.
 * <p>
 * 명칭[구분자 배율] 로 된 문자열을 입력 받아 (예: "홍길동!2.0") 파싱하고
 * 결과를 지닌다
 */

public class Element {
    private String mSource;
    private boolean mError;
    private String mMessage;
    private String mName;
    private double mRatio;

    public Element(String source) {

        mError = source.isEmpty();
        mSource = source;
        if (isError()) {
            mMessage = ERROR_EMPTY_INPUT + "(구성원)";
        } else {
            mMessage = "";
            mName = "";
            mRatio = 1.0;
            parsing();
        }
    }

    private void parsing() {
        if (!Pattern.matches("^[" + TEXT + MEMBERnRATIO + MEMBER2MEMBER + DOT + "]*$", mSource)) {
            mError = true;
            mMessage = ERROR_WRONG_EXPRESSION + "(" + mSource + ")";
        } else if (mSource.length() > mSource.replace(MEMBERnRATIO, "").length() + 1 ||
                mSource.contains(MEMBERnRATIO) && mSource.length() < 3) {
            mError = true;
            mMessage = ERROR_DELIMITER + "(" + mSource + ")";
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

    public String getMessage() {
        return mMessage;
    }

}
