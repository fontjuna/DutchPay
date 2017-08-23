package com.nohseunghwa.fontjuna.dutchpay.backing;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_EMPTY_INPUT;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_WRONG_EXPRESSION;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ITEMnITEM;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.MINUS;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.PLUS;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.VALID_CHARACTERS_ALL;

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

public class Parsing {

    private static final String TAG = Parsing.class.getSimpleName();

    private boolean mError = false;
    private String mOutText = "";

    private int mUnit = 1;
    private ArrayList<Title> mTitles;
    private TreeMap<String, Double> mMemberMap;
    private TreeMap<String, Integer> mResultMap;
    private int mAmount = 0;
    private int mGather = 0;
    private int mRemain = 0;

    private String mTitle;
    private DecimalFormat df = new DecimalFormat("000");
//    private String mOutText = "";

    //==========================================================================================//

    public Parsing(String text, int unit) {
        mUnit = unit;
        parseInputExpression(text);
        if (!isError()) {
            makeListAndResult();
        }
    }

    private void makeListAndResult() {
        // 결과를 개인별 합산및 리스트로 만들기
        boolean fistTime = true;
        double dblVal;
        mAmount = 0;
        mGather = 0;
        mRemain = 0;
        mMemberMap = new TreeMap<>();
        for (Title t : mTitles) {
            if (fistTime) {
                mTitle = t.getTitle();
                fistTime = false;
            }
            mAmount += t.getTotal();
            for (String key : t.getResultsMap().keySet()) {
                dblVal = 0.0;
                if (mMemberMap.containsKey(key)) {
                    dblVal = mMemberMap.get(key);
                }
                mMemberMap.put(key, dblVal + t.get(key));
            }
        }

        // 결과를 담은 리스트를 이용 텍스트로 저장
        int intVal;
        String tempText = "";
        mResultMap = new TreeMap<>();
        for (String key : mMemberMap.keySet()) {
            intVal = (int) ((mMemberMap.get(key) * 10 + 5) / 10);
            intVal = (int) ((double) ((intVal + mUnit - 1) / mUnit) * mUnit);
            mResultMap.put(key, intVal);
            mGather += intVal;
            tempText += "\n" + key + " : " + intVal;
        }
        mRemain = mAmount - mGather;
        mOutText = mTitle + tempText;
    }

    public boolean isError() {
        return mError;
    }

    public int getUnit() {
        return mUnit;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mOutText;
    }

    public int getAmount() {
        return mAmount;
    }

    public int getGather() {
        return mGather;
    }

    public int getRemain() {
        return mRemain;
    }

    public ArrayList<Title> getTitles() {
        return mTitles;
    }

//------------------------------------------------------------------------------------------//
    // Start Process
    //------------------------------------------------------------------------------------------//

    // 1st Level Proccess
    private void parseInputExpression(String text) {
        int autoSeq = 1;
        mTitle = "";
        mTitles = new ArrayList<>();
        String[] strings = splitInput2ItemAndItem(text); //s.split(ITEM);
        for (String string : strings) {
            Title title = new Title(string);
            if (title.isError()) {
                mError = true;
                mOutText = title.getMessage();
                break;
            } else {
                if (title.getTitle().isEmpty()) {
                    title.setTitle("DutchPay" + df.format(autoSeq++));
                }
                mTitles.add(title);
                if (mTitle.isEmpty()) {
                    mTitle = title.getTitle();
                }
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
            mOutText = ERROR_EMPTY_INPUT;
            mError = true;
            text = "";
        } else {
            text = text.replace((char) 13, (char) 32);
            text = text.replace((char) 10, (char) 32);
            text = text.replace(" ", "");
            text = text.replace(MINUS + PLUS, MINUS);
            text = text.replace(PLUS + MINUS, MINUS);
            if (!Pattern.matches(VALID_CHARACTERS_ALL, text)) {
                mOutText = ERROR_WRONG_EXPRESSION;
                mError = true;
            }
        }
        return text;
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
