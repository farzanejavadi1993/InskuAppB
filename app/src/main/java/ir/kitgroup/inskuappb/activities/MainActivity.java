package ir.kitgroup.inskuappb.activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.orm.query.Select;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.classes.CustomDialog;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.databinding.ActivityMainBinding;

import ir.kitgroup.inskuappb.ui.launcher.homeItem.LauncherFragment;
import ir.kitgroup.inskuappb.ui.launcher.messageItem.AllMessageFragment;
import ir.kitgroup.inskuappb.ui.launcher.profileitem.ProfileFragment;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
    SharedPreferences sharedPreferences;
    private boolean exit = false;
    private CustomDialog customDialog;
    private NavController navController;
    private ActivityMainBinding binding;
    private FirebaseAnalytics mFirebaseAnalytics;
    int count = 0;

    private PowerManager powerManager;

    private boolean flagMessage = false;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);


      /*  Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();*/


        customDialog = new CustomDialog();
        customDialog.setOnClickPositiveButton(() -> finish());
        customDialog.setOnClickNegativeButton(() -> customDialog.hideProgress());

        setClearCounterOrder();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            switch (destination.getId()) {

                case R.id.IntroFragment:
                case R.id.VerifyCodeFragment:
                case R.id.CatalogFragment:
                case R.id.SplashScreenFragment:
                case R.id.ListOrderFragment:
                case R.id.DetailCompanyFragment:
                case R.id.FilterFragment:
                case R.id.DetailAdvertiseFragment:
                case R.id.DetailMessageFragment:
                case R.id.RuleFragment:
                case R.id.SearchCompany:
                case R.id.CompanyAdvertise:
                case R.id.ContactUsFragment:
                case R.id.SearchAdvertiseFragment:
                case R.id.CallMeRequest:
                case R.id.WantAdvFragment:

                    binding.navView.setVisibility(View.GONE);
                    exit = false;
                    break;
                case R.id.HomeFragment:
                    exit = true;

                    binding.navView.setVisibility(View.VISIBLE);
                    break;

                case R.id.ProfileFragment:
                case R.id.AllMessageFragment:
                    exit = false;
                    binding.navView.setVisibility(View.VISIBLE);
                    break;

                case R.id.LoginFragment:
                    exit = true;
                    binding.navView.setVisibility(View.GONE);
                    break;


            }
        });

        NavigationUI.setupWithNavController(binding.navView, navController);


        if (getIntent() != null && getIntent().getExtras() != null) {

            String notification = getIntent().getExtras().getString("notification");

            if (notification != null) {
                flagMessage = true;
                new android.os.Handler(Looper.getMainLooper()).postDelayed(
                        () -> binding.navView.setSelectedItemId(R.id.AllMessageFragment),
                        2000);
            }
        }



       /* binding.navView.setOnNavigationItemSelectedListener { item ->
            if(item.itemId != binding.navView.selectedItemId)
                NavigationUI.onNavDestinationSelected(item, navController)
            true
        }*/
    }

    @Override
    public void onBackPressed() {

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().getPrimaryNavigationFragment();
        FragmentManager fragmentManager = navHostFragment.getChildFragmentManager();
        Fragment fragment = fragmentManager.getPrimaryNavigationFragment();
        int size = navController.getBackQueue().size();

        if (!exit && size >= 3 && (fragment instanceof ProfileFragment || fragment instanceof AllMessageFragment ||
                fragment instanceof LauncherFragment
        )) {
          int loopSize;
            if (navController.getBackQueue().get(1).getDestination().getDisplayName().contains("LoginFragment"))
               loopSize= size-3;
            else
                loopSize=size-2;


            for (int i = 0; i < loopSize ; i++) {
                navController.popBackStack();
            }
        }

       else if (exit) {
            finishApp();
        }


       else
            super.onBackPressed();


    }


    @Override
    protected void onPause() {
        super.onPause();
        boolean isScreenOn = powerManager.isScreenOn();
        if (isScreenOn && !flagMessage && sharedPreferences.getBoolean("loginSuccess", false))
            finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Select.from(Account.class).first() != null)
            sharedPreferences.edit().putBoolean("loginSuccess", true).apply();
    }

    public void setCounterOrder(int count) {
        binding.navView.getOrCreateBadge(R.id.AllMessageFragment).setBackgroundColor(getResources().getColor(R.color.red));
        binding.navView.getOrCreateBadge(R.id.AllMessageFragment).setNumber(count);
    }

    public int getCounter() {

        return binding.navView.getOrCreateBadge(R.id.AllMessageFragment).getNumber();
    }

    public void setClearCounterOrder() {
        binding.navView.getOrCreateBadge(R.id.AllMessageFragment).setBackgroundColor(getResources().getColor(R.color.white));
        binding.navView.getOrCreateBadge(R.id.AllMessageFragment).clearNumber();

    }





    private void finishApp() {
        if (count == 1) {
            finish();
            return;
        } else {
            count += 1;
        }
        Toasty.success(this, "برای خروج دوبار پشت سرهم بازگشت را بزنید").show();
        new Handler().postDelayed(() -> count = 0, 2000);
    }
}