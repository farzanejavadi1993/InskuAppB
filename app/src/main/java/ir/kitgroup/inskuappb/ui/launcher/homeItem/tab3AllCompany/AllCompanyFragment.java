package ir.kitgroup.inskuappb.ui.launcher.homeItem.tab3AllCompany;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import org.apache.commons.collections4.CollectionUtils;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;

import ir.kitgroup.inskuappb.classes.CustomDialogOpenWith;
import ir.kitgroup.inskuappb.classes.EndlessParentScrollListener;
import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.classes.share.ShareCompany;
import ir.kitgroup.inskuappb.classes.filterr.Filters;
import ir.kitgroup.inskuappb.classes.itent.OpenWith;
import ir.kitgroup.inskuappb.classes.pdf.PdfTools;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.dataBase.Files;
import ir.kitgroup.inskuappb.databinding.AllCompanyFragmentBinding;

import ir.kitgroup.inskuappb.ui.launcher.homeItem.LauncherFragment;
import ir.kitgroup.inskuappb.util.Constant;

import java.util.ArrayList;


import javax.inject.Inject;

@AndroidEntryPoint
public class AllCompanyFragment extends Fragment {

    //region Parameter
    @Inject
    SharedPreferences sharedPreferences;
    private AllCompanyFragmentBinding binding;
    private MainViewModel mainViewModel;

    private Filters filters;
    private String linkUpdateApplication = "";

    private UniversalAdapter2 adapter2;
    private final ArrayList<Company> companies = new ArrayList<>();
    private int positionSelectCompany = 0;

    private Account account;
    private ProgressBar progressSave;
    private ImageView imgSave;


    private OpenWith openWith;
    private CustomDialogOpenWith customDialogOpenWith;
    private int pageMain = 1;

    private TextView tvCount;
    private PdfTools pdfTools;
    private CustomSnackBar snackBar;
    private View view;
    private int oldSizeLadderList = 0;

    private EndlessParentScrollListener endlessParentScrollListener;


    //endregion Parameter

    //region Override Method
//    @SuppressLint("NotifyDataSetChanged")
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//
//        super.setUserVisibleHint(isVisibleToUser);
//
//        if (isVisibleToUser && !LauncherFragment.fragment.equals("")) {
//            if (mainViewModel != null) {
//                reloadCompanies();
//            }
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = AllCompanyFragmentBinding.inflate(getLayoutInflater());
        try {
            init();
            initAnimation();
            initDialogOpenWith();
            setUpTabView();
        } catch (Exception ignored) {
        }
        return binding.getRoot();
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getResultMessage().setValue(null);

        if (pageMain == 1 && pageMain * 10 > companies.size()) {
            mainViewModel.getAllCompany("", pageMain, filters.getAccountFilter());
            binding.progress.setVisibility(View.VISIBLE);
        }


        mainViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;

            try {
                binding.progress.setVisibility(View.GONE);
                binding.progressBar22.setVisibility(View.GONE);
                progressSave.setVisibility(View.GONE);
            } catch (Exception ignored) {
            }

            if (result.getCode() == 10) {
                binding.tvError.setText(result.getDescription());
                binding.cardError.setVisibility(View.VISIBLE);
            } else
                ShowMessageWarning(result.getDescription());
        });
        mainViewModel.getResultAllCompany().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            mainViewModel.getResultAllCompany().setValue(null);
            binding.progressBar22.setVisibility(View.VISIBLE);


            if (result.size() > 0) {
                if (pageMain == 1)
                    companies.clear();

                companies.addAll(result);

                mainViewModel.getMyCompany(account.getI());
            } else if (result.size() == 0) {
                binding.progress.setVisibility(View.GONE);
                binding.progressBar22.setVisibility(View.GONE);

                if (companies.size() == 0)
                    binding.layoutNotFound.setVisibility(View.VISIBLE);
                else
                    binding.layoutNotFound.setVisibility(View.GONE);
            }

        });
        mainViewModel.getResultMyCompany().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;

            mainViewModel.getResultMyCompany().setValue(null);

            for (int i = 0; i < result.size(); i++) {
                ArrayList<Company> res = new ArrayList<>(companies);
                int finalI = i;
                CollectionUtils.filter(res, r -> r.getI().equals(result.get(finalI).getI()));
                if (res.size() > 0)
                    res.get(0).setSave(true);
            }


            adapter2.notifyItemRangeChanged(oldSizeLadderList, companies.size() - 1);
            binding.progress.setVisibility(View.GONE);
            binding.progressBar22.setVisibility(View.GONE);
        });


        mainViewModel.getResultAddMyCompany().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                mainViewModel.getResultAddMyCompany().setValue(null);
                if (result.get(0).getMessage() == 1) {
                    companies.get(positionSelectCompany).setCount(companies.get(positionSelectCompany).getCount() + 1);
                    tvCount.setText(String.valueOf(companies.get(positionSelectCompany).getCount()));

                    companies.get(positionSelectCompany).setSave(true);
                    imgSave.setImageResource(R.drawable.ic_complete_bookmark);

                } else
                    ShowMessageWarning("خطا در ذخیره شرکت");


            }
            progressSave.setVisibility(View.GONE);
        });
        mainViewModel.getResultDeleteMyAccount().observe(getViewLifecycleOwner(), result -> {

            if (result != null) {
                mainViewModel.getResultDeleteMyAccount().setValue(null);
                if (result.get(0).getMessage() == 1) {
                    companies.get(positionSelectCompany).setCount(companies.get(positionSelectCompany).getCount() - 1);
                    tvCount.setText(String.valueOf(companies.get(positionSelectCompany).getCount()));

                    companies.get(positionSelectCompany).setSave(false);
                    imgSave.setImageResource(R.drawable.ic_bookmark);

                } else
                    ShowMessageWarning("خطا در حذف شرکت");

                progressSave.setVisibility(View.GONE);

            }
        });

    }


    //endregion Override Method

    //region Method
    private void setUpTabView() {

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
        binding.recyclerView.setLayoutManager(linearManger);
        adapter2 = new UniversalAdapter2(R.layout.row_recycler_company, companies, BR.company);
        binding.recyclerView.setAdapter(adapter2);
        adapter2.setOnItemBindListener((bin, position) -> {

            if (position <= companies.size()) {
                View view = bin.getRoot();
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


//                if (companies.get(position).getAc_area() == 1)
//                    ivCircle.setImageResource(R.drawable.four);
//
//
//                else if (companies.get(position).getAc_area() == 2)
//                    ivCircle.setImageResource(R.drawable.three);
//
//
//                else if (companies.get(position).getAc_area() == 3)
//                    ivCircle.setImageResource(R.drawable.two);
//
//
//                else if (companies.get(position).getAc_area() == 4)
//                    ivCircle.setImageResource(R.drawable.one);


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
                        positionSelectCompany = position;
                        if (companies.get(position).isSave())
                            mainViewModel.deleteMyAccount(account.getI(), companies.get(positionSelectCompany).getI());
                        else
                            mainViewModel.addMyCompany(account.getI(), companies.get(positionSelectCompany).getI());
                    });


                    btnPdf.setOnClickListener(view15 -> {
                        ArrayList<Files> fileList = new ArrayList<>(companies.get(position).getFiles());
                        CollectionUtils.filter(fileList, f -> f.getMime().contains("pdf"));
                        if (fileList.size() > 0) {
                            String pdfUrl = "http://" + Constant.Main_URL_IMAGE + "/getCompanyPdf?id=" + fileList.get(0).getI();
                            pdfTools.showPDFUrl(getActivity(), pdfUrl, sharedPreferences);

                        }
                    });


                    btnShare.setOnClickListener(view14 ->
                            shareApplication(position));


                }

            }
        });


    }

    private void shareApplication(int position) {
        ShareCompany shared = new ShareCompany();
        shared.onShare(getActivity(),
                companies.get(position).getN(),
                companies.get(position).getDesc(),
                companies.get(position).getM1(),
                linkUpdateApplication);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void init() {

        Company.deleteAll(Company.class);
        account = Select.from(Account.class).first();
        account.setTab(0);
        account.save();

        snackBar = new CustomSnackBar();
        pdfTools = new PdfTools();
        filters = new Filters();
        openWith = new OpenWith(sharedPreferences, getActivity());


        linkUpdateApplication = sharedPreferences.getString("link_update", "");


        binding.cardError.setOnClickListener(view -> reloadCompanies());


    }

    private void initDialogOpenWith() {
        customDialogOpenWith = CustomDialogOpenWith.getInstance();
        customDialogOpenWith.setOnClickSaleinItem((pkg, path, link, id) -> {
                    customDialogOpenWith.hide();
                    openWith.click(pkg, path, link, id);
                }
        );

    }

    @SuppressLint("NotifyDataSetChanged")
    public void reloadCompanies() {
        companies.clear();
        adapter2.notifyDataSetChanged();
        binding.layoutNotFound.setVisibility(View.GONE);
        binding.cardError.setVisibility(View.GONE);
        binding.progress.setVisibility(View.VISIBLE);
        pageMain = 1;
        mainViewModel.getResultMessage().setValue(null);
        mainViewModel.getAllCompany("", pageMain, filters.getAccountFilter());
    }

    private void ShowMessageWarning(String error) {
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }

        snackBar.showSnack(getActivity(), binding.getRoot(), error);

    }

    private void initAnimation() {
        binding.progressBar22.setAnimation("loading.json");
        binding.progressBar22.loop(true);
        binding.progressBar22.setSpeed(2f);
        binding.progressBar22.playAnimation();

    }

    private void loadMore() {
        binding.progressBar22.setVisibility(View.VISIBLE);
        oldSizeLadderList = companies.size();
        pageMain++;
        mainViewModel.getAllCompany("", pageMain, filters.getAccountFilter());
    }
    //endregion Method
}