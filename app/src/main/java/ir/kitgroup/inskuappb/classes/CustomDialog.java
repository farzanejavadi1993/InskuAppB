package ir.kitgroup.inskuappb.classes;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.github.gcacace.signaturepad.views.SignaturePad;

import ir.kitgroup.inskuappb.R;

public class CustomDialog {


    //region Interface Clear Button
    public interface ClickClearButton {
        void onClick(boolean active, String SIG);
    }

    private ClickClearButton clickClearButton;

    public void setOnClickClearButton(ClickClearButton clickClearButton) {
        this.clickClearButton = clickClearButton;
    }
    //endregion Interface Clear Button


    //region Interface Negative Button
    public interface ClickNegativeButton {
        void onClick();
    }

    private ClickNegativeButton clickNegativeButton;

    public void setOnClickNegativeButton(ClickNegativeButton clickNegativeButton) {
        this.clickNegativeButton = clickNegativeButton;
    }
    //endregion Interface Negative Button


    //region Interface Positive Button
    public interface ClickPositiveButton {
        void onClick();
    }

    private ClickPositiveButton clickPositiveButton;

    public void setOnClickPositiveButton(ClickPositiveButton clickPositiveButton) {
        this.clickPositiveButton = clickPositiveButton;
    }
    //endregion Interface Positive Button


    private static CustomDialog customDialog = null;
    private Dialog mDialog;


    public static CustomDialog getInstance() {
        if (customDialog == null) {
            customDialog = new CustomDialog();
        }
        return customDialog;
    }

    public void showDialog(Context context, String message, boolean cancelable, String textNegative, String textPositive, boolean signature,boolean showPositiveButton , boolean showNegativeBotton) {


        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.custom_dialog);

        TextView tvMessage = mDialog.findViewById(R.id.tv_message);
        RelativeLayout rlCancel = mDialog.findViewById(R.id.rl_cancel);
        TextView tvNegative = mDialog.findViewById(R.id.tv_no);
        RelativeLayout rlOk = mDialog.findViewById(R.id.rl_ok);
        TextView tvPositive = mDialog.findViewById(R.id.tv_ok);


        RelativeLayout rlClear = mDialog.findViewById(R.id.rl_clear);
        SignaturePad mSignaturePad = mDialog.findViewById(R.id.signaturePad);


        if (!showNegativeBotton)
            rlCancel.setVisibility(View.GONE);

        if (!showPositiveButton)
            rlOk.setVisibility(View.GONE);



        tvMessage.setText(message);
        tvPositive.setText(textPositive);
        tvNegative.setText(textNegative);

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);

        if (signature)
            mSignaturePad.setVisibility(View.VISIBLE);





        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                rlClear.setVisibility(View.VISIBLE);
                clickClearButton.onClick(true, mSignaturePad.getSignatureSvg());
            }

            @Override
            public void onClear() {
                rlClear.setVisibility(View.GONE);
                clickClearButton.onClick(false, "");
            }
        });


        rlOk.setOnClickListener(view -> clickPositiveButton.onClick());
        rlCancel.setOnClickListener(v12 -> {


            try {
                mSignaturePad.clear();
            }catch (Exception ignored) {}

            if (clickNegativeButton!=null)
                clickNegativeButton.onClick();
                }
        );
        mDialog.show();
    }

    public void hideProgress() {
        if (mDialog != null) {
            try {
                mDialog.dismiss();
            }catch (Exception ignored){}

            mDialog = null;
        }
    }
}