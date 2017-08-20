package com.example.fontjuna.dutchpay;

import com.example.fontjuna.dutchpay.backing.Calculator;

/**
 * Created by fontjuna on 2017-08-15.
 */

public class TestMain {
    public static void main(String[] args) {
        System.out.println(new Calculator("5000:a,b!1.5,c/3000:a,c",10).getTextResult());
    }
}
