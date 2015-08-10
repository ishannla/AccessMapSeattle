package com.sabersoft.ishannarula.accessmapseattle.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class CustomDialog extends DialogFragment{

    String title;
    String message;

    public CustomDialog(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public CustomDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title)
               .setMessage(message)
               .setPositiveButton("Dismiss", null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
