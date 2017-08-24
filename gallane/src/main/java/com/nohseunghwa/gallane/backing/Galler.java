package com.nohseunghwa.gallane.backing;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static com.nohseunghwa.gallane.backing.Constants.COMMA;
import static com.nohseunghwa.gallane.backing.Constants.ERROR_EMPTY_INPUT;
import static com.nohseunghwa.gallane.backing.Constants.ERROR_IN_AMOUNT;
import static com.nohseunghwa.gallane.backing.Constants.ERROR_IN_DONT_DIVIDE;
import static com.nohseunghwa.gallane.backing.Constants.ERROR_IN_MEMBER;
import static com.nohseunghwa.gallane.backing.Constants.ERROR_IN_RATIO;
import static com.nohseunghwa.gallane.backing.Constants.ERROR_WRONG_EXPRESSION;
import static com.nohseunghwa.gallane.backing.Constants.ITEMnITEM;
import static com.nohseunghwa.gallane.backing.Constants.LEFTnRIGHT;
import static com.nohseunghwa.gallane.backing.Constants.MEMBER2MEMBER;
import static com.nohseunghwa.gallane.backing.Constants.MEMBERnMEMBER;
import static com.nohseunghwa.gallane.backing.Constants.MEMBERnRATIO;
import static com.nohseunghwa.gallane.backing.Constants.MINUS;
import static com.nohseunghwa.gallane.backing.Constants.PLUS;
import static com.nohseunghwa.gallane.backing.Constants.TITLEnMONEY;
import static com.nohseunghwa.gallane.backing.Constants.VALID_CHARACTERS_ALL;
import static com.nohseunghwa.gallane.backing.Constants.VALID_CHARACTERS_AMOUNT;
import static com.nohseunghwa.gallane.backing.Constants.VALID_CHARACTERS_MEMBER;
import static com.nohseunghwa.gallane.backing.Constants.VALID_CHARACTERS_RATIO;

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

public class Galler {

    private static final String TAG = Galler.class.getSimpleName();

    private Map<String, Double> mMemberList;  // 단위 정리 전 리스트
    private Map<String, Integer> mListResult;  // 단위 정리 후 리스트
    private int mUnit = 1;
    private double mAmount = 0.0;
    private int mGather = 0;
    private int mRemain = 0;
    private String mTextResult = "";
    private boolean mError = false;
    private DecimalFormat df = new DecimalFormat("#,##0");

    //==========================================================================================//

    public Galler(String text, int unit) {
        mUnit = unit;
        mListResult = new LinkedHashMap<>();
        parseInputExpression(text);
        makeResultOutText();
    }

    public String getTextResult() {
        return mTextResult;
    }

    public Map<String, Integer> getListResult() {
        return mListResult;
    }

    public boolean isError() {
        return mError;
    }

    //------------------------------------------------------------------------------------------//
    // Start Process
    //------------------------------------------------------------------------------------------//

    // 1st Level Proccess
    private void parseInputExpression(String text) {
        mMemberList = new HashMap<>();

        String[] items = splitInput2ItemAndItem(text); //s.split(ITEM);
        for (String item : items) {
            if (isError()) {
                break;
            }
            parseItem2LeftAndRight(item);
        }
    }

    // 2nd Level Proccess
    private void parseItem2LeftAndRight(String item) {
        String[] leftNright = splitItem2LeftAndRight(item);      // s.split(MONEY);
        if (!isError()) {
            // 금액합산
            double amount = calurateAmount(leftNright[0]);
            mAmount += amount;

            // 나눌사람들
            double rate = 0.0;
            HashMap<String, Double> members = splitRight2Members(leftNright[1]);
            for (String member : members.keySet()) {
                rate += members.get(member);
            }
            double everyMoney = amount / rate;
            double money;
            for (String member : members.keySet()) {
                money = 0.0;
                if (mMemberList.containsKey(member)) {
                    money = mMemberList.get(member);
                }
                money += members.get(member) * everyMoney;
                mMemberList.put(member, money);
            }
        }
    }

    private String[] splitItem2LeftAndRight(String item) {
        String[] leftNright = item.split(LEFTnRIGHT);
        // common check
        if (item.isEmpty()) {
            mError = true;
            mTextResult = ERROR_EMPTY_INPUT;
        } else if (leftNright.length < 2) {
            mError = true;
            mTextResult = ERROR_IN_DONT_DIVIDE;
            // left check
        } else if (!Pattern.matches(VALID_CHARACTERS_AMOUNT, leftNright[0])) {
            mError = true;
            mTextResult = ERROR_IN_AMOUNT;
            // right check
        } else {
            checkRightOfItem(leftNright[1]);
        }
        return leftNright;
    }

    private void checkRightOfItem(String right) {
        if (!Pattern.matches(VALID_CHARACTERS_MEMBER, right)) {
            mError = true;
            mTextResult = ERROR_IN_MEMBER;
        } else {
            String[] members = right.split(MEMBERnMEMBER);
            for (String member : members) {
                String[] memberNrate = member.split(MEMBERnRATIO);
                if (memberNrate.length > 1) {
                    if (memberNrate.length > 2 || memberNrate[0].isEmpty() || memberNrate[1].isEmpty()) {
                        mError = true;
                        mTextResult = ERROR_IN_MEMBER;
                        break;
                    } else if (!Pattern.matches(VALID_CHARACTERS_RATIO, memberNrate[1])) {
                        mError = true;
                        mTextResult = ERROR_IN_RATIO;
                        break;
                    }
                }
            }
        }
    }

    // 3rd-left Level Proccess
    private static double calurateAmount(String left) {
        double amount = 0;
        left = left.replace(COMMA, "");  //숫자에서 컴마 제거
        String[] plus = ("0" + left).split("[" + PLUS + "]"); // + 는 []로 감싼다
        for (String i : plus) {
            if (i.contains(MINUS)) {
                amount += parseMinus(i);
            } else {
                amount += Double.parseDouble(i);
            }
        }
        return amount;
    }

    // 3rd-left-under Level Proccess
    private static double parseMinus(String s) {
        String[] minus = ("0" + s).split(MINUS);
        double amount = Integer.parseInt(minus[0]);
        for (int j = 1; j < minus.length; j++) {
            amount -= Double.parseDouble(minus[j]);
        }
        return amount;
    }

    // 3rd-right Level Proccess
    private static HashMap<String, Double> splitRight2Members(String right) {
        final int MEMBER = 0;
        final int RATIO = 1;
        HashMap<String, Double> memberList = new HashMap<>();
        String[] members = right.split(MEMBERnMEMBER);
        for (String member : members) {
            String[] memberNratio = member.split(MEMBERnRATIO);
            if (memberNratio.length == 2) {
                memberList.put(memberNratio[MEMBER], Math.abs(Double.parseDouble(memberNratio[RATIO])));
            } else {
                memberList.put(memberNratio[MEMBER], 1.0);
            }
        }
        return memberList;
    }

    private void makeResultOutText() {
        if (!isError()) {
            int money = 0;
            int digits = getDigits();

            mTextResult = "";
            mTextResult += "총_금_액 = " + padNum(mAmount, digits);
            mTextResult += "\n계산단위 : " + padNum(mUnit, digits);

            String temp = "";

            int value;
            TreeMap<String, Double> treeMap = new TreeMap<>(mMemberList);
            for (String key : treeMap.keySet()) {
                value = (int) ((treeMap.get(key) * 10 + 5) / 10);
                money = (int) ((double) ((value + mUnit - 1) / mUnit) * mUnit);
                temp += "\n" + key + " : " + padNum(money, digits);
                mGather += money;
                mListResult.put(key, money);
            }
            mRemain = mGather - (int) mAmount;
            mTextResult += "\n걷는금액 : " + padNum(mGather, digits);
            mTextResult += "\n남는금액 : " + padNum(mRemain, digits);
            mTextResult += "\n" + temp;
        }
    }

    private String checkAndRemoveIllegal(String text) {
        if (text.isEmpty()) {
            mTextResult = ERROR_EMPTY_INPUT;
            mError = true;
            text = "";
        } else {
            text = text.replace((char) 13, (char) 32);
            text = text.replace((char) 10, (char) 32);
            text = text.replace(" ", "");
            text = text.replace(MINUS + PLUS, MINUS);
            text = text.replace(PLUS + MINUS, MINUS);
            if (!Pattern.matches(VALID_CHARACTERS_ALL, text)) {
                mTextResult = ERROR_WRONG_EXPRESSION;
                mError = true;
            }
        }
        return text;
    }

    private String[] splitInput2ItemAndItem(String s) {
        s = checkAndRemoveIllegal(s);
        String[] itemNitem = s.split(ITEMnITEM);
        return itemNitem;
    }

    private String[] splitLeft2TitleAndMoney(String s) {
        String[] titleNmoney = s.split(TITLEnMONEY);
        switch (titleNmoney.length) {
            case 0:
                titleNmoney[0] = "그룹";
                titleNmoney[1] = "0";
                return titleNmoney;
            case 1:
                titleNmoney[0] = "";
                return titleNmoney;
        }
        return titleNmoney;
    }

    private String[] splitGROUP(String s) {
        return s.split(MEMBER2MEMBER);
    }

    private String[] splitMEMBER(String s) {
        return s.split(MEMBERnMEMBER);
    }

    private String[] splitRATIO(String s) {
        return s.split(MEMBERnRATIO);
    }

    private int getDigits() {
        return df.format(mAmount).length();
    }

    private String padNum(double num, int n) {
        return padLeft(df.format(num), n);
    }

    private static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);//.replace(" ", "_");
    }

}
