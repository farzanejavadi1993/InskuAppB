package ir.kitgroup.inskuappb.ui.launcher.homeItem.tab3AllCompany;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.query.Select;

import org.apache.commons.collections4.CollectionUtils;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;

import ir.kitgroup.inskuappb.classes.CustomDialogOpenWith;
import ir.kitgroup.inskuappb.classes.EndlessParentScrollListener;
import ir.kitgroup.inskuappb.classes.SharedPrefrenceValue;
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
import java.util.HashMap;


import javax.inject.Inject;

@AndroidEntryPoint
public class AllCompanyFragment extends Fragment {


    //region Parameter
    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    SharedPrefrenceValue sharedPrefrenceValue;


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
    private boolean saving;


    private OpenWith openWith;
    private CustomDialogOpenWith customDialogOpenWith;
    private int pageMain = 1;

    private TextView tvCount;
    private PdfTools pdfTools;
    private CustomSnackBar snackBar;
    private View view;
    private int oldSizeLadderList = 0;

    private EndlessParentScrollListener endlessParentScrollListener;
    private boolean firstSync = false;





    //endregion Parameter

    //region Override Method
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        saving = false;
        if (isVisibleToUser && !LauncherFragment.fragment.equals("")) {
            if (mainViewModel != null) {


                ArrayList<Company>  saveCompanies = sharedPrefrenceValue.getListFromSharedPrefrence();
                for (int i = 0; i < saveCompanies.size(); i++) {
                    int finalI = i;
                    ArrayList<Company> res = new ArrayList<>(companies);
                    CollectionUtils.filter(res, r -> r.getI().equals(saveCompanies.get(finalI).getI()));

                    if (res.size() > 0) {
                        int index = companies.indexOf(res.get(0));
                        adapter2.notifyItemChanged(index);
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AllCompanyFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initAnimation();
        initDialogOpenWith();
        setUpTabView();
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getResultMessage().setValue(null);

        if (!firstSync) {
            binding.progress.setVisibility(View.VISIBLE);
            mainViewModel.getAllCompany(filters.getAccountFilter(), "", Constant.APPLICATION_ID, account.getI(), pageMain);
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

            saving = false;
        });
        mainViewModel.getResultAllCompany().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            firstSync = true;
            mainViewModel.getResultAllCompany().setValue(null);

            binding.progressBar22.setVisibility(View.VISIBLE);

            if (result.size() > 0) {
                if (pageMain == 1)
                    companies.clear();

                companies.addAll(result);

                if (pageMain == 1)
                    adapter2.notifyDataSetChanged();
                else
                    adapter2.notifyItemRangeChanged(oldSizeLadderList, companies.size() - 1);

            } else if (result.size() == 0) {

                if (companies.size() == 0)
                    binding.layoutNotFound.setVisibility(View.VISIBLE);
                else
                    binding.layoutNotFound.setVisibility(View.GONE);
            }

            binding.progress.setVisibility(View.GONE);
            binding.progressBar22.setVisibility(View.GONE);

        });
        mainViewModel.getResultAddMyCompany().observe(getViewLifecycleOwner(), result -> {
            if (result == null) return;

            mainViewModel.getResultAddMyCompany().setValue(null);

            if (result.get(0).getMessage() == 4) {
                sharedPrefrenceValue.addToMyAccount(companies.get(positionSelectCompany));
                companies.get(positionSelectCompany).setCount(companies.get(positionSelectCompany).getCount() + 1);
                tvCount.setText(String.valueOf(companies.get(positionSelectCompany).getCount()));
                companies.get(positionSelectCompany).setSave(true);
                imgSave.setImageResource(R.drawable.ic_complete_bookmark);


            } else
                ShowMessageWarning("خطا در ذخیره شرکت");


            saving = false;
            progressSave.setVisibility(View.GONE);
        });
        mainViewModel.getResultDeleteMyAccount().observe(getViewLifecycleOwner(), result -> {
            if (result == null) return;

            mainViewModel.getResultDeleteMyAccount().setValue(null);

            if (result.get(0).getMessage() == 4) {
                sharedPrefrenceValue.deleteFromMyCompany(companies.get(positionSelectCompany).getI());
                companies.get(positionSelectCompany).setCount(companies.get(positionSelectCompany).getCount() - 1);
                tvCount.setText(String.valueOf(companies.get(positionSelectCompany).getCount()));
                companies.get(positionSelectCompany).setSave(false);
                imgSave.setImageResource(R.drawable.ic_bookmark);
            } else
                ShowMessageWarning("خطا در حذف شرکت");

            saving = false;
            progressSave.setVisibility(View.GONE);
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

            saving = false;

            View view = bin.getRoot();
            RelativeLayout btnSave = view.findViewById(R.id.save);
            progressSave = view.findViewById(R.id.progressSave);
            progressSave.setVisibility(View.GONE);
            imgSave = view.findViewById(R.id.ivSave);
            RelativeLayout btnPdf = view.findViewById(R.id.pdf);


            RelativeLayout private_application = view.findViewById(R.id.private_application);
            ImageView imgPrivateApp = view.findViewById(R.id.ivPrivate_app);
            if (!companies.get(position).getInskId().equals(""))
                private_application.setVisibility(View.VISIBLE);

             else
                private_application.setVisibility(View.GONE);


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


            if (companies.size() >= position && position >= 0) {

                ArrayList<Company> objects = new ArrayList<>(sharedPrefrenceValue.getListFromSharedPrefrence());
                CollectionUtils.filter(objects, s -> s.getI().equals(companies.get(position).getI()));

                if (objects.size() == 0 )
                    companies.get(position).setSave(false);

                else if (!objects.get(0).issaved) {
                    companies.get(position).setSave(false);
                    sharedPrefrenceValue.deleteFromMyCompany(companies.get(position).getI());
                } else
                    companies.get(position).setSave(true);


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
                    if (!saving) {
                        saving = true;
                        tvCount = view.findViewById(R.id.tvCount);
                        progressSave = view.findViewById(R.id.progressSave);
                        imgSave = view.findViewById(R.id.ivSave);
                        progressSave.setVisibility(View.VISIBLE);
                        positionSelectCompany = position;
                        if (companies.get(position).isSave())
                            mainViewModel.removeFromSavedAccounts(account.getI(), companies.get(positionSelectCompany).getI(), Constant.APPLICATION_ID);
                        else
                            mainViewModel.addToSavedAccounts(account.getI(), companies.get(positionSelectCompany).getI(), Constant.APPLICATION_ID);
                    }

                });


                btnPdf.setOnClickListener(view15 -> {
                    ArrayList<Files> fileList = new ArrayList<>(companies.get(position).getFiles());
                    CollectionUtils.filter(fileList, f -> f.getMime().contains("pdf"));
                    if (fileList.size() > 0) {
                        String pdfUrl = "http://" + Constant.Main_URL_IMAGE + "/getCompanyPdf?id=" + fileList.get(0).getI();
                        pdfTools.showPDFUrl(getActivity(), pdfUrl, sharedPreferences);

                    }
                });
            }
        });

        adapter2.setOnItemClickListener((binding, position) -> {
            Company.deleteAll(Company.class);
            Company.saveInTx(companies.get(position));
            Files.deleteAll(Files.class);
            Files.saveInTx(companies.get(position).getFiles());
            Navigation.findNavController(getView()).navigate(R.id.DetailCompanyFragment);
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
        });
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
        mainViewModel.getAllCompany(filters.getAccountFilter(), "", Constant.APPLICATION_ID, account.getI(), pageMain);
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
        mainViewModel.getAllCompany(filters.getAccountFilter(), "", Constant.APPLICATION_ID, account.getI(), pageMain);
    }




    public ArrayList<Company> getCompanyFromList(String id) {
        String storeCompany = sharedPreferences.getString("storeCompany", "");
        ArrayList<Company> changeCompany = new ArrayList<>();

        if (!storeCompany.equals("")) {
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<HashMap<String, ArrayList<Company>>>() {
            }.getType();
            HashMap<String, ArrayList<Company>> hashMap = gson.fromJson(storeCompany, type);
            changeCompany = hashMap.get("changeCompany");

        }
        CollectionUtils.filter(changeCompany, l -> l.issaved);
        return changeCompany;
    }







    //endregion Method
}