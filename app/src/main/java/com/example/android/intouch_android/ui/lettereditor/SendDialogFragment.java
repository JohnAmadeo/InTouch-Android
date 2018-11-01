package com.example.android.intouch_android.ui.lettereditor;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.utils.VoidFunction;

public class SendDialogFragment extends DialogFragment {
    public interface SendDialogListener {
        void onDialogPositiveClick();
    }

    SendDialogListener mListener = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void setOnDialogPositiveClickListener(SendDialogListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return builder.setMessage(R.string.send_dialog_message)
                .setPositiveButton(
                        R.string.send_dialog_positive,
                        (dialog, id) -> {
                            if (mListener != null) {
                                mListener.onDialogPositiveClick();
                            }
                        }
                )
                .setNegativeButton(
                        R.string.send_dialog_negative,
                        (dialog, id) -> {}
                )
                .create();
    }
}
