package com.nohseunghwa.fontjuna.dutchpay.temporary;

import java.util.TreeMap;

/**
 * Created by fontjuna on 2017-08-29.
 */

public class Title {
    private String mTitle;
    private double mAmount;
    private TreeMap<String, Double> mMember;
    private TreeMap<String, Double> mRatio;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double amount) {
        mAmount = amount;
    }

    public TreeMap<String, Double> getMember() {
        return mMember;
    }

    public void setMember(TreeMap<String, Double> member) {
        mMember = member;
    }

    public TreeMap<String, Double> getRatio() {
        return mRatio;
    }

    public void setRatio(TreeMap<String, Double> ratio) {
        mRatio = ratio;
    }
}
