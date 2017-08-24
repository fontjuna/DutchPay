package noh.seung.hwa.calculator;

import java.math.BigDecimal;

import static noh.seung.hwa.calculator.Calculator.Calculate;

/**
 * Created by fontjuna on 2017-08-24.
 */

public class TestMain {
    public static void main(String[] args) {
        String exp = "(1 + 2) * 3";

        BigDecimal cal = Calculate(exp);
        System.out.println(cal.toString());
    }
}
