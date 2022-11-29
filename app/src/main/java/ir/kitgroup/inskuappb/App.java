package ir.kitgroup.inskuappb;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import com.orm.SugarContext;

import dagger.hilt.android.HiltAndroidApp;
import ir.kitgroup.inskuappb.classes.AppSignatureHelper;


@HiltAndroidApp
public class App extends Application {

    @Override
    public void onCreate() {

        SugarContext.init(getApplicationContext());

        super.onCreate();
    }
}