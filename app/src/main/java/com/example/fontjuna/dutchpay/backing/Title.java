package com.example.fontjuna.dutchpay.backing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Created by fontjuna on 2017-08-20.
 */

public class Title implements CommonDutchPay {
    // 받은 데이타
    private String mExpression = "";

    // 계산 데이타
    private String mTitle = "";
    private double mSumMoney = 0.0;
    private double mSumRatio = 0.0;
    private double mUnitPrice = 0.0;
    private ArrayList<Double> mAmountList;
    private LinkedHashMap<String, Double> mResultsMap;
    private HashMap<String, Double> mMembersMap;

    // 상태 메세지
    private boolean mError = false;
    private String mMessage = "";

    public Title() {
        mError = true;
        mMessage = ERROR_EMPTY_INPUT;
    }

    public Title(String expression) {
        mExpression = expression;
        setUpDataFromExpression();
    }

    private void setUpDataFromExpression() {
        mAmountList = new ArrayList<>();
        mMembersMap = new HashMap<>();
        String remainText = checkAndFix(mExpression);
        if (!isError()) {
            remainText = extractTitle(remainText);
            if (!isError()) {
                remainText = extractMembers(remainText);
                if (!isError()) {
                    remainText = extractAmount(remainText);
                    finalExcute(remainText);
                }
            }
        }
    }

    public void finalExcute(String remainText) {
        mSumMoney = 0.0;
        for (double t : mAmountList) {
            mSumMoney += t;
        }

        mSumRatio = 0.0;
        for (String key : mMembersMap.keySet()) {
            mSumRatio += mMembersMap.get(key);
        }

        mUnitPrice = mSumMoney / mSumRatio;

        double value;
        TreeMap<String, Double> treeMap = new TreeMap<>(mMembersMap);
        for (String key : treeMap.keySet()) {
            value = 0.0;
            if (mResultsMap.containsKey(key)) {
                value = mMembersMap.get(key);
            }
            value = value + treeMap.get(key) * mUnitPrice;
            mResultsMap.put(key, value);
        }

    }

    private String checkAndFix(String expression) {
        if (expression.isEmpty()) {
            mMessage = ERROR_EMPTY_INPUT;
            mError = true;
            expression = "";
        } else {
            expression = expression.replace((char) 13, (char) 32);
            expression = expression.replace((char) 10, (char) 32);
            expression = expression.replace(" ", "");
            expression = expression.replace(MINUS + PLUS, MINUS);
            expression = expression.replace(PLUS + MINUS, MINUS);
            if (!Pattern.matches(VALID_CHARACTERS_ALL.replace(TITLEnMONEY, ""), expression)) {
                mMessage = ERROR_WRONG_EXPRESSION;
                mError = true;
            }
        }
        return expression;
    }

    private String extractAmount(String remainText) {
        if (remainText.isEmpty()) {
            mError = true;
            mMessage = ERROR_EMPTY_INPUT;
            return "";
        }
        if (!Pattern.matches(VALID_CHARACTERS_AMOUNT, remainText)) {
            mError = true;
            mMessage = ERROR_IN_AMOUNT;
            return "";
        }
        remainText = remainText.replace(COMMA, "");
        String[] plus = ("0" + remainText).split("[" + PLUS + "]"); // + 는 []로 감싼다
        for (String i : plus) {
            if (i.contains(MINUS)) {
                parseMinus(i);
            } else {
                mAmountList.add(Double.parseDouble(i));
            }
        }

        return remainText;
    }

    private void parseMinus(String s) {
        String[] minus = ("0" + s).split(MINUS);
        for (int j = 0; j < minus.length; j++) {
            mAmountList.add(Double.parseDouble(minus[j]));
        }
    }

    private String extractMembers(String remainText) {
        if (remainText.isEmpty()) {
            mError = true;
            mMessage = ERROR_EMPTY_INPUT;
            return "";
        }
        if (remainText.replace(LEFTnRIGHT, "").length() + 1 != remainText.length()) {
            // 구분자가 없거나 2개 이상일경우임 ( +1 )
            mError = true;
            mMessage = ERROR_INVALID;
            return "";
        }

        // 무조건 2개임 - 위에서 걸럿음
        String[] amount = remainText.split(LEFTnRIGHT);
        remainText = amount[0];
        if (Pattern.matches(VALID_CHARACTERS_MEMBER, amount[1])) {
            makeMemberList(amount[1]);
        } else {
            mError = true;
            mMessage = ERROR_IN_MEMBER;
        }
        return remainText;
    }

    private void makeMemberList(String right) {
        double value;
        String key;
        String[] members = right.split(MEMBERnMEMBER);
        for (String member : members) {
            if ((member.length() - member.replace(MEMBER2MEMBER, "").length()) < 2) {
                if (member.contains(MEMBER2MEMBER)) {
                    makeMemberListFromTo(member);
                } else {
                    Member ember = new Member(member);
                    if (!ember.isError()) {
                        value = 0.0;
                        key = ember.getName();
                        if (mMembersMap.containsKey(key)) {
                            value = mMembersMap.get(key);
                        }
                        mMembersMap.put(key, value + ember.getRatio());
                    }
                }
            } else {
                mError = true;
                mMessage = ERROR_WRONG_EXPRESSION;
                break;
            }
        }
    }

    private void makeMemberListFromTo(String member) {
        Member item = new Member(member);
        if (!item.isError()) {
            double value;
            String key;
            double ratio = item.getRatio();
            String[] fromTo = item.getName().split(MEMBER2MEMBER);
            int from = Integer.parseInt(fromTo[0]);
            int to = Integer.parseInt(fromTo[1]);
            for (int i = from; i <= to; i++) {
                key = i + "";
                value = 0.0;
                if (mMembersMap.containsKey(key)) {
                    value = mMembersMap.get(key);
                }
                mMembersMap.put(key, value + ratio);
            }
        }
    }

    private String extractTitle(String remainText) {
        if (remainText.isEmpty()) {
            mError = true;
            mMessage = ERROR_EMPTY_INPUT;
            return "";
        }
        if (remainText.length() - remainText.replace(TITLEnMONEY, "").length() > 1) {
            mError = true;
            mMessage = ERROR_INVALID;
            return "";
        }
        if (remainText.contains(TITLEnMONEY)) {
            String[] title = remainText.split(TITLEnMONEY);
            mTitle = title[0];
            return title[1];
        } else {
            mTitle = "";
            return remainText;
        }
    }

    public boolean isError() {
        return mError;
    }

    public String getMessage() {
        return mMessage;
    }

    public double get(String name) {
        return mResultsMap.get(name);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public ArrayList<Double> getAmountList() {
        return mAmountList;
    }

    public HashMap<String, Double> getMembersMap() {
        return mMembersMap;
    }

    public LinkedHashMap<String, Double> getResultsMap() {
        return mResultsMap;
    }

    public double getTotal() {
        return mSumMoney;
    }

    public double getRatio() {
        return mSumRatio;
    }

    public double getUnitPrice() {
        return mUnitPrice;
    }
}
