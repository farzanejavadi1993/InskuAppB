package ir.kitgroup.inskuappb.ui.launcher.profileitem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;


import com.orm.query.Select;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;

import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.classes.CustomDialog;

import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.classes.share.ShareApp;
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
import ir.kitgroup.inskuappb.databinding.MoreFragmentBinding;
import ir.kitgroup.inskuappb.util.Constant;


@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    @Inject
    SharedPreferences sharedPreferences;


    private Account account;
    private MoreFragmentBinding binding;
    private CustomDialog customDialog;
    private String linkUpdate = "";
    private MainViewModel myViewModel;
    private CustomSnackBar snackBar;


    private String IMEI = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MoreFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        IMEI = Constant.getAndroidID(getActivity());
        snackBar = new CustomSnackBar();
        linkUpdate = sharedPreferences.getString("link_update", "");


        customDialog = CustomDialog.getInstance();

        customDialog.setOnClickNegativeButton(() -> customDialog.hideProgress());

        customDialog.setOnClickPositiveButton(() -> {
            customDialog.hideProgress();
            myViewModel.setFirebaseToken(Constant.APPLICATION_ID, account.getI(), IMEI,
                    "");
        });

        account = Select.from(Account.class).first();
        account.setTab(2);
        account.save();
        binding.tvName.setText(account.getN() + " " + account.getLn());
        binding.tvMobile.setText(account.getM());


        binding.cardSaveAdvertisement.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putString("companyId", "");
            bundle.putString("guid", "");
            bundle.putString("companyName", " نشان شده");
            Navigation.findNavController(getView()).navigate(R.id.CompanyAdvertise, bundle);
        });


        binding.cardRequestCall.setOnClickListener(view15 -> {
            Bundle bundle=new Bundle();
            Navigation.findNavController(getView()).navigate(R.id.CallMeRequest);
        });

        binding.cardRequestAdvertisement.setOnClickListener(view15 -> {
            Bundle bundle=new Bundle();
            Navigation.findNavController(getView()).navigate(R.id.WantAdvFragment);
        });


        binding.cardpower.setOnClickListener(view12 -> {
            customDialog.showDialog(getActivity(), "آیا مایل به خروج از حساب کاربری خود می باشد؟", true, "خیر", "بله", false, true, true);
        });



        binding.shareApp.setOnClickListener(view13 -> {
            sharedPreferences.edit().putBoolean("loginSuccess",false).apply();
            shareApplication();
        });

        binding.detailProfile.setOnClickListener(view14 -> {
            NavDirections action =  ProfileFragmentDirections.actionGoToFilterFragment("","profile");
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });


        binding.detailProfile.setOnClickListener(view14 -> {
            NavDirections action =  ProfileFragmentDirections.actionGoToFilterFragment("","profile");
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });



        binding.cardContactUs.setOnClickListener(view14 -> {
            NavDirections action =  ProfileFragmentDirections.actionGoToContactUsFragment();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });


        binding.cardStar.setOnClickListener(view18 -> {
            sharedPreferences.edit().putBoolean("loginSuccess", false).apply();


                try {

                    Intent commentIntent = new Intent(Intent.ACTION_EDIT);
                    commentIntent.setData(Uri
                            .parse("bazaar://details?id=" +
                                    "ir.kitgroup.inskuappb"));
                    commentIntent.setPackage("com.farsitel.bazaar");
                    startActivity(commentIntent);
                } catch (Exception ignored) {

                    Toasty.warning(getActivity(),
                            "مشکلی رخ داد، " + "\n" +
                                    "برای نظر دادن بایستی نرم افزار بازار در دستگاه شما نصب باشد.",
                            Toast.LENGTH_LONG).show();
                }

        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        myViewModel.getResultMessage().setValue(null);


        myViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                ShowMessageWarning(result.getDescription());
            }
        });

        myViewModel.getResultFirebaseToken().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;

            myViewModel.getResultCustomerFromServer().setValue(null);
            customDialog.hideProgress();
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
            getActivity().finish();
            getActivity().startActivity(getActivity().getIntent());
        });


    }

    private void shareApplication() {
        ShareApp shareApp = new ShareApp();
        shareApp.onShare(getActivity(), account.getN(), account.getLn(), "", linkUpdate);


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
        sharedPreferences.edit().putBoolean("loginSuccess",true).apply();
    }




}
