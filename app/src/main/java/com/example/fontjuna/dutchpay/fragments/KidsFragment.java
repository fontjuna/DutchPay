package com.example.fontjuna.dutchpay.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.fontjuna.dutchpay.R;
import com.example.fontjuna.dutchpay.backing.Calculator;

public class KidsFragment extends Fragment implements View.OnClickListener {

    /***
     * 똑 같이 나눌경우 (12,000원을 A,B,C 세사람이 분배)
     * 12000:A,B,C
     *
     * 꼴찌한 횟수만큼 내기 (총액 6,000원 게임당 1,000원, A=1회, B=2회, C=3회)
     * 6000:A,B!2,C!3         (6,000원중 A=1,000원, B=2,000원, C=3,000원)
     *
     * 낼 금액이 두가지 이상인 경우("/"로 구분)
     * 6000:A,B,C/3000:A,D    (A=3,500원, B,C=2,000원, D=1,500원)
     *
     * 찬조금액을 빼고 계산할 경우 (총액이 10,000원 이고 찬조금이 1,000원 가정)
     * 10000-1000:A,B,C       (9,000원중 A,B,C=3,000원)
     * 9000:A,B,C             (9,000원중 A,B,C=3,000원)
     *
     * 특정인이 정해진 금액을 더 낼 경우
     * 5000-1000:A,B,C/1000:C (5,000원중 C가 1,000원을 더 낸다.| A,B=1,333원,C=2,333원)
     * 4000:A,B,C/1000:C      (5,000원중 C가 1,000원을 더 낸다.| A,B=1,333원,C=2,333원)
     * 5000:A,B,C/1000:C      (6,000원중 C가 1,000원을 더 낸다.| A,B=1,667원,C=2,667원)
     *
     * 특정인이 정해진 금액만 낼 경우
     * 5000-1000:A,B/1000:C (5,000원중 C는 1,000원만 낸다.| A,B=2,000원,C=1,000원)
     * 4000:A,B/1000:C      (5,000원중 C는 1,000원만 낸다.| A,B=2,000원,C=1,000원)
     *
     * 특정인이 다른 사람 몫까지 낼 경우 (C의 몫을 다른 사람이 낸다는 가정)
     * 10000:A,B,C!0,D!2      (10,000원 중 A,B=2,500원, D=5,000원) => 10000:A,B,D!2
     * 10000:A,B!1.5,C!0,D!1.5(10,000원 중 A=2,500원, B,D=2,750원) => 10000:A,B!1.5,D!1.5
     */

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
    public static final String MESSAGE_STR = "message";

    TextView mResultTextView;
    EditText mInputEditText;
    String mResultString;
    int mUnit;

    public KidsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kids, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mResultTextView = (TextView) view.findViewById(R.id.result_text_view);
        mResultTextView.setHint(INFORMATION);

        view.findViewById(R.id.calc_button).setOnClickListener(this);
        view.findViewById(R.id.init_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calc_button:
                calcEditText();
                break;
            case R.id.init_button:
                initEditText();
                break;
        }
        sendThemMessage();
    }

    private void calcEditText() {
        mInputEditText = (EditText) getView().findViewById(R.id.input_edit_text);
        Calculator cal = new Calculator(mInputEditText.getText().toString(), getUnit());
        mResultString = cal.getResult();
        mResultTextView.setText(mResultString);
    }

    private void initEditText() {
        mResultString = "";
        mInputEditText.setText("");
        mInputEditText.setHint("금액:이름!배율,...");
        mResultTextView.setText("");
        mResultTextView.setHint(INFORMATION);
    }

    private void sendThemMessage() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = message.edit();
        editor.putString(MESSAGE_STR, mResultString);
        editor.apply();
    }

    private void bringMeMessage() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mResultString = message.getString(MESSAGE_STR, "");
        mResultTextView.setText(mResultString);
    }

    @Override
    public void onResume() {
        super.onResume();
        bringMeMessage();
    }

    private int getUnit() {
        RadioGroup unitRadioGroup = (RadioGroup) getView().findViewById(R.id.unit_radio_group);
        int radioButtonId = unitRadioGroup.getCheckedRadioButtonId();
        int unit;
        if (radioButtonId == -1) {
            unit = 10;
        } else {
            RadioButton unitRadioButton = (RadioButton) getView().findViewById(radioButtonId);
            unit = Integer.parseInt(unitRadioButton.getText().toString());
        }
        return unit;
    }
}
