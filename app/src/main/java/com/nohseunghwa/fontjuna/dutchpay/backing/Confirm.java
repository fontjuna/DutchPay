package com.nohseunghwa.fontjuna.dutchpay.backing;

import android.content.DialogInterface;

import com.nohseunghwa.fontjuna.dutchpay.fragments.MiaADF;

/**
 * Created by fontjuna on 2017-08-19.
 */

public abstract class Confirm {
    private MiaADF mMiaADF;

    public Confirm(String title, String message, String negative, String positive) {
        MiaADF mMiaADF = MiaADF.newInstance(title, message, positive, negative,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                            case DialogInterface.BUTTON_POSITIVE:
                                break;
                        }
                    }
                });
    }

    public MiaADF getMiaADF() {
        return mMiaADF;
    }

}
