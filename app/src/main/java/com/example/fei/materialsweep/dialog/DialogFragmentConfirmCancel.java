package com.example.fei.materialsweep.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.fei.materialsweep.R;
import com.example.fei.materialsweep.utils.DialogCallBack;

/**
 * Created by fei on 2017/7/26.
 */

public class DialogFragmentConfirmCancel extends DialogFragment{

    public DialogCallBack dcb;

    public void setDcb(DialogCallBack dcb) {
        this.dcb = dcb;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_confirm_cancel, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setPositiveButton(R.string.soft_update_updatebtn,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                    dcb.confirm();
                    }
                }).setNegativeButton(R.string.soft_update_later, null);
        return builder.create();
    }
}

