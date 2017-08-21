package com.example.fontjuna.dutchpay.classes_pack;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_EMPTY_INPUT;
import static com.example.fontjuna.dutchpay.backing.CommonDutchPay.ITEMnITEM;

/**
 * Created by fontjuna on 2017-08-22.
 */

public class Titles {
    private String mSource;
    private ArrayList<TitleData> mSourceList;
    private boolean mError;
    private String mMessage;
    private int mAmount;
    private int mGather;
    private int mRemain;
    private HashMap<String, Double> mResultList;

    public Titles(String source) {
        mError = source.isEmpty();
        mMessage = ERROR_EMPTY_INPUT + "(" + mSource + ")";
        mSource = source;
        if (!isError()) {
            mSourceList = new ArrayList<>();
            mResultList = new HashMap<>();
            mMessage = "";
            mAmount = 0;
            mGather = 0;
            mRemain = 0;
            parsing();
        }
    }

    private void parsing() {
        String[] sources = mSource.split(ITEMnITEM);
        for (String source : sources) {
            String title = Title(source);
            Amount amount = new Amount(source);
            Elements elements = new Elements(source);
            TitleData titleData = new TitleData(title, amount, elements);
            mSourceList.add(titleData);
        }
    }

    private String Title(String source) {
        return null;
    }

    public boolean isError() {
        return mError;
    }

    public String getMessage() {
        return mMessage;
    }

    private class TitleData {
        String mTitle;
        Amount mAmount;
        Elements mElements;

        public TitleData(String title, Amount amount, Elements elements) {
            mTitle = title;
            mAmount = amount;
            mElements = elements;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            mTitle = title;
        }

        public Amount getAmount() {
            return mAmount;
        }

        public void setAmount(Amount amount) {
            mAmount = amount;
        }

        public Elements getElements() {
            return mElements;
        }

        public void setElements(Elements elements) {
            mElements = elements;
        }
    }
}
