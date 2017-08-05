package com.example.fontjuna.dutchpay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;

public class DutchPayActivity extends AppCompatActivity {

    public static final String DELIMITER_ITEM = "/";
    public static final String DELIMITER_RATIO = "!";
    public static final String DELIMITER_MEMBER = ",";
    public static final String DELIMITER_MONEY = ":";
    Map<String, Integer> resultNames = new LinkedHashMap<>();
    int mAmount = 0;
    int mRemain = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutch_pay);

        String information = "구분자로 [ : , ! / ] 괄호안의 4개 문자를 사용합니다";
        information += "\n : 걷을금액과 낼사람들을 구분합니다";
        information += "\n , 낼사람들 서로간을  구분 합니다";
        information += "\n ! 그사람의 배율 (A는 2인분이면 A!2로 입력)";
        information += "\n / 걷을 금액이 또 있으면 / 다음에 이어서 입력";
        information += "\n상황1)5000원을 A,B,C 이렇게 세사람이 똑 같이 나눈다면";
        information += "\n입력1)5000:A,B,C";
        information += "\n상황2)상황1 에서 B는 다른 사람보다 1.5배 내야 한다면";
        information += "\n입력2)5000:A,B!1.5,C (!를 사용 B!2 로 입력)";
        information += "\n상황3)상황2 에 더해서 3000원을 A와 C가 똑같이 더 낸다면";
        information += "\n입력3)5000:A,B!2,C/3000:A,C";

        TextView resultTextView = (TextView) findViewById(R.id.result_text_view);
        resultTextView.setHint(information);
//        EditText editText = (EditText) findViewById(R.id.goods_edit_text);
//        editText.setHint(
//                "금액" + DELIMITER_MONEY + "이름" + DELIMITER_RATIO + "배율"
//                        + DELIMITER_MEMBER + "..." + DELIMITER_ITEM + "..."
//                        + " ('" + DELIMITER_RATIO + "배율' 생략하면 '" + DELIMITER_RATIO + "1'임)"
//        );
    }

    public void onClick_calc_button(View view) {
        TextView result = (TextView) findViewById(R.id.result_text_view);
        EditText editText = (EditText) findViewById(R.id.goods_edit_text);

        // 47500:1,2,3,4,5,6/12000:4,5,6/6000:1,2,3,4
        String[] lists = editText.getText().toString().split(DELIMITER_ITEM);
        for (String s : lists) {
            parseBill(s);
        }

        // 명단 등록하고 나눈 금액을 저장한다
        RadioGroup unitRadioGroup = (RadioGroup) findViewById(R.id.unit_radio_group);
        int pos = unitRadioGroup.getCheckedRadioButtonId();
        if (pos < 1) {
            pos = 3; // 기본 단위 1,000원5000
        }
        int unit = new int[]{1, 5, 10, 50, 100, 500, 1000}[pos - 1];

        int money = 0;
        String temp = "";
        String text = "총금액 = " + mAmount;
        for (String key : resultNames.keySet()) {
            money = (int) ((resultNames.get(key) + unit - 1) / unit) * unit;
            temp += "\n" + key + " : " + money;
            mRemain += money;
        }
        text += "\n걷는금액 : " + mRemain;
        text += "\n남는금액 : " + (mRemain - mAmount);
        text += temp;

        result.setText(text);
        mAmount = 0;
        mRemain = 0;
        resultNames.clear();
    }

    private void parseBill(String text) {
        Map<String, Double> readNames = new LinkedHashMap<>();
        int divider = 0;
        text = text.replace(" ", "");
        text = text.replace(DELIMITER_ITEM, "");
        String[] target = text.split(DELIMITER_MONEY);

        // 나눠야 할 금액
        int amount = Integer.parseInt(target[0]);
        mAmount += amount;
        // 대상 명단
        String[] mans = target[1].split(DELIMITER_MEMBER);

        String name;
        double ratio;
        double ratioSum = 0.0;
        for (String s : mans) {
            if (!s.contains(DELIMITER_RATIO)) {
                name = s;
                ratio = 1;
            } else {
                name = s.split(DELIMITER_RATIO)[0];
                ratio = Double.parseDouble(s.split(DELIMITER_RATIO)[1]);
            }
            readNames.put(name, ratio);
            ratioSum += ratio;
        }

//        // 명단 등록하고 나눈 금액을 저장한다
//        RadioGroup unitRadioGroup = (RadioGroup) findViewById(R.id.unit_radio_group);

//        int unit = new int[]{100, 500, 1000, 5000}[unitRadioGroup.getCheckedRadioButtonId() - 1];
        double everyMoney = (amount / ratioSum);
        int money;
        for (String key : readNames.keySet()) {
            money = 0;
            if (resultNames.containsKey(key)) {
                money = resultNames.get(key);
            }
            // 단위지정
            money += (int) (readNames.get(key) * everyMoney);
//            money += (int) ((readNames.get(key) * everyMoney + unit) / unit) * unit;
            resultNames.put(key, money);
        }

    }
}
