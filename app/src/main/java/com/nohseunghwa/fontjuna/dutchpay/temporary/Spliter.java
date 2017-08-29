package com.nohseunghwa.fontjuna.dutchpay.temporary;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static android.R.attr.data;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_EMPTY_INPUT;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_INVALID;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_IN_DONT_DIVIDE;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_IN_MEMBER;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_WRONG_EXPRESSION;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.VALID_CHARACTERS_ALL;
import static com.nohseunghwa.gallane.backing.Constants.ERROR_WRONG_EXPRESSION;
import static com.nohseunghwa.gallane.backing.Constants.VALID_CHARACTERS_ALL;

/**
 * Created by fontjuna on 2017-08-29.
 */

public class Spliter {
    private static Spliter spliter;

    private boolean mError; // 입력 데이타 검사 결과

    private String mData;   // 받은 텍스트
    private String mResult; // 보낼 텍스트 (결과)

    private ArrayList<Title> mDatas;   // 받은 데이타 구조화
    private ArrayList<Title> mResults; // 보낼 데이타 구조화

    //    private ArrayList<String> mToken; // 작업용, 입력 데이타를 각 원소로 분리
    private ArrayList<ArrayList<String>> mTokens; // 작업용, 입력 데이타를 각 원소로 분리

    // Procedure( Methods )
    //=========================================================================================//
    private void excute(String data) {
        mData = data;
        intializeData();
        confirmData();
        if (!isError()) {
            splitData();
        }
    }

    private void splitData() {
        String[] element = mData.split("/");
        for (String s : element) {
            mTokens.add(splitTokens(mData, ":@,"));
        }
        if (mTokens.isEmpty()) {
            mError = true;
            mResult = ERROR_EMPTY_INPUT;
        }
        if (!isError()) {
            mDatasCreate();         // make a mDatas
            splitElement();         // split to title, amount, member from mDatas
            if (!isError()) {
                mResultsCreate();   // make a mResults
                if (!isError()) {
                    createText();   // make a mResult
                }
            }
        }
    }

    private void mDatasCreate() {
        Title title = new Title();
        String[] element = {"", ""};
        TreeMap<String, Double> ratio = new TreeMap<>();
        int position = -1;
        for (ArrayList<String> tokenList : mTokens) {
            position = tokenList.indexOf(":");
            if (position >= 0) {
                title.setTitle(tokenList.get(position - 1));
            }
            position = tokenList.indexOf("@");
            if (position < 0) {
                mError = true;
                mResult = ERROR_IN_DONT_DIVIDE;
            } else {
                try {
                    title.setAmount(Double.parseDouble(Calculation.excute(tokenList.get(position - 1))));
                } catch (RuntimeException e) {
                    mError = true;
                    mResult = ERROR_INVALID;
                }
                if (!isError()) {
                    for (int i = position + 1; i < tokenList.size(); i++) {
                        if (!",".equals(tokenList.get(i))) {
                            element = splitElement(tokenList.get(i));
                            ratio.put(element[0], Double.parseDouble(element[1]));
                        }
                    }
                    if (!isError()) {
                        title.setRatio(ratio);
                    }
                }
            }

        }
    }

    private String[] splitElement(String element) {
        String[] item = element.split("!");
        String[] items = {"", ""};
        if (item.length < 1) {
            mError = true;
            mResult = ERROR_IN_MEMBER;
        } else if (item.length < 2) {
            items[0] = item[0];
            items[1] = "1.0";
        } else {
            items = item;
        }
        return items;
    }

    // split to token from input data
    private ArrayList<String> splitTokens(String data, String delimeter) {
        ArrayList<String> tokenList = new ArrayList<>();
        String token = "";
        for (int i = 0; i < data.length(); i++) {
            if (delimeter.contains(data.substring(i, i + 1))) {
                if (!token.isEmpty()) {
                    tokenList.add(token);
                    token = "";
                }
                tokenList.add(data.substring(i, i + 1));
            } else {
                token += data.substring(i, i + 1);
            }
        }
        if (!token.isEmpty()) {
            tokenList.add(token);
        }
        return tokenList;
    }

    // intialize and check validaiton
    //=========================================================================================//
    private void intializeData() {
        mError = false;
        mDatas = new ArrayList<>();
        mResult = "";
        mResults = new ArrayList<>();
        mTokens = new ArrayList<ArrayList<String>>();
    }

    private void confirmData() {
        mData = mData.replace(" ", "");
        mError = Pattern.matches(VALID_CHARACTERS_ALL, mData);
        if (isError()) {
            mResult = ERROR_WRONG_EXPRESSION;
        }
    }

    // Getters
    //=========================================================================================//
    public boolean isError() {
        return mError;
    }

    public String getData() {
        return mData;
    }

    public String getResult() {
        return mResult;
    }

    public ArrayList<Title> getDatas() {
        return mDatas;
    }

    public ArrayList<Title> getResults() {
        return mResults;
    }

    // Construstor
    //=========================================================================================//
    private void Spliter() {
    }

    private static Spliter getInstance() {
        if (spliter == null) {
            spliter = new Spliter();
        }
        return spliter;
    }

    public void Compute(String data) {
        Spliter.getInstance().excute(data);
    }
    //=========================================================================================//
}
