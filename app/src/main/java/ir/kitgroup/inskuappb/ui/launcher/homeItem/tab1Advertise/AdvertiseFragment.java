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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.adapter.SliderAdapter;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.classes.EndlessParentScrollListener;
import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.classes.filterr.Filters;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Files;
import ir.kitgroup.inskuappb.databinding.AdvertiseFragmentBinding;
import ir.kitgroup.inskuappb.model.Advertise;
import ir.kitgroup.inskuappb.util.Constant;


@AndroidEntryPoint
public class AdvertiseFragment extends Fragment {

    @Inject
    SharedPreferences sharedPreferences;
    private AdvertiseFragmentBinding binding;
    private MainViewModel mainViewModel;


    private final ArrayList<Advertise> allAdvertisement = new ArrayList<>();


    private SliderAdapter adapterSlider;

    private final ArrayList<Advertise> sliderList = new ArrayList<>();
    private int height;


    private UniversalAdapter2 limitAdapter;
    private final ArrayList<Advertise> limitList = new ArrayList<>();


    private EndlessParentScrollListener endlessParentScrollListener;
    private UniversalAdapter2 ladderAdapter;
    private final ArrayList<Advertise> ladderList = new ArrayList<>();
    private int pageMain = 1;
    private ImageView imgSave;
    private final int request = 0;
    private ProgressBar progressSave;
    private ProgressBar progressCompany;
    private Filters doFilter;
    private int positionSelect = 0;
    private int oldSizeLadderList = 0;


    private CustomSnackBar snackBar;
    private Account account;
    private final boolean requset = false;

    private boolean firstSync = false;


 /*   @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !LauncherFragment.fragment.equals("advertise")) {
            if (mainViewModel != null) {

            }
        }
    }*/


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
        initLimit();
        initLadder();
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
            mainViewModel.getResultMessage().setValue(null);
            mainViewModel.getResultSimpleAdvertisements().setValue(null);

            if (!firstSync) {
                binding.animation.setVisibility(View.VISIBLE);
                mainViewModel.getResultSimpleAdvertisements().setValue(null);
                mainViewModel.getBillboardAdvs(doFilter.getAccountFilter(), Constant.APPLICATION_ID, account.getI());
                mainViewModel.getVipAdvertisements(doFilter.getAccountFilter(), Constant.APPLICATION_ID);
                mainViewModel.getSimpleAdvertisements(doFilter.getAccountFilter(), Constant.APPLICATION_ID, account.getI(), pageMain);
            }
            else {
                String storedHashMap = sharedPreferences.getString("storeHashMap", "");
                if (!storedHashMap.equals("")) {
                    sharedPreferences.edit().remove("storeHashMap").apply();
                    useDataToSetInList(storedHashMap);
                }
            }


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
            });


            mainViewModel.getResultBillboardAdvs().observe(getViewLifecycleOwner(), result -> {
                if (result == null)
                    return;

                firstSync = true;
                mainViewModel.getResultBillboardAdvs().setValue(null);

                sliderList.clear();

                if (result.size() > 0) {
                    binding.cardSpecial.setVisibility(View.VISIBLE);
                    int random = new Random(System.nanoTime()).nextInt(result.size());

                    for (int i = random; i < result.size(); i++) {
                        sliderList.add(result.get(i));
                    }

                    for (int j = 0; j < random; j++) {
                        sliderList.add(result.get(j));
                    }
                }

                adapterSlider.notifyDataSetChanged();
                binding.slider.setSliderAdapter(adapterSlider);
            });

            mainViewModel.getResultVipAdvertisements().observe(getViewLifecycleOwner(), result -> {
                if (result == null)
                    return;

                firstSync = true;
                mainViewModel.getResultVipAdvertisements().setValue(null);

                limitList.clear();

                if (result.size() > 0)
                    limitList.addAll(result);

                limitAdapter.notifyDataSetChanged();

            });

            mainViewModel.getResultSimpleAdvertisements().observe(getViewLifecycleOwner(), result -> {

                if (result == null)
                    return;
                firstSync = true;
                mainViewModel.getResultSimpleAdvertisements().setValue(null);

                if (pageMain == 1)
                    ladderList.clear();


                if (result.size() > 0) {
                    ladderList.addAll(result);
                } else if (result.size() == 0 && ladderList.size() == 0 && limitList.size() == 0 && sliderList.size() == 0) {
                    binding.progressBar22.setVisibility(View.GONE);
                    binding.animation.setVisibility(View.GONE);
                    binding.layoutNotFound.setVisibility(View.VISIBLE);
                } else if (result.size() == 0)
                    binding.progressBar22.setVisibility(View.GONE);


                if (ladderList.size() > 0 && limitList.size() > 0)
                    binding.v2.setVisibility(View.VISIBLE);

                if (pageMain == 1)
                    ladderAdapter.notifyDataSetChanged();
                else if (oldSizeLadderList <= ladderList.size())
                    ladderAdapter.notifyItemRangeInserted(oldSizeLadderList, ladderList.size() - 1);

            });

            mainViewModel.getResultAddMyAdvertisement().observe(getViewLifecycleOwner(), result -> {

                if (result == null)
                    return;

                progressSave.setVisibility(View.GONE);
                progressCompany.setVisibility(View.GONE);
                mainViewModel.getResultAddMyAdvertisement().setValue(null);
                if (result.get(0).getMessage() == 1) {
                    ladderList.get(positionSelect).setIsSaved(true);
                    imgSave.setImageResource(R.drawable.ic_complete_bookmark);

                } else
                    ShowMessageWarning("خطا در ذخیره آگهی");


            });

            mainViewModel.getResultDeleteMyAdvertisement().observe(getViewLifecycleOwner(), result -> {

                if (result == null)
                    return;

                mainViewModel.getResultDeleteMyAdvertisement().setValue(null);
                if (result.get(0).getMessage() == 1) {
                    ladderList.get(positionSelect).setIsSaved(false);
                    imgSave.setImageResource(R.drawable.ic_bookmark);

                } else
                    ShowMessageWarning("خطا در حذف آگهی");

                progressSave.setVisibility(View.GONE);


            });

            mainViewModel.getResultCompany().observe(getViewLifecycleOwner(), result -> {

                if (result == null)
                    return;


                progressCompany.setVisibility(View.GONE);
                mainViewModel.getResultCompany().setValue(null);

                if (result.size() > 0) {
                    Company.deleteAll(Company.class);
                    Company.saveInTx(result.get(0));
                    Files.deleteAll(Files.class);
                    Files.saveInTx(result.get(0).getFiles());
                    Navigation.findNavController(getView()).navigate(R.id.DetailCompanyFragment);

                }


            });

        } catch (Exception ignored) {
        }

    }


    //region Method


    //region Config AnimationView
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
    //endregion Config AnimationView


    private void initSpecial() {
        // binding.cardSpecial.getLayoutParams().height = (int) (Constant.width * .544);
        adapterSlider = new SliderAdapter(getActivity(), sliderList);

        adapterSlider.setOnClickListener((advertise, position) -> {
            saveValueInSharedPrefrence("slider", position);
            String id = advertise.getI();
            boolean save = advertise.getIsSaved();
            NavDirections action = AdvertiseFragmentDirections.actionGoToDetailAdvertiseFragment(id, save);
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });


        binding.slider.setSliderAdapter(adapterSlider);
        binding.slider.setScrollTimeInSec(3);
        binding.slider.setIndicatorEnabled(true);
        binding.slider.setAutoCycle(true);
        binding.slider.startAutoCycle();

        if (sliderList.size() > 0)
            binding.cardSpecial.setVisibility(View.VISIBLE);

    }

    private void initLimit() {


        limitAdapter = new UniversalAdapter2(R.layout.limit_advertise_item, limitList, BR.limit);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        manager.setReverseLayout(true);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getActivity());
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
        flexboxLayoutManager.setAlignItems(AlignItems.BASELINE);


        binding.recycleLimitAdvertise.setLayoutManager(flexboxLayoutManager);
        binding.recycleLimitAdvertise.setAdapter(limitAdapter);


        limitAdapter.setOnItemClickListener((binding, position) -> {
            Bundle bundle = new Bundle();
            bundle.putString("guid", limitList.get(position).getI());
            bundle.putString("companyId", limitList.get(position).getCompanyId());
            bundle.putString("companyName", limitList.get(position).getCompanyName());
            Navigation.findNavController(getView()).navigate(R.id.CompanyAdvertise, bundle);
        });
    }


    @SuppressLint("SetTextI18n")
    private void initLadder() {
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

            binding.nested.setOnScrollChangeListener(endlessParentScrollListener);
            binding.recycleLadderAdvertise.setLayoutManager(linearManger);

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


            binding.nested.setOnScrollChangeListener(endlessParentScrollListener);
            binding.recycleLadderAdvertise.setLayoutManager(gridLayoutManager);
        }

        ladderAdapter = new UniversalAdapter2(R.layout.ladder_advertise_item, ladderList, BR.ladder);
        binding.recycleLadderAdvertise.setAdapter(ladderAdapter);

        ladderAdapter.setOnItemClickListener((bin, position) -> {

            saveValueInSharedPrefrence("ladder", position);
            String id = ladderList.get(position).getI();
            boolean save = ladderList.get(position).getIsSaved();
            NavDirections action = AdvertiseFragmentDirections.actionGoToDetailAdvertiseFragment(id, save);
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

        ladderAdapter.setOnItemBindListener((bind, position) -> {

            if (ladderList.size() > position && position >= 0) {
                View v = bind.getRoot();

                imgSave = v.findViewById(R.id.ivSave_ladder);

                progressSave = v.findViewById(R.id.progress_save_ladder);

                progressCompany = v.findViewById(R.id.progress_company);


                RelativeLayout cardCompany = v.findViewById(R.id.cardCompany);

                if (ladderList.get(position).getIsSaved())
                    imgSave.setImageResource(R.drawable.ic_complete_bookmark);
                else
                    imgSave.setImageResource(R.drawable.ic_bookmark);


                imgSave.setOnClickListener(view13 -> {
                    progressSave = v.findViewById(R.id.progress_save_ladder);
                    imgSave = v.findViewById(R.id.ivSave_ladder);
                    progressSave.setVisibility(View.VISIBLE);
                    positionSelect = position;
                    if (ladderList.get(position).getIsSaved())
                        mainViewModel.deleteMyAdvertisement(account.getI(), ladderList.get(positionSelect).getI());
                    else
                        mainViewModel.addToMyAdvertisement(account.getI(), ladderList.get(positionSelect).getI());
                });


                cardCompany.setOnClickListener(view -> {
                    progressCompany = v.findViewById(R.id.progress_company);
                    progressCompany.setVisibility(View.VISIBLE);
                    mainViewModel.getCompany(ladderList.get(position).getCompanyId());
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
        height = (Constant.width / 3) * 2;
        doFilter = new Filters();
        account = Select.from(Account.class).first();

        initSnackBar();
        binding.cardError2.setOnClickListener(view -> reloadAdvertisement());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void reloadAdvertisement() {
        pageMain = 1;
        binding.cardError2.setVisibility(View.GONE);
        ladderList.clear();
        limitList.clear();

        //  binding.v1.setVisibility(View.GONE);
        binding.v2.setVisibility(View.GONE);
        binding.layoutNotFound.setVisibility(View.GONE);
        try {
            limitAdapter.notifyDataSetChanged();
            ladderAdapter.notifyDataSetChanged();
        } catch (Exception ignored) {
        }
        binding.cardSpecial.setVisibility(View.GONE);
        sliderList.clear();
        binding.animation.setVisibility(View.VISIBLE);
        // mainViewModel.getBillboardAdvs(doFilter.getAccountFilter(), "", pageMain);
    }

    private void initSnackBar() {
        snackBar = new CustomSnackBar();
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }

    }

    private void loadMore() {
        oldSizeLadderList = ladderList.size();
        pageMain++;
        binding.progressBar22.setVisibility(View.VISIBLE);
        mainViewModel.getSimpleAdvertisements(doFilter.getAccountFilter(), Constant.APPLICATION_ID, account.getI(), pageMain);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // mainViewModel.clearRequest();
    }

    private void saveValueInSharedPrefrence(String name, int position) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put(name, position);
        Gson gson = new Gson();
        String storeHashMap = gson.toJson(hashMap);
        sharedPreferences.edit().putString("storeHashMap", storeHashMap).apply();
    }

    private void useDataToSetInList(String storedHashMap) {
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Integer>>() {
        }.getType();
        HashMap<String, Integer> hashMap = gson.fromJson(storedHashMap, type);


        if (hashMap.get("ladder") != null) {
            int position = hashMap.get("ladder");
            int count = sharedPreferences.getInt("view", ladderList.get(position).getCount());
            boolean save = sharedPreferences.getBoolean("save", ladderList.get(position).getIsSaved());
            ladderList.get(position).setCount(count);
            ladderList.get(position).setIsSaved(save);
            ladderAdapter.notifyItemChanged(position);
            return;
        }


        int position = hashMap.get("slider");
        int count = sharedPreferences.getInt("view", sliderList.get(position).getCount());
        boolean save = sharedPreferences.getBoolean("save", sliderList.get(position).getIsSaved());
        sliderList.get(position).setCount(count);
        sliderList.get(position).setIsSaved(save);

    }

    //endregion Method
}
