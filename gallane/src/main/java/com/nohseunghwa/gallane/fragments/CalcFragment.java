package com.nohseunghwa.gallane.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nohseunghwa.gallane.R;
import com.nohseunghwa.gallane.backing.Calculator;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalcFragment extends Fragment implements View.OnClickListener {

    private TextView mResultTextView;
    private TextView mInputTextView;
    private String mInput;
    private String mResult;
    private DecimalFormat df = new DecimalFormat("#,##0.######");


    public CalcFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calc, container, false);
    }

    public void restoreResult() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        mMessage = message.getString(INPUT_EXPRESSION, "");
//        mMsgText.setText(mMessage);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInput = "0";
        mResult = "";
        mInputTextView = (TextView) view.findViewById(R.id.input_text_view);
        mResultTextView = (TextView) view.findViewById(R.id.result_text_view);
        mInputTextView.setText(mInput);
        mResultTextView.setText(mResult);

        view.findViewById(R.id.button_0).setOnClickListener(this);
        view.findViewById(R.id.button_1).setOnClickListener(this);
        view.findViewById(R.id.button_2).setOnClickListener(this);
        view.findViewById(R.id.button_3).setOnClickListener(this);
        view.findViewById(R.id.button_4).setOnClickListener(this);
        view.findViewById(R.id.button_5).setOnClickListener(this);
        view.findViewById(R.id.button_6).setOnClickListener(this);
        view.findViewById(R.id.button_7).setOnClickListener(this);
        view.findViewById(R.id.button_8).setOnClickListener(this);
        view.findViewById(R.id.button_9).setOnClickListener(this);
        view.findViewById(R.id.button_dot).setOnClickListener(this);

        view.findViewById(R.id.button_plus).setOnClickListener(this);
        view.findViewById(R.id.button_minus).setOnClickListener(this);
        view.findViewById(R.id.button_divide).setOnClickListener(this);
        view.findViewById(R.id.button_by).setOnClickListener(this);
        view.findViewById(R.id.button_left).setOnClickListener(this);
        view.findViewById(R.id.button_right).setOnClickListener(this);

        view.findViewById(R.id.button_del).setOnClickListener(this);
        view.findViewById(R.id.button_ac).setOnClickListener(this);
        view.findViewById(R.id.button_go).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_go: {
                try {
                    BigDecimal val = Calculator.Calculate(mInput);
                    mResult = mInput + " = " + df.format(val) + "\n" + mResult;
                    mResultTextView.setText(mResult);
                    mInput = "0";
                    mInputTextView.setText(mInput);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "수식에 이상이 있습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.button_del: {
                mInput = mInput.length() > 0 ? mInput.substring(0, mInput.length() - 1) : "";
                mInput = mInput.isEmpty() ? "0" : mInput;
                mInputTextView.setText(mInput);
                break;
            }
            case R.id.button_ac: {
                mInput = "0";
                mInputTextView.setText(mInput);
                break;
            }
            default: {
                if (mInput.equals("0")) {
                    mInput = "";
                }
                mInput += ((TextView) view).getText().toString();
                mInputTextView.setText(mInput);
                break;
            }
        }
    }

}
