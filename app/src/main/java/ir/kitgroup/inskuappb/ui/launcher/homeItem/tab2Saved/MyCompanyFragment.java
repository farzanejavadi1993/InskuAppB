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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.query.Select;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ui.launcher.homeItem.LauncherFragment;
import ir.kitgroup.inskuappb.ui.viewmodel.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.component.CustomDialogOpenWith;
import ir.kitgroup.inskuappb.component.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.component.itent.OpenWith;
import ir.kitgroup.inskuappb.component.pdf.PdfTools;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Files;
import ir.kitgroup.inskuappb.databinding.MyCompanyFragmentBinding;
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


    private int positionSelect = 0;
    private ProgressBar progressSave;
    private ImageView imgSave;
    private String pdfUrl = "";
    private CustomDialogOpenWith customDialogOpenWith;
    private OpenWith openWith;
    private PdfTools pdfTools;


    private CustomSnackBar snackbar;
    private Account account;
    private boolean deleteSaving;

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
        init();
        initDialogOpenWith();
        setUpTabView();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        deleteSaving = false;
        if (isVisibleToUser && !LauncherFragment.fragment.equals("myCompany")) {
            if (myViewModel != null) {


                List<Company> list = getListFromSharedPrefrence();

                    companies.clear();
                    if (list.size() > 0) {
                        companies.addAll(list);
                        binding.layoutNotFound.setVisibility(View.GONE);
                    } else
                        binding.layoutNotFound.setVisibility(View.VISIBLE);

                    adapter2.notifyDataSetChanged();
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
                myViewModel.getMyCompany(account.getI(), Constant.APPLICATION_ID);


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

                deleteSaving = false;
            });


            myViewModel.getResultMyCompany().observe(getViewLifecycleOwner(), result -> {
                binding.progress.setVisibility(View.GONE);
                if (result == null)
                    return;
                myViewModel.getResultMyCompany().setValue(null);
                companies.clear();

                if (result.size() > 0) {
                    saveCompany((ArrayList<Company>) result);
                    companies.addAll(result);
                    binding.layoutNotFound.setVisibility(View.GONE);
                } else
                    binding.layoutNotFound.setVisibility(View.VISIBLE);

                adapter2.notifyDataSetChanged();
            });


            myViewModel.getResultDeleteMyAccount().observe(getViewLifecycleOwner(), result -> {

                if (result == null)
                    return;
                myViewModel.getResultDeleteMyAccount().setValue(null);

                if (result.get(0).getMessage() == 4) {
                    deActiveFromMyCompany(companies.get(positionSelect).getI());
                    companies.remove(positionSelect);
                    adapter2.notifyDataSetChanged();

                    if (companies.size() == 0)
                        binding.layoutNotFound.setVisibility(View.VISIBLE);
                } else
                    ShowMessageWarning("خطا در حذف شرکت");

                deleteSaving = false;
                progressSave.setVisibility(View.GONE);
            });
        } catch (Exception ignored) {
        }
    }
    //endregion OverrideMethod


    //region Method
    private void setUpTabView() {
        adapter2 = new UniversalAdapter2(R.layout.row_recycler_company, companies, BR.company);
        binding.recyclerMyCompany.setAdapter(adapter2);
        binding.recyclerMyCompany.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter2.setOnItemBindListener((binding, position) -> {
            deleteSaving = false;
            View view = binding.getRoot();
            LinearLayout cardView = view.findViewById(R.id.tv_1);
            cardView.setVisibility(View.INVISIBLE);

            RelativeLayout btnSave = view.findViewById(R.id.save);
            progressSave = view.findViewById(R.id.progressSave);
            progressSave.setVisibility(View.GONE);
            imgSave = view.findViewById(R.id.ivSave);

            RelativeLayout btnPdf = view.findViewById(R.id.pdf);

            RelativeLayout private_application = view.findViewById(R.id.private_application);


            if (!companies.get(position).getInskId().equals("")) {
                private_application.setVisibility(View.VISIBLE);
            } else
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
                    if (!deleteSaving) {
                        deleteSaving = true;
                        progressSave = view.findViewById(R.id.progressSave);
                        imgSave = view.findViewById(R.id.ivSave);
                        progressSave.setVisibility(View.VISIBLE);
                        positionSelect = position;
                        myViewModel.removeFromSavedAccounts(account.getI(), companies.get(positionSelect).getI(), Constant.APPLICATION_ID);
                    }
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

        adapter2.setOnItemClickListener((binding, position) -> {
            Company.deleteAll(Company.class);
            Company.saveInTx(companies.get(position));
            Files.deleteAll(Files.class);
            Files.saveInTx(companies.get(position).getFiles());
            Navigation.findNavController(getView()).navigate(R.id.DetailCompanyFragment);
        });


    }

    private void init() {
        deleteSaving = false;
        pdfTools = new PdfTools();
        snackbar = new CustomSnackBar();
        account = Select.from(Account.class).first();
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
        myViewModel.getMyCompany(account.getI(), Constant.APPLICATION_ID);
    }

    public void deActiveFromMyCompany(String id) {
        String storeCompany = sharedPreferences.getString("storeCompany", "");

        ArrayList<Company> storeChangeCompany = null;
        Gson gson = new Gson();

        if (!storeCompany.equals("")) {
            java.lang.reflect.Type type = new TypeToken<HashMap<String, ArrayList<Company>>>() {
            }.getType();
            HashMap<String, ArrayList<Company>> hashMap = gson.fromJson(storeCompany, type);

            storeChangeCompany = hashMap.get("changeCompany");

            ArrayList<Company> res = new ArrayList<>(storeChangeCompany);
            CollectionUtils.filter(res, s -> s.getI().equals(id));

            if (res.size() > 0) {
                res.get(0).setSave(false);
            }

        }
        HashMap<String, ArrayList<Company>> hashMapStore = new HashMap<>();
        hashMapStore.put("changeCompany", storeChangeCompany);
        String storeHashMap = gson.toJson(hashMapStore);
        sharedPreferences.edit().putString("storeCompany", storeHashMap).apply();
    }


    private void saveCompany(ArrayList<Company> companies) {
        Gson gson = new Gson();
        HashMap<String, ArrayList<Company>> hashMapStore = new HashMap<>();
        hashMapStore.put("changeCompany", companies);
        String storeHashMap = gson.toJson(hashMapStore);
        sharedPreferences.edit().putString("storeCompany", storeHashMap).apply();
    }

    public ArrayList<Company> getListFromSharedPrefrence() {
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