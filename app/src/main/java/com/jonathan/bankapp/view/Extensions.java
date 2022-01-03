package com.jonathan.bankapp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Patterns;
import com.jonathan.bankapp.R;
import java.util.regex.Pattern;
import dmax.dialog.SpotsDialog;

public class Extensions {

    public void Constructor() {
        //
    }

    public boolean isValidEmail(String email)  {
        Pattern patterns = Patterns.EMAIL_ADDRESS;
        return patterns.matcher(email).matches();
    }

    public AlertDialog setUpProgress(Context context) {
        return new SpotsDialog.Builder()
                .setContext(context)
                .setTheme(R.style.Custom)
                .setCancelable(false)
                .build();
    }
}
