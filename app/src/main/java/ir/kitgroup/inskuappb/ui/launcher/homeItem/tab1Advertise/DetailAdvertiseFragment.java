package ir.kitgroup.inskuappb.ui.launcher.homeItem.tab1Advertise;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.text.LineBreaker;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.classes.filterr.Filters;
import ir.kitgroup.inskuappb.classes.socialMedia.WebSite;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Files;
import ir.kitgroup.inskuappb.databinding.DetailAdvertiseFragmentBinding;
import ir.kitgroup.inskuappb.model.Advertise;
import ir.kitgroup.inskuappb.model.VisitAdvertisement;
import ir.kitgroup.inskuappb.util.Constant;

@AndroidEntryPoint
public class DetailAdvertiseFragment extends Fragment {
    //region Parameter
    @Inject
    SharedPreferences sharedPreferences;
    private DetailAdvertiseFragmentBinding binding;
    private MainViewModel mainViewModel;
    private String advGuid = "";
    private boolean save = false;
    private String link = "";
    private Account account;
    private CustomSnackBar snackBar;
    private ArrayList<Advertise> advertises;
    private Filters doFilter;
    private WebSite webSite;
    private String guidCompany;
    private boolean want = false;
    //endregion Parameter

    //region Override Method
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DetailAdvertiseFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();
        init();
        setAdvertiseImage();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        mainViewModel.getResultMessage().setValue(null);
        mainViewModel.getResultAdvertisementStatus().setValue(null);
        mainViewModel.getResultSetAdvertisementStatus().setValue(null);
        mainViewModel.getResultAdvertisement().setValue(null);
        mainViewModel.getResultCreateVisitAdvertise().setValue(null);
        mainViewModel.getResultCompany().setValue(null);
        mainViewModel.getResultAddMyAdvertisement().setValue(null);
        mainViewModel.getResultDeleteMyAdvertisement().setValue(null);


        binding.progressBar.setVisibility(View.VISIBLE);
        mainViewModel.getAdvertisement(advGuid);
        mainViewModel.getWannaAdvertisementStatus(account.getI(), advGuid);


        mainViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            binding.tvWant.setEnabled(true);
            binding.progressWant.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
            binding.progressCompany.setVisibility(View.GONE);
            if (result.getCode() == 1) {
                binding.tvError3.setText(result.getDescription());
                binding.cardError3.setVisibility(View.VISIBLE);
            } else
                ShowMessageWarning(result.getDescription());

            if (result.getCode() == 12)
                binding.tvViewS.setText(String.valueOf(advertises.get(0).getCount()));

            binding.save.setEnabled(true);

        });

        mainViewModel.getResultAdvertisementStatus().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            mainViewModel.getResultAdvertisementStatus().setValue(null);

            binding.vi1.setVisibility(View.VISIBLE);
            binding.cardWant.setVisibility(View.VISIBLE);

            if (result.size() > 0 && result.get(0).getStatuswana()) {
                want = true;
                binding.tvWant.setBackgroundResource(R.drawable.deactive_button);
                binding.tvWant.setText("نمیخوام");
            }

        });

        mainViewModel.getResultSetAdvertisementStatus().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            binding.progressWant.setVisibility(View.GONE);
            mainViewModel.getResultSetAdvertisementStatus().setValue(null);

            binding.tvWant.setEnabled(true);
            if (result.size() > 0) {
                if (result.get(0).getStatuswana()) {
                    want = true;
                    binding.tvWant.setBackgroundResource(R.drawable.deactive_button);
                    binding.tvWant.setText("نمیخوام");

                } else {
                    want = false;
                    binding.tvWant.setBackgroundResource(R.drawable.green_button);
                    binding.tvWant.setText("میخوام");

                }

            }

        });

        mainViewModel.getResultAdvertisement().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            mainViewModel.getResultAdvertisement().setValue(null);

            advertises.clear();
            advertises.addAll(result);

            if (advertises.size() > 0) {
                guidCompany = advertises.get(0).getCompanyId();
                String companyName = advertises.get(0).getCompanyName();
                String companyId = advertises.get(0).getCompanyId();
                String dType = advertises.get(0).getdTypeText();
                String title = advertises.get(0).getTitle();
                String description = advertises.get(0).getDescription();
                String startDate = advertises.get(0).getStartDate();
                String expirationDate = advertises.get(0).getExpirationDate();
                String phone = advertises.get(0).getPhone();
                String linkAdv = advertises.get(0).getLink();

                if (dType.equals(""))
                    binding.tvType.setVisibility(View.GONE);
                else
                    binding.tvType.setText(dType);


                binding.tvTitle.setText(title);
                binding.tvCompanyName.setText(companyName);
                binding.tvDescription.setText(description);
                binding.tvStart.setText("تاریخ شروع : " + startDate);
                binding.tvExpire.setText("تاریخ انقضا : " + expirationDate);



                if (!phone.equals("")) {
                    binding.vi1.setVisibility(View.VISIBLE);
                    binding.cardPhone.setVisibility(View.VISIBLE);
                    binding.tvPhone.setText(phone);
                }

                if (!linkAdv.equals("")) {
                    String html = "<a href=" + linkAdv + ">مشاهده سایت</a>";
                    binding.tvLink.setText(Html.fromHtml(html));
                    binding.vi1.setVisibility(View.VISIBLE);
                    binding.cardLink.setVisibility(View.VISIBLE);
                    link = linkAdv;
                }

//                Picasso.get()
//                        .load("http://" + Constant.Main_URL_IMAGE + "/GetCompanyImage?id=" +
//                                companyId + "&width=120&height=120")
//                        .error(R.drawable.loading)
//                        .placeholder(R.drawable.loading)
//                        .into(binding.ivCompanyAd);

                binding.mainLayout.setVisibility(View.VISIBLE);

                //region Visit Advertise
                VisitAdvertisement visitAdvertisement = new VisitAdvertisement();

                visitAdvertisement.setCustomerId(account.getI());
                visitAdvertisement.setAdvertisementId(advGuid);
                visitAdvertisement.setId(UUID.randomUUID().toString());

                ArrayList<VisitAdvertisement> list = new ArrayList<>();
                Constant.JsonObjectVisitAdvertisement jsonObjectA = new Constant.JsonObjectVisitAdvertisement();
                list.add(visitAdvertisement);
                jsonObjectA.VisitAdvertisement = list;
                mainViewModel.CreateVisitAdvertisement(jsonObjectA);
                //endregion Visit Advertise
            }

            binding.progressBar.setVisibility(View.GONE);
        });

        mainViewModel.getResultCreateVisitAdvertise().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            mainViewModel.getResultCreateVisitAdvertise().setValue(null);

            int count = advertises.get(0).getCount();

            if (result.size() > 0 && result.get(0).getMessage() == 1) {
                count = count + 1;
                sharedPreferences.edit().putInt("view", count).apply();
            }

            binding.tvViewS.setText(String.valueOf(count));
        });

        mainViewModel.getResultCompany().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            binding.progressCompany.setVisibility(View.GONE);
            mainViewModel.getResultCompany().setValue(null);

            if (result.size() > 0) {


                Company.deleteAll(Company.class);
                Company.saveInTx(result.get(0));
                Files.deleteAll(Files.class);
                Files.saveInTx(result.get(0).getFiles());
                Navigation.findNavController(getView()).navigate(R.id.DetailCompanyFragment);


            }


        });

        mainViewModel.getResultAddMyAdvertisement().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            mainViewModel.getResultAddMyAdvertisement().setValue(null);

            if (result.get(0).getMessage() == 1) {
                sharedPreferences.edit().putBoolean("save", true).apply();
                save = true;
                binding.ivSave.setImageResource(R.drawable.ic_complete_bookmark);
            } else
                ShowMessageWarning("خطا در ذخیره آگهی");

            binding.save.setEnabled(true);
            binding.progressSave.setVisibility(View.GONE);
        });

        mainViewModel.getResultDeleteMyAdvertisement().observe(getViewLifecycleOwner(), result -> {

            if (result == null) return;
            mainViewModel.getResultDeleteMyAdvertisement().setValue(null);
            if (result.get(0).getMessage() == 1) {
                sharedPreferences.edit().putBoolean("save", false).apply();
                save = false;
                binding.ivSave.setImageResource(R.drawable.ic_bookmark);
            } else
                ShowMessageWarning("خطا در حذف آگهی");

            binding.save.setEnabled(true);
            binding.progressSave.setVisibility(View.GONE);


        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.edit().putBoolean("loginSuccess", true).apply();
    }
    //endregion Override Method

    //region Custom Method
    private void getBundle() {
        advGuid = DetailAdvertiseFragmentArgs.fromBundle(getArguments()).getGuid();
        save = DetailAdvertiseFragmentArgs.fromBundle(getArguments()).getSave();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void init() {

        sharedPreferences.edit().remove("view").apply();
        sharedPreferences.edit().remove("save").apply();

        advertises = new ArrayList<>();
        snackBar = new CustomSnackBar();
        doFilter = new Filters();
        webSite = new WebSite();
        account = Select.from(Account.class).first();

        if (save)
            binding.ivSave.setImageResource(R.drawable.ic_complete_bookmark);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.tvDescription.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        binding.ivBack.setOnClickListener(view1 -> Navigation.findNavController(binding.getRoot()).popBackStack());

        binding.cardError3.setOnClickListener(view14 -> reloadDetail());

        binding.cardPhone.setOnClickListener(view12 -> {
            sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + binding.tvPhone.getText().toString()));
            startActivity(intent);
        });

        binding.save.setOnClickListener(view13 -> {
            binding.save.setEnabled(false);
            if (save)
                mainViewModel.deleteMyAdvertisement(account.getI(), advertises.get(0).getI());
            else
                mainViewModel.addToMyAdvertisement(account.getI(), advertises.get(0).getI());
        });

        binding.cardLink.setOnClickListener(view18 -> {
            if (!link.equals("")) {
                try {
                    webSite.check(getActivity(), link);
                    sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
                } catch (Exception ignored) {}
            }
        });

        binding.tvCompanyName.setOnClickListener(view15 -> {
            binding.progressCompany.setVisibility(View.VISIBLE);
            mainViewModel.getCompany(guidCompany);
        });

        binding.tvWant.setOnClickListener(view16 -> {
            binding.progressWant.setVisibility(View.VISIBLE);
            binding.tvWant.setEnabled(false);
            mainViewModel.setWannaAdvertisementStatus(account.getI(), advGuid, !want);
        });
    }

    private void setAdvertiseImage() {
        Glide.with(getActivity())
                .load("http://" + Constant.Main_URL_IMAGE + "/GetAdvertisementImage?id=" +
                        advGuid + "&width=" + 600 + "&height=" + 400)
                .centerInside()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivImage);
    }

    private void ShowMessageWarning(String error) {
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }
        snackBar.showSnack(getActivity(), binding.getRoot(), error);

    }

    private void reloadDetail() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.mainLayout.setVisibility(View.GONE);
        binding.cardError3.setVisibility(View.GONE);
        mainViewModel.getAdvertisement(advGuid);
    }
    //endregion Custom Method

}
