package com.nohseunghwa.fontjuna.dutchpay.temporary;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.ERROR_EMPTY_INPUT;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.ERROR_INVALID;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.ERROR_IN_DONT_DIVIDE;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.ERROR_IN_MEMBER;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.ERROR_WRONG_EXPRESSION;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.ITEMnITEM;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.LEFTnRIGHT;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.MEMBERnMEMBER;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.MEMBERnRATIO;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.TITLEnMONEY;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.VALID_CHARACTERS_MEMBER;


/**
 * Created by fontjuna on 2017-08-29.
 */

public class Spliter {
//    private static Spliter spliter;

    private boolean mError; // 입력 데이타 검사 결과

    private int mUnit;
    private String mData;   // 받은 텍스트
    private String mResult; // 보낼 텍스트 (결과)

    private ArrayList<Title> mDatas;    // 받은 데이타 구조화
    private ArrayList<Title> mResults;  // 보낼 데이타 구조화

    private Title mFinal;    // 최종 집계 금액 데이타

    //    private ArrayList<String> mToken; // 작업용, 입력 데이타를 각 원소로 분리
    private ArrayList<ArrayList<String>> mTokens; // 작업용, 입력 데이타를 각 원소로 분리

    public Spliter(String input, int unit) {
        excute(input, unit);
    }

    // Procedure( Methods )
    //=========================================================================================//
    private void excute(String data, int unit) {
        mData = data;
        mUnit = unit;
        intializeData();
        confirmData();
        if (!isError()) {
            splitData();
        }
    }

    private void splitData() {
        String[] element = mData.split(ITEMnITEM);
        for (String s : element) {
            mTokens.add(splitTokens(s, TITLEnMONEY + LEFTnRIGHT));// + MEMBERnMEMBER));
        }
        if (mTokens.isEmpty()) {
            mError = true;
            mResult = ERROR_EMPTY_INPUT;
        }
        if (!isError()) {
            mDatasCreate();         // make a mDatas
            if (!isError()) {
                mResultsCreate();   // make a mResults
                if (!isError()) {
                    createText();   // make a mResult
                }
            }
        }
    }

    private void createText() {
        int gather = 0;
        int intVal = 0;
        String member = "";
        for (String key : mFinal.keySet()) {
            intVal = ((int) (mFinal.get(key) / mUnit)) * mUnit;
            gather += intVal;
            member += key + " : " + intVal + "\n";
        }
        mResult = mFinal.getTitle() + "\n"
                + "총 금 액 : " + (int) mFinal.getAmount() + "\n"
                + "걷는금액 : " + gather + "\n"
                + "남는금액 : " + (gather - (int) mFinal.getAmount()) + "\n\n"
                + member;

    }

    private void mResultsCreate() {
        for (Title data : mDatas) {
            double amount = 0.0;
            double ratio = 0.0;
            double unitPrice = 0.0;
            Title result = new Title();
            result.setTitle(data.getTitle());
            result.setAmount(data.getAmount());
            amount += data.getAmount();
            for (String key : data.keySet()) {
                ratio += data.get(key);
            }
            unitPrice = amount / ratio;
            for (String key : data.keySet()) {
                result.put(key, data.get(key) * unitPrice);
            }
            mResults.add(result);
        }
        for (Title t : mResults) {
            if (mFinal.getTitle().isEmpty()) {
                mFinal.setTitle(t.getTitle());
            }
            mFinal.setAmount(mFinal.getAmount() + t.getAmount());
            for (String key : t.keySet()) {
                mFinal.put(key, (mFinal.getData().containsKey(key) ? mFinal.get(key) : 0.0) + t.get(key));
            }
        }
    }

    private void mDatasCreate() {
        String head;
        String[] element;
        double amount;
        int position;
        int no = 1;

        for (ArrayList<String> tokenList : mTokens) {
            head = "DutchPay" + no++;
            amount = 0.0;
            TreeMap<String, Double> ratio = new TreeMap<>();
            position = tokenList.indexOf(TITLEnMONEY);
            if (position > 0) {
                head = tokenList.get(position - 1);
            }
            position = tokenList.indexOf(LEFTnRIGHT);
            if (position < 1) {
                mError = true;
                mResult = ERROR_IN_DONT_DIVIDE;
            } else {
                try {
                    amount = Double.parseDouble(Calculation.Calculate(tokenList.get(position - 1)));
                } catch (RuntimeException e) {
                    mError = true;
                    mResult = ERROR_INVALID;
                }
                if (Pattern.matches(VALID_CHARACTERS_MEMBER, tokenList.get(position + 1))) {
                    ArrayList<String> member = splitTokens(tokenList.get(position + 1), MEMBERnMEMBER);
                    for (int i = 0; i < member.size(); i++) {
                        if (!MEMBERnMEMBER.equals(member.get(i))) {
                            element = splitElement(member.get(i));
                            ratio.put(element[0], Double.parseDouble(element[1]));
                        }
                    }
                } else {
                    mError = true;
                    mResult = ERROR_IN_MEMBER;
                }
//                if (!isError()) {
//                    for (int i = position + 1; i < tokenList.size(); i++) {
//                        if (!MEMBERnMEMBER.equals(tokenList.get(i))) {  // "," 이면 skip
//                            element = splitElement(tokenList.get(i));
//                            ratio.put(element[0], Double.parseDouble(element[1]));
//                        }
//                    }
//                }
            }
            if (!isError()) {
                mDatas.add(new Title(head, amount, ratio));
            }
        }
    }

    // split to member and ratio
    private String[] splitElement(String element) {
        String[] item = element.split(MEMBERnRATIO);
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
        mFinal = new Title();
    }

    private void confirmData() {
        mData = mData.replace(" ", "");
        mError = false;//!Pattern.matches(VALID_CHARACTERS_ALL, mData);
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
//    public void Spliter(String data, int unit) {
//        excute(data, unit);
//    }

//    private static Spliter getInstance() {
//        if (spliter == null) {
//            spliter = new Spliter();
//        }
//        return spliter;
//    }
//
//    public void Compute(String data, int unit) {
//        Spliter.getInstance().excute(data, unit);
//    }
    //=========================================================================================//
}
