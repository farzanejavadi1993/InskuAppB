package ir.kitgroup.inskuappb.classes.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import ir.kitgroup.inskuappb.R;

public class ErrorDialog implements ShowDialog {


    public interface ClickButton {
        void onClick();
    }

    public ClickButton clickButton;

    public void setOnClickListener(ClickButton clickButton){
        this.clickButton=clickButton;
    }



    private Dialog mDialog;
    private static ErrorDialog errorDialog;


    // Use Singletone Design Pattern
    public static ErrorDialog getInstance() {
        if (errorDialog == null) {
            errorDialog = new ErrorDialog();
        }
        return errorDialog;
    }


    @Override
    public void showDialog(Activity activity, String message, String positiveButton, String negativeButton) {
        // Use Dry Rule For Create Dialog (OOP Concept)
        DialogInstance dialogInstance = DialogInstance.getInstance();
        mDialog = dialogInstance.dialog(activity, R.layout.error_dialog);
        mDialog.setCancelable(false);

        TextView messageError = mDialog.findViewById(R.id.tvMessageError);
        MaterialButton btn = mDialog.findViewById(R.id.btnErrorDialog);

        messageError.setText(message);

        btn.setOnClickListener(view -> {
            mDialog.dismiss();
            clickButton.onClick();
        });
        mDialog.dismiss();
        mDialog.show();

    }

    @Override
    public void hide() {

        mDialog.dismiss();
    }


}
