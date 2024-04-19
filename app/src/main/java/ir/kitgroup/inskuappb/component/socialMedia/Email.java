package ir.kitgroup.inskuappb.component.socialMedia;

import android.app.Activity;
import android.content.Intent;

import ir.kitgroup.inskuappb.component.dialog.Error;

public class Email implements GoToSocialMedia{


    public Error error;

    public void showError(Error error){
        this.error=error;
    }

    @Override
    public void check(Activity activity, String url) {
        try {
            Intent intent=new Intent(Intent.ACTION_SEND);
            String[] recipients={url};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            activity.startActivity(Intent.createChooser(intent, "Send mail"));
        }catch (Exception ignored){
            error.onError("خطا در یافتن ایمیل ثبت شده در موبایل");
        }

    }
}
