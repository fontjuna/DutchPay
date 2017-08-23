package com.nohseunghwa.fontjuna.dutchpay.classes_pack;

import java.text.DecimalFormat;
import java.util.TreeMap;

/**
 * Created by fontjuna on 2017-08-22.
 */

public class Spliter {
    private String mSource;
    private Titles mTitles;
    private int mUnit;
    private String mMessage;
    private boolean mError;
    private int mCount;
    private double mAmount;
    private double mGather;
    private double mRemain;
    private TreeMap<String, Double> mMember;
    private DecimalFormat df = new DecimalFormat("#,##0");

    public Spliter(String source, int unit) {
        mSource = source;
        mUnit = unit;
        mCount = 0;
        mAmount = 0;
        mGather = 0;
        mRemain = 0;
        mMember = new TreeMap<>();
        mTitles = new Titles(source);
        mError = mTitles.isError();
        if (isError()) {
            mMessage = mTitles.getMessage();
        } else {
            makeResult();
        }
    }

    private void makeResult() {
        double amount = 0;
        double sumRaio = 0.0;
        double unitPrice = 0.0;
        double value = 0.0;
        for (TitleData td : mTitles.getTitleDatas()) {
            amount = td.getAmounts().getTotal();
            sumRaio = td.getElements().getSumRatio();
            unitPrice = amount / sumRaio;
            for (String key : td.getElements().getElementDatas().keySet()) {
                value = 0.0;
                if (mMember.containsKey(key)) {
                    value = mMember.get(key);
                }
                mMember.put(key, value + td.getElements().getRatio(key) * unitPrice);
            }
            mAmount += amount;
        }
    }

    public String getResultText() {
        if (isError()) {
            mMessage = mTitles.getMessage();
        } else {
            int money = 0;
            int digits = getDigits();

            mMessage = "";
            mMessage += "총_금_액 = " + padNum(mAmount, digits);
            mMessage += "\n계산단위 : " + padNum(mUnit, digits);

            String temp = "";

            int value;
            for (String key : mMember.keySet()) {
                value = (int) ((mMember.get(key) * 10 + 5) / 10);
                money = (int) ((double) ((value + mUnit - 1) / mUnit) * mUnit);
                temp += "\n" + key + " : " + padNum(money, digits);
                mGather += money;
            }
            mRemain = mGather - (int) mAmount;
            mMessage += "\n걷는금액 : " + padNum(mGather, digits);
            mMessage += "\n남는금액 : " + padNum(mRemain, digits);
            mMessage += "\n" + temp;
        }
        return mMessage;
    }

    public boolean isError() {
        return mError;
    }

    public int getCount() {
        return mCount;
    }

    private int getDigits() {
        return df.format(mAmount).length();
    }

    private String padNum(double num, int n) {
        return padLeft(df.format(num), n);
    }

    private static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s).replace(" ", "_");
    }
}
