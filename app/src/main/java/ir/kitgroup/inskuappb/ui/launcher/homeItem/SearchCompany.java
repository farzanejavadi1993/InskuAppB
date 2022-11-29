package ir.kitgroup.inskuappb.ui.launcher.homeItem;

import android.annotation.SuppressLint;

import android.app.DownloadManager;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.card.MaterialCardView;
import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.classes.CustomDialogOpenWith;
import ir.kitgroup.inskuappb.classes.EndlessParentScrollListener;
import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.classes.filterr.Filters;
import ir.kitgroup.inskuappb.classes.itent.OpenWith;
import ir.kitgroup.inskuappb.classes.pdf.PdfTools;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Files;

import ir.kitgroup.inskuappb.databinding.SearchCompanyFragmentBinding;
import ir.kitgroup.inskuappb.util.Constant;

@AndroidEntryPoint
public class SearchCompany extends Fragment {

    @Inject
    SharedPreferences sharedPreferences;
    private SearchCompanyFragmentBinding binding;
    private MainViewModel myViewModel;
    private CustomDialogOpenWith customDialogOpenWith;

    private UniversalAdapter2 adapter2;
    private final ArrayList<Company> companies = new ArrayList<>();
    private CustomSnackBar snackBar;
    private ProgressBar progressSave;
    private ImageView imgSave;
    private Account account;
    private String wordSearch = "";

    private OpenWith openWith;
    private TextView tvCount;
    private int positionSelect = 0;
    private String pdfUrl = "";
    private PdfTools pdfTools;
    private int pageMain = 1;
    private Filters doFilter;

    private EndlessParentScrollListener endlessParentScrollListener;
    private int oldSizeLadderList = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchCompanyFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        initDialogOpenWith();
        initAnimation();
        initSearch();
        setUpTabView();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        myViewModel.getResultMessage().setValue(null);

        /*if (pageMain * 10 > companies.size() && pageMain == 1) {
            binding.progress.setVisibility(View.VISIBLE);
            myViewModel.getAllCompany("", pageMain, doFilter.getAccountFilter());
        }*/

        myViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;


            binding.cardError.setVisibility(View.GONE);
            if (result.getCode() == 10) {
                binding.tvError.setText(result.getDescription());
                binding.cardError.setVisibility(View.VISIBLE);
            }

            binding.progress.setVisibility(View.GONE);
            ShowMessageWarning(result.getDescription());
        });


        myViewModel.getResultAllCompany().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            binding.progressBar22.setVisibility(View.VISIBLE);

            if (result.size() > 0) {
                if (pageMain == 1)
                    companies.clear();
                companies.addAll(result);
                myViewModel.getMyCompany(account.getI());
            } else if (result.size() == 0 ) {
                binding.progressBar22.setVisibility(View.GONE);
                binding.progress.setVisibility(View.GONE);
                binding.progressSearch.setVisibility(View.GONE);
                if (companies.size() == 0) {
                  //  binding.searchViewCompany.setQuery("",true);
                    binding.layoutNotFound.setVisibility(View.VISIBLE);
                }
                else
                    binding.layoutNotFound.setVisibility(View.GONE);
            }


        });


        myViewModel.getResultMyCompany().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            myViewModel.getResultMyCompany().setValue(null);
            for (int i = 0; i < result.size(); i++) {

                ArrayList<Company> res = new ArrayList<>(companies);
                int finalI = i;
                CollectionUtils.filter(res, r -> r.getI().equals(result.get(finalI).getI()));
                if (res.size() > 0)
                    res.get(0).setSave(true);
            }


            if (pageMain == 1)
                adapter2.notifyDataSetChanged();
            else if (oldSizeLadderList <= companies.size())
                adapter2.notifyItemRangeChanged(oldSizeLadderList, companies.size() - 1);


            binding.progress.setVisibility(View.GONE);
            binding.progressSearch.setVisibility(View.GONE);
            binding.progressBar22.setVisibility(View.GONE);

        });
        myViewModel.getResultAddMyCompany().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                myViewModel.getResultAddMyCompany().setValue(null);
                if (result.get(0).getMessage() == 1) {
                    companies.get(positionSelect).setCount(companies.get(positionSelect).getCount() + 1);
                    tvCount.setText(String.valueOf(companies.get(positionSelect).getCount()));

                    companies.get(positionSelect).setSave(true);
                    imgSave.setImageResource(R.drawable.ic_complete_bookmark);

                } else
                    ShowMessageWarning("خطا در ذخیره شرکت");


            }
            progressSave.setVisibility(View.GONE);
        });
        myViewModel.getResultDeleteMyAccount().observe(getViewLifecycleOwner(), result -> {

            if (result != null) {
                myViewModel.getResultDeleteMyAccount().setValue(null);
                if (result.get(0).getMessage() == 1) {
                    companies.get(positionSelect).setCount(companies.get(positionSelect).getCount() - 1);
                    tvCount.setText(String.valueOf(companies.get(positionSelect).getCount()));

                    companies.get(positionSelect).setSave(false);
                    imgSave.setImageResource(R.drawable.ic_bookmark);

                } else
                    ShowMessageWarning("خطا در حذف شرکت");

                progressSave.setVisibility(View.GONE);

            }
        });
    }


    private void setSearchViewConfig(SearchView searchView) {
        try {

            searchView.setIconifiedByDefault(false);
            searchView.setQueryHint("جستجوی شرکت");
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setBackgroundColor(Color.TRANSPARENT);
            searchView.setQuery("", false);
            searchView.clearFocus();


            // @SuppressLint("UseCompatLoadingForDrawables")
            // Drawable drawColor = getResources().getDrawable(R.drawable.white_radius);
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

    private void setUpTabView() {

        LinearLayoutManager managerParent = new LinearLayoutManager(getActivity());
        if (endlessParentScrollListener == null) {
            endlessParentScrollListener = new EndlessParentScrollListener(managerParent) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    if (page + 1 <= pageMain)
                        return;
                    loadMore();
                }
            };
        }

        binding.nested.setOnScrollChangeListener(endlessParentScrollListener);
        binding.recyclerViewSearch.setLayoutManager(managerParent);
        adapter2 = new UniversalAdapter2(R.layout.row_recycler_company, companies, BR.company);
        binding.recyclerViewSearch.setAdapter(adapter2);


        adapter2.setOnItemBindListener((binding, position) -> {

            View view = binding.getRoot();
            RelativeLayout btnSave = view.findViewById(R.id.save);
            progressSave = view.findViewById(R.id.progressSave);
            progressSave.setVisibility(View.GONE);

            imgSave = view.findViewById(R.id.ivSave);
            RelativeLayout btnShare = view.findViewById(R.id.share);
            RelativeLayout btnPdf = view.findViewById(R.id.pdf);
            RelativeLayout onlineShop = view.findViewById(R.id.viewDetail);
            ImageView ivCircle = view.findViewById(R.id.ivCircle);
            ivCircle.setVisibility(View.GONE);
            RelativeLayout private_application = view.findViewById(R.id.private_application);
            ImageView imgPrivateApp = view.findViewById(R.id.ivPrivate_app);
            if (!companies.get(position).getInskId().equals("")) {
                private_application.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load("http://" + Constant.Main_URL_IMAGE + "/GetCompanyImage?id=" +
                                companies.get(position).getI() + "&width=120&height=120")
                        .error(R.drawable.loading)
                        .placeholder(R.drawable.loading)
                        .into(imgPrivateApp);
            }
            else
                private_application.setVisibility(View.GONE);

            onlineShop.setOnClickListener(view12 -> {
                Company.deleteAll(Company.class);
                Company.saveInTx(companies.get(position));
                Files.deleteAll(Files.class);
                Files.saveInTx(companies.get(position).getFiles());
                Navigation.findNavController(getView()).navigate(R.id.DetailCompanyFragment);
            });

            private_application.setOnClickListener(view1 -> {
                String pkg = companies.get(position).getInskId();
                String path = pkg + getActivity().getString(R.string.path_salein_companies);
                String link = getActivity().getString(R.string.link_salein_companies) + pkg;
                String id = companies.get(position).getI();
                String name = companies.get(position).getN();
                customDialogOpenWith.showDialog(
                        getActivity(),
                        pkg,
                        path,
                        link,
                        id,
                        name);
            });


          /*  if (companies.get(position).getAc_area() == 1)
                ivCircle.setImageResource(R.drawable.four);

            else if (companies.get(position).getAc_area() == 2)
                ivCircle.setImageResource(R.drawable.three);

            else if (companies.get(position).getAc_area() == 3)
                ivCircle.setImageResource(R.drawable.two);

            else if (companies.get(position).getAc_area() == 4)
                ivCircle.setImageResource(R.drawable.one);*/


            if (companies.size() >= position && position >= 0) {
                if (companies.get(position).isSave())
                    imgSave.setImageResource(R.drawable.ic_complete_bookmark);
                else
                    imgSave.setImageResource(R.drawable.ic_bookmark);


                btnPdf.setVisibility(View.GONE);
                try {
                    ArrayList<Files> files = new ArrayList<>(companies.get(position).getFiles());
                    CollectionUtils.filter(files, f -> f.getMime().contains("pdf"));

                    if (files.size() > 0)
                        btnPdf.setVisibility(View.VISIBLE);
                } catch (Exception ignored) {
                }



                btnSave.setOnClickListener(view13 -> {
                    tvCount = view.findViewById(R.id.tvCount);
                    progressSave = view.findViewById(R.id.progressSave);
                    imgSave = view.findViewById(R.id.ivSave);
                    progressSave.setVisibility(View.VISIBLE);
                    positionSelect = position;
                    if (companies.get(position).isSave())
                        myViewModel.deleteMyAccount(account.getI(), companies.get(positionSelect).getI());
                    else
                        myViewModel.addMyCompany(account.getI(), companies.get(positionSelect).getI());
                });
                btnPdf.setOnClickListener(view15 -> {
                    ArrayList<Files> fileList = new ArrayList<>(companies.get(position).getFiles());
                    CollectionUtils.filter(fileList, f -> f.getMime().contains("pdf"));
                    if (fileList.size() > 0) {
                        pdfUrl = "http://" + Constant.Main_URL_IMAGE + "/getCompanyPdf?id=" + fileList.get(0).getI();
                        pdfTools.showPDFUrl(getActivity(), pdfUrl, sharedPreferences);

                    }
                });



            }
        });


    }

    private void init() {
        snackBar = new CustomSnackBar();
        doFilter = new Filters();
        setSearchViewConfig(binding.searchViewCompany);
        customDialogOpenWith = CustomDialogOpenWith.getInstance();
        account = Select.from(Account.class).first();

        Company.deleteAll(Company.class);
        pdfTools = new PdfTools();
        openWith = new OpenWith(sharedPreferences, getActivity());

        binding.ivBack.setOnClickListener(view -> Navigation.findNavController(binding.getRoot()).popBackStack());
    }

    private void ShowMessageWarning(String error) {
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }
        snackBar.showSnack(getActivity(), binding.getRoot(), error);

    }

    private void initSearch() {
        binding.searchViewCompany.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextSubmit(String query) {

              //  binding.searchViewCompany.clearFocus();
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


    private void showMore(LinearLayout informationLayout, ImageView ivCircle, TextView tvAbout, MaterialCardView cardCompany) {

        if (informationLayout.getVisibility() == View.GONE) {

            ivCircle.setVisibility(View.GONE);
            informationLayout.setVisibility(View.VISIBLE);
            tvAbout.setSingleLine(false);
            cardCompany.setStrokeColor(getActivity().getResources().getColor(R.color.color_primary));
            cardCompany.setStrokeWidth(15);
        } else {

            ivCircle.setVisibility(View.VISIBLE);
            informationLayout.setVisibility(View.GONE);
            tvAbout.setSingleLine(true);
            cardCompany.setStrokeColor(null);
            cardCompany.setStrokeWidth(0);
        }
    }


    private void initDialogOpenWith() {
        customDialogOpenWith = CustomDialogOpenWith.getInstance();
        customDialogOpenWith.setOnClickSaleinItem((pkg, path, link, id) -> {
                    customDialogOpenWith.hide();
                    openWith.click(pkg, path, link, id);
                }
        );

    }

    private void loadMore() {
        binding.progressBar22.setVisibility(View.VISIBLE);
        oldSizeLadderList = companies.size();
        pageMain++;
        myViewModel.getAllCompany(wordSearch, pageMain, doFilter.getAccountFilter());
    }

    private void initAnimation() {
        binding.progressBar22.setAnimation("loading.json");
        binding.progressBar22.loop(true);
        binding.progressBar22.setSpeed(2f);
        binding.progressBar22.playAnimation();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void search(String text) {
        wordSearch = text.trim();
        oldSizeLadderList = 0;
        binding.layoutNotFound.setVisibility(View.GONE);
        companies.clear();
        adapter2.notifyDataSetChanged();
        binding.progress.setVisibility(View.VISIBLE);
        binding.progressSearch.setVisibility(View.VISIBLE);
        pageMain = 1;
        if (!wordSearch.trim().equals(""))
        myViewModel.getAllCompany(wordSearch, pageMain, doFilter.getAccountFilter());

    }

}
