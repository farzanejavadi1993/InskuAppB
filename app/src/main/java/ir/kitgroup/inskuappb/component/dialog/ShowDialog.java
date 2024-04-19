package ir.kitgroup.inskuappb.component.dialog;

import android.app.Activity;

public interface ShowDialog {
    void showDialog(Activity activity,String message,String positiveButton,String negativeButton);
    void hide();
}

