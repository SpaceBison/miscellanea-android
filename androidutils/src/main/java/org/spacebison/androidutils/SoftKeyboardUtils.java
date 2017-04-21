package org.spacebison.androidutils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by cmb on 24.12.16.
 */

public class SoftKeyboardUtils {
    public static void hideKeyboard(View view) {
        if (view != null) {
            final Context context = view.getContext();
            SystemServiceUtils.getSystemService(context, InputMethodManager.class)
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
