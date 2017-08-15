package com.example.fontjuna.dutchpay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class DutchPayActivity extends AppCompatActivity {

    private static final String INFORMATION
            = "구분자로 [ : , ! / ] 괄호안의 4개 문자를 사용합니다"
            + "\n : 걷을금액과 낼사람들을 구분합니다"
            + "\n , 낼사람들 서로간을 구분 합니다"
            + "\n ! 그 사람의 배율 (A는 2배이면 A!2로 입력)"
            + "\n / 걷을 금액이 또 있으면 / 에 이어서 같은 방법으로\n"
            + "\n상황1)5000원을 A,B,C 이렇게 세사람이 똑 같이 나눈다면"
            + "\n입력1)5000:A,B,C"
            + "\n상황2)상황1 에서 B는 다른 사람보다 1.5배 내야 한다면"
            + "\n입력2)5000:A,B!1.5,C (!를 사용 B!1.5 로 입력)"
            + "\n상황3)상황2 와 함께 3000원을 A와 C가 똑같이 더 낸다면"
            + "\n입력3)5000:A,B!1.5,C/3000:A,C\n"
            + "\n계산결과"
            + "\n총 금 액 : 8,000"
            + "\n계산단위 :   100"
            + "\n걷는금액 : 8,200"
            + "\n남는금액 :   200\n"
            + "\nA : 3,000"
            + "\nB : 2,200"
            + "\nC : 3,000";

    TextView mResultTextView;
    EditText mInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutch_pay);

        mResultTextView = (TextView) findViewById(R.id.result_text_view);
        mResultTextView.setHint(INFORMATION);
    }

    public void onClick_calc_button(View view) {
        mInputEditText = (EditText) findViewById(R.id.input_edit_text);
        Calculator cal = new Calculator(mInputEditText.getText().toString(), getUnit());
        mResultTextView.setText(cal.getResult());
    }

    private int getUnit() {
        RadioGroup unitRadioGroup = (RadioGroup) findViewById(R.id.unit_radio_group);
        int radioButtonId = unitRadioGroup.getCheckedRadioButtonId();
        int unit;
        if (radioButtonId == -1) {
            unit = 10;
        } else {
            RadioButton unitRadioButton = (RadioButton) findViewById(radioButtonId);
            unit = Integer.parseInt(unitRadioButton.getText().toString());
        }
        return unit;
    }

    public void onClick_init_button(View view) {
        mInputEditText.setText("");
        mInputEditText.setHint("금액:이름!배율,...");
        mResultTextView.setText("");
        mResultTextView.setHint(INFORMATION);
    }
}
