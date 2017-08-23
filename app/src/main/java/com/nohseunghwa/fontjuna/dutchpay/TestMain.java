package com.nohseunghwa.fontjuna.dutchpay;

import com.nohseunghwa.fontjuna.dutchpay.backing.Parsing;
import com.nohseunghwa.fontjuna.dutchpay.classes_pack.Amount;
import com.nohseunghwa.fontjuna.dutchpay.classes_pack.Elements;
import com.nohseunghwa.fontjuna.dutchpay.classes_pack.TitleData;
import com.nohseunghwa.fontjuna.dutchpay.classes_pack.Titles;

/**
 * Created by fontjuna on 2017-08-15.
 */

public class TestMain {

    static final int TEST_ELEMENTS = 1;
    static final int TEST_ELEMENT = 2;
    static final int TEST_PARSING = 3;
    static final int TEST_AMOUNT = 4;
    static final int TEST_TITLES = 5;

    private static String s0 = "test:500-+200.,+-300@1~10!2,22!0.2,a,b,x!2/60000@a,x,y!1.5";
    private static String s1 = "test:53000@22!0.2,a";
    private static String s2 = "test:53000@1~1!0.2";

    public static void main(String[] args) {

        int whatDoYouWant = 5;
        switch (whatDoYouWant) {
            case TEST_ELEMENTS: {
                testElements();
                break;
            }
            case TEST_ELEMENT: {
                testElement();
                break;
            }
            case TEST_PARSING: {
                testParsing();
                break;
            }
            case TEST_AMOUNT: {
                testAmount();
                break;
            }
            case TEST_TITLES: {
                testTitles();
                break;
            }
        }
    }

    private static void testTitles() {
        Titles obj = new Titles(s0);

        System.out.println("input : " + obj.getSource());
        System.out.println("Error ? " + obj.isError());
        System.out.println("Text ? " + obj.getText());
        System.out.println("Message : " + obj.getMessage());

        for (TitleData t : obj.getTitleDatas()) {
            System.out.println(t.getText());
        }

    }

    private static void testAmount() {
        Amount obj = new Amount(s0);

        System.out.println("input : " + obj.getSource());
        System.out.println("Error ? " + obj.isError());
        System.out.println("Text ? " + obj.getText());
        System.out.println("Message : " + obj.getMessage());
        System.out.println("count : " + obj.getCount());
        System.out.println("Total : " + obj.getTotal());
        if (!obj.isError()) {
            int key = 1;
            for (Double d : obj.getAmount()) {
                System.out.println(key++ + " : " + d);
            }
        }
    }

    private static void testParsing() {
        Parsing obj = new Parsing(s0, 10);

        System.out.println("Error ? " + obj.isError());
        System.out.println("Unit : " + obj.getUnit());
        System.out.println("\nAmount : " + obj.getAmount());
        System.out.println("Gather : " + obj.getGather());
        System.out.println("Remain : " + obj.getRemain());
        System.out.println("\nResult : " + obj.getText());
    }

    private static void testElement() {

    }

    private static void testElements() {
        Elements obj = new Elements(s0);

        System.out.println("input : " + obj.getSource());
        System.out.println("Text ? " + obj.getText());
        System.out.println("Error ? " + obj.isError());
        System.out.println("Message : " + obj.getMessage());
        System.out.println("count : " + obj.getCount());
        System.out.println("SumRatio : " + obj.getSumRatio());
        if (!obj.isError()) {
            for (String key : obj.getDescending().keySet()) {
                System.out.println(key + " : " + obj.getRatio(key));
            }
        }
    }
}
