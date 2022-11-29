package ir.kitgroup.inskuappb.ui.launcher.homeItem.tab3AllCompany;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.text.LineBreaker;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.orm.query.Select;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ConnectServer.CompanyViewModel;
import ir.kitgroup.inskuappb.ConnectServer.ConnectToServer;
import ir.kitgroup.inskuappb.ConnectServer.HostSelectionInterceptor;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.R;

import ir.kitgroup.inskuappb.adapter.SliderImageAdapter;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.classes.CustomDialogOpenWith;
import ir.kitgroup.inskuappb.classes.CustomOrderDialog;
import ir.kitgroup.inskuappb.classes.CustomRecycleDialog;
import ir.kitgroup.inskuappb.classes.SliderImage;
import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.classes.itent.OpenWith;
import ir.kitgroup.inskuappb.classes.pdf.PdfTools;
import ir.kitgroup.inskuappb.classes.socialMedia.Email;
import ir.kitgroup.inskuappb.classes.socialMedia.Instagram;
import ir.kitgroup.inskuappb.classes.socialMedia.InstallWatsApp;
import ir.kitgroup.inskuappb.classes.socialMedia.Telegram;
import ir.kitgroup.inskuappb.classes.socialMedia.WebSite;
import ir.kitgroup.inskuappb.classes.watsApp.WatsApp;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Customer;
import ir.kitgroup.inskuappb.dataBase.Files;
import ir.kitgroup.inskuappb.dataBase.ModelCatalog;
import ir.kitgroup.inskuappb.databinding.DetailCompanyFragmentBinding;
import ir.kitgroup.inskuappb.model.CustomerCatalog;
import ir.kitgroup.inskuappb.model.ModelSetting;
import ir.kitgroup.inskuappb.util.Constant;


@AndroidEntryPoint
public class DetailCompanyFragment extends Fragment {
    //region Parameter
    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    HostSelectionInterceptor hostSelectionInterceptor;

    private DetailCompanyFragmentBinding binding;
    private CompanyViewModel companyViewModel;
    private MainViewModel mainViewModel;
    private Account account;
    private String userName = "";
    private String passWord = "";
    private ArrayList<Customer> customerList;
    private ArrayList<CustomerCatalog> customerCatalogs;
    private UniversalAdapter2 customerAdapter;

    private CustomRecycleDialog dialogShowCatalog;
    private LottieAnimationView animationView;
    private ArrayList<ModelCatalog> catalogsList;

    private Customer customerChoose;
    private boolean settingSuccess = false;
    private boolean contactSuccess = false;
    private boolean reRequest = false;

    private Company company;
    //private Files files;

    private int width;
    private int height;
    private List<Files> fileList;
    private List<Files> resFileImage;
    private String pdfUrl = "";
    private PdfTools pdfTools;
    private CustomOrderDialog customOrderDialog;

    private CustomSnackBar snackBar;
    private SliderImageAdapter adapterSlider;
    private ArrayList<SliderImage> sliderDataArrayList;


    private WatsApp watsApp;
    private InstallWatsApp installWatsApp;
    private String urlEmail = "";
    private Email email;
    private String urlTelegram = "";
    private Telegram telegram;
    private String urlWebSite = "";
    private WebSite webSite;
    private String urlInstagram = "";
    private Instagram instagram;

    private boolean call=false;


    //endregion Parameter
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DetailCompanyFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            init();
            connectToServe();
            initRecycle();
        } catch (Exception ignored) {
        }

    }


    @SuppressLint({"NotifyDataSetChanged", "ResourceAsColor", "UseCompatLoadingForDrawables"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        companyViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        companyViewModel.getResultMessage().setValue(null);
        mainViewModel.getResultMessage().setValue(null);

        binding.progress.setVisibility(View.VISIBLE);
        companyViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;


            binding.progress.setVisibility(View.GONE);
            if (result.getCode() == 3) {
                animationView.setVisibility(View.GONE);
                ShowMessageWarning(result.getDescription());
                return;
            } else if (result.getCode() == -2)
                contactSuccess = false;

            else if (result.getCode() == -3)
                settingSuccess = false;


            if (company.getIp1().equals("") && company.getUser().equals("") && company.getPass().equals("")) {
                binding.tvError15.setText("در حال حاضر این شرکت مجهز به سامانه سفارش گیری آنلاین نمی باشد.");
            } else if (!company.getIp1().equals("") && !company.getUser().equals("") && !company.getPass().equals("") && result.getName().contains("Unterminated")) {
                binding.tvError15.setText("شما در سامانه سفارش آنلاین این شرکت معرفی نشده اید.");

            } else if (result.getCode() == -1 && result.getName().contains("Failed to connect")) {
                binding.tvError15.setText("سفارش گیری آنلاین در حال حاضر غیر فعال می باشد.");
            } else
                binding.tvError15.setText(result.getDescription());


            binding.cardError15.setVisibility(View.VISIBLE);

        });


        mainViewModel.getCallMeStatus(account.getI(), company.getI());

        mainViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            binding.tvCall.setEnabled(true);
            binding.progressCall.setVisibility(View.GONE);
            ShowMessageWarning(result.getDescription());

        });
        mainViewModel.getResultCallMeStatus().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            mainViewModel.getResultCallMeStatus().setValue(null);


            binding.cardCall.setVisibility(View.VISIBLE);

            if (result.size() > 0 && result.get(0).isCallStatus()) {
                call = true;
                binding.tvCall.setBackgroundResource(R.drawable.deactive_button);
                binding.tvCall.setText("انصراف از تماس");
            }

        });
        mainViewModel.getResultSetCompanyStatus().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            mainViewModel.getResultSetCompanyStatus().setValue(null);

            binding.progressCall.setVisibility(View.GONE);

            binding.tvCall.setEnabled(true);
            if (result.size() > 0  ) {
                if (result.get(0).isCallStatus()){
                    call = true;
                    binding.tvCall.setBackgroundResource(R.drawable.deactive_button);
                    binding.tvCall.setText("انصراف از تماس");

                }else {
                    call = false;
                    binding.tvCall.setBackgroundResource(R.drawable.green_button);
                    binding.tvCall.setText("با من تماس بگیر");
                }
            }
        });


        companyViewModel.getCustomerList(userName, passWord, account.getM(), getActivity());
        companyViewModel.getResultCustomerList().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;
            companyViewModel.getResultCustomerList().setValue(null);
            binding.cardCustomer.setVisibility(View.VISIBLE);
            binding.progress.setVisibility(View.GONE);
            customerList.clear();

            try {
                JsonArray customer = result.getAsJsonObject().get("Customer").getAsJsonArray();
                Gson gson = new Gson();
                Type typeCustomer = new TypeToken<List<Customer>>() {
                }.getType();
                List<Customer> customers = gson.fromJson(customer.toString(), typeCustomer);

                customerList.addAll(customers);


                JsonArray customerCatalog = result.getAsJsonObject().get("CustomerCatalog").getAsJsonArray();
                Type typeCustomerCatalog = new TypeToken<List<CustomerCatalog>>() {
                }.getType();
                List<CustomerCatalog> customerCatalogList = gson.fromJson(customerCatalog.toString(), typeCustomerCatalog);

                customerCatalogs.addAll(customerCatalogList);
            } catch (Exception ignored) {
            }


            binding.tvCustomer.setVisibility(View.VISIBLE);

            if (customerList.size() > 0) {
                binding.cardOrder.setVisibility(View.VISIBLE);
                binding.tvCustomer.setText("فروشگاه\u200Cها");

                try {
                    customerAdapter.notifyDataSetChanged();
                } catch (Exception ignored) {
                }
                companyViewModel.getContactId(userName, passWord, account.getM(), getActivity());
            } else {

                binding.cardOrder.setVisibility(View.GONE);
                binding.tvCustomer.setTextColor(R.color.red);
                binding.tvCustomer.setText("شما مشترک این شرکت نیستید لطفا با شرکت تماس گرفته و اطلاعات خود را ثبت کنید.");

            }


        });
        companyViewModel.getResultContactId().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            companyViewModel.getResultContactId().setValue(null);
            contactSuccess = true;
            sharedPreferences.edit().putString("contactId", result.get(0).getCntid()).apply();
            companyViewModel.getSetting(userName, passWord, getActivity());

        });
        companyViewModel.getResultSetting().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            companyViewModel.getResultSetting().setValue(null);
            if (result.size() > 0) {
                settingSuccess = true;
                setSetting(result.get(0));
                if (reRequest) {
                    reRequest = false;
                    companyViewModel.getCatalog(userName, passWord, getActivity());
                }
            }


        });
        companyViewModel.getResultCatalog().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                animationView.setVisibility(View.GONE);
                companyViewModel.getResultCatalog().setValue(null);
                if (result.size() > 0) {
                    catalogsList.clear();
                    if (customerCatalogs.size() > 0) {
                        ArrayList<CustomerCatalog> resCat = new ArrayList<>(customerCatalogs);
                        CollectionUtils.filter(resCat, r -> r.getA().equals(customerChoose.getI()));
                        for (int i = 0; i < resCat.size(); i++) {
                            ArrayList<ModelCatalog> resCatalog = new ArrayList<>(result);
                            int finalI = i;
                            CollectionUtils.filter(resCatalog, r -> r.getI().equals(resCat.get(finalI).getC()) && r.getSts().equals("0"));
                            catalogsList.addAll(resCatalog);

                        }
                    } else {
                        ShowMessageWarning("هیچ کاتالوگی برای این مشتری وجود ندارد.");

                    }
                    if (catalogsList.size() == 1)
                        navigateToCatalogFragment(customerChoose, catalogsList.get(0));
                    else if (catalogsList.size() > 1)
                        dialogShowCatalog.showDialog(getActivity(), false, "انتخاب کاتالوگ", true, "بستن", 1, catalogsList);
                    else
                        ShowMessageWarning("هیچ کاتالوگی برای این مشتری وجود ندارد.");
                } else
                    ShowMessageWarning("هیچ کاتالوگی برای این مشتری وجود ندارد.");


            }
        });
    }


    //region Method

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    private void init() {

        company = new Company();
        installWatsApp = new InstallWatsApp();
        watsApp = new WatsApp();
        email = new Email();
        webSite = new WebSite();
        telegram = new Telegram();
        instagram = new Instagram();

        installWatsApp = new InstallWatsApp();
        telegram.showError(this::ShowMessageWarning);
        email.showError(this::ShowMessageWarning);


        //region Create Size
        DisplayMetrics dm = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        height = dm.widthPixels;
        width = dm.heightPixels;
        //endregion Create Size

        binding.cardImage.getLayoutParams().height = (int) (height * .518);
        sliderDataArrayList = new ArrayList<>();
        adapterSlider = new SliderImageAdapter(getActivity(), sliderDataArrayList);
        pdfTools = new PdfTools();
        snackBar = new CustomSnackBar();
        customOrderDialog = CustomOrderDialog.getInstance();
        customOrderDialog.hideDialog();


        company = Select.from(Company.class).first();
        account = Select.from(Account.class).first();
        userName = company.getUser();
        passWord = company.getPass();
        catalogsList = new ArrayList<>();


        binding.tvNameCompany.setText(company.getN());


        fileList = Select.from(Files.class).list();
        resFileImage = new ArrayList<>(fileList);
        CollectionUtils.filter(resFileImage, f -> f.getMime().contains("image"));
        if (resFileImage.size() == 0)
            binding.cardImage.setVisibility(View.GONE);
        else
            initSlider();


        ArrayList<Files> files = new ArrayList<>(fileList);
        CollectionUtils.filter(files, f -> f.getMime().contains("pdf"));

        if (files.size() > 0)
            binding.pdf.setVisibility(View.VISIBLE);

        binding.pdf.setOnClickListener(view15 -> {
            if (files.size() > 0) {
                pdfUrl = "http://" + Constant.Main_URL_IMAGE + "/getCompanyPdf?id=" + files.get(0).getI();
                pdfTools.showPDFUrl(getActivity(), pdfUrl, sharedPreferences);

            }
        });


        binding.tvphone.setText(company.getT1() != null ? company.getT1() : "");
        binding.btnPhone.setOnClickListener(view12 -> {
            sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + binding.tvphone.getText().toString()));
            startActivity(intent);
        });





        if (company.getAc_area() == 1)
            binding.txtGeo.setText("فعالیت این پخش سراسری می باشد.");

        else if (company.getAc_area() == 2)
            binding.txtGeo.setText("فعالیت این پخش منطقه ای می باشد.");

        else if (company.getAc_area() == 3)
            binding.txtGeo.setText("فعالیت این پخش استانی می باشد.");

        else if (company.getAc_area() == 4)
            binding.txtGeo.setText("فعالیت این پخش محلی می باشد.");




        if (company.getDesc().equals(""))
            binding.txtTitleCompany.setVisibility(View.GONE);


        binding.txtTitleCompany.setText(!company.getAbus().equals("")?company.getAbus():"درباره شرکت");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.txtDescription.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }
        binding.txtDescription.setText(company.getDesc());


        if (!company.getAddr().equals("")) {
            binding.cardAddress.setVisibility(View.VISIBLE);
            binding.txtAddress.setText(company.getAddr());
            if (!company.getLong().equals("") && !company.getLat().equals("") && company.getLong() != null && company.getLat() != null) {
                binding.txtNavigable.setVisibility(View.VISIBLE);
            }
        }


        binding.txtNavigable.setOnClickListener(view -> {
            sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
            String geoUri = "http://maps.google.com/maps?q=loc:" + company.getLat() + "," + company.getLong() + " (" + company.getAddr() + ")";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            startActivity(intent);
        });


        boolean showSocialCard = false;
        if (!company.getWhata().equals("")) {
            binding.cardWhatsapp.setVisibility(View.VISIBLE);
            showSocialCard = true;
        }
        if (!company.getTelg().equals("")) {
            binding.cardTelegram.setVisibility(View.VISIBLE);
            showSocialCard = true;
        }
        if (!company.getINSTA().equals("")) {
            binding.cardInstagram.setVisibility(View.VISIBLE);
            showSocialCard = true;
        }
        if (!company.getWebs().equals("")) {
            binding.cardWeb.setVisibility(View.VISIBLE);
            showSocialCard = true;
        }
        if (!company.getEmail().equals("")) {
            binding.cardEmail.setVisibility(View.VISIBLE);
            showSocialCard = true;
        }

        if (showSocialCard)
            binding.cardSocialMedia.setVisibility(View.VISIBLE);


        binding.cardWhatsapp.setOnClickListener(view -> {

            String urlWhatsApp = company.getWhata();

            if (!urlWhatsApp.equals("")) {


                boolean install = installWatsApp.check(getActivity(), getString(R.string.watApp_b_link));
                if (install) {
                    try {
                        String pkgWhatsApp = installWatsApp.getPkgWatsApp();
                        watsApp.go(urlWhatsApp, pkgWhatsApp, getActivity());
                        sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
                    } catch (Exception ignored) {
                    }


                } else
                    ShowMessageWarning(getString(R.string.error_watsApp));

            }
        });

        binding.cardEmail.setOnClickListener(view18 -> {
            urlEmail = company.getEmail();
            if (!urlEmail.equals("")) {
                try {
                    email.check(getActivity(), urlEmail);
                    sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
                } catch (Exception ignored) {
                }
            }
        });

        binding.cardTelegram.setOnClickListener(view18 -> {
            urlTelegram = company.getTelg();
            if (!urlTelegram.equals("")) {
                try {
                    telegram.check(getActivity(), urlTelegram);
                    sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
                } catch (Exception ignored) {
                }
            }
        });

        binding.cardWeb.setOnClickListener(view18 -> {
            urlWebSite = company.getWebs();
            if (!urlWebSite.equals("")) {
                try {
                    webSite.check(getActivity(), urlWebSite);
                    sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
                } catch (Exception ignored) {
                    int p = 0;
                }
            }
        });

        binding.cardInstagram.setOnClickListener(view18 -> {
            urlInstagram = company.getINSTA();
            if (!urlInstagram.equals("")) {
                try {
                    instagram.check(getActivity(), urlInstagram);
                    sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
                } catch (Exception ignored) {
                }
            }
        });


        //region  Dialog Catalog
        dialogShowCatalog = CustomRecycleDialog.getInstance();
        dialogShowCatalog.setOnClickItemRecycle(position ->
                navigateToCatalogFragment(customerChoose, catalogsList.get(position))
        );
        //endregion  Dialog Catalog

        binding.cardError15.setOnClickListener(view -> {
            binding.cardError15.setVisibility(View.GONE);
            binding.progress.setVisibility(View.VISIBLE);
            companyViewModel.getCustomerList(userName, passWord, account.getM(), getActivity());
        });


        binding.tvCustomer.setOnClickListener(view1 -> {
            binding.progress.setVisibility(View.VISIBLE);
            binding.tvCustomer.setTextColor(requireActivity().getResources().getColor(R.color.normalColor));
            binding.tvCustomer.setText("");
            companyViewModel.getCustomerList(userName, passWord, account.getM(), getActivity());
        });
        binding.cardOrder.setOnClickListener(view13 -> {
            Customer.deleteAll(Customer.class);
            NavDirections action = DetailCompanyFragmentDirections.actionGoToListOrderFragment();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });
        binding.cardGift.setOnClickListener(view14 -> {
            NavDirections action = DetailCompanyFragmentDirections.actionGoToCompanyAdvertise(company.getI(), company.getN());
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });
        binding.cardMessage.setOnClickListener(view14 -> {
            NavDirections action = DetailCompanyFragmentDirections.actionGoToDetailMessage(company.getI(), company.getN());
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });
        binding.ivBack.setOnClickListener(view15 -> Navigation.findNavController(binding.getRoot()).popBackStack());


        binding.tvCall.setOnClickListener(view16 -> {
                    binding.progressCall.setVisibility(View.VISIBLE);
                    binding.tvCall.setEnabled(false);
                    mainViewModel.setStatusCompany(account.getI(), company.getI(), !call);
                }
        );
    }

    private void setSetting(ModelSetting settings) {
        sharedPreferences.edit().putBoolean("no_check_amount_switch", settings.getPa() != null && settings.getPa().equals("1")).apply();
        sharedPreferences.edit().putBoolean("signature_enable_switch", settings.getDs() != null && settings.getDs().equals("1")).apply();

        sharedPreferences.edit().putBoolean("catalog_amount_switch", settings.getCa() != null && settings.getCa().equals("1")).apply();
        sharedPreferences.edit().putBoolean("catalog_page_desc_switch", settings.getCd() != null && settings.getCd().equals("1")).apply();
        sharedPreferences.edit().putString("cmd", settings.getCmd()).apply();
        sharedPreferences.edit().putBoolean("check_online_order", settings.getOo() != null && settings.getOo().equals("1")).apply();
        sharedPreferences.edit().putBoolean("check_returned_check_order", settings.getCrchq() != null && settings.getCrchq().equals("1")).apply();
        sharedPreferences.edit().putBoolean("check_cheque_not_passed_order", settings.getCpchq() != null && settings.getCpchq().equals("1")).apply();
        sharedPreferences.edit().putBoolean("check_account_remaining_order", settings.getCaccb() != null && settings.getCaccb().equals("1")).apply();
        sharedPreferences.edit().putString("StartTime", settings.getSti() != null ? settings.getSti() : "").apply();
        sharedPreferences.edit().putString("EndTime", settings.getEti() != null ? settings.getEti() : "").apply();


    }

    private void navigateToCatalogFragment(Customer customers, ModelCatalog modelCatalog) {
        dialogShowCatalog.hideDialog();
        Customer.deleteAll(Customer.class);
        Customer.saveInTx(customers);
        ModelCatalog.deleteAll(ModelCatalog.class);
        ModelCatalog.saveInTx(modelCatalog);
        NavDirections action = DetailCompanyFragmentDirections.actionGoToCatalogFragment(modelCatalog.getI(), -10);//create order or use from save ord
        Navigation.findNavController(binding.getRoot()).navigate(action);
    }

    private void initSlider() {


        if (resFileImage.size() > 0) {
            binding.cardImage.setVisibility(View.VISIBLE);
            sliderDataArrayList.clear();
            for (int i = 0; i < resFileImage.size(); i++) {
                String picture = "http://" + Constant.Main_URL_IMAGE + "/GetCompanyFile?id=" + resFileImage.get(i).getI() + "&&width=" + 600 + "&&height=" + 400;
                sliderDataArrayList.add(
                        new SliderImage(picture)
                );
            }
            binding.slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            binding.slider.setSliderAdapter(adapterSlider);
            binding.slider.setScrollTimeInSec(3);
            binding.slider.setAutoCycle(true);
            if (sliderDataArrayList.size() == 1)
                binding.slider.stopAutoCycle();
            else
                binding.slider.startAutoCycle();

        }
    }


    private void initRecycle() {
        customerList = new ArrayList<>();
        customerCatalogs = new ArrayList<>();
        customerAdapter = new UniversalAdapter2(R.layout.name_customer_item, customerList, BR.customer);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(customerAdapter);
        customerAdapter.setOnItemClickListener((binding, position) -> {

            try {
                View view = binding.getRoot();
                animationView = view.findViewById(R.id.animationView);
                animationView.setAnimation("animation.json");
                animationView.loop(true);
                animationView.setSpeed(2f);
                animationView.playAnimation();
                animationView.setVisibility(View.VISIBLE);
                customerChoose = customerList.get(position);
                if (!settingSuccess || !contactSuccess) {
                    reRequest = true;
                    companyViewModel.getContactId(userName, passWord, account.getM(), getActivity());
                    return;
                }

                companyViewModel.getCatalog(userName, passWord, getActivity());
            } catch (Exception ignored) {
            }


        });


    }


    //endregion Method

    @Override
    public void onDestroyView() {
        super.onDestroyView();
 /*       Constant.PRODUCTION_BASE_URL = "";
        sharedPreferences.edit().putBoolean("status", false).apply();
        hostSelectionInterceptor.setHostBaseUrl();*/

    }


    private void ShowMessageWarning(String error) {
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }
        snackBar.showSnack(getActivity(), binding.getRoot(), error);

    }

    private void connectToServe() {
        ConnectToServer connectToServer = new ConnectToServer();
        connectToServer.connect(sharedPreferences, hostSelectionInterceptor, true, "http://" + company.getIp1() + "/api/REST/");
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.edit().putBoolean("loginSuccess",true).apply();
    }
}
