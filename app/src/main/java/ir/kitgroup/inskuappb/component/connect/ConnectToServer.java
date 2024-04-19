package ir.kitgroup.inskuappb.component.connect;

import android.content.SharedPreferences;

import ir.kitgroup.inskuappb.util.Constant;

public class ConnectToServer {


    public void connect(SharedPreferences sharedPreferences,HostSelectionInterceptor hostSelectionInterceptor,boolean connect,String url){
        sharedPreferences.edit().putBoolean("status", connect).apply();
        Constant.PRODUCTION_BASE_URL=url;
        hostSelectionInterceptor.setHostBaseUrl();
    }

}
