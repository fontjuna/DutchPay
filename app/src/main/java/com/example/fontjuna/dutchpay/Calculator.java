package com.example.fontjuna.dutchpay;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Created by fontjuna on 2017-08-15.
 */

public class Calculator {

    private static final String TAG = Calculator.class.getSimpleName();
    private static final String MINUS = "-";
    private static final String PLUS = "+";
    private static final String ITEM = "/";
    private static final String MONEY = ":";
    private static final String MEMBER = ",";
    private static final String RATIO = "!";
    private static final String OK_STR = "^[0-9a-zA-Zㄱ-힣!:/,+-.]*$";

    private Map<String, Double> mResultList;
    private int mUnit = 0;
    private double mAmount = 0.0;
    private int mGather = 0;
    private int mRemain = 0;
    private String mResultText = "";
    private boolean mError = false;
    private DecimalFormat df = new DecimalFormat("#,##0");

    //==========================================================================================//
    public Calculator(String text, int unit) {
        mUnit = unit;
        mError = false;
        text = text.replace(" ", "");
        text = text.replace("-+", "-");
        text = text.replace("+-", "-");
        checkBeforeCalcResult(text);
        if (!isError()) {
            calcResult(text);
        }
    }

    public String getResult() {
        return mResultText;
    }

    public boolean isError() {
        return mError;
    }

    //------------------------------------------------------------------------------------------//
    private void checkBeforeCalcResult(String text) {
        if (text.isEmpty()) {
            mResultText = "입력된 내용이 없습니다.";
            mError = true;
        } else {
            if (!Pattern.matches(OK_STR, text)) {
                mResultText = "불필요한 문자가 들어 있습니다.";
                mError = true;
            }
        }
    }

    // 1st Level Proccess
    private void calcResult(String text) {
        mResultList = new HashMap<>();
        String textResult = "";
        String[] items = text.split(ITEM);
        for (String s : items) {
            checkBeforeSplitMoney(s);
            if (isError()) {
                break;
            }
            splitMoney(s);
        }
        if (!isError()) {
            makeResultText();
        }
    }

    private void checkBeforeSplitMoney(String s) {
        if (s.isEmpty() || !s.contains(MONEY) || s.split(MONEY).length < 2) {
            mError = true;
            mResultText = "금액과 나눌 사람들이 명확하지 않습니다.";
        } else {

        }
    }

    // 2nd Level Proccess
    private void splitMoney(String s) {
        String[] text = s.split(MONEY);
        checkBeforeSplitAmount(text[0]);
        checkBeforeSplitPerson(text[1]);

        if (!isError()) {
            // 금액합산
            double amount = splitAmount(text[0]);
            mAmount += amount;

            // 나눌 사람들
            double rate = 0.0;
            HashMap<String, Double> map = splitPerson(text[1]);
            for (String key : map.keySet()) {
                rate += map.get(key);
            }
            double everyMoney = amount / rate;
            double money;
            for (String key : map.keySet()) {
                money = 0.0;
                if (mResultList.containsKey(key)) {
                    money = mResultList.get(key);
                }
                money += map.get(key) * everyMoney;
                mResultList.put(key, money);
            }
        }
    }

    private void checkBeforeSplitPerson(String s) {
        if (!Pattern.matches("^[0-9a-zA-Zㄱ-힣,.!]*$", s)) {
            mError = true;
            mResultText = "나눌 인원에 불필요한 문자가 있습니다.";
        } else {
            String[] text = s.split(MEMBER);
            for (String t : text) {
                String[] rate = t.split(RATIO);
                if (rate.length > 1) {
                    if (rate.length > 2 || rate[0].isEmpty() || rate[1].isEmpty()) {
                        mError = true;
                        mResultText = "나눌 인원에 불필요한 문자가 있습니다.";
                        break;
                    } else if (!Pattern.matches("^[0-9.]*$", rate[1])) {
                        mError = true;
                        mResultText = "나눌 배율에 불필요한 문자가 있습니다.";
                        break;
                    }
                }
            }
        }
    }

    private void checkBeforeSplitAmount(String s) {
        if (!Pattern.matches("^[0-9+-.]*$", s)) {
            mError = true;
            mResultText = "금액에 불필요한 문자가 있습니다.";
        }
    }

    // 3rd-left Level Proccess
    private static double splitAmount(String s) {
        double amount = 0;
        String[] plus = ("0" + s).split("[+]");
        for (String i : plus) {
            if (i.contains("-")) {
                amount += parseMinus(i);
            } else {
                amount += Double.parseDouble(i);
            }
        }
        return amount;
    }

    // 3rd-left-under Level Proccess
    private static double parseMinus(String i) {
        String[] minus = ("0" + i).split("-");
        double amount = Integer.parseInt(minus[0]);
        for (int j = 1; j < minus.length; j++) {
            amount -= Double.parseDouble(minus[j]);
        }
        return amount;
    }

    // 3rd-right Level Proccess
    private static HashMap<String, Double> splitPerson(String s) {
        HashMap<String, Double> personList = new HashMap<>();
        String[] persons = s.split(MEMBER);
        for (String p : persons) {
            String[] rate = p.split(RATIO);
            if (rate.length == 2) {
                personList.put(rate[0], Math.abs(Double.parseDouble(rate[1])));
            } else {
                personList.put(rate[0], 1.0);
            }
        }
        return personList;
    }

    private void makeResultText() {
        int money = 0;
        int digits = getDigits();

        mResultText = "";
        mResultText += "총 금 액 = " + padNum(mAmount, digits);
        mResultText += "\n계산단위 : " + padNum(mUnit, digits);

        String temp = "";

        int value;
        TreeMap<String, Double> treeMap = new TreeMap<>(mResultList);
        for (String key : treeMap.keySet()) {
            value = (int) ((treeMap.get(key) * 10 + 5) / 10);
            money = (int) ((double) ((value + mUnit - 1) / mUnit) * mUnit);
            temp += "\n" + key + " : " + padNum(money, digits);
            mRemain += money;
        }
        mResultText += "\n걷는금액 : " + padNum(mRemain, digits);
        mResultText += "\n남는금액 : " + padNum(mRemain - mAmount, digits);
        mResultText += "\n" + temp;
    }

    private int getDigits() {
        return df.format(mAmount).length();
    }

    private String padNum(double num, int n) {
        return padLeft(df.format(num), n);
    }

    private static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

}
