package ir.kitgroup.inskuappb.component.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;

import ir.kitgroup.inskuappb.R;

public class DialogInstance implements CustomDialog {
    Dialog mDialog;
    private static DialogInstance dialogInstance;


    public static DialogInstance getInstance() {
        if ( dialogInstance== null) {
            dialogInstance = new DialogInstance();
        }
        return dialogInstance;
    }

    @Override
    public Dialog dialog(Activity activity, @LayoutRes int res) {

        mDialog = new android.app.Dialog(activity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        mDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.setContentView(res);
        return mDialog;
    }



}
