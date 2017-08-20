package com.example.fontjuna.dutchpay.backing;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Created by fontjuna on 2017-08-15.
 * <p>
 * '/' 그룹과 그룹 구분
 * '=' 그룹에서 그룹명칭과 내용 구분
 * ':' 내용에서 금액과 구성멤버 구분
 * ',' 구성멤버에서 멤버와 멤버 구분
 * '!' 멤버에서 멤버와 멤버의 비중 구분
 * '~' 숫자와 숫자사이에 써서 이름대신 순번으로 대치 1~10 (1부터 10까지 10명)
 */

public class Parsing implements CommonDutchPay {

    private static final String TAG = Parsing.class.getSimpleName();

    private boolean mError = false;
    private String mErrorMessage = "";

    private int mUnit = 1;
    private ArrayList<Title> mTitles;
    private TreeMap<String, Double> mMemberMap;
    private TreeMap<String, Integer> mResultMap;
    private int mAmount = 0;
    private int mGather = 0;
    private int mRemain = 0;
    private String mOutText = "";

    //==========================================================================================//

    public Parsing(String text, int unit) {
        mUnit = unit;
        parseInputExpression(text);
        if (!isError()) {
            makeListAndResult();
        }
    }

    private void makeListAndResult() {
        String tempText = "";
        double dblVal;
        mAmount = 0;
        mGather = 0;
        mRemain = 0;
        mMemberMap = new TreeMap<>();
        for (Title t : mTitles) {
            mAmount += t.getTotal();
            for (String key : t.getResultsMap().keySet()) {
                dblVal = 0.0;
                if (mMemberMap.containsKey(key)) {
                    dblVal = mMemberMap.get(key);
                }
                mMemberMap.put(key, dblVal + t.get(key));
            }

        }
        int intVal;
        mResultMap = new TreeMap<>();
        for (String key : mMemberMap.keySet()) {
            intVal = (int) ((mMemberMap.get(key) * 10 + 5) / 10);
            intVal = (int) ((double) ((intVal + mUnit - 1) / mUnit) * mUnit);
            mResultMap.put(key, intVal);
            mGather += intVal;
            tempText += "\n" + key + " : " + intVal;
        }
        mRemain = mAmount - mGather;
        mOutText = tempText;
    }

    public boolean isError() {
        return mError;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public int getUnit() {
        return mUnit;
    }

    public ArrayList<Title> getItems() {
        return mTitles;
    }

    public String getText() {
        return mOutText;
    }
//------------------------------------------------------------------------------------------//
    // Start Process
    //------------------------------------------------------------------------------------------//

    // 1st Level Proccess
    private void parseInputExpression(String text) {
        int autoSeq = 1;
        mTitles = new ArrayList<>();
        String[] strings = splitInput2ItemAndItem(text); //s.split(ITEM);
        for (String string : strings) {
            Title items = new Title(string);
            if (items.isError()) {
                mErrorMessage = items.getMessage();
                break;
            } else {
                if (items.getTitle().isEmpty()) {
                    items.setTitle("Item" + autoSeq++);
                }
                mTitles.add(items);
            }
        }
    }

    private String[] splitInput2ItemAndItem(String s) {
        s = checkAndRemoveIllegal(s);
        String[] itemNitem = s.split(ITEMnITEM);
        return itemNitem;
    }

    private String checkAndRemoveIllegal(String text) {
        if (text.isEmpty()) {
            mErrorMessage = ERROR_EMPTY_INPUT;
            mError = true;
            text = "";
        } else {
            text = text.replace((char) 13, (char) 32);
            text = text.replace((char) 10, (char) 32);
            text = text.replace(" ", "");
            text = text.replace(MINUS + PLUS, MINUS);
            text = text.replace(PLUS + MINUS, MINUS);
            if (!Pattern.matches(VALID_CHARACTERS_ALL, text)) {
                mErrorMessage = ERROR_WRONG_EXPRESSION;
                mError = true;
            }
        }
        return text;
    }

}
