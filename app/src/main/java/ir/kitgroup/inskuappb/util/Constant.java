package ir.kitgroup.inskuappb.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.List;

import ir.kitgroup.inskuappb.classes.CustomTypefaceSpan;
import ir.kitgroup.inskuappb.model.VisitAdvertisement;

public class Constant {


  /* public static String Main_URL = "http://api.kitgroup.ir/api/REST/";
   public static String Main_URL_IMAGE = "api.kitgroup.ir";
   public static String Dood_URL = "http://api.kitgroup.ir:4466/api/REST/";
   public static String Dood_IMAGE = "http://api.kitgroup.ir:4466";*/


    /*public static String Main_URL = "http://192.168.20.63:1993/api/REST/";
    public static String Main_URL_IMAGE = "http://192.168.20.63:1993";
    public static String Dood_URL = "http://api.kitgroup.ir:4466/api/REST/";
    public static String Dood_IMAGE = "http://api.kitgroup.ir:4466";*/


    public static String Main_URL = "http://2.180.28.6:1993/api/REST/";
    public static String Main_URL_IMAGE = "2.180.28.6:1993";
    public static String Dood_URL = "http://api.kitgroup.ir:4466/api/REST/";
    public static String Dood_IMAGE = "http://api.kitgroup.ir:4466";

    public static String PRODUCTION_BASE_URL = "";

    public static String APPLICATION_CODE = "123456";
    public static String APPLICATION_ID = "";
    public static final String APPLICATION_ID_MAP = "ir.kitgroup.inskuappb.v2.farzane";
    public static int height;
    public static int width;
    public static double screenInches;

    public static String toEnglishNumber(String input) {

        String[] persian = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};
        String[] arabic = new String[]{"٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩"};

        for (int j = 0; j < persian.length; j++) {
            if (input.contains(persian[j]))
                input = input.replace(persian[j], String.valueOf(j));
        }

        for (int j = 0; j < arabic.length; j++) {
            if (input.contains(arabic[j]))
                input = input.replace(arabic[j], String.valueOf(j));
        }

        return input;
    }

    public static class JsonObjectAccount {
        public List<ir.kitgroup.inskuappb.dataBase.Account> Account;
    }


    public static class JsonObjectVisitAdvertisement {
        public List<VisitAdvertisement> VisitAdvertisement;
    }

    public static void configSize(Activity activity) {
        //region Create Size
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
        double x = Math.pow(width / dm.xdpi, 2);
        double y = Math.pow(height / dm.ydpi, 2);
        screenInches = Math.sqrt(x + y);
        screenInches = Math.sqrt(x + y);


        //endregion Create Size
    }


    public static boolean GetCheckAmountSt(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("no_check_amount_switch", false);
    }


    public static boolean GetOnLineOrder(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("check_online_order", false);
    }

    public static String GetCmd(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString("cmd", "");
    }

    public static Boolean GetReturnedCheque(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("check_returned_check_order", false);
    }

    public static Boolean GetChequeNotPassed(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("check_cheque_not_passed_order", false);

    }

    public static Boolean GetAccountRemaining(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("check_account_remaining_order", false);

    }

    public static Boolean GetCatalogAmountSt(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("catalog_amount_switch", false);
    }

    public static boolean GetCatalogPageDescSt(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("catalog_page_desc_switch", false);
    }

    public static String GetStartTime(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString("StartTime", "");
    }

    public static String GetEndTime(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString("EndTime", "");
    }

    public static boolean GetSignatureSt(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("signature_enable_switch", false);
    }


    private static SpannableString setTypeface(SpannableString sb, Typeface typeface, int start, int end, int color) {
        TypefaceSpan robotoRegularSpan = new CustomTypefaceSpan("", typeface);
        sb.setSpan(robotoRegularSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(typeface, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }


    public static void setTextFontToSpecialPart(String description, Typeface typeface, int start, int end, int color, TextView textView) {
        SpannableString spannable = new SpannableString(description);
        SpannableString sb = setTypeface(spannable, typeface, start, end, color);
        textView.setText(sb);
    }


    public static void setText(final String text, int start, int end, TextView tv) {
        SpannableString ss1 = new SpannableString(text);
        ss1.setSpan(new RelativeSizeSpan(0.75f), start, end, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.GRAY), start, end, 0);// set color
        tv.setText(ss1);
    }


    public static String appVersion(Activity activity) throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
        return pInfo.versionName;
    }

    public static boolean connectedToInternet(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }

    public static void hideKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public static String getAndroidID(Activity activity) {

        String id;

        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (androidId == null || androidId.equals("")) {
            try {
                androidId = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                        Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                        Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                        Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                        Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                        Build.USER.length() % 10;
            } catch (Exception ignored) {
                androidId = Build.ID + Build.HOST;
            }
        }
        id = androidId;


        return id;
    }
}
