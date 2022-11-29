package ir.kitgroup.inskuappb.classes.dialog;

import android.app.Activity;

import androidx.annotation.LayoutRes;

public interface ShowDialog {
    void showDialog(Activity activity,String message,String positiveButton,String negativeButton);
    void hide();
}

