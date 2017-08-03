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

    Map<Integer, String> mSentence = new LinkedHashMap<>();
    Map<String, Double> mMembers = new LinkedHashMap<>();
    Map<String, Integer> mResult = new LinkedHashMap<>();

    int mAmount = 0;
    int mRemain = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutch_pay);

        EditText editText = (EditText) findViewById(R.id.goods_edit_text);
        editText.setHint(
                "금액" + DELIMITER_MONEY + "이름" + DELIMITER_RATIO + "배율"
                        + DELIMITER_MEMBER + "..." + DELIMITER_ITEM + "..."
                        + " ('" + DELIMITER_RATIO + "배율' 생략하면 '" + DELIMITER_RATIO + "1'임)"
        );
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
        int unit = new int[]{100, 500, 1000, 5000}[pos - 1];

        int money = 0;
        String text = "총금액 = " + mAmount;
        for (String key : mResult.keySet()) {
            money = (int) ((mResult.get(key) + unit - 1) / unit) * unit;
            text += "\n" + key + " : " + money;
            mRemain += money;
        }
        text += "\n걷는금액 : " + mRemain;
        text += "\n남는금액 : " + (mRemain - mAmount);

        result.setText(text);
        mAmount = 0;
        mRemain = 0;
        mResult.clear();
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
            if (mResult.containsKey(key)) {
                money = mResult.get(key);
            }
            // 단위지정
            money += (int) (readNames.get(key) * everyMoney);
//            money += (int) ((readNames.get(key) * everyMoney + unit) / unit) * unit;
            mResult.put(key, money);
        }

    }

    public void onCalcButtonClicked(){

    }

    class Items {
        private int mAmount;
        private Members mMembers;

        public Items(int amount, Members members) {
            this.mAmount = amount;
            mMembers = members;
        }

        public int getAmount() {
            return mAmount;
        }

        public void setAmount(int amount) {
            this.mAmount = amount;
        }

        public Members getMembers() {
            return mMembers;
        }

        public void setMembers(Members members) {
            mMembers = members;
        }
    }

    class Members {
        private String mPerson;
        private double mRatio;

        public Members(String name, double ratio) {
            this.mPerson = name;
            this.mRatio = ratio;
        }

        public String getName() {
            return mPerson;
        }

        public void setName(String name) {
            this.mPerson = name;
        }

        public double getRatio() {
            return mRatio;
        }

        public void setRatio(double ratio) {
            this.mRatio = ratio;
        }
    }

    class Result {
        private String mName;
        private int mDivide;

        public Result(String name, int money) {
            this.mName = name;
            this.mDivide = money;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            this.mName = name;
        }

        public int getDivide() {
            return mDivide;
        }

        public void setDivide(int divide) {
            mDivide = divide;
        }
    }
}
