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
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.orm.query.Select;


import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Random;


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
import ir.kitgroup.inskuappb.model.SliderData;
import ir.kitgroup.inskuappb.ui.launcher.homeItem.LauncherFragment;
import ir.kitgroup.inskuappb.util.Constant;


@AndroidEntryPoint
public class AdvertiseFragment extends Fragment {

    @Inject
    SharedPreferences sharedPreferences;
    private AdvertiseFragmentBinding binding;
    private MainViewModel mainViewModel;


    private final ArrayList<Advertise> allAdvertisement = new ArrayList<>();


    private SliderAdapter adapterSlider;
    private final ArrayList<SliderData> sliderList = new ArrayList<>();
    private int height;


    private UniversalAdapter2 limitAdapter;
    private final ArrayList<Advertise> limitList = new ArrayList<>();


    private EndlessParentScrollListener endlessParentScrollListener;
    private UniversalAdapter2 ladderAdapter;
    private final ArrayList<Advertise> ladderList = new ArrayList<>();
    private int pageMain = 1;
    private ImageView imgSave;
    private int request = 0;
    private ProgressBar progressSave;
    private ProgressBar progressCompany;
    private Filters doFilter;
    private int positionSelect = 0;
    private int oldSizeLadderList = 0;


    private CustomSnackBar snackBar;
    private Account account;
    private boolean requset=false;


   /*@SuppressLint("NotifyDataSetChanged")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !LauncherFragment.fragment.equals("advertise")) {
            if (mainViewModel != null) {
                reloadAdvertisement();
            }
        }
    }
*/

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
        try {
            init();
            initAnimation();
            initSpecial();
            initLimit();
            initLadder();
        } catch (Exception ignored) {}
    }


    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
            mainViewModel.getResultMessage().setValue(null);



            if (pageMain * 10 > ladderList.size() && pageMain == 1) {
                binding.animation.setVisibility(View.VISIBLE);
                mainViewModel.getAllAdvertisement(doFilter.getAccountFilter(), "", pageMain);
            }
            else {
                int position=sharedPreferences.getInt("position",-1);
                if (position>=0){
                    int count=sharedPreferences.getInt("view",ladderList.get(position).getCount());
                    ladderList.get(position).setCount(count);
                    sharedPreferences.edit().putInt("position",-1).apply();
                    ladderAdapter.notifyItemChanged(position);
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
            mainViewModel.getResultAllAdvertisement().observe(getViewLifecycleOwner(), result -> {

                if (result == null)
                    return;

                mainViewModel.getResultAllAdvertisement().setValue(null);
                binding.progressBar22.setVisibility(View.VISIBLE);


                if (result.size() > 0) {
                    allAdvertisement.clear();
                    allAdvertisement.addAll(result);
                    mainViewModel.getMyAdvertisement(account.getI());

                } else if (result.size() == 0 && ladderList.size() == 0) {
                    binding.progressBar22.setVisibility(View.GONE);
                    binding.animation.setVisibility(View.GONE);
                    binding.layoutNotFound.setVisibility(View.VISIBLE);
                } else if (result.size() == 0)
                    binding.progressBar22.setVisibility(View.GONE);


            });
            mainViewModel.getResultMyAdvertisement().observe(getViewLifecycleOwner(), rst -> {
                if (rst == null)
                    return;


                mainViewModel.getResultMyAdvertisement().setValue(null);


                if (rst != null) {


                    for (int i = 0; i < rst.size(); i++) {
                        ArrayList<Advertise> result = new ArrayList<>(allAdvertisement);
                        int finalI = i;
                        CollectionUtils.filter(result, r -> r.getI().equals(rst.get(finalI).getI()));
                        if (result.size() > 0)
                            allAdvertisement.get(allAdvertisement.indexOf(result.get(0))).setSave(true);
                    }


                    if (pageMain == 1) {
                        ladderList.clear();
                        limitList.clear();
                    }


                    ArrayList<Advertise> resultLimit = new ArrayList<>(allAdvertisement);
                    CollectionUtils.filter(resultLimit, r -> r.getDShowIn().get2() != null);


                    ArrayList<Advertise> finalLimitList = new ArrayList<>();

                  for (int i = 0; i < resultLimit.size(); i++) {
                        ArrayList<Advertise> res = new ArrayList<>(finalLimitList);
                        int finalI = i;
                        CollectionUtils.filter(res, r -> r.getCompanyId().equals(resultLimit.get(finalI).getCompanyId()));
                        if (res.size() == 0)
                            finalLimitList.add(resultLimit.get(i));
                    }


                    //region Show Only 8 Item
                    int size = Math.min(finalLimitList.size(), 8);
                    for (int i = size - 1; i >= 0; i--) {
                        limitList.add(finalLimitList.get(i));
                    }
                    //endregion Show Only 8 Item


                    ArrayList<Advertise> resultLadder = new ArrayList<>(allAdvertisement);
                    CollectionUtils.filter(resultLadder, r -> r.getDShowIn().get1() != null);

                    ladderList.addAll(resultLadder);


                    if (ladderList.size() > 0 && limitList.size() > 0)
                        binding.v2.setVisibility(View.VISIBLE);


                    ArrayList<Advertise> resultSpecial = new ArrayList<>(allAdvertisement);
                    CollectionUtils.filter(resultSpecial, r -> r.getDShowIn().get3() != null);
                    if (resultSpecial.size() > 0) {

                        binding.cardSpecial.setVisibility(View.VISIBLE);
                        binding.v1.setVisibility(View.VISIBLE);
                        sliderList.clear();
                        int random  = new Random(System.nanoTime()).nextInt(resultSpecial.size()) ;

                        for (int i = random; i < resultSpecial.size(); i++) {

                            sliderList.add(new SliderData
                                    (resultSpecial.get(i).getI()
                                            , resultSpecial.get(i).getCompanyName(), resultSpecial.get(i).getDType(), resultSpecial.get(i).getI(), resultSpecial.get(i).getCount(), resultSpecial.get(i).isSave())
                            );
                        }

                        for (int i = 0; i < random; i++) {

                            sliderList.add(new SliderData
                                    (resultSpecial.get(i).getI()
                                            , resultSpecial.get(i).getCompanyName(), resultSpecial.get(i).getDType(), resultSpecial.get(i).getI(), resultSpecial.get(i).getCount(), resultSpecial.get(i).isSave())
                            );
                        }

                        adapterSlider.notifyDataSetChanged();
                        binding.slider.setSliderAdapter(adapterSlider);


                    } else if (pageMain == 1 && resultSpecial.size() == 0) {
                        binding.cardSpecial.setVisibility(View.GONE);
                        binding.v1.setVisibility(View.GONE);
                    }


//                    if (limitList.size() == 0 && ladderList.size() == 0 && sliderList.size() == 0)
//                        binding.layoutNotFound.setVisibility(View.VISIBLE);
//                    else
//                        binding.layoutNotFound.setVisibility(View.GONE);

                }


                if (pageMain == 1) {
                    limitAdapter.notifyDataSetChanged();
                    ladderAdapter.notifyDataSetChanged();
                } else if (oldSizeLadderList <= ladderList.size())
                    ladderAdapter.notifyItemRangeInserted(oldSizeLadderList, ladderList.size() - 1);


                binding.animation.setVisibility(View.GONE);
                binding.progressBar22.setVisibility(View.GONE);


            });
            mainViewModel.getResultAddMyAdvertisement().observe(getViewLifecycleOwner(), result -> {

                if (result == null)
                    return;

                progressSave.setVisibility(View.GONE);
                progressCompany.setVisibility(View.GONE);
                mainViewModel.getResultAddMyAdvertisement().setValue(null);
                if (result.get(0).getMessage() == 1) {
                    ladderList.get(positionSelect).setSave(true);
                    imgSave.setImageResource(R.drawable.ic_complete_bookmark);

                } else
                    ShowMessageWarning("خطا در ذخیره آگهی");


            });
            mainViewModel.getResultDeleteMyAdvertisement().observe(getViewLifecycleOwner(), result -> {

                if (result == null)
                    return;

                mainViewModel.getResultDeleteMyAdvertisement().setValue(null);
                if (result.get(0).getMessage() == 1) {
                    ladderList.get(positionSelect).setSave(false);
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
        binding.cardSpecial.getLayoutParams().height = (int) (Constant.width * .522);
        adapterSlider = new SliderAdapter(getActivity(), sliderList);

        adapterSlider.setOnClickListener((sliderData, position) -> {

            NavDirections action = AdvertiseFragmentDirections.actionGoToDetailAdvertiseFragment(sliderData.getI(), sliderData.isSave());
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });

 /*       binding.slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);*/
        binding.slider.setSliderAdapter(adapterSlider);
        binding.slider.setScrollTimeInSec(3);
        binding.slider.setIndicatorEnabled(true);
        binding.slider.setAutoCycle(true);
        binding.slider.startAutoCycle();


        if (sliderList.size() > 0) {
            binding.v1.setVisibility(View.VISIBLE);
            binding.cardSpecial.setVisibility(View.VISIBLE);
        }

        // binding.nested.setSaveFromParentEnabled(true);
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
            try {
                Bundle bundle = new Bundle();
                bundle.putString("companyId", limitList.get(position).getCompanyId());
                bundle.putString("companyName", limitList.get(position).getCompanyName());
                Navigation.findNavController(getView()).navigate(R.id.CompanyAdvertise, bundle);
            } catch (Exception ignored) {
                int p = 0;
            }

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
            sharedPreferences.edit().putInt("position",position).apply();
            Bundle bundle = new Bundle();
            bundle.putString("Guid", ladderList.get(position).getI());
            bundle.putBoolean("save", ladderList.get(position).isSave());
            Navigation.findNavController(getView()).navigate(R.id.DetailAdvertiseFragment, bundle);
        });

        ladderAdapter.setOnItemBindListener((bind, position) -> {

            if (ladderList.size() > position && position >= 0) {
                View v = bind.getRoot();
                imgSave = v.findViewById(R.id.ivSave_ladder);
                progressSave = v.findViewById(R.id.progress_save_ladder);
                progressCompany = v.findViewById(R.id.progress_company);
                progressCompany = v.findViewById(R.id.progress_company);

                RelativeLayout cardCompany = v.findViewById(R.id.cardCompany);
                if (ladderList.get(position).isSave())
                    imgSave.setImageResource(R.drawable.ic_complete_bookmark);
                else
                    imgSave.setImageResource(R.drawable.ic_bookmark);

                ArrayList<Advertise> list = new ArrayList<>(ladderList);
                CollectionUtils.filter(list, l -> l.getI().equals(ladderList.get(position).getId()));


                imgSave.setOnClickListener(view13 -> {

                    progressSave = v.findViewById(R.id.progress_save_ladder);
                    imgSave = v.findViewById(R.id.ivSave_ladder);
                    progressSave.setVisibility(View.VISIBLE);
                    positionSelect = position;
                    if (ladderList.get(position).isSave())
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

        binding.v1.setVisibility(View.GONE);
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
        mainViewModel.getAllAdvertisement(doFilter.getAccountFilter(), "", pageMain);
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
        mainViewModel.getAllAdvertisement(doFilter.getAccountFilter(), "", pageMain);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // mainViewModel.clearRequest();
    }


    //endregion Method
}
