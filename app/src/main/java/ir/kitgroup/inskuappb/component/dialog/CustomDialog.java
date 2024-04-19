package ir.kitgroup.inskuappb.component.dialog;

import android.app.Activity;
import android.app.Dialog;

import androidx.annotation.LayoutRes;

public interface CustomDialog {
    Dialog dialog(Activity activity,@LayoutRes int res);
}
