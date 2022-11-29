package ir.kitgroup.inskuappb.ui.launcher.homeItem.tab2Saved;

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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.classes.itent.OpenWith;
import ir.kitgroup.inskuappb.classes.pdf.PdfTools;
import ir.kitgroup.inskuappb.classes.share.ShareCompany;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Files;
import ir.kitgroup.inskuappb.databinding.MyCompanyFragmentBinding;
import ir.kitgroup.inskuappb.ui.launcher.homeItem.LauncherFragment;
import ir.kitgroup.inskuappb.ui.launcher.homeItem.LauncherFragmentDirections;
import ir.kitgroup.inskuappb.util.Constant;

@AndroidEntryPoint
public class MyCompanyFragment extends Fragment {

    //region Parameter

    @Inject
    SharedPreferences sharedPreferences;

    private MyCompanyFragmentBinding binding;
    private MainViewModel myViewModel;

    private UniversalAdapter2 adapter2;
    private final ArrayList<Company> companies = new ArrayList<>();


    private String linkUpdate = "";
    private int positionSelect = 0;
    private ProgressBar progressSave;
    private ImageView imgSave;
    private String pdfUrl = "";
    private CustomDialogOpenWith customDialogOpenWith;
    private OpenWith openWith;
    private PdfTools pdfTools;


    private CustomSnackBar snackbar;
    private Account account;

    //endregion Parameter

    //region OverrideMethod
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MyCompanyFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            init();
            initDialogOpenWith();
            setUpTabView();
        } catch (Exception ignored) {
        }


    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !LauncherFragment.fragment.equals("myCompany") ) {
            if (myViewModel != null) {
                reloadCompanies();
            }

        }
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            myViewModel = new ViewModelProvider(this).get(MainViewModel.class);
            myViewModel.getResultMessage().setValue(null);


            binding.progress.setVisibility(View.VISIBLE);
            if (companies.size() == 0)
                myViewModel.getMyCompany(account.getI());

            myViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
                if (result == null)
                    return;

                try {
                    binding.progress.setVisibility(View.GONE);
                    progressSave.setVisibility(View.GONE);
                } catch (Exception ignored) {
                }


                if (result.getCode() == 10) {
                    binding.tvError1.setText(result.getDescription());
                    binding.cardError1.setVisibility(View.VISIBLE);
                } else
                    ShowMessageWarning(result.getDescription());


            });
            myViewModel.getResultMyCompany().observe(getViewLifecycleOwner(), result -> {
                binding.progress.setVisibility(View.GONE);
                if (result == null)
                    return;
                myViewModel.getResultMyCompany().setValue(null);

                if (result.size() > 0)
                    binding.layoutNotFound.setVisibility(View.GONE);
                else
                    binding.layoutNotFound.setVisibility(View.VISIBLE);

                companies.clear();
                companies.addAll(result);
                try {
                    adapter2.notifyDataSetChanged();
                } catch (Exception ignored) {
                }


            });
            myViewModel.getResultDeleteMyAccount().observe(getViewLifecycleOwner(), result -> {
                progressSave.setVisibility(View.GONE);

                if (result == null)
                    return;
                myViewModel.getResultDeleteMyAccount().setValue(null);
                if (result.get(0).getMessage() == 1) {
                    companies.remove(positionSelect);
                    try {
                        adapter2.notifyDataSetChanged();
                    } catch (Exception ignored) {
                    }
                    if (companies.size() == 0)
                        binding.layoutNotFound.setVisibility(View.VISIBLE);
                } else
                    ShowMessageWarning("خطا در حذف شرکت");


            });
        } catch (Exception ignored) {
        }

    }


    //endregion OverrideMethod

    //region Method
    private void setUpTabView() {


        binding.recyclerMyCompany.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter2 = new UniversalAdapter2(R.layout.row_recycler_company, companies, BR.company);
        binding.recyclerMyCompany.setAdapter(adapter2);
        binding.recyclerMyCompany.setNestedScrollingEnabled(true);
        adapter2.setOnItemBindListener((binding, position) -> {
            if (companies.size() >= position) {
                View view = binding.getRoot();
                LinearLayout cardView = view.findViewById(R.id.tv_1);
                cardView.setVisibility(View.INVISIBLE);

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

                imgSave.setImageResource(R.drawable.ic_complete_bookmark);

                if (companies.size() >= position && position >= 0) {
                    btnPdf.setVisibility(View.GONE);
                    try {
                        ArrayList<Files> files = new ArrayList<>(companies.get(position).getFiles());
                        CollectionUtils.filter(files, f -> f.getMime().contains("pdf"));
                        if (files.size() > 0)
                            btnPdf.setVisibility(View.VISIBLE);
                    } catch (Exception ignored) {
                    }


                    btnSave.setOnClickListener(view13 -> {
                        progressSave = view.findViewById(R.id.progressSave);
                        imgSave = view.findViewById(R.id.ivSave);
                        progressSave.setVisibility(View.VISIBLE);
                        positionSelect = position;
                        myViewModel.deleteMyAccount(account.getI(), companies.get(positionSelect).getI());

                    });


                    btnPdf.setOnClickListener(view15 -> {
                        ArrayList<Files> fileList = new ArrayList<>(companies.get(position).getFiles());
                        CollectionUtils.filter(fileList, f -> f.getMime().contains("pdf"));
                        if (fileList.size() > 0) {
                            pdfUrl = "http://" + Constant.Main_URL_IMAGE + "/getCompanyPdf?id=" + fileList.get(0).getI();
                            pdfTools.showPDFUrl(getActivity(), pdfUrl, sharedPreferences);
                        }
                    });


                    btnShare.setOnClickListener(view14 -> shareApplication(position));





                }
            }
        });


    }

    private void init() {
        Company.deleteAll(Company.class);
        pdfTools = new PdfTools();
        snackbar = new CustomSnackBar();
        account = Select.from(Account.class).first();
        linkUpdate = sharedPreferences.getString("link_update", "");
        customDialogOpenWith = CustomDialogOpenWith.getInstance();
        openWith = new OpenWith(sharedPreferences, getActivity());
        binding.cardError1.setOnClickListener(view -> reloadCompanies());
    }

    private void initDialogOpenWith() {
        customDialogOpenWith = CustomDialogOpenWith.getInstance();
        customDialogOpenWith.setOnClickSaleinItem((pkg, path, link, id) -> {
            customDialogOpenWith.hide();
            openWith.click(pkg, path, link, id);
        });

    }

    private void shareApplication(int position) {
        ShareCompany shared = new ShareCompany();
        shared.onShare(getActivity(),
                companies.get(position).getN(),
                companies.get(position).getDesc(),
                companies.get(position).getM1(),
                linkUpdate);
    }

    private void ShowMessageWarning(String error) {

        snackbar.showSnack(getActivity(), binding.getRoot(), error);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void reloadCompanies() {
        binding.cardError1.setVisibility(View.GONE);
        binding.progress.setVisibility(View.VISIBLE);
        companies.clear();
        adapter2.notifyDataSetChanged();
        myViewModel.getResultMessage().setValue(null);
        binding.layoutNotFound.setVisibility(View.GONE);
        myViewModel.getMyCompany(account.getI());
    }


    //endregion Method
}
