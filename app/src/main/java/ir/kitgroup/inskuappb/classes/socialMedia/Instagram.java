package ir.kitgroup.inskuappb.classes.socialMedia;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

public class Instagram implements GoToSocialMedia{


    @Override
    public void check(Activity activity, String url) {
        Uri uri = Uri.parse(url);


        Intent i= new Intent(Intent.ACTION_VIEW,uri);

        i.setPackage("com.instagram.android");

        try {
           activity. startActivity(i);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com")));
        }
    }
}
