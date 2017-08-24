package noh.seung.hwa.calculator;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static noh.seung.hwa.calculator.Calculator.Calculate;

/**
 * Created by fontjuna on 2017-08-24.
 */

public class TestMain {
    public static void main(String[] args) {
        String exp = "(8-2)";

        DecimalFormat dg = new DecimalFormat("#,##0.######");

        BigDecimal cal = Calculate(exp);
        System.out.println(dg.format(cal));

        BigDecimal op1 = BigDecimal.valueOf(1);
        BigDecimal op2 = BigDecimal.valueOf(3);
        System.out.println(op1.subtract(op2));
        System.out.println(op2.subtract(op1));
    }
}
