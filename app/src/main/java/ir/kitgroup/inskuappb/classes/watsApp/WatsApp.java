package ir.kitgroup.inskuappb.classes.watsApp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import javax.inject.Singleton;


public class WatsApp implements GoToWatsApp {

    @Override
    public void go(String url, String pkg, Activity activity) {
        Intent intentWhatsAppGroup = new Intent(Intent.ACTION_VIEW); Uri uri =
                Uri.parse(url);
        intentWhatsAppGroup.setData(uri);
        intentWhatsAppGroup.setPackage(pkg);
        activity.startActivity(intentWhatsAppGroup);
    }
}
