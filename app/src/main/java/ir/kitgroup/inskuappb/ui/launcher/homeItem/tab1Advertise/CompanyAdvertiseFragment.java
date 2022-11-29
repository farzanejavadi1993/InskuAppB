package ir.kitgroup.inskuappb.ui.launcher.homeItem.tab1Advertise;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orm.query.Select;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.classes.EndlessParentScrollListener;

import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;

import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Files;
import ir.kitgroup.inskuappb.databinding.CompanyAdvertiseFragmentBinding;
import ir.kitgroup.inskuappb.model.Advertise;
import ir.kitgroup.inskuappb.util.Constant;

@AndroidEntryPoint
public class CompanyAdvertiseFragment extends Fragment {

    //region Parameter
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


    //endregion Parameter

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
        mainViewModel.getResultMessage().setValue(null);


        binding.progress.setVisibility(View.VISIBLE);

        if (pageMain * 10 > advertiseList.size() && companiesId.size()>0)
            mainViewModel.getCompanyAdvertisement(companiesId, pageMain);
        else if (companiesId.size()==0)
            mainViewModel.getMyAdvertisement(account.getI());



        mainViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            try {
                binding.progress.setVisibility(View.GONE);
                binding.progressBar22.setVisibility(View.GONE);
                progressSave.setVisibility(View.GONE);
                progressCompany.setVisibility(View.GONE);

            } catch (Exception ignored) {
            }

            if (result.getCode() == 1) {
                binding.tvError40.setText(result.getDescription());
                binding.cardError40.setVisibility(View.VISIBLE);
            } else
                showMessageWarning(result.getDescription());
        });
        mainViewModel.getResultCompanyAdvertisement().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            mainViewModel.getResultCompanyAdvertisement().setValue(null);

            binding.progressBar22.setVisibility(View.VISIBLE);
            if (pageMain == 1)
                advertiseList.clear();

            if (result.size() > 0) {
                advertiseList.addAll(result);
                mainViewModel.getMyAdvertisement(account.getI());
            }
            else if (result.size() == 0 && advertiseList.size() > 0) {
                binding.progressBar22.setVisibility(View.GONE);
            }
            else {
                binding.progress.setVisibility(View.GONE);
                binding.layoutNotFound.setVisibility(View.VISIBLE);
            }

        });
        mainViewModel.getResultMyAdvertisement().observe(getViewLifecycleOwner(), rst -> {
            if (rst == null)
                return;

            if (companiesId.size()==0) {
                advertiseList.clear();
                advertiseList.addAll(rst);

                 if (rst.size() == 0 && advertiseList.size() > 0) {
                    binding.progressBar22.setVisibility(View.GONE);
                }
                else {
                    binding.progress.setVisibility(View.GONE);
                    binding.layoutNotFound.setVisibility(View.VISIBLE);
                }
            }

            binding.progressBar22.setVisibility(View.GONE);

            mainViewModel.getResultMyAdvertisement().setValue(null);

            if (rst != null) {
                for (int i = 0; i < rst.size(); i++) {
                    ArrayList<Advertise> result = new ArrayList<>(advertiseList);
                    int finalI = i;
                    CollectionUtils.filter(result, r -> r.getI().equals(rst.get(finalI).getI()));
                    if (result.size() > 0)
                        advertiseList.get(advertiseList.indexOf(result.get(0))).setSave(true);
                }
            }

            if (pageMain == 1)
                adapter.notifyDataSetChanged();
            else
                adapter.notifyItemRangeChanged(oldSizeLadderList, advertiseList.size());


            binding.progress.setVisibility(View.GONE);

        });
        mainViewModel.getResultAddMyAdvertisement().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;

            progressSave.setVisibility(View.GONE);
            mainViewModel.getResultAddMyAdvertisement().setValue(null);
            if (result.get(0).getMessage() == 1) {
                advertiseList.get(positionSelect).setSave(true);
                imgSave.setImageResource(R.drawable.ic_complete_bookmark);

            } else
                showMessageWarning("خطا در ذخیره آگهی");


        });
        mainViewModel.getResultDeleteMyAdvertisement().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;

            progressSave.setVisibility(View.GONE);
            mainViewModel.getResultDeleteMyAdvertisement().setValue(null);
            if (result.get(0).getMessage() == 1) {
                advertiseList.get(positionSelect).setSave(false);
                imgSave.setImageResource(R.drawable.ic_bookmark);

            } else
                showMessageWarning("خطا در حذف آگهی");


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


    }


    //region CustomMethod

    private void init() {
        account = Select.from(Account.class).first();
        binding.ivBack.setOnClickListener(view -> Navigation.findNavController(binding.getRoot()).popBackStack());
    }

    @SuppressLint("SetTextI18n")
    private void getArgument() {
        String companyId = CompanyAdvertiseFragmentArgs.fromBundle(getArguments()).getCompanyId();
        companyName = CompanyAdvertiseFragmentArgs.fromBundle(getArguments()).getCompanyName();

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


        if (Constant.screenInches > 7 ) {
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
        }
        else {

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


        if (companiesId.size()>0)
            binding.nested.setOnScrollChangeListener(endlessParentScrollListener);

        adapter = new UniversalAdapter2(R.layout.ladder_advertise_item, advertiseList, BR.ladder);
        binding.recyclerCompanyAdvertise.setAdapter(adapter);


        adapter.setOnItemClickListener((bin, position) -> {
            Bundle bundle = new Bundle();
            bundle.putString("Guid", advertiseList.get(position).getI());
            bundle.putBoolean("save", advertiseList.get(position).isSave());
            Navigation.findNavController(getView()).navigate(R.id.DetailAdvertiseFragment, bundle);
        });


        adapter.setOnItemBindListener((bind, position) -> {

            View v = bind.getRoot();
            imgSave = v.findViewById(R.id.ivSave_ladder);
            progressSave = v.findViewById(R.id.progress_save_ladder);
            progressCompany = v.findViewById(R.id.progress_company);
            RelativeLayout cardCompany = v.findViewById(R.id.cardCompany);

            if (advertiseList.get(position).isSave())
                imgSave.setImageResource(R.drawable.ic_complete_bookmark);
            else
                imgSave.setImageResource(R.drawable.ic_bookmark);


            imgSave.setOnClickListener(view13 -> {
                progressSave = v.findViewById(R.id.progress_save_ladder);
                imgSave = v.findViewById(R.id.ivSave_ladder);
                progressSave.setVisibility(View.VISIBLE);
                positionSelect = position;
                if (advertiseList.get(position).isSave())
                    mainViewModel.deleteMyAdvertisement(account.getI(), advertiseList.get(positionSelect).getI());
                else
                    mainViewModel.addToMyAdvertisement(account.getI(), advertiseList.get(positionSelect).getI());
            });


            cardCompany.setOnClickListener(view -> {
                progressCompany = v.findViewById(R.id.progress_company);
                progressCompany.setVisibility(View.VISIBLE);
                mainViewModel.getCompany(advertiseList.get(position).getCompanyId());
            });

        });


    }


    private void loadMoreList() {
        oldSizeLadderList = advertiseList.size();
        binding.progressBar22.setVisibility(View.VISIBLE);
        pageMain++;
        if (companiesId.size()>0)
        mainViewModel.getCompanyAdvertisement(companiesId, pageMain);
        else
            mainViewModel.getMyAdvertisement(account.getI());

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
        mainViewModel.clearRequest();
    }


    //endregion CustomMethod

}
