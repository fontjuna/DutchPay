package com.example.fontjuna.dutchpay;

import com.example.fontjuna.dutchpay.backing.Parsing;

/**
 * Created by fontjuna on 2017-08-15.
 */

public class TestMain {
    public static void main(String[] args) {
        String msg = "test:53000@a,b,x!2/60000@a,x,y!1.5";

        Parsing parsing = new Parsing(msg, 10);

        System.out.println("Error ? " + parsing.isError());
        System.out.println("Unit : " + parsing.getUnit());
        System.out.println("\nAmount : " + parsing.getAmount());
        System.out.println("Gather : " + parsing.getGather());
        System.out.println("Remain : " + parsing.getRemain());
        System.out.println("\nResult : " + parsing.getText());


//        System.out.println(new Calculator("5000:a,b!1.5,c/3000:a,c", 10).getTextResult());
    }
}
