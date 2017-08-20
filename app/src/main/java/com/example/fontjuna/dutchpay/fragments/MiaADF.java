package com.example.fontjuna.dutchpay.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class MiaADF extends DialogFragment {

    private String mTitle;
    private String mMessage;
    private String mPositiveMessage;
    private String mNegativeMessage;
    private int mIcon;
    private boolean mCancelable = false;
    private DialogInterface.OnClickListener mListener;

    public MiaADF() {
        // Required empty public constructor
    }

    public static MiaADF newInstance(
            String title,
            String message,
            String negative,
            String positive,
            DialogInterface.OnClickListener listener) {
        Bundle args = new Bundle();

        MiaADF fragment = new MiaADF();
        fragment.setArguments(args);
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.mNegativeMessage = negative;
        fragment.mPositiveMessage = positive;
        fragment.setButtonClickListener(listener);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mTitle);
        builder.setMessage(mMessage);
        builder.setNegativeButton(mNegativeMessage, null);
        builder.setPositiveButton(mPositiveMessage, mListener);

        builder.setCancelable(false);

        return builder.create();
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public void setPositiveMessage(String message) {
        mPositiveMessage = message;
    }

    public void setNegativeMessage(String message) {
        mNegativeMessage = message;
    }

    public void setButtonClickListener(DialogInterface.OnClickListener listener) {
        mListener = listener;
    }

}
