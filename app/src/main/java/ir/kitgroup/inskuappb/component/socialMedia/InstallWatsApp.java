package ir.kitgroup.inskuappb.component.socialMedia;

import android.app.Activity;
import android.content.pm.PackageManager;

import ir.kitgroup.inskuappb.R;

public class InstallWatsApp {

    private String pkgWatsApp;

    public boolean check(Activity activity, String pkg) {
        pkgWatsApp=pkg;
        boolean app_install;


        PackageManager packageManager = activity.getPackageManager();

        try {
            packageManager.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES);
            app_install = true;
        } catch (Exception ignored) {
            app_install = false;
            pkgWatsApp = "";
        }

        try {
            if (!app_install) {
                pkgWatsApp = activity.getResources().getString(R.string.watApp_link);
                packageManager.getPackageInfo(pkgWatsApp, PackageManager.GET_ACTIVITIES);
                app_install = true;
            }
        } catch (Exception ignored) {
            app_install = false;
            pkgWatsApp = "";
        }

        return app_install;
    }

    public  String getPkgWatsApp(){
        return pkgWatsApp;
    }
}
