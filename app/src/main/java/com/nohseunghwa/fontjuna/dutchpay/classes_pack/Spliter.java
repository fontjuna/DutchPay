package com.nohseunghwa.fontjuna.dutchpay.classes_pack;

/**
 * Created by fontjuna on 2017-08-22.
 */

public class Spliter {
    private String mSource;
    private String mDelimiter;
    private boolean mError;
    private int mCount;

    public Spliter(String source, String delimiter) {
        mSource = source;
        mDelimiter = delimiter;
        split();
    }

    private void split() {
        String[] result = mSource.split(mDelimiter);
        mCount = result.length;
    }


}
