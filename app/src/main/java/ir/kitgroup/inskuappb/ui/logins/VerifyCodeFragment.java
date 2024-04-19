package ir.kitgroup.inskuappb.ui.logins;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;


import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import ir.kitgroup.inskuappb.ui.viewmodel.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.component.CustomDialog;

import ir.kitgroup.inskuappb.component.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.dataBase.Account;

import ir.kitgroup.inskuappb.dataBase.Guild;
import ir.kitgroup.inskuappb.dataBase.State;
import ir.kitgroup.inskuappb.databinding.VerifyCodeFragmentBinding;
import ir.kitgroup.inskuappb.data.model.AppDetail;
import ir.kitgroup.inskuappb.service.AppSMSBroadcastReceiver;
import ir.kitgroup.inskuappb.util.Constant;

@AndroidEntryPoint
public class VerifyCodeFragment extends Fragment {

    //region Parameter
    @Inject
    Typeface typeface;

    private CustomSnackBar snackBar;
    private VerifyCodeFragmentBinding binding;
    private String code;
    private String phoneNumber;
    private MainViewModel myViewModel;
    private String appVersion = "";
    private String IMEI = "";
    private CustomDialog customDialog;
    private List<Account> accountsList;

    private AppSMSBroadcastReceiver appSMSBroadcastReceiver;

    //endregion Parameter


    //region Override Method
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = VerifyCodeFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    private void initBroadCast() {

        appSMSBroadcastReceiver = new AppSMSBroadcastReceiver();
        appSMSBroadcastReceiver.setOnSmsReceiveListener(code -> {

            binding.etOtp.setText(code);

            new android.os.Handler(Looper.getMainLooper()).postDelayed(
                    () -> binding.btnEnter.performClick(),
                    600);
        });
    }

    private void smsListener() {
        SmsRetrieverClient client = SmsRetriever.getClient(getContext());
        Task<Void> task = client.startSmsRetriever();

        task.addOnSuccessListener(aVoid -> {
        });

        task.addOnFailureListener(e -> {
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


        init();

        binding.ivBack.setOnClickListener(view1 ->
                Navigation.findNavController(binding.getRoot()).popBackStack()
        );
        binding.btnEnter.setOnClickListener(view1 -> {

            if (Objects.requireNonNull(binding.etOtp.getText()).toString().equals(code)) {

                binding.progressBar.setVisibility(View.VISIBLE);
                binding.btnEnter.setBackgroundColor(getResources().getColor(R.color.bottom_background_inActive_color));
                binding.btnEnter.setEnabled(false);
                myViewModel.getCustomerFromServer(phoneNumber);
            } else {
                ShowMessageWarning("کد وارد شده نامعتبر است.");
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        myViewModel.getResultMessage().setValue(null);

        myViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            binding.progressBar.setVisibility(View.GONE);
            binding.btnEnter.setBackgroundColor(getResources().getColor(R.color.color_primary_dark));
            binding.btnEnter.setEnabled(true);
            ShowMessageWarning(result.getDescription());

        });
        myViewModel.getResultCustomerFromServer().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;

            myViewModel.getResultCustomerFromServer().setValue(null);

            //region Account Is Register
            if (result.size() > 0) {
                List<AppDetail> Apps = result.get(0).getApps();
                CollectionUtils.filter(Apps, l -> l.getAppId().equals(Constant.APPLICATION_ID) && l.getIemi().equals(IMEI));
                if (Apps.size() > 0) {

                    //region Save the user's State and guild in the mobile database to apply the filter in the application
                    if (Apps.get(0).getGuildId() != null) {
                        Guild guild = new Guild();
                        guild.setI(Apps.get(0).getGuildId());
                        guild.setName(Apps.get(0).getGuildName());
                        Guild.saveInTx(guild);
                    } else
                        Guild.deleteAll(Guild.class);


                    if (Apps.get(0).getStateId() != null) {
                        State state = new State();
                        state.setI(Apps.get(0).getStateId());
                        state.setName(Apps.get(0).getStateName());
                        State.saveInTx(state);
                    } else
                        State.deleteAll(State.class);


                    //endregion Save the user's State and guild in the mobile database to apply the filter in the application


                    //region When the user's State and guild are removed from the database, we save them back in the database using this information
                    result.get(0).setStateId(Apps.get(0).getStateId());
                    result.get(0).setStateName(Apps.get(0).getStateName());
                    result.get(0).setGuildId(Apps.get(0).getGuildId());
                    result.get(0).setGuildName(Apps.get(0).getGuildName());
                    result.get(0).setCityId(Apps.get(0).getCityId());
                    result.get(0).setCityName(Apps.get(0).getCityName());
                    result.get(0).setShn(Apps.get(0).getShopName());
                    //endregion When the user's State and guild are removed from the database, we save them back in the database using this information

                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnEnter.setBackgroundColor(getResources().getColor(R.color.color_primary_dark));
                    binding.btnEnter.setEnabled(true);
                    Account.deleteAll(Account.class);

                    NavDirections action;
                    if (Apps.get(0).getCityId() == null || Apps.get(0).getGuildId() == null || Apps.get(0).getShopName().equals(""))
                        action = VerifyCodeFragmentDirections.actionGoToFilterFragment(phoneNumber, "verify");
                    else {
                        result.get(0).setVersion(appVersion);
                        action = VerifyCodeFragmentDirections.actionGoToHomeFragment();
                    }

                    Account.saveInTx(result);
                    Navigation.findNavController(binding.getRoot()).navigate(action);


                } else {
                    result.get(0).setImei(IMEI);
                    result.get(0).setAppId(Constant.APPLICATION_ID);
                    accountsList.clear();
                    accountsList.addAll(result);

                    //region Create JsonAccount
                    Constant.JsonObjectAccount jsonObjectAcc = new Constant.JsonObjectAccount();
                    jsonObjectAcc.Account = accountsList;
                    myViewModel.addAccountToServer(jsonObjectAcc);
                    //endregion Create JsonAccount
                }
            }
            //endregion Account Is Register


            //region Account Is Not Register
            else {
                NavDirections action = VerifyCodeFragmentDirections.actionGoToFilterFragment(phoneNumber, "verify");
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
            //endregion Account Is Not Register


        });
        myViewModel.getResultAddAccount().observe(getViewLifecycleOwner(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnEnter.setBackgroundColor(getResources().getColor(R.color.color_primary_dark));
            binding.btnEnter.setEnabled(true);

            if (result == null)
                return;

            myViewModel.getResultAddAccount().setValue(null);

            if (result.get(0).getMessage() == 1) {
                Account.deleteAll(Account.class);
                accountsList.get(0).setI(result.get(0).getCurrent());

                NavDirections action;
                if (accountsList.get(0).getCityId() == null || accountsList.get(0).getStateId() == null || accountsList.get(0).getStateId() == null || accountsList.get(0).getStateName().equals("")) {
                    action = VerifyCodeFragmentDirections.actionGoToFilterFragment(phoneNumber, "verify");
                } else {
                    accountsList.get(0).setVersion(appVersion);
                    action = VerifyCodeFragmentDirections.actionGoToHomeFragment();
                }
                Account.saveInTx(accountsList);
                Navigation.findNavController(binding.getRoot()).navigate(action);


            } else
                ShowMessageWarning(result.get(0).getDescription());

        });

    }
    //endregion Override Method


    //region Method
    private void setTextFontToTvMessage() {
        // Set Text Font To Special Part Of TextRule(پخش یاب)
        String description1 = "لطفا کدی که از طرف پخش یاب برای " + phoneNumber + " ارسال شده را وارد کنید. ";
        Constant.setTextFontToSpecialPart(description1, typeface, 19, 26, Color.BLACK, binding.tvMessage);

    }

    private void ShowMessageWarning(String error) {
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }
        snackBar.showSnack(getActivity(), binding.getRoot(), error);

    }

    private void init() {
        getBundle();
        setTextFontToTvMessage();
        getCodeFromSms();
        initCustomDialog();
        getAppVersion();
        configEditText();
        snackBar = new CustomSnackBar();
        accountsList = new ArrayList<>();
        IMEI = Constant.getAndroidID(getActivity());
    }

    private void getBundle() {
        code = VerifyCodeFragmentArgs.fromBundle(getArguments()).getCode();
        phoneNumber = VerifyCodeFragmentArgs.fromBundle(getArguments()).getPhoneNumber();

    }

    private void getCodeFromSms() {
        try {
            smsListener();
            initBroadCast();
        } catch (Exception ignored) {
        }

    }

    private void initCustomDialog() {
        customDialog = CustomDialog.getInstance();
        customDialog.hideProgress();
        customDialog.setOnClickNegativeButton(() -> {
            customDialog.hideProgress();
            getActivity().finish();
        });
    }

    private void getAppVersion() {
        try {
            appVersion = Constant.appVersion(getActivity());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void configEditText() {
        binding.etOtp.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.etOtp, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appSMSBroadcastReceiver.clearAbortBroadcast();

    }

    //endregion Method
}