package com.nohseunghwa.fontjuna.dutchpay.temporary;

import java.util.Set;
import java.util.TreeMap;

/**
 * Created by fontjuna on 2017-08-29.
 */

public class Title {
    private String mTitle;
    private double mAmount;
    private TreeMap<String, Double> mData;

    public Title() {
        mTitle = "";
        mAmount = 0.0;
        mData = new TreeMap<>();
    }

    public Title(String title, double amount, TreeMap<String, Double> data) {
        mTitle = title;
        mAmount = amount;
        mData = data;
    }

    public double get(String key) {
        return mData.containsKey(key) ? mData.get(key) : 0.0;
    }

    public void put(String key, Double value) {
        mData.put(key, value);
    }

    public Set<String> keySet() {
        return mData.keySet();
    }

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

    public TreeMap<String, Double> getData() {
        return mData;
    }

    public void setData(TreeMap<String, Double> data) {
        mData = data;
    }
}
