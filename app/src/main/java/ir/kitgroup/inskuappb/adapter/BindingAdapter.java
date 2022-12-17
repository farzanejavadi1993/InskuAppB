package ir.kitgroup.inskuappb.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.text.DecimalFormat;

import ir.kitgroup.inskuappb.App;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.ui.launcher.homeItem.LauncherFragment;
import ir.kitgroup.inskuappb.util.Constant;


public class BindingAdapter {

    @androidx.databinding.BindingAdapter("setImageView")
    public static void setImageView(ImageView imageView, String src) {

        if (imageView != null) {
            Picasso.get()
                    .load("http://" + Constant.Main_URL_IMAGE + "/GetCompanyImage?id=" +
                            src + "&width=120&height=120")
                    .error(R.drawable.loading)
                    .placeholder(R.drawable.loading)
                    .into(imageView);
        }
    }


    @androidx.databinding.BindingAdapter("setImageViewCatalogPage")
    public static void setImageViewCatalogPage(ImageView imageView, String src) {

        Company company = Select.from(Company.class).first();
        if (imageView != null) {
            Picasso.get()
                    .load("http://" + company.getIp1() + "/GetImage?userName=" + company.getUser() + "&&passWord=" + company.getPass() + "&&Id=" + src + "&&width=" + 120 + "&&height=" + 120)
                    .error(R.drawable.loading)
                    .placeholder(R.drawable.loading)
                    .into(imageView);
        }
    }


    @androidx.databinding.BindingAdapter("setImageViewAdvertise")
    public static void setImageViewAdvertise(ImageView imageView, String src) {


        if (imageView != null) {

            if (!LauncherFragment.fragment.equals("advertise")){
                Glide.with(imageView.getContext())
                        .load("http://" + Constant.Main_URL_IMAGE + "/GetAdvertisementImage?id=" +
                                src + "&width=" + 600 + "&height=" + 400)
                        .error(R.drawable.loading2)
                        .placeholder(R.drawable.loading12)
                        .centerInside()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageView);
            }else {
                Glide.with(imageView.getContext())
                        .load("http://" + Constant.Main_URL_IMAGE + "/GetAdvertisementImage?id=" +
                                src + "&width=" + 600 + "&height=" + 400)
//                        .error(R.drawable.loading2)
//                        .placeholder(R.drawable.loading12)
                        .centerInside()
                        .fitCenter()
                        .into(imageView);
            }



        }
    }


    @SuppressLint("SetTextI18n")
    @androidx.databinding.BindingAdapter("textPrice")
    public static void textPrice(TextView textView, String price) {
        if (textView != null) {
            DecimalFormat formatter = new DecimalFormat("#,###,###,###");
            textView.setText(formatter.format(Float.parseFloat(String.valueOf(price))) + " ریال ");
        }
    }


    @SuppressLint("SetTextI18n")
    @androidx.databinding.BindingAdapter("textDescMessage")
    public static void textDescMessage(TextView textView, String text) {
        if (textView != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                textView.setText(Html.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY));
            else
                textView.setText(Html.fromHtml(text));

            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }


    @SuppressLint("SetTextI18n")
    @androidx.databinding.BindingAdapter("textChangeInt")
    public static void textChangeInt(TextView textView, int text) {
        if (textView != null) {
            textView.setText(String.valueOf(text));
        }
    }

    @SuppressLint("SetTextI18n")
    @androidx.databinding.BindingAdapter("textExpire")
    public static void textExpire(TextView textView, String text) {
        if (textView != null) {
            textView.setText("تاریخ انقضا : " + text);
        }
    }


    @SuppressLint("SetTextI18n")
    @androidx.databinding.BindingAdapter("textMessage")
    public static void textMessage(TextView textView, int number) {
        if (textView != null) {
            if (number == 0)
                textView.setVisibility(View.GONE);
            else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(String.valueOf(number));
            }
        } else
            textView.setVisibility(View.GONE);

    }

    @SuppressLint("SetTextI18n")
    @androidx.databinding.BindingAdapter("textEmpty")
    public static void textEmpty(TextView textView, String text) {
        if (textView != null && !text.equals("")) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        } else
            textView.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    @androidx.databinding.BindingAdapter("textInclude")
    public static void textInclude(TextView textView, String text) {

        if (textView != null && text != null) {
            textView.setText("(" + text + ")");
        } else
            textView.setVisibility(View.GONE);
    }


}
