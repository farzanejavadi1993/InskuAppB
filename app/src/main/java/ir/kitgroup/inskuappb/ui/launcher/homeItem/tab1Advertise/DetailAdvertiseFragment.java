package ir.kitgroup.inskuappb.ui.launcher.homeItem.tab1Advertise;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.text.LineBreaker;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orm.query.Select;

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


    @Inject
    SharedPreferences sharedPreferences;

    //region Parameter
    private String link = "";
    private DetailAdvertiseFragmentBinding binding;
    private MainViewModel mainViewModel;
    private String guid = "";
    private boolean save = false;

    private Account account;

    private CustomSnackBar snackBar;
    private ArrayList<Advertise> advertises;
    private Filters doFilter;
    private WebSite webSite;
    private String GuidCompany;
    private boolean want = false;

    //endregion Parameter

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DetailAdvertiseFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        guid = DetailAdvertiseFragmentArgs.fromBundle(getArguments()).getGuid();
        save = DetailAdvertiseFragmentArgs.fromBundle(getArguments()).getSave();
        advertises = new ArrayList<>();
        snackBar = new CustomSnackBar();
        doFilter = new Filters();
        webSite = new WebSite();
        binding.cardError3.setOnClickListener(view14 -> reloadDetail());

        //region Create Size
        account = Select.from(Account.class).first();
        //endregion Create Size

        Glide.with(getActivity())
                .load("http://" + Constant.Main_URL_IMAGE + "/GetAdvertisementImage?id=" +
                        guid + "&width=" + 600 + "&height=" + 400)
                .centerInside()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivImage);


        binding.ivBack.setOnClickListener(view1 -> Navigation.findNavController(binding.getRoot()).popBackStack());

        binding.cardPhone.setOnClickListener(view12 -> {
            sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + binding.tvPhone.getText().toString()));
            startActivity(intent);
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.tvDescription.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        binding.save.setOnClickListener(view13 -> {
            if (advertises.get(0).isSave())
                mainViewModel.deleteMyAdvertisement(account.getI(), advertises.get(0).getI());
            else
                mainViewModel.addToMyAdvertisement(account.getI(), advertises.get(0).getI());
        });


        binding.cardLink.setOnClickListener(view18 -> {

            if (!link.equals("")) {
                try {
                    webSite.check(getActivity(), link);
                    sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
                } catch (Exception ignored) {
                }
            }
        });

        binding.tvCompanyName.setOnClickListener(view15 -> {
            binding.progressCompany.setVisibility(View.VISIBLE);
            mainViewModel.getCompany(GuidCompany);
        });

        binding.tvWant.setOnClickListener(view16 -> {
                    binding.progressWant.setVisibility(View.VISIBLE);
                    binding.tvWant.setEnabled(false);
                    mainViewModel.setWannaAdvertisementStatus(account.getI(), guid, !want);
                }
        );


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.getResultMessage().setValue(null);


        binding.progressBar.setVisibility(View.VISIBLE);

        mainViewModel.getAdvertisement(guid);

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

            if (result.getCode()==12)
            binding.tvViewS.setText(String.valueOf(advertises.get(0).getCount()));


        });
        mainViewModel.getWannaAdvertisementStatus(account.getI(), guid);
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
                GuidCompany = advertises.get(0).getCompanyId();
                binding.tvCompanyName.setText(advertises.get(0).getCompanyName());
                if (advertises.get(0).getDType().get1() == null && advertises.get(0).getDType().get2() == null && advertises.get(0).getDType().get2() == null)
                    binding.tvType.setVisibility(View.GONE);
                else
                    binding.tvType.setText("این طرح شامل " +
                            advertises.get(0).getDType().get1() != null ?
                            advertises.get(0).getDType().get1() :
                            advertises.get(0).getDType().get2() != null ?
                                    advertises.get(0).getDType().get2() :
                                    advertises.get(0).getDType().get3() + " می باشد. ");

                binding.tvTitle.setText(advertises.get(0).getTitle());
                binding.tvDescription.setText(advertises.get(0).getDescription());
                binding.tvStart.setText("تاریخ شروع : " + advertises.get(0).getStartDate());
                binding.tvExpire.setText("تاریخ انقضا : " + advertises.get(0).getExpirationDate());


                if (!advertises.get(0).getPhone().equals("") && advertises.get(0).getPhone() != null) {
                    binding.vi1.setVisibility(View.VISIBLE);
                    binding.cardPhone.setVisibility(View.VISIBLE);
                    binding.tvPhone.setText(advertises.get(0).getPhone());
                }
                if (!advertises.get(0).getLink().equals("") && advertises.get(0).getLink() != null) {
                    binding.vi1.setVisibility(View.VISIBLE);
                    binding.cardLink.setVisibility(View.VISIBLE);
                    binding.tvLink.setText(advertises.get(0).getLink());
                    link = advertises.get(0).getLink();
                }


                binding.mainLayout.setVisibility(View.VISIBLE);
                //region Visit Advertise

                VisitAdvertisement visitAdvertisement = new VisitAdvertisement();
                visitAdvertisement.setCustomerId(account.getI());
                visitAdvertisement.setAdvertisementId(advertises.get(0).getI());
                visitAdvertisement.setId(UUID.randomUUID().toString());


                ArrayList<VisitAdvertisement> list = new ArrayList<>();
                Constant.JsonObjectVisitAdvertisement jsonObjectA = new Constant.JsonObjectVisitAdvertisement();
                list.add(visitAdvertisement);
                jsonObjectA.VisitAdvertisement = list;

                mainViewModel.CreateVisitAdvertisement(jsonObjectA);


                if (save) {
                    advertises.get(0).setSave(true);
                    binding.ivSave.setImageResource(R.drawable.ic_complete_bookmark);
                }

                //endregion Visit Advertise
            }

            binding.progressBar.setVisibility(View.GONE);


        });
        mainViewModel.getResultCreateVisitAdvertise().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            mainViewModel.getResultCreateVisitAdvertise().setValue(null);

            int count=advertises.get(0).getCount();

            if (result.size()>0 && result.get(0).getMessage() == 1) {
                count=count+1;
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
                NavDirections action = DetailAdvertiseFragmentDirections.actionGoToDetailFragment();
                Navigation.findNavController(binding.getRoot()).navigate(action);


            }


        });
        mainViewModel.getResultAddMyAdvertisement().observe(getViewLifecycleOwner(), result -> {


            if (result != null) {
                mainViewModel.getResultAddMyAdvertisement().setValue(null);
                if (result.get(0).getMessage() == 1) {
                    advertises.get(0).setSave(true);
                    binding.ivSave.setImageResource(R.drawable.ic_complete_bookmark);

                } else
                    ShowMessageWarning("خطا در ذخیره آگهی");

            }
            binding.progressSave.setVisibility(View.GONE);
        });
        mainViewModel.getResultDeleteMyAdvertisement().observe(getViewLifecycleOwner(), result -> {

            if (result != null) {
                mainViewModel.getResultDeleteMyAdvertisement().setValue(null);
                if (result.get(0).getMessage() == 1) {
                    advertises.get(0).setSave(false);
                    binding.ivSave.setImageResource(R.drawable.ic_bookmark);

                } else
                    ShowMessageWarning("خطا در حذف آگهی");

                binding.progressSave.setVisibility(View.GONE);

            }
        });
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
        mainViewModel.getAdvertisement(guid);
    }


    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.edit().putBoolean("loginSuccess", true).apply();
    }
}
