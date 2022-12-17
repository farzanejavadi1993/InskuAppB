package ir.kitgroup.inskuappb.ui.splashscreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.orm.query.Select;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.classes.CustomDialog;
import ir.kitgroup.inskuappb.classes.dialog.ErrorDialog;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.BusinessR;
import ir.kitgroup.inskuappb.dataBase.City;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Customer;
import ir.kitgroup.inskuappb.dataBase.Files;
import ir.kitgroup.inskuappb.dataBase.Guild;
import ir.kitgroup.inskuappb.dataBase.ModelCatalog;
import ir.kitgroup.inskuappb.dataBase.ModelCatalogPageItem;
import ir.kitgroup.inskuappb.dataBase.Ord;
import ir.kitgroup.inskuappb.dataBase.OrdDetail;
import ir.kitgroup.inskuappb.dataBase.State;
import ir.kitgroup.inskuappb.databinding.SplashScreenFragmentBinding;
import ir.kitgroup.inskuappb.model.AppDetail;
import ir.kitgroup.inskuappb.util.Constant;

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
public class SplashScreenFragment extends Fragment {

    //region Parameter
    @Inject
    Typeface typeface;

    @Inject
    SharedPreferences sharedPreferences;


    private Account account;
    private SplashScreenFragmentBinding binding;
    private MainViewModel mainViewModel;

    private CustomDialog updateApplicationDialog;
    private String linkUpdate = "";
    private String appVersion = "";
    private String messageUpdate = "";
    private String newVersion = "";
    private String from = "update";
    private boolean forcedUpdate = false;
    private String titleUpdate = "";
    private ErrorDialog errorDialog;

    //endregion Parameter


    //region Override Method
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SplashScreenFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Constant.configSize(getActivity());
        configToasty();
        init();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.getResultMessage().setValue(null);

        mainViewModel.getApp(Constant.APPLICATION_CODE);
        mainViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            binding.progress.setVisibility(View.GONE);
            errorDialog.showDialog(getActivity(), result.getDescription(), "", "");

        });
        mainViewModel.getResultgetApp().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            mainViewModel.getResultgetApp().setValue(null);

            if (result.size() > 0) {
                Constant.APPLICATION_ID = result.get(0).getAppId();
                if (result.get(0).getIsActive()) {
                    binding.tvError.setVisibility(View.GONE);
                    newVersion = result.get(0).getVersion();
                    titleUpdate = result.get(0).getUpdateTitle();
                    messageUpdate = result.get(0).getUpdateDesc();
                    linkUpdate = result.get(0).getLink();
                    sharedPreferences.edit().putString("link_update", linkUpdate).apply();
                    forcedUpdate = result.get(0).getForced();
                    if (account != null)
                        mainViewModel.getCustomerFromServer(account.getM());
                    else
                        checkUpdate();

                } else {
                    binding.tvError.setText("اپلیکیشن غیر فعال شده است.");
                    binding.tvError.setVisibility(View.VISIBLE);
                }

            }

        });
        mainViewModel.getResultCustomerFromServer().observe(getViewLifecycleOwner(), result -> {
            binding.progress.setVisibility(View.GONE);
            if (result == null)
                return;

            mainViewModel.getResultCustomerFromServer().setValue(null);

            //region Information Account From Server
            if (result.size() > 0) {
                String IMEI = Constant.getAndroidID(getActivity());
                List<AppDetail> Apps = result.get(0).getApps();
                CollectionUtils.filter(Apps, l -> l.getAppId().equals(Constant.APPLICATION_ID) && l.getIemi().equals(IMEI));

                if (Apps!=null && Apps.size() > 0) {
                    if (!Apps.get(0).getIsActive()) {
                        from = "disable";
                        updateApplicationDialog.showDialog(getActivity(), "با این تلفن نمیتوانید وارد نرم افزار شوید.", false, "خروج", "", false, false, true);
                        return;
                    }

                    //region Save the user's State and guild in the mobile database to apply the filter in the application
                    if (Select.from(Guild.class).list().size() == 0 && Apps.get(0).getGuildId() != null) {
                        Guild guild = new Guild();
                        guild.setI(Apps.get(0).getGuildId());
                        guild.setName(Apps.get(0).getGuildName());
                        Guild.saveInTx(guild);
                    } else if (Apps.get(0).getGuildId() == null)
                        Guild.deleteAll(Guild.class);


                    if (Select.from(State.class).list().size() == 0 && Apps.get(0).getStateId() != null) {
                        State state = new State();
                        state.setI(Apps.get(0).getStateId());
                        state.setName(Apps.get(0).getStateName());
                        State.saveInTx(state);
                    } else if (Apps.get(0).getStateId() == null)
                        State.deleteAll(State.class);


                    //endregion Save the user's State and guild in the mobile database to apply the filter in the application

                    //region When the user's State and guild are removed from the database, we save them back in the database using this information
                    account.setStateId(Apps.get(0).getStateId());
                    account.setStateName(Apps.get(0).getStateName());
                    account.setGuildId(Apps.get(0).getGuildId());
                    account.setGuildName(Apps.get(0).getGuildName());
                    account.setCityId(Apps.get(0).getCityId());
                    account.setCityName(Apps.get(0).getCityName());
                    account.setShn(Apps.get(0).getShopName());
                    //endregion When the user's State and guild are removed from the database, we save them back in the database using this information

                    account.save();

                }
                else
                    clearData();
            } else
                clearData();

            checkUpdate();
            //endregion Information Account From Server


        });


    }




    //endregion Override Method


    //region Method
    private void setUpTimer() {
        new Handler().postDelayed(() -> {
            Account account = Select.from(Account.class).first();
            NavDirections action;

            if (account == null)
                //farzane change
                action = SplashScreenFragmentDirections.actionGoToIntroFragment();

            else if (account != null && account.getCityId() == null || account.getStateId() == null || account.getGuildId() == null || account.getShn().equals(""))
                action = SplashScreenFragmentDirections.actionGoToFilterFragment(account.getM(),"splash");
            else
                action = SplashScreenFragmentDirections.actionGoToHomeFragment();


            if (account != null) {
                account.setTab(2);
                account.save();
            }
            Navigation.findNavController(binding.getRoot()).navigate(action);


        }, 1500);


    }

    private void configToasty() {
        Toasty.Config.getInstance()
                .setToastTypeface(Typeface.createFromAsset(getActivity().getAssets(), "iransans.ttf"))
                .allowQueue(false)
                .apply();
    }

    private void checkUpdate() {
        Account account = Select.from(Account.class).first();

        if (forcedUpdate && !newVersion.equals("") && !appVersion.equals(newVersion)) {
            from = "update";
            updateApplicationDialog.showDialog(getActivity(), titleUpdate, false, "بستن", "آپدیت", false, true, false);

        }
        else if (!forcedUpdate && !newVersion.equals("") && !appVersion.equals(newVersion)) {
            from = "update";
            updateApplicationDialog.showDialog(getActivity(), titleUpdate, true, "بعدا", "آپدیت", false, true, true);

        }

        else if (account != null && account.getVersion()!=null && !account.getVersion().equals(newVersion)) {

            if (!messageUpdate.equals("")) {
                from = "showMessage";
                updateApplicationDialog.showDialog(getActivity(), messageUpdate, false, "", "بستن", false, true, false);
            }

            else  {
                account.setVersion(appVersion);
                account.save();
                setUpTimer();
            }
        }
        else
            setUpTimer();
    }

    private void clearData() {
        sharedPreferences.edit().clear().apply();
        Account.deleteAll(Account.class);
        BusinessR.deleteAll(BusinessR.class);
        City.deleteAll(City.class);
        Company.deleteAll(Company.class);
        Customer.deleteAll(Customer.class);
        Files.deleteAll(Files.class);
        Guild.deleteAll(Guild.class);
        ModelCatalog.deleteAll(ModelCatalog.class);
        ModelCatalogPageItem.deleteAll(ModelCatalogPageItem.class);
        Ord.deleteAll(Ord.class);
        OrdDetail.deleteAll(OrdDetail.class);
        State.deleteAll(State.class);
    }

    private void init() {
        sharedPreferences.edit().remove("storeCompany").apply();
        Company.deleteAll(Company.class);
        account = Select.from(Account.class).first();

        //If the user has never logged in application
        if (account != null && account.getVersion() == null) {
            Account.deleteAll(Account.class);
        }
        else if (account==null){
            Guild.deleteAll(Guild.class);
            State.deleteAll(State.class);
            City.deleteAll(City.class);
        }


        getApplicationVersion();
        initDialogError();
        initUpdateApplicationDialog();
    }

    private void initUpdateApplicationDialog() {
        updateApplicationDialog = CustomDialog.getInstance();
        updateApplicationDialog.setOnClickPositiveButton(() -> {
            if (from.equals("update")) {
                if (!linkUpdate.equals("")) {
                    Uri uri = Uri.parse(linkUpdate);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            } else {
                updateApplicationDialog.hideProgress();
                Account account = Select.from(Account.class).first();
                if (account != null) {
                    account.setVersion(appVersion);
                    account.save();
                }
                setUpTimer();
            }

        });
        updateApplicationDialog.setOnClickNegativeButton(() ->
                {
                    if (from.equals("update")) {
                        setUpTimer();
                        updateApplicationDialog.hideProgress();
                    } else
                        getActivity().finish();
                }
        );
    }

    private void getApplicationVersion() {
        try {
            appVersion = Constant.appVersion(getActivity());
            binding.tvVersion.setText("نسخه " + appVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initDialogError() {
        errorDialog = ErrorDialog.getInstance();
        errorDialog.setOnClickListener(() -> {
            binding.progress.setVisibility(View.VISIBLE);
            mainViewModel.getApp(Constant.APPLICATION_CODE);
        });
    }






    /*Navigation.findNavController(getView()).clearBackStack()*/
    //endregion Method
}
