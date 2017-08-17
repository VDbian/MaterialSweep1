package com.example.fei.materialsweep.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.example.fei.materialsweep.R;
import com.example.fei.materialsweep.utils.UpdateManager;

/**
 * Created by fei on 2017/7/26.
 */

public class DialogFragmentDownLoad extends DialogFragment {

    public ProgressBar progressBar;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_download, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        progressBar = (ProgressBar) view.findViewById(R.id.update_progress);
        builder.setView(view)
                // Add action buttons
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancel();
                    }
                });
        return builder.create();
    }

    public void setProgressBarNum(int progress){
        progressBar.setProgress(progress);
    }

    public void cancel(){
        UpdateManager.cancelUpdate = true;
    }
}
