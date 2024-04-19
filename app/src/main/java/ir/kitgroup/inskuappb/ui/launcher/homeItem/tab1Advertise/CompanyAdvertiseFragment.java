package ir.kitgroup.inskuappb.ui.launcher.homeItem.tab1Advertise;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.orm.query.Select;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ui.launcher.homeItem.tab1Advertise.CompanyAdvertiseFragmentArgs;
import ir.kitgroup.inskuappb.ui.viewmodel.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.component.EndlessParentScrollListener;

import ir.kitgroup.inskuappb.component.SharedPrefrenceValue;
import ir.kitgroup.inskuappb.component.dialog.CustomSnackBar;

import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Files;
import ir.kitgroup.inskuappb.dataBase.StoreChangeAdvertise;
import ir.kitgroup.inskuappb.databinding.CompanyAdvertiseFragmentBinding;
import ir.kitgroup.inskuappb.data.model.Advertise;
import ir.kitgroup.inskuappb.util.Constant;

@AndroidEntryPoint
public class CompanyAdvertiseFragment extends Fragment {

    //region Parameter
    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    SharedPrefrenceValue sharedPrefrenceValue;
    private CompanyAdvertiseFragmentBinding binding;
    private MainViewModel mainViewModel;

    private String companyName;
    private CustomSnackBar snackBar;

    private UniversalAdapter2 adapter;
    private final ArrayList<Advertise> advertiseList = new ArrayList<>();
    private int pageMain = 1;

    private ImageView imgSave;
    private ProgressBar progressSave;
    private ProgressBar progressCompany;

    private int positionSelect = 0;
    private Account account;

    private final List<String> companiesId = new ArrayList<>();
    private int oldSizeLadderList = 0;
    private EndlessParentScrollListener endlessParentScrollListener;

    private boolean firstSync;
    private String advGuid;
    private boolean savingAdv;


    //endregion Parameter

    //region Override Method
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CompanyAdvertiseFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        getArgument();
        initAnimation();
        initAdvertiseList();
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
            try {
                binding.progress.setVisibility(View.GONE);
                binding.progressBar22.setVisibility(View.GONE);
                progressSave.setVisibility(View.GONE);
                progressCompany.setVisibility(View.GONE);
            } catch (Exception ignored) {}

            if (result.getCode() == 1) {
                binding.tvError40.setText(result.getDescription());
                binding.cardError40.setVisibility(View.VISIBLE);
            } else
                showMessageWarning(result.getDescription());

            savingAdv=false;
        });
        mainViewModel.getResultCompanyAdvertisement().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            mainViewModel.getResultCompanyAdvertisement().setValue(null);

            binding.progressBar22.setVisibility(View.VISIBLE);

            if (pageMain == 1)
                advertiseList.clear();

            if (result.size() > 0) {
                if (!advGuid.equals("") && !firstSync) {
                    ArrayList<Advertise> res = new ArrayList<>(result);
                    CollectionUtils.filter(res, r -> r.getI().equals(advGuid));
                    if (res.size() > 0) {
                        res.get(0).setSpeacial(true);
                        advertiseList.add(0, res.get(0));
                        CollectionUtils.filter(result, r -> !r.getI().equals(advGuid));
                        advertiseList.addAll(1, result);
                    }
                } else
                    advertiseList.addAll(result);

                if (pageMain == 1)
                    adapter.notifyDataSetChanged();
                else
                    adapter.notifyItemRangeChanged(oldSizeLadderList, advertiseList.size());
            } else if (result.size() == 0 && advertiseList.size() > 0)
                binding.progressBar22.setVisibility(View.GONE);

            else
                binding.layoutNotFound.setVisibility(View.VISIBLE);

            binding.progress.setVisibility(View.GONE);
            firstSync = true;
        });

        mainViewModel.getResultMyAdvertisement().observe(getViewLifecycleOwner(), rst -> {
            if (rst == null)
                return;
            firstSync = true;
            mainViewModel.getResultMyAdvertisement().setValue(null);

            advertiseList.clear();

            binding.progress.setVisibility(View.GONE);
            binding.progressBar22.setVisibility(View.GONE);

            if (rst.size() > 0) {
                advertiseList.addAll(rst);
                binding.layoutNotFound.setVisibility(View.GONE);
            } else {
                binding.layoutNotFound.setVisibility(View.VISIBLE);
            }

            adapter.notifyDataSetChanged();
        });

        mainViewModel.getResultAddMyAdvertisement().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            mainViewModel.getResultAddMyAdvertisement().setValue(null);
            progressSave.setVisibility(View.GONE);

            if (result.get(0).getMessage() == 1) {
                advertiseList.get(positionSelect).setIsSaved(true);
                imgSave.setImageResource(R.drawable.ic_complete_bookmark);
                sharedPrefrenceValue.saveValueInSharedPrefrence(advertiseList.get(positionSelect).getI(), true, advertiseList.get(positionSelect).getCount());
            } else
                showMessageWarning("خطا در ذخیره آگهی");

            savingAdv=false;

        });
        mainViewModel.getResultDeleteMyAdvertisement().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            mainViewModel.getResultDeleteMyAdvertisement().setValue(null);
            progressSave.setVisibility(View.GONE);
            if (result.get(0).getMessage() == 1) {
                advertiseList.get(positionSelect).setIsSaved(false);
                imgSave.setImageResource(R.drawable.ic_bookmark);
                sharedPrefrenceValue.saveValueInSharedPrefrence(advertiseList.get(positionSelect).getI(), false, advertiseList.get(positionSelect).getCount());
            } else
                showMessageWarning("خطا در حذف آگهی");

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

    //region CustomMethod

    private void init() {
        savingAdv=false;
        account = Select.from(Account.class).first();
        binding.ivBack.setOnClickListener(view -> Navigation.findNavController(binding.getRoot()).popBackStack());
    }

    @SuppressLint("SetTextI18n")
    private void getArgument() {
        String companyId = ir.kitgroup.inskuappb.ui.launcher.homeItem.tab1Advertise.CompanyAdvertiseFragmentArgs.fromBundle(getArguments()).getCompanyId();
        companyName = CompanyAdvertiseFragmentArgs.fromBundle(getArguments()).getCompanyName();
        advGuid = CompanyAdvertiseFragmentArgs.fromBundle(getArguments()).getGuid();

        companiesId.clear();
        if (!companyId.equals(""))
            companiesId.add(companyId);

        binding.tvToolbar.setText("طرح های " + companyName);
    }

    private void showMessageWarning(String error) {
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }
        snackBar.showSnack(getActivity(), binding.getRoot(), error);
    }

    private void initAdvertiseList() {
        LinearLayoutManager linearManger;
        GridLayoutManager gridLayoutManager;

        if (Constant.screenInches > 7) {
            gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
            if (endlessParentScrollListener == null)
                if (endlessParentScrollListener == null) {
                    endlessParentScrollListener = new EndlessParentScrollListener(gridLayoutManager) {
                        @Override
                        public void onLoadMore(int page, int totalItemsCount) {
                            if (page + 1 <= pageMain)
                                return;
                            loadMoreList();
                        }
                    };
                }
            binding.recyclerCompanyAdvertise.setLayoutManager(gridLayoutManager);
        } else {
            linearManger = new LinearLayoutManager(getActivity());
            if (endlessParentScrollListener == null) {
                endlessParentScrollListener = new EndlessParentScrollListener(linearManger) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        if (page + 1 <= pageMain)
                            return;
                        loadMoreList();
                    }
                };
            }
            binding.recyclerCompanyAdvertise.setLayoutManager(linearManger);
        }


        if (companiesId.size() > 0)
            binding.nested.setOnScrollChangeListener(endlessParentScrollListener);

        adapter = new UniversalAdapter2(R.layout.simple_advertise_item, advertiseList, BR.simple);
        binding.recyclerCompanyAdvertise.setAdapter(adapter);

        adapter.setOnItemClickListener((bin, position) -> {

            sharedPrefrenceValue.saveValueInSharedPrefrence(advertiseList.get(position).getI(), advertiseList.get(position).getIsSaved(), position);

            Bundle bundle = new Bundle();
            bundle.putString("Guid", advertiseList.get(position).getI());
            Navigation.findNavController(getView()).navigate(R.id.DetailAdvertiseFragment, bundle);
        });


        adapter.setOnItemBindListener((bind, position) -> {
            View v = bind.getRoot();
            imgSave = v.findViewById(R.id.ivSave_simple);
            progressSave = v.findViewById(R.id.progress_save_simple);
            progressCompany = v.findViewById(R.id.progress_company);
            LinearLayout cardAdvertisement = v.findViewById(R.id.cardAdvertisement);
            RelativeLayout cardCompany = v.findViewById(R.id.cardCompany);

            if (advertiseList.get(position).getIsSaved())
                imgSave.setImageResource(R.drawable.ic_complete_bookmark);
            else
                imgSave.setImageResource(R.drawable.ic_bookmark);


            if (advertiseList.get(position).isSpeacial())
                cardAdvertisement.setBackgroundResource(R.drawable.special_background);
            else
                cardAdvertisement.setBackgroundResource(R.color.white);

            imgSave.setOnClickListener(view13 -> {
                if (!savingAdv){
                    savingAdv=true;
                    progressSave = v.findViewById(R.id.progress_save_simple);
                    imgSave = v.findViewById(R.id.ivSave_simple);
                    progressSave.setVisibility(View.VISIBLE);
                    positionSelect = position;
                    if (advertiseList.get(position).getIsSaved())
                        mainViewModel.removeFromSavedAdvs(account.getI(), advertiseList.get(positionSelect).getI());
                    else
                        mainViewModel.addToSavedAdvs(account.getI(), advertiseList.get(positionSelect).getI());
                }
            });


            cardCompany.setOnClickListener(view -> {
                progressCompany = v.findViewById(R.id.progress_company);
                progressCompany.setVisibility(View.VISIBLE);
                mainViewModel.getCompanyDetailById(advertiseList.get(position).getCompanyId());
            });

        });
    }


    private void loadMoreList() {
        oldSizeLadderList = advertiseList.size();
        binding.progressBar22.setVisibility(View.VISIBLE);
        pageMain++;
        if (companiesId.size() > 0)
            mainViewModel.getAdvsByCompanyId(companiesId, pageMain, Constant.APPLICATION_ID, account.getI());
        else
            mainViewModel.getCustomerSavedAdvs(account.getI(), Constant.APPLICATION_ID);

    }

    private void initAnimation() {
        binding.progressBar22.setAnimation("loading.json");
        binding.progressBar22.loop(true);
        binding.progressBar22.setSpeed(2f);
        binding.progressBar22.playAnimation();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // mainViewModel.clearRequest();
    }

    private void useDataToSetInList(String storedHashMap) {
        List<StoreChangeAdvertise> storeChangeAdvertises = sharedPrefrenceValue.useDataToSetInList(storedHashMap);
        for (int i = 0; i < storeChangeAdvertises.size(); i++) {
            int finalI = i;
            int count = storeChangeAdvertises.get(finalI).getCount();
            boolean save = storeChangeAdvertises.get(finalI).isSave();
            ArrayList<Advertise> advertises = new ArrayList<>(advertiseList);
            CollectionUtils.filter(advertises, a -> a.getI().equals(storeChangeAdvertises.get(finalI).getI()));
            if (advertises.size() > 0) {
                advertiseList.get(advertiseList.indexOf(advertises.get(0))).setIsSaved(save);
                advertiseList.get(advertiseList.indexOf(advertises.get(0))).setCount(count);
                adapter.notifyItemChanged(advertiseList.indexOf(advertises.get(0)));
            }
        }
    }

    private void nullTheMutable() {
        mainViewModel.getResultMessage().setValue(null);
        mainViewModel.getResultCompanyAdvertisement().setValue(null);
        mainViewModel.getResultMyAdvertisement().setValue(null);
        mainViewModel.getResultAddMyAdvertisement().setValue(null);
        mainViewModel.getResultDeleteMyAdvertisement().setValue(null);
        mainViewModel.getResultCompany().setValue(null);
    }

    private void getFirstRequest() {
        if (!firstSync) {
            nullTheMutable();

            binding.progress.setVisibility(View.VISIBLE);
            if (companiesId.size() > 0)
                mainViewModel.getAdvsByCompanyId(companiesId, pageMain, Constant.APPLICATION_ID, account.getI());
            else
                mainViewModel.getCustomerSavedAdvs(account.getI(), Constant.APPLICATION_ID);
        } else {
            String storedHashMap = sharedPreferences.getString("storeHashMap", "");
            if (!storedHashMap.equals("")) {
                useDataToSetInList(storedHashMap);
            }
        }
    }

    private void navigateToDetailCompany(Company company) {
        Company.deleteAll(Company.class);
        Company.saveInTx(company);
        Files.deleteAll(Files.class);
        Files.saveInTx(company.getFiles());
        Navigation.findNavController(getView()).navigate(R.id.DetailCompanyFragment);
    }


    //endregion CustomMethod
}
