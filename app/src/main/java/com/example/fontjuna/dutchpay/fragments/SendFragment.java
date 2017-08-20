package com.example.fontjuna.dutchpay.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fontjuna.dutchpay.R;
import com.example.fontjuna.dutchpay.backing.CommonDutchPay;

public class SendFragment extends Fragment implements CommonDutchPay {

    TextView mMsgText;
    String mMessage;
    String mBanking;

    public SendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mMessage = message.getString(INPUT_EXPRESSION, "");
        mMsgText.setText(mMessage);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMsgText = (TextView) view.findViewById(R.id.content_text);

        Button sendButton = (Button) view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendingSms();
            }
        });

        Button previewButton = (Button) view.findViewById(R.id.preview_button);
        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewSms();
            }
        });
    }

    private void previewSms() {
        mMsgText.setText(mMessage + makeBankingText());
    }

    private void sendingSms() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mMessage + makeBankingText());
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private String makeBankingText() {
        EditText nameText = (EditText) getView().findViewById(R.id.name_text);
        EditText bankText = (EditText) getView().findViewById(R.id.bank_text);
        EditText accountText = (EditText) getView().findViewById(R.id.account_text);

        String name = nameText.getText().toString();
        String bank = bankText.getText().toString();
        String account = accountText.getText().toString();
        boolean yn = (name.isEmpty() || bank.isEmpty() || account.isEmpty());

        String message = (yn ? NO_BANKING :
                "\n\n각자 몫을 계좌로 입금 하시려면 참고 하세요 ^_^"
                        + "\n예금주 : " + name + "\n은행명 : "
                        + bank + "\n계좌번호 : " + account);

        return message;

    }

}
