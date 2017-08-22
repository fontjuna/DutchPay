package com.nohseunghwa.fontjuna.dutchpay.classes_pack;


import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.DIGIT;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_EMPTY_INPUT;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ERROR_WRONG_EXPRESSION;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.ITEMnITEM;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.LEFTnRIGHT;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.MEMBER2MEMBER;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.MEMBERnMEMBER;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.MINUS;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.PLUS;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.TITLEnMONEY;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.VALID_CHARACTERS_ALL;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.VALID_CHARACTERS_MEMBER;

/**
 * Created by fontjuna on 2017-08-21.
 */

public class Elements {
    private String mSource;
    private HashMap<String, Double> mElements;
    private boolean mError;
    private String mMessage;
    private double mSumRatio;

    public Elements(String source) {
        mError = source.isEmpty();
        mMessage = ERROR_EMPTY_INPUT + "(" + mSource + ")";
        mSource = source;
        if (!isError()) {
            mElements = new HashMap<>();
            mMessage = "";
            mSumRatio = 0.0;
            parsing();
        }
    }

    public boolean isError() {
        return mError;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getSource() {
        return mSource;
    }

    public HashMap<String, Double> getElements() {
        return isError() ? null : mElements;
    }

    public TreeMap<String, Double> getAscending() {
        TreeMap<String, Double> map = new TreeMap<>(mElements);
        return isError() ? null : map;
    }

    public TreeMap<String, Double> getDescending() {
        TreeMap<String, Double> map = new TreeMap<>(Collections.<String>reverseOrder());
        map.putAll(mElements);
        return isError() ? null : map;
    }

    public int getCount() {
        return isError() ? -1 : mElements.size();
    }

    public double getSumRatio() {
        return isError() ? -1 : mSumRatio;
    }

    public double getRatio(String name) {
        return isError() ? -1 : mElements.get(name);
    }

    private String checkAndFix(String source) {
        source = source.replace((char) 13, (char) 32);
        source = source.replace((char) 10, (char) 32);
        source = source.replace(" ", "");
        source = source.replace(MINUS + PLUS, MINUS);
        source = source.replace(PLUS + MINUS, MINUS);
        if (!Pattern.matches(VALID_CHARACTERS_ALL, source)) {
            mMessage = ERROR_WRONG_EXPRESSION + "(" + source + ")";
            mError = true;
        }
        return source;
    }

    private void parsing() {
        // 무조건 첫번째만 해당함
        String source = checkAndFix(mSource);
        if (!isError()) {
            source = source.split(ITEMnITEM)[0];

            if (source.contains(LEFTnRIGHT)) {
                source = source.split(LEFTnRIGHT)[1];
            }
            if (source.isEmpty()) {
                mError = true;
                mMessage = ERROR_EMPTY_INPUT + "(" + source + ")";
            } else {
                if (source.contains(TITLEnMONEY)) {
                    source = source.split(TITLEnMONEY)[1];
                    if (source.isEmpty()) {
                        mError = true;
                        mMessage = ERROR_EMPTY_INPUT + "(" + source + ")";
                    }
                }
                if (!isError()) {
                    if (!Pattern.matches(VALID_CHARACTERS_MEMBER, source)) {
                        mError = true;
                        mMessage = ERROR_WRONG_EXPRESSION + "(" + source + ")";
                    }
                }
            }
            if (!isError()) {
                makeMemberList(source);
            }
        }
    }

    private void makeMemberList(String source) {
        double value;
        String key;
        String[] members = source.split(MEMBERnMEMBER);
        for (String member : members) {
            if ((member.length() - member.replace(MEMBER2MEMBER, "").length()) < 2) {
                if (member.contains(MEMBER2MEMBER)) {
                    makeMemberListFromTo(member);
                    if (isError()) {
                        break;
                    }
                } else {
                    Element element = new Element(member);
                    if (element.isError()) {
                        mError = true;
                        mMessage = element.getMessage();
                        break;
                    } else {
                        value = 0.0;
                        key = element.getName();
                        if (mElements.containsKey(key)) {
                            value = mElements.get(key);
                        }
                        mElements.put(key, value + element.getRatio());
                    }
                }
            } else {
                mError = true;
                mMessage = ERROR_WRONG_EXPRESSION + "(" + member + ")";
                break;
            }
        }
    }

    private void makeMemberListFromTo(String member) {
        Element element = new Element(member);
        if (!element.isError()) {
            String elementName = element.getName();
            if (!Pattern.matches("^[" + DIGIT + MEMBER2MEMBER + "]*$", elementName) ||
                    elementName.length() < 3) {
                mError = true;
                mMessage = ERROR_WRONG_EXPRESSION + "(" + elementName + ")";
            } else {
                double value;
                String key;
                double ratio = element.getRatio();

                String[] fromTo = elementName.split(MEMBER2MEMBER);
                if (fromTo.length < 2) {
                    mError = true;
                    mMessage = ERROR_WRONG_EXPRESSION + "(" + elementName + ")";
                } else {
                    int from = Integer.parseInt(fromTo[0]);
                    int to = Integer.parseInt(fromTo[1]);
                    if (from > to) {
                        int val = from;
                        from = to;
                        to = val;
                    }
                    for (int i = from; i <= to; i++) {
                        key = i + "";
                        value = 0.0;
                        if (mElements.containsKey(key)) {
                            value = mElements.get(key);
                        }
                        mElements.put(key, value + ratio);
                    }
                }
            }
        } else {
            mError = true;
            mMessage = element.getMessage();
        }
    }
}
