package com.example.fontjuna.dutchpay;

import com.example.fontjuna.dutchpay.backing.Calculator;
import com.example.fontjuna.dutchpay.backing.Parsing;
import com.example.fontjuna.dutchpay.backing.Title;

import java.util.ArrayList;

/**
 * Created by fontjuna on 2017-08-15.
 */

public class TestMain {
    public static void main(String[] args) {
        String msg = "test:53000@a,b,x!2";

        Parsing parsing = new Parsing(msg, 10);

        System.out.println(parsing.isError());
        System.out.println(parsing.getUnit());
        System.out.println(parsing.getErrorMessage());

        ArrayList<Title> titles =parsing.getItems();
        System.out.println("Title:"+titles.get(0).getTitle());


        System.out.println(new Calculator("5000:a,b!1.5,c/3000:a,c", 10).getTextResult());
    }
}
