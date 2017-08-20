package com.example.fontjuna.dutchpay.fragments;

import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.fontjuna.dutchpay.R;
import com.example.fontjuna.dutchpay.backing.Calculator;
import com.example.fontjuna.dutchpay.backing.CommonDutchPay;

public class KidsFragment extends Fragment implements CommonDutchPay, View.OnClickListener {

    private TextView mResultTextView;
    private EditText mInputEditText;
    private String mInputExpression = "";
    private int mUnit = 0;
    private int mChoice = -1;

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

        mInputEditText = (EditText) getView().findViewById(R.id.input_edit_text);
        mResultTextView = (TextView) view.findViewById(R.id.result_text_view);
        mResultTextView.setHint(HINT_INFORMATION);

        view.findViewById(R.id.calc_button).setOnClickListener(this);
        view.findViewById(R.id.init_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        Confirm confirm = new Confirm("계산하기", "입력한대로 금액을 나눕니다.", "취소", "실행");
//        confirm.getMiaADF().show(getActivity().getSupportFragmentManager(),"confirm");
//        if (confirm.getChoice() == 0) {
        switch (v.getId()) {
            case R.id.calc_button:
                calcEditText();
                break;
            case R.id.init_button:
                confirm("초기화 하기",
                        "다른 내용을 입력 하시려면\n계산 결과를 지우고 입력을 초기화 합니다.",
                        "취소", "실행");
//                initEditText();
                break;
        }
        setInputExpression();
    }

    private void confirm(String title, String message, final String negative, final String positive) {
        MiaADF miaADF = MiaADF.newInstance(title, message, negative, positive,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_NEGATIVE:
                                Toast.makeText(getContext(), negative, Toast.LENGTH_SHORT).show();
                                break;
                            case DialogInterface.BUTTON_POSITIVE:
                                initEditText();
                                Toast.makeText(getContext(), positive, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
        miaADF.show(getActivity().getSupportFragmentManager(), "choice");
    }

    private void calcEditText() {
//        confirm("계산 하기", "더 입력할 것이 없으면\n입력한대로 금액을 계산 합니다.", "취소", "실행");
//        if (mChoice == 0) {
        mInputEditText = (EditText) getView().findViewById(R.id.input_edit_text);
        Calculator cal = new Calculator(mInputEditText.getText().toString(), getUnit());
        mInputExpression = cal.getTextResult();
        mResultTextView.setText(mInputExpression);
//    }
    }

    private void initEditText() {
//        confirm("초기화 하기", "다른 내용을 입력 하시려면\n계산 결과를 지우고 입력을 초기화 합니다.", "취소", "실행");
        mInputExpression = "";
        mInputEditText.setText("");
        mResultTextView.setText("");
        mInputEditText.setHint(HINT_EXPRESSION);
        mResultTextView.setHint(HINT_INFORMATION);
//        }
    }

    private void setInputExpression() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = message.edit();
        editor.putString(INPUT_EXPRESSION, mInputExpression);
        editor.apply();
    }

    private void getInputExpression() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mInputExpression = message.getString(INPUT_EXPRESSION, "");
        mResultTextView.setText(mInputExpression);
    }

    @Override
    public void onResume() {
        super.onResume();
        getInputExpression();
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
