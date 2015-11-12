package com.cultivatingcows;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

/**
 * Created by colleenminor on 11/11/15.
 */
public class ErrorHelper {

    public static void displayAlertDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle("Uh oh!")
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void handleError(String tag, Context context, String message) {
        Log.e(tag, message);
        displayAlertDialog(context, message);
    }
}