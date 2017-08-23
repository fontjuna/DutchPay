package com.nohseunghwa.fontjuna.dutchpay.classes_pack;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_EMPTY_INPUT;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_WRONG_EXPRESSION;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ITEMnITEM;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.TITLEnMONEY;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.VALID_CHARACTERS_ALL;

/**
 * Created by fontjuna on 2017-08-22.
 */

public class Titles {
    private String mSource;
    private ArrayList<TitleData> mTitleDatas;
    private boolean mError;
    private String mMessage;
    private String mText;

    public Titles(String source) {
        mError = source.isEmpty();
        mMessage = ERROR_EMPTY_INPUT + "(" + mSource + ")";
        mSource = source;
        if (!isError()) {
            mTitleDatas = new ArrayList<>();
            mMessage = "";
            mText="";
            parsing();
        }
    }

    private void parsing() {
        String[] sources = mSource.split(ITEMnITEM);
        for (String source : sources) {
            String title = Title(source);
            Amount amount = new Amount(source);
            if (amount.isError()) {
                mError = true;
                mMessage = amount.getMessage();
                break;
            }
            Elements elements = new Elements(source);
            if (elements.isError()) {
                mError = true;
                mMessage = elements.getMessage();
                break;
            }
            TitleData titleData = new TitleData(title, amount, elements);
            mTitleDatas.add(titleData);
            mText += (mText.isEmpty() ? "" : "/") + titleData.getText();
        }
    }

    private String[] Items(String source) {
        if (!Pattern.matches(VALID_CHARACTERS_ALL, source)) {
            mError = true;
            mMessage = ERROR_WRONG_EXPRESSION;
            return new String[]{};
        } else {
            String[] items = source.split(ITEMnITEM);
            if (items.length == 0) {
                mError = true;
                mMessage = ERROR_EMPTY_INPUT;
            }
            return items;
        }
    }

    private String Title(String source) {
        String title = "";
        if (source.contains(TITLEnMONEY)) {
            title = source.split(TITLEnMONEY)[0];
        }
        return title;
    }

    public boolean isError() {
        return mError;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getText() {
        return mText;
    }
    public String getSource() {return mSource;}
    public ArrayList<TitleData> getTitleDatas(){return mTitleDatas;}
}
