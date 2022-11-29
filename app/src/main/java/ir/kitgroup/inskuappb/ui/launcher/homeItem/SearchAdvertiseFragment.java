package ir.kitgroup.inskuappb.ui.launcher.homeItem;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.orm.query.Select;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.classes.EndlessParentScrollListener;
import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.classes.filterr.Filters;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Files;
import ir.kitgroup.inskuappb.databinding.SearchAdvertiseFragmentBinding;
import ir.kitgroup.inskuappb.model.Advertise;
import ir.kitgroup.inskuappb.util.Constant;

@AndroidEntryPoint
public class SearchAdvertiseFragment extends Fragment {

    //region Parameter
    @Inject
    SharedPreferences sharedPreferences;
    private SearchAdvertiseFragmentBinding binding;
    private MainViewModel mainViewModel;

    private CustomSnackBar snackBar;
    private UniversalAdapter2 adapter;
    private ArrayList<Advertise> advertiseList = new ArrayList<>();
    private int pageMain = 1;
    private EndlessParentScrollListener endlessParentScrollListener;
    private int oldSizeLadderList = 0;

    private ImageView imgSave;
    private ProgressBar progressSave;
    private ProgressBar progressCompany;

    private int positionSelect = 0;
    private Account account;


    private Filters doFilter;
    private String wordSearch = "";
    //endregion Parameter


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchAdvertiseFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        initAnimation();
        initSearch();
        initAdvertiseList();


    }


    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.getResultMessage().setValue(null);

        mainViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            try {
                if (result.getCode() == 3) {
                    binding.tvError.setText(result.getDescription());
                    binding.cardError.setVisibility(View.VISIBLE);
                }


                binding.progress.setVisibility(View.GONE);
                binding.progressBar22.setVisibility(View.GONE);
                progressSave.setVisibility(View.GONE);
                progressCompany.setVisibility(View.GONE);
            } catch (Exception ignored) {
            }


            if (result.getCode() == 1) {
                binding.tvError.setText(result.getDescription());
                binding.cardError.setVisibility(View.VISIBLE);
            } else
                showMessageWarning(result.getDescription());

        });
        mainViewModel.getResultAllAdvertisement().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            mainViewModel.getResultAllAdvertisement().setValue(null);

            if (pageMain == 1)
                advertiseList.clear();


            binding.layoutNotFound.setVisibility(View.GONE);
            if (result.size() > 0) {
                advertiseList.addAll(result);
                mainViewModel.getMyAdvertisement(account.getI());
            } else if (result.size() == 0 ) {
                binding.progressBar22.setVisibility(View.GONE);
                binding.progress.setVisibility(View.GONE);
                binding.progressSearch.setVisibility(View.GONE);
                if (advertiseList.size() ==0){
                  //  binding.searchViewAdvertise.setQuery("",true);
                    binding.layoutNotFound.setVisibility(View.VISIBLE);
                }else {
                    binding.layoutNotFound.setVisibility(View.GONE);
                }
            }

        });
        mainViewModel.getResultMyAdvertisement().observe(getViewLifecycleOwner(), rst -> {
            if (rst == null)
                return;

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
            binding.progressSearch.setVisibility(View.GONE);
            binding.progressBar22.setVisibility(View.GONE);

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

    private void init() {
        snackBar = new CustomSnackBar();
        doFilter = new Filters();
        setSearchViewConfig(binding.searchViewAdvertise);
        account = Select.from(Account.class).first();
        binding.ivBack.setOnClickListener(view -> Navigation.findNavController(binding.getRoot()).popBackStack());
    }

    private void showMessageWarning(String error) {
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }
        snackBar.showSnack(getActivity(), binding.getRoot(), error);
    }

    private void initAdvertiseList() {
        // advertiseList = new ArrayList<>();
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
            binding.recyclerView.setLayoutManager(gridLayoutManager);
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
            binding.recyclerView.setLayoutManager(linearManger);

        }

        binding.nested.setOnScrollChangeListener(endlessParentScrollListener);
        adapter = new UniversalAdapter2(R.layout.ladder_advertise_item, advertiseList, BR.ladder);
        binding.recyclerView.setAdapter(adapter);

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
        mainViewModel.getAllAdvertisement(doFilter.getAccountFilter(), wordSearch, pageMain);

    }

    private void initAnimation() {
        binding.progressBar22.setAnimation("loading.json");
        binding.progressBar22.loop(true);
        binding.progressBar22.setSpeed(2f);
        binding.progressBar22.playAnimation();

    }

    private void setSearchViewConfig(SearchView searchView) {
        try {

            searchView.setIconifiedByDefault(false);
            searchView.setQueryHint("جستجوی طرح");
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setBackgroundColor(Color.TRANSPARENT);
            searchView.setQuery("", false);
            searchView.clearFocus();


            searchView.setBackground(null);

            LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
            LinearLayout linearLayout2;
            try {
                linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
            } catch (Exception ignore) {
                linearLayout2 = (LinearLayout) linearLayout1.getChildAt(0);
            }

            LinearLayout linearLayout3;
            try {
                linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
            } catch (Exception ignore) {
                linearLayout3 = (LinearLayout) linearLayout2.getChildAt(0);
            }


            AutoCompleteTextView autoComplete;
            try {
                autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
            } catch (Exception ignore) {
                autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(1);
            }
            autoComplete.setTextSize(12);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                autoComplete.setTextColor(getActivity().getColor(R.color.teal_200));
                autoComplete.setHintTextColor(getActivity().getColor(R.color.grey_font));

            }
            Typeface iranSansBold = Typeface.createFromAsset(getActivity().getAssets(), "iransans.ttf");
            autoComplete.setTypeface(iranSansBold);


        } catch (Exception ignore) {
        }
    }

    private void initSearch() {
        binding.searchViewAdvertise.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextSubmit(String query) {
             //   binding.searchViewAdvertise.clearFocus();
                if (!wordSearch.equals(query.trim()))
                search(query);

                return true;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!wordSearch.equals(newText.trim()))
                search(newText);
                return false;

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void search(String text) {

        binding.progressSearch.setVisibility(View.VISIBLE);
        binding.progress.setVisibility(View.VISIBLE);
        binding.layoutNotFound.setVisibility(View.GONE);
        pageMain = 1;
        wordSearch = text;
        oldSizeLadderList = 0;
        advertiseList.clear();
        adapter.notifyDataSetChanged();
        if (!text.trim().equals(""))
            mainViewModel.getAllAdvertisement(doFilter.getAccountFilter(), wordSearch, pageMain);
        else {

            binding.progressSearch.setVisibility(View.GONE);
            binding.progress.setVisibility(View.GONE);
            mainViewModel.clearRequest();
        }


    }
}
