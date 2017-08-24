package com.nohseunghwa.gallane.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nohseunghwa.gallane.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PapaFragment extends Fragment {

    private String mMessage;

    public PapaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_papa, container, false);
    }

    public void restoreResult() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        mMessage = message.getString(INPUT_EXPRESSION, "");
//        mMsgText.setText(mMessage);
    }

}
