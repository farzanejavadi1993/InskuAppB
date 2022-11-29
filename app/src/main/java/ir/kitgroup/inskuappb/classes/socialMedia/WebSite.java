package ir.kitgroup.inskuappb.classes.socialMedia;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class WebSite implements GoToSocialMedia {

    @Override
    public void check(Activity activity, String url) {
        if (!url.contains("http"))
            url="http://"+url;
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        activity.startActivity(launchBrowser);
    }
}
