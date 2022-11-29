package ir.kitgroup.inskuappb.classes;



import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.util.Constant;

public class CustomOrderDialog {

    private DecimalFormat formatter = new DecimalFormat("#,###,###,###");

    //region Interface Positive Button
    public interface ClickPositiveButton{
        void onClick(double amount);
    }
    private ClickPositiveButton clickPositiveButton;
    public void  setOnClickPositiveButton(ClickPositiveButton clickPositiveButton){
        this.clickPositiveButton=clickPositiveButton;
    }
    //endregion Interface Positive Button





    //region Interface Max Button
    public interface ClickMaxButton{
        void onClick(double amount,EditText edtAmount);
    }
    private ClickMaxButton clickMaxButton;
    public void  setOnClickMaxButton(ClickMaxButton clickMaxButton){
        this.clickMaxButton=clickMaxButton;
    }
    //endregion Interface Max Button



    //region Interface Min Button
    public interface ClickMinButton{
        void onClick(double amount,EditText edtAmount);
    }
    private ClickMinButton clickMinButton;
    public void  setOnClickMinButton(ClickMinButton clickMinButton){
        this.clickMinButton=clickMinButton;
    }
    //endregion Interface Max Button

    private static   CustomOrderDialog customOrderDialog = null;
    private Dialog mDialog;


    public static CustomOrderDialog getInstance() {
        if (customOrderDialog == null) {
            customOrderDialog = new CustomOrderDialog();
        }
        return customOrderDialog;
    }



    public void showOrderDialog(Context context, String nameProduct, String cofficient,String gift,
            String discount,String lower,String upper, String remain,String textDescription) {




        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.about_item);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        EditText edtAmount = mDialog.findViewById(R.id.edt_amount_dialog);
        RelativeLayout  rlCancel = mDialog.findViewById(R.id.rl_cancel);
        RelativeLayout rlOk = mDialog.findViewById(R.id.rl_ok);

        TextView nameDialog =mDialog.findViewById(R.id.name);
        TextView tvCofficient = mDialog.findViewById(R.id.cofficient);
        TextView giftDialog = mDialog.findViewById(R.id.gift);
        TextView discountDialog = mDialog.findViewById(R.id.discount);
        TextView lowerDialog = mDialog.findViewById(R.id.lower);
        TextView upperDialog = mDialog.findViewById(R.id.upper);
        TextView remainDialog = mDialog.findViewById(R.id.remain);
        TextView textDescribe = mDialog.findViewById(R.id.textDescribe);
        ImageView iv_minus = mDialog.findViewById(R.id.iv_minus);
        ImageView iv_max = mDialog.findViewById(R.id.iv_max);
       RelativeLayout r_cofficient_product = mDialog.findViewById(R.id.r_cofficient_product);
       RelativeLayout r_gift_product = mDialog.findViewById(R.id.r_gift_product);
       RelativeLayout r_discount_product = mDialog.findViewById(R.id.r_discount_product);
       RelativeLayout r_lower_product = mDialog.findViewById(R.id.r_lower_product);
       RelativeLayout r_upper_product = mDialog.findViewById(R.id.r_upper_product);

       if (cofficient.equals(""))
           r_cofficient_product.setVisibility(View.GONE);


        if (gift.equals(""))
            r_gift_product.setVisibility(View.GONE);

        if (discount.equals(""))
            r_discount_product.setVisibility(View.GONE);


        if (lower.equals(""))
            r_lower_product.setVisibility(View.GONE);

        if (upper.equals(""))
            r_upper_product.setVisibility(View.GONE);


        edtAmount.setText("");
        nameDialog.setText(nameProduct);
        tvCofficient.setText(cofficient);
        giftDialog.setText(gift);
        discountDialog.setText(discount);
        lowerDialog.setText(lower);
        upperDialog.setText(upper);
        remainDialog.setText(remain);
        textDescribe.setText(textDescription);


        rlCancel.setOnClickListener(view -> hideDialog());
        rlOk.setOnClickListener(view -> {

            mDialog.hide();
            clickPositiveButton.onClick(getAmount(edtAmount));

        });

        iv_max.setOnClickListener(view -> clickMaxButton.onClick(getAmount(edtAmount),edtAmount));


        iv_minus.setOnClickListener(view -> clickMinButton.onClick(getAmount(edtAmount),edtAmount));

        mDialog.show();
    }

    public void hideDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }


    public void setAmount(EditText edtAmount,double amount) {
        edtAmount.setText(formatter.format(amount));

    }

    private double getAmount(EditText edtAmount){
        double amount;
         amount=Double.parseDouble(
                !edtAmount.getText().toString().equals("") ?
                        Constant.toEnglishNumber(edtAmount.getText().toString() ): "0.0");
        return amount;
    }
}
