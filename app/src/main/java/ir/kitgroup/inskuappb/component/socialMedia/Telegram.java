package ir.kitgroup.inskuappb.component.socialMedia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import ir.kitgroup.inskuappb.component.dialog.Error;

public class Telegram implements GoToSocialMedia {



    public Error error;

    public void showError(Error error){
        this.error=error;
    }

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    public void check(Activity activity, String url) {
        try {
            if (!url.contains("http"))
                url="https://"+url;


            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            PackageManager pm = activity.getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                activity.startActivity(intent);
            }
        } catch (Exception ignored) {
            error.onError("پیامرسان تلگرام نصب نشده است.");
        }

    }
}
