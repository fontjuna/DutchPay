package com.nohseunghwa.fontjuna.dutchpay.fragments;

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

import com.nohseunghwa.fontjuna.dutchpay.R;
import com.nohseunghwa.fontjuna.dutchpay.classes_pack.Spliter;

import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.HINT_EXPRESSION;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.HINT_INFORMATION;
import static com.nohseunghwa.fontjuna.dutchpay.backing.CommonDutchPay.INPUT_EXPRESSION;


public class KidsFragment extends Fragment implements  View.OnClickListener {

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
        switch (v.getId()) {
            case R.id.calc_button:
                calcEditText();
                break;
            case R.id.init_button:
                initEditText();
                break;
        }
        setInputExpression();
    }

    private void calcEditText() {
        mInputEditText = (EditText) getView().findViewById(R.id.input_edit_text);
//        Calculator obj = new Calculator(mInputEditText.getText().toString(), getUnit());
        Spliter obj = new Spliter(mInputEditText.getText().toString(), getUnit());
        mInputExpression = obj.getResultText();
        mResultTextView.setText(mInputExpression);
    }

    private void initEditText() {
        mInputExpression = "";
        mInputEditText.setText("");
        mResultTextView.setText("");
        mInputEditText.setHint(HINT_EXPRESSION);
        mResultTextView.setHint(HINT_INFORMATION);
    }

    private void setInputExpression() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = message.edit();
        editor.putString(INPUT_EXPRESSION, mInputExpression);
        editor.apply();
    }

    public void restoreResult() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        mMessage = message.getString(INPUT_EXPRESSION, "");
//        mMsgText.setText(mMessage);
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
