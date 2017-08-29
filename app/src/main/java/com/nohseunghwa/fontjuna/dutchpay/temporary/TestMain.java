package com.nohseunghwa.fontjuna.dutchpay.temporary;

import com.nohseunghwa.gallane.backing.Calculation;

import java.util.Scanner;

/**
 * Created by fontjuna on 2017-08-26.
 */

public class TestMain {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String input;

        while (true) {
            input = sc.nextLine();
            if (input.isEmpty()) {
                break;
            }
            try {
                System.out.println(input + " = " + Calculation.Calculate(input));
            } catch (Exception e) {
                System.out.println(input + " = " + "error!");
            }
        }
    }
}