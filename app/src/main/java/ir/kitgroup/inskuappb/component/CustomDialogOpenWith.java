package ir.kitgroup.inskuappb.component;

import android.app.Activity;
import android.app.Dialog;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.component.dialog.DialogInstance;
import ir.kitgroup.inskuappb.component.itent.ClickItem;
import ir.kitgroup.inskuappb.util.Constant;

public class CustomDialogOpenWith {


    //region Interface SaleinJam Button


    private ClickItem clickItem;

    public void setOnClickSaleinItem(ClickItem clickSaleinJam) {
        this.clickItem = clickSaleinJam;
    }
    //endregion Interface Salein Button






    private static CustomDialogOpenWith customDialogOpenWith = null;
    private Dialog mDialog;


    public static CustomDialogOpenWith getInstance() {
        if (customDialogOpenWith == null) {
            customDialogOpenWith = new CustomDialogOpenWith();
        }
        return customDialogOpenWith;
    }

    public void showDialog(Activity activity,String pkg,String path,String link,String id,String nameCompany) {


        DialogInstance dialogInstance = DialogInstance.getInstance();
        mDialog= dialogInstance.dialog(activity,R.layout.open_with_dialog);

        RelativeLayout jam = mDialog.findViewById(R.id.companies);
        ImageView ivCompanies = mDialog.findViewById(R.id.iv_companies);
        TextView tvCompanies = mDialog.findViewById(R.id.tv_companies);
        RelativeLayout salein = mDialog.findViewById(R.id.salein);
        Picasso.get()
                .load("http://"+ Constant.Main_URL_IMAGE +"/GetCompanyImage?id=" +
                        id + "&width=120&height=120")
                .error(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .into(ivCompanies);
        tvCompanies.setText(nameCompany);



        jam.setOnClickListener(view ->
                clickItem.click(pkg, path , link,id));

        salein.setOnClickListener(view -> clickItem.click(
                activity.getString(R.string.pkg_salein),
                activity.getString(R.string.path_salein) ,
                activity.getString(R.string.link_salein),
                id));

        mDialog.show();
    }

    public void hide() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}