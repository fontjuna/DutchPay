package com.example.fontjuna.dutchpay;

import com.example.fontjuna.dutchpay.backing.Parsing;
import com.example.fontjuna.dutchpay.classes_pack.Amount;
import com.example.fontjuna.dutchpay.classes_pack.Elements;

/**
 * Created by fontjuna on 2017-08-15.
 */

public class TestMain {

    static final int TEST_ELEMENTS = 1;
    static final int TEST_ELEMENT = 2;
    static final int TEST_PARSING = 3;
    static final int TEST_AMOUNT = 4;

    public static void main(String[] args) {

        int whatDoYouWant = 4;
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
        }
    }

    private static void testAmount() {

        String so = "test:500-+200.,+-300@1~10!2,22!0.2,a,b,x!2/60000@a,x,y!1.5";
        String s1 = "test:53000@22!0.2,a";
        String s = "test:53000@1~1!0.2";

        Amount am = new Amount(so);

        System.out.println("input : " + am.getSource());
        System.out.println("Error ? " + am.isError());
        System.out.println("Message : " + am.getMessage());
        System.out.println("count : " + am.getCount());
        System.out.println("Total : " + am.getTotal());
        if (!am.isError()) {
            int key = 1;
            for (Double d : am.getAmount()) {
                System.out.println(key++ + " : " + d);
            }
        }
    }

    private static void testParsing() {
        String msg = "test:53000@a,b,x!2/60000@a,x,y!1.5";

        Parsing parsing = new Parsing(msg, 10);

        System.out.println("Error ? " + parsing.isError());
        System.out.println("Unit : " + parsing.getUnit());
        System.out.println("\nAmount : " + parsing.getAmount());
        System.out.println("Gather : " + parsing.getGather());
        System.out.println("Remain : " + parsing.getRemain());
        System.out.println("\nResult : " + parsing.getText());
    }

    private static void testElement() {

    }

    private static void testElements() {
        String so = "1/test:53000@1~10!2,22!0.2,a,b,x!2/60000@a,x,y!1.5";
        String s1 = "test:53000@22!0.2,a";
        String s = "test:53000@1~1!0.2";

        Elements el = new Elements(so);

        System.out.println("input : " + el.getSource());
        System.out.println("Error ? " + el.isError());
        System.out.println("Message : " + el.getMessage());
        System.out.println("count : " + el.getCount());
        System.out.println("SumRatio : " + el.getSumRatio());
        if (!el.isError()) {
            for (String key : el.getDescending().keySet()) {
                System.out.println(key + " : " + el.getRatio(key));
            }
        }
    }
}
