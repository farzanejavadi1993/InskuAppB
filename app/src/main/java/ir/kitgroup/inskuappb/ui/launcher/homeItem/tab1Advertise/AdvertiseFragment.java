package ir.kitgroup.inskuappb.ui.launcher.homeItem.tab1Advertise;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.orm.query.Select;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.adapter.SliderAdapter;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.classes.EndlessParentScrollListener;
import ir.kitgroup.inskuappb.classes.SharedPrefrenceValue;
import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.classes.filterr.Filters;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Files;
import ir.kitgroup.inskuappb.dataBase.StoreChangeAdvertise;
import ir.kitgroup.inskuappb.databinding.AdvertiseFragmentBinding;
import ir.kitgroup.inskuappb.model.Advertise;
import ir.kitgroup.inskuappb.util.Constant;

@AndroidEntryPoint
public class AdvertiseFragment extends Fragment {

    //region Parameter
    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    SharedPrefrenceValue sharedPrefrenceValue;

    private AdvertiseFragmentBinding binding;
    private MainViewModel mainViewModel;


    private SliderAdapter adapterBanner;
    private final ArrayList<Advertise> bannerList = new ArrayList<>();
    private int height;


    private UniversalAdapter2 vipAdapter;
    private final ArrayList<Advertise> vipList = new ArrayList<>();


    private EndlessParentScrollListener endlessParentScrollListener;
    private UniversalAdapter2 simpleAdapter;
    private final ArrayList<Advertise> simpleList = new ArrayList<>();
    private int pageMain = 1;
    private ImageView imgSave;
    private ProgressBar progressSave;
    private ProgressBar progressCompany;
    private Filters doFilter;
    private int positionSelect = 0;
    private int oldSizeLadderList = 0;


    private CustomSnackBar snackBar;
    private Account account;

    private boolean firstSync = false;
    private boolean bannerSync = false;
    private boolean vipSync = false;
    private boolean sampleSync = false;

    private boolean savingAdv;
    //endregion Parameter

    //region Override Method
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AdvertiseFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initAnimation();
        initSpecial();
        initVip();
        initSimple();
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);


        getFirstRequest();

        mainViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            binding.animation.setVisibility(View.GONE);
            binding.progressBar22.setVisibility(View.GONE);

            if (result.getCode() == 1) {
                binding.tvError2.setText(result.getDescription());
                binding.cardError2.setVisibility(View.VISIBLE);
            } else
                ShowMessageWarning(result.getDescription());

            savingAdv=false;
        });

        mainViewModel.getResultBillboardAdvs().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            mainViewModel.getResultBillboardAdvs().setValue(null);

            bannerSync = true;

            bannerList.clear();

            if (result.size() > 0) {
                binding.cardSpecial.setVisibility(View.VISIBLE);
                int random = new Random(System.nanoTime()).nextInt(result.size());

                for (int i = random; i < result.size(); i++) {
                    bannerList.add(result.get(i));
                }

                for (int j = 0; j < random; j++) {
                    bannerList.add(result.get(j));
                }
            }

            adapterBanner.notifyDataSetChanged();
            binding.slider.setSliderAdapter(adapterBanner);

            completeSync();
        });

        mainViewModel.getResultVipAdvertisements().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            mainViewModel.getResultVipAdvertisements().setValue(null);
            vipSync = true;

            vipList.clear();
            if (result.size() > 0)
                vipList.addAll(result);

            vipAdapter.notifyDataSetChanged();
            completeSync();

        });

        mainViewModel.getResultSimpleAdvertisements().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;

            mainViewModel.getResultSimpleAdvertisements().setValue(null);
            sampleSync = true;


            if (pageMain == 1)
                simpleList.clear();

            binding.progressBar22.setVisibility(View.VISIBLE);
            binding.layoutNotFound.setVisibility(View.GONE);

            if (result.size() > 0)
                simpleList.addAll(result);

            else if (result.size() == 0 && simpleList.size() == 0 && vipList.size() == 0 && bannerList.size() == 0)
                binding.layoutNotFound.setVisibility(View.VISIBLE);



            visibleView();

            if (pageMain == 1)
                simpleAdapter.notifyDataSetChanged();
            else if (oldSizeLadderList <= simpleList.size())
                simpleAdapter.notifyItemRangeInserted(oldSizeLadderList, simpleList.size() - 1);

            binding.progressBar22.setVisibility(View.GONE);
            completeSync();
        });

        mainViewModel.getResultAddMyAdvertisement().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            mainViewModel.getResultAddMyAdvertisement().setValue(null);

            progressSave.setVisibility(View.GONE);
            progressCompany.setVisibility(View.GONE);

            if (result.get(0).getMessage() == 1) {
                simpleList.get(positionSelect).setIsSaved(true);
                imgSave.setImageResource(R.drawable.ic_complete_bookmark);
            } else
                ShowMessageWarning("خطا در ذخیره آگهی");

            savingAdv=false;

        });
        mainViewModel.getResultDeleteMyAdvertisement().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;
            mainViewModel.getResultDeleteMyAdvertisement().setValue(null);

            if (result.get(0).getMessage() == 1) {
                simpleList.get(positionSelect).setIsSaved(false);
                imgSave.setImageResource(R.drawable.ic_bookmark);
            } else
                ShowMessageWarning("خطا در حذف آگهی");

            progressSave.setVisibility(View.GONE);
            savingAdv=false;
        });

        mainViewModel.getResultCompany().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            mainViewModel.getResultCompany().setValue(null);

            progressCompany.setVisibility(View.GONE);
            if (result.size() > 0)
                navigateToDetailCompany(result.get(0));
        });
    }
    //endregion Override Method

    //region Method
    private void initAnimation() {
        binding.animation.setAnimation("animation.json");
        binding.animation.loop(true);
        binding.animation.setSpeed(2f);
        binding.animation.playAnimation();
        binding.animation.setVisibility(View.VISIBLE);

        binding.progressBar22.setAnimation("loading.json");
        binding.progressBar22.loop(true);
        binding.progressBar22.setSpeed(2f);
        binding.progressBar22.playAnimation();

    }
    private void initSpecial() {
        binding.cardSpecial.getLayoutParams().height = (int) (Constant.width * .544);

        adapterBanner = new SliderAdapter(getActivity(), bannerList);

        adapterBanner.setOnClickListener((advertise, position) -> {
            NavDirections action = AdvertiseFragmentDirections.actionGoToDetailAdvertiseFragment(advertise.getI());
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        binding.slider.setSliderAdapter(adapterBanner);
        binding.slider.setScrollTimeInSec(3);
        binding.slider.setIndicatorEnabled(true);
        binding.slider.setAutoCycle(true);
        binding.slider.startAutoCycle();

        if (bannerList.size() > 0)
            binding.cardSpecial.setVisibility(View.VISIBLE);
    }
    private void initVip() {
        vipAdapter = new UniversalAdapter2(R.layout.vip_advertise_item, vipList, BR.vip);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        manager.setReverseLayout(true);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getActivity());
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
        flexboxLayoutManager.setAlignItems(AlignItems.BASELINE);

        binding.recycleVipAdvertise.setLayoutManager(flexboxLayoutManager);
        binding.recycleVipAdvertise.setAdapter(vipAdapter);

        vipAdapter.setOnItemClickListener((binding, position) -> {
            Bundle bundle = new Bundle();
            bundle.putString("guid", vipList.get(position).getI());
            bundle.putString("companyId", vipList.get(position).getCompanyId());
            bundle.putString("companyName", vipList.get(position).getCompanyName());
            Navigation.findNavController(getView()).navigate(R.id.CompanyAdvertise, bundle);
        });
    }
    @SuppressLint("SetTextI18n")
    private void initSimple() {
        if (Constant.screenInches < 7) {
            LinearLayoutManager linearManger = new LinearLayoutManager(getActivity());

            if (endlessParentScrollListener == null) {
                endlessParentScrollListener = new EndlessParentScrollListener(linearManger) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        if (page + 1 <= pageMain)
                            return;
                        loadMore();
                    }
                };
            }
            binding.recycleSimpleAdvertise.setLayoutManager(linearManger);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);

            if (endlessParentScrollListener == null) {
                endlessParentScrollListener = new EndlessParentScrollListener(gridLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        if (page + 1 <= pageMain)
                            return;
                        loadMore();
                    }
                };
            }
            binding.recycleSimpleAdvertise.setLayoutManager(gridLayoutManager);
        }

        binding.nested.setOnScrollChangeListener(endlessParentScrollListener);

        simpleAdapter = new UniversalAdapter2(R.layout.simple_advertise_item, simpleList, BR.simple);

        binding.recycleSimpleAdvertise.setAdapter(simpleAdapter);

        simpleAdapter.setOnItemClickListener((bin, position) -> {
            sharedPrefrenceValue.saveValueInSharedPrefrence(simpleList.get(position).getI(), simpleList.get(position).getIsSaved(), simpleList.get(position).getCount());

            NavDirections action = AdvertiseFragmentDirections.actionGoToDetailAdvertiseFragment(simpleList.get(position).getI());
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        simpleAdapter.setOnItemBindListener((bind, position) -> {
            if (simpleList.size() > position && position >= 0) {
                View v = bind.getRoot();

                imgSave = v.findViewById(R.id.ivSave_simple);
                progressSave = v.findViewById(R.id.progress_save_simple);
                progressCompany = v.findViewById(R.id.progress_company);
                RelativeLayout cardCompany = v.findViewById(R.id.cardCompany);

                if (simpleList.get(position).getIsSaved())
                    imgSave.setImageResource(R.drawable.ic_complete_bookmark);
                else
                    imgSave.setImageResource(R.drawable.ic_bookmark);

                imgSave.setOnClickListener(view13 -> {
                    if (!savingAdv){
                        savingAdv=true;
                        progressSave = v.findViewById(R.id.progress_save_simple);
                        imgSave = v.findViewById(R.id.ivSave_simple);
                        progressSave.setVisibility(View.VISIBLE);

                        positionSelect = position;
                        if (simpleList.get(position).getIsSaved())
                            mainViewModel.deleteMyAdvertisement(account.getI(), simpleList.get(positionSelect).getI());
                        else
                            mainViewModel.addToMyAdvertisement(account.getI(), simpleList.get(positionSelect).getI());
                    }

                });


                cardCompany.setOnClickListener(view -> {
                    progressCompany = v.findViewById(R.id.progress_company);
                    progressCompany.setVisibility(View.VISIBLE);
                    mainViewModel.getCompany(simpleList.get(position).getCompanyId());
                });
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private void init() {
        savingAdv=false;
        height = (Constant.width / 3) * 2;
        doFilter = new Filters();
        account = Select.from(Account.class).first();

        initSnackBar();

        binding.cardError2.setOnClickListener(view -> reloadAdvertisement());

        binding.ISwipe.setOnRefreshListener(this::reloadAdvertisement);
    }
    @SuppressLint("NotifyDataSetChanged")
    private void reloadAdvertisement() {
        bannerSync = false;
        sampleSync = false;
        vipSync = false;
        pageMain = 1;
        binding.cardError2.setVisibility(View.GONE);
        simpleList.clear();
        vipList.clear();

        binding.v2.setVisibility(View.GONE);
        binding.layoutNotFound.setVisibility(View.GONE);
        try {
            vipAdapter.notifyDataSetChanged();
            simpleAdapter.notifyDataSetChanged();
        } catch (Exception ignored) {
        }
        binding.cardSpecial.setVisibility(View.GONE);
        bannerList.clear();
        binding.animation.setVisibility(View.VISIBLE);
        mainViewModel.getBillboardAdvs(doFilter.getAccountFilter(), Constant.APPLICATION_ID, account.getI());
        mainViewModel.getVipAdvertisements(doFilter.getAccountFilter(), Constant.APPLICATION_ID);
        mainViewModel.getSimpleAdvertisements(doFilter.getAccountFilter(), Constant.APPLICATION_ID, account.getI(), pageMain);
    }
    private void initSnackBar() {
        snackBar = new CustomSnackBar();
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }

    }
    private void loadMore() {
        oldSizeLadderList = simpleList.size();
        pageMain++;
        binding.progressBar22.setVisibility(View.VISIBLE);
        mainViewModel.getSimpleAdvertisements(doFilter.getAccountFilter(), Constant.APPLICATION_ID, account.getI(), pageMain);
    }
    private void useDataToSetInList(String storedHashMap) {
        List<StoreChangeAdvertise> storeChangeAdvertises = sharedPrefrenceValue.useDataToSetInList(storedHashMap);
        for (int i = 0; i < storeChangeAdvertises.size(); i++) {
            int finalI = i;
            int count = storeChangeAdvertises.get(finalI).getCount();
            boolean save = storeChangeAdvertises.get(finalI).isSave();
            ArrayList<Advertise> advertises = new ArrayList<>(simpleList);
            CollectionUtils.filter(advertises, a -> a.getI().equals(storeChangeAdvertises.get(finalI).getI()));
            if (advertises.size() > 0) {
                simpleList.get(simpleList.indexOf(advertises.get(0))).setIsSaved(save);
                simpleList.get(simpleList.indexOf(advertises.get(0))).setCount(count);
                simpleAdapter.notifyItemChanged(simpleList.indexOf(advertises.get(0)));
            }
        }
    }

    private void completeSync() {
        if (bannerSync && sampleSync && vipSync) {
            firstSync = true;
            binding.animation.setVisibility(View.GONE);
            binding.ISwipe.refreshComplete();
        }
    }
    private void visibleView(){
        if (simpleList.size() > 0 && vipList.size() > 0)
            binding.v2.setVisibility(View.VISIBLE);
    }
    private void nullTheMutable(){
        mainViewModel.getResultMessage().setValue(null);
        mainViewModel.getResultBillboardAdvs().setValue(null);
        mainViewModel.getResultVipAdvertisements().setValue(null);
        mainViewModel.getResultSimpleAdvertisements().setValue(null);
        mainViewModel.getResultAddMyAdvertisement().setValue(null);
        mainViewModel.getResultDeleteMyAdvertisement().setValue(null);
        mainViewModel.getResultCompany().setValue(null);
    }
    private void getFirstRequest(){
        if (!firstSync) {
            nullTheMutable();

            pageMain = 1;
            binding.animation.setVisibility(View.VISIBLE);

            mainViewModel.getBillboardAdvs(doFilter.getAccountFilter(), Constant.APPLICATION_ID, account.getI());
            mainViewModel.getVipAdvertisements(doFilter.getAccountFilter(), Constant.APPLICATION_ID);
            mainViewModel.getSimpleAdvertisements(doFilter.getAccountFilter(), Constant.APPLICATION_ID, account.getI(), pageMain);
        }
        else {
            visibleView();

            String storedHashMap = sharedPreferences.getString("storeHashMap", "");
            if (!storedHashMap.equals("")) {
                sharedPreferences.edit().remove("storeHashMap").apply();
                useDataToSetInList(storedHashMap);
            }
        }
    }
    private void navigateToDetailCompany(Company company){
        Company.deleteAll(Company.class);
        Company.saveInTx(company);
        Files.deleteAll(Files.class);
        Files.saveInTx(company.getFiles());
        Navigation.findNavController(getView()).navigate(R.id.DetailCompanyFragment);
    }
    //endregion Method
}