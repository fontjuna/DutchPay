package com.example.fontjuna.dutchpay.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fontjuna.dutchpay.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PapaFragment extends Fragment {


    public PapaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_papa, container, false);
    }

}
