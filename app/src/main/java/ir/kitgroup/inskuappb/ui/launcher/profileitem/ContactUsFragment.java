package ir.kitgroup.inskuappb.ui.launcher.profileitem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.classes.socialMedia.Email;
import ir.kitgroup.inskuappb.classes.socialMedia.Instagram;
import ir.kitgroup.inskuappb.classes.socialMedia.InstallWatsApp;
import ir.kitgroup.inskuappb.classes.socialMedia.Telegram;
import ir.kitgroup.inskuappb.classes.socialMedia.WebSite;
import ir.kitgroup.inskuappb.classes.watsApp.WatsApp;
import ir.kitgroup.inskuappb.databinding.ContactUsFragmentBinding;

@AndroidEntryPoint
public class ContactUsFragment extends Fragment {
    @Inject
    SharedPreferences sharedPreferences;
    private ContactUsFragmentBinding binding;

    private WatsApp watsApp;
    private InstallWatsApp installWatsApp;
    private Email email;
    private Telegram telegram;
    private WebSite webSite;

    private CustomSnackBar snackBar;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ContactUsFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        init();

    }


    private void init() {
        snackBar = new CustomSnackBar();

        installWatsApp = new InstallWatsApp();
        watsApp = new WatsApp();
        email = new Email();
        webSite = new WebSite();
        telegram = new Telegram();


        binding.cardWhatsapp.setOnClickListener(view -> {

            String urlWhatsApp = getActivity().getResources().getString(R.string.wat_url);

            if (!urlWhatsApp.equals("")) {
                boolean install = installWatsApp.check(getActivity(), getString(R.string.watApp_b_link));
                if (install) {
                    try {
                        String pkgWhatsApp = installWatsApp.getPkgWatsApp();
                        watsApp.go(urlWhatsApp, pkgWhatsApp, getActivity());
                        sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
                    } catch (Exception ignored) {
                    }
                } else
                    ShowMessageWarning(getString(R.string.error_watsApp));

            }
        });
        binding.cardGmail.setOnClickListener(view18 -> {
            String urlEmail = getActivity().getResources().getString(R.string.gmail_url);
            if (!urlEmail.equals("")) {
                try {
                    email.check(getActivity(), urlEmail);
                    sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
                } catch (Exception ignored) {
                }
            }
        });
        binding.cardTelegram.setOnClickListener(view18 -> {
            String urlTelegram = getActivity().getResources().getString(R.string.tel_url);
            if (!urlTelegram.equals("")) {
                try {
                    telegram.check(getActivity(), urlTelegram);
                    sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
                } catch (Exception ignored) {
                }
            }
        });
        binding.cardWeb.setOnClickListener(view18 -> {
            String urlWebSite = getActivity().getResources().getString(R.string.gmail_url);
            if (!urlWebSite.equals("")) {
                try {
                    webSite.check(getActivity(), urlWebSite);
                    sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
                } catch (Exception ignored) {
                }
            }
        });

        binding.txtNavigable.setOnClickListener(view -> {
            sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
            String geoUri = "http://maps.google.com/maps?q=loc:" + getString(R.string.lat) + "," + getString(R.string.lng) + " (" + getString(R.string.companyAddress) + ")";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            startActivity(intent);
        });


        binding.cardPhone.setOnClickListener(view12 -> {
            sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + getString(R.string.company_phone)));
            startActivity(intent);
        });


        binding.cardMessage.setOnClickListener(view -> {
            sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
            sendSMS();
        });


        binding.ivBack.setOnClickListener(view -> Navigation.findNavController(binding.getRoot()).popBackStack());
    }

    private void ShowMessageWarning(String error) {
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }
        snackBar.showSnack(getActivity(), binding.getRoot(), error);

    }


    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.edit().putBoolean("loginSuccess", true).apply();
    }


    @SuppressLint("IntentReset")
    protected void sendSMS() {

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", getString(R.string.company_message));
        smsIntent.putExtra("sms_body", "");
        try {
            startActivity(smsIntent);
        } catch (android.content.ActivityNotFoundException ex) {
        }
    }

}
