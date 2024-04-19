package ir.kitgroup.inskuappb;

import android.app.Application;

import com.orm.SugarContext;

import dagger.hilt.android.HiltAndroidApp;


@HiltAndroidApp
public class App extends Application {

    @Override
    public void onCreate() {

        SugarContext.init(getApplicationContext());

        super.onCreate();
    }
}