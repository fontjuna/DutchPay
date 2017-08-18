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
        saveMessage();
    }

    private void calcEditText() {
        mInputEditText = (EditText) getView().findViewById(R.id.input_edit_text);
        Calculator cal = new Calculator(mInputEditText.getText().toString(), getUnit());
        mResultString = cal.getResult();
        mResultTextView.setText(mResultString);
    }

    private void initEditText() {
        mResultString = "";
        mInputEditText.setText(mResultString);
        mInputEditText.setHint("금액:이름!배율,...");
        mResultTextView.setText("");
        mResultTextView.setHint(INFORMATION);
    }

    private void saveMessage() {
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
