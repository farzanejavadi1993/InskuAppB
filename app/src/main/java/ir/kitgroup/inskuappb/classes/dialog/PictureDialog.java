package ir.kitgroup.inskuappb.classes.dialog;

import android.app.Activity;
import android.app.Dialog;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.util.Constant;

public class PictureDialog implements ShowDialog {


    private Dialog mDialog;
    private static PictureDialog pictureDialog;


    // Use Singletone Design Pattern
    public static PictureDialog getInstance() {
        if (pictureDialog == null) {
            pictureDialog = new PictureDialog();
        }
        return pictureDialog;
    }


    @Override
    public void showDialog(Activity activity, String message, String accountId, String url) {

        // Use Dry Rule For Create Dialog (OOP Concept)
        DialogInstance dialogInstance = DialogInstance.getInstance();
        mDialog = dialogInstance.dialog(activity, R.layout.picture_dialog);
        mDialog.setCancelable(true);

        RoundedImageView ivMessage = mDialog.findViewById(R.id.iv_message_pic);



     //  ivMessage.getLayoutParams().height =  (Constant.height );
       //ivMessage.getLayoutParams().width =  (Constant.height );

        Picasso.get()
                .load(Constant.Dood_IMAGE + "/GetMessagePicture?userId=" + accountId + "&appId=" + Constant.APPLICATION_ID + "&messageId=" + url + "&width=0&height=0")
                .error(R.drawable.loading)
                .fit()
                .centerInside()
                .placeholder(R.drawable.loading)
                .into(ivMessage);
        mDialog.dismiss();
        mDialog.show();

    }

    @Override
    public void hide() {

        mDialog.dismiss();
    }
}
