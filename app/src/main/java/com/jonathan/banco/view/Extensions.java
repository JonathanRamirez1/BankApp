package com.jonathan.banco.view;

import android.util.Patterns;

import java.util.regex.Pattern;

public class Extensions {

    public void Constructor() {
        //
    }

    public boolean isValidEmail(String email)  {
        Pattern patterns = Patterns.EMAIL_ADDRESS;
        return patterns.matcher(email).matches();
    }
}
