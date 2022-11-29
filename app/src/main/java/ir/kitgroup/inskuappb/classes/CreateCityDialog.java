package ir.kitgroup.inskuappb.classes;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ir.kitgroup.inskuappb.R;

public class CreateCityDialog {
    //region Interface Positive Button
    public interface ClickPositiveButton {
        void onClick(String city);
    }

    private ClickPositiveButton clickPositiveButton;

    public void setOnClickPositiveButton(ClickPositiveButton clickPositiveButton) {
        this.clickPositiveButton = clickPositiveButton;
    }
    //endregion Interface Positive Button


    //region Interface Positive Button
    public interface ClickNegativeButton {
        void onClick();
    }

    private ClickNegativeButton clickNegativeButton;

    public void setOnClickNegativeButton(ClickNegativeButton clickNegativeButton) {
        this.clickNegativeButton = clickNegativeButton;
    }
    //endregion Interface Positive Button

    private static CreateCityDialog createCityDialog = null;
    private Dialog mDialog;


    public static CreateCityDialog getInstance() {
        if (createCityDialog == null) {
            createCityDialog = new CreateCityDialog();
        }
        return createCityDialog;
    }

    public void showDialog(Context context,String addCity,String message,boolean error) {


        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.create_city_dialog);

        EditText edtAddCity = mDialog.findViewById(R.id.edtAddCity);
        TextView txtMessage = mDialog.findViewById(R.id.txtMessage);
        RelativeLayout rlCancel = mDialog.findViewById(R.id.rl_cancel);
        RelativeLayout rlOk = mDialog.findViewById(R.id.rl_ok);

        if (error)
            txtMessage.setTextColor(context.getResources().getColor(R.color.red));
        else
            txtMessage.setTextColor(context.getResources().getColor(R.color.normal_color));

        txtMessage.setText(message);
        edtAddCity.setText(addCity);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        rlOk.setOnClickListener(view -> {
            String city = edtAddCity.getText().toString();
            if (!city.trim().equals("")) {
                clickPositiveButton.onClick(city);
            }

        });
        rlCancel.setOnClickListener(v12 ->
                clickNegativeButton.onClick());

        mDialog.show();
    }

    public void hideProgress() {
        if (mDialog != null) {
            try {
                mDialog.dismiss();
            } catch (Exception ignored) {
            }
            mDialog = null;
        }
    }
}
