package ir.kitgroup.inskuappb.classes.itent;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import ir.kitgroup.inskuappb.classes.dialog.DialogInstance;


public class OpenWith implements ClickItem{



    private Activity activity;
    private SharedPreferences sharedPreferences;


    public  OpenWith (SharedPreferences sharedPreferences,  Activity context) {
      this.activity=context;
      this.sharedPreferences=sharedPreferences;
    }


    @Override
    public void click(String pkg, String path, String link,String id) {
        try {
            ComponentName name = new ComponentName(pkg, path);
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            i.setComponent(name);
            i.putExtra("companyId",id);
            activity.startActivity(i);
            sharedPreferences.edit().putBoolean("loginSuccess",false).apply();
        }
        catch (Exception ignored) {
            if (!link.equals("")) {
                Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
                activity.startActivityForResult(intent, 44);
                sharedPreferences.edit().putBoolean("loginSuccess",false).apply();
            }
        }
    }
}
