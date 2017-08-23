package com.nohseunghwa.fontjuna.dutchpay.classes_pack;

/**
 * Created by fontjuna on 2017-08-23.
 */

public class TitleData {
    private String mText;
    private String mTitle;
    private Amount mAmounts;
    private Elements mElements;

    public TitleData(String title, Amount amount, Elements elements) {
        mTitle = title;
        mAmounts = amount;
        mElements = elements;
        mText = (mTitle.isEmpty() ? "" : mTitle + ":")
                + (mAmounts.isError() ? "" : mAmounts.getText() + "@")
                + (mElements.isError() ? "" : mElements.getText());
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Amount getAmounts() {
        return mAmounts;
    }

    public void setAmounts(Amount amounts) {
        mAmounts = amounts;
    }

    public Elements getElements() {
        return mElements;
    }

    public void setElements(Elements elements) {
        mElements = elements;
    }

    public String getText() {
        return mText;
    }
}
