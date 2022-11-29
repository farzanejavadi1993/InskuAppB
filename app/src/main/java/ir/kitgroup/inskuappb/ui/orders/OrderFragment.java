package ir.kitgroup.inskuappb.ui.orders;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.orm.query.Select;

import org.apache.commons.collections4.CollectionUtils;


import ir.kitgroup.inskuappb.ConnectServer.CompanyViewModel;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.classes.CustomDialog;
import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Customer;
import ir.kitgroup.inskuappb.dataBase.ModelCatalog;
import ir.kitgroup.inskuappb.dataBase.ModelCatalogPageItem;
import ir.kitgroup.inskuappb.dataBase.Ord;
import ir.kitgroup.inskuappb.classes.CustomProgress;
import ir.kitgroup.inskuappb.databinding.OrderFragmentBinding;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.dataBase.OrdDetail;
import ir.kitgroup.inskuappb.model.Message;
import ir.kitgroup.inskuappb.model.ModelGift;
import ir.kitgroup.inskuappb.util.Constant;

@AndroidEntryPoint
public class OrderFragment extends Fragment {

    //region Parameter
    @Inject
    SharedPreferences sharedPreferences;

    private OrderFragmentBinding binding;
    private CompanyViewModel myViewModel;

    private CustomSnackBar snackBar;
    private Account account;

    //region Config Variable
    private boolean SIG = false;
    private Company company;
    private String userName;
    private String passWord;
    private CustomProgress customProgress;
    DecimalFormat formatter = new DecimalFormat("#,###,###,###");
    private CustomDialog customDialog;
    //endregion Config Variable


    //region Variable  Requirement recyclerView
    private String OrdGid;
    private String customerId;
    private long OrderId;
    private ArrayList<OrdDetail> ordDetailList;
    private List<OrdDetail> ordDetailListTemp;
    private OrderAdapter adapter;
    //endregion Variable Requirement recyclerView


    //region Catalog Detail
    private boolean CatalogDoneDetails = false;
    private Float MaxSumPrice;
    private Float MinSumPrice;
    private Float DefaultDiscount;
    private Float DefPaymentDay;
    private Float MaxPaymentDay;
    private Float MinPaymentDay;

    //endregion Catalog Detail


    private String signature = "";
    private boolean activeSendOrder;
    private Float SumPrices;
    private Float SumDiscountPercent;
    private Float TotalPurePrice;
    private int from = 1;

    //region Account Variable
    private boolean checkAccountingRefresh = false;
    private String returnedCheque = "0";//چک برگشتی
    private String returnedChequeRial = "0";//ریال چک برگشتی
    private String chequeNotPass = "0";//چک های پاس نشده
    private String chequeCredit = "0";//اعتبار چکی
    private String accountRemaining = "0";//مانده حساب
    private String cashCredit = "0";//اعتبار نقدی
    //endregion Account Variable


    private Customer customer;


    //endregion Parameter


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = OrderFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //region Cast Config


        snackBar=new CustomSnackBar();
        customer = Select.from(Customer.class).first();
        OrderId = OrderFragmentArgs.fromBundle(getArguments()).getOrderId();
        String catId = OrderFragmentArgs.fromBundle(getArguments()).getCat();
        OrdGid = OrderFragmentArgs.fromBundle(getArguments()).getOrdGid();
        boolean edit = OrderFragmentArgs.fromBundle(getArguments()).getEdit();
        if (customer == null)
            customerId = OrderFragmentArgs.fromBundle(getArguments()).getCustomerId();
        else
            customerId = customer.getI();


        if (edit)
            binding.layoutEdit.setVisibility(View.VISIBLE);


        account = Select.from(Account.class).first();
        if (OrderId == -10) {
            from = 2;//edit


            //region Config
            List<Ord> rstOrd = Select.from(Ord.class).list();
            if (rstOrd.size() > 0)
                CollectionUtils.filter(rstOrd, o -> o.getDeleted() != null && o.getDeleted());
            if (rstOrd.size() > 0) {
                for (int i = 0; i < rstOrd.size(); i++) {
                    List<OrdDetail> ordDetailList = Select.from(OrdDetail.class).where("O =" + rstOrd.get(i).getId()).list();
                    OrdDetail.deleteInTx(ordDetailList);
                }
                Ord.deleteInTx(rstOrd);
            }

            //endregion Config
        }

        if (OrderId == -10) {
            Ord order = new Ord();
            order.setGid(OrdGid);
            order.setAcid(customerId);
            order.setType(0);
            order.setDate("-");
            order.setPdate("-");
            order.setCat(catId);
            order.setDeleted(true);
            OrderId = Ord.save(order);
        }

        customDialog = CustomDialog.getInstance();
        company = Select.from(Company.class).first();
        userName = company.getUser();
        passWord = company.getPass();
        customProgress = CustomProgress.getInstance();


        if (customer != null) {
            if (customer.getRc() != null && customer.getRr() != null && customer.getNpr() != null && customer.getHc() != null && customer.getRm() != null && customer.getCc() != null) {
                checkAccountingRefresh = true;
            }

            accountRemaining = customer.getRm() != null ? customer.getRm() : "0";
            cashCredit = customer.getCc() != null ? customer.getCc() : "0";
            returnedCheque = customer.getRc() != null ? customer.getRc() : "0";
            returnedChequeRial = customer.getRr() != null ? customer.getRr() : "0";
            chequeNotPass = customer.getNpr() != null ? customer.getNpr() : "0";
            chequeCredit = customer.getHc() != null ? customer.getHc() : "0";
        }
        //endregion Cast Config


        //region Cast Catalog Information
        ModelCatalog catalog = Select.from(ModelCatalog.class).where("I ='" + catId + "'").first();
        if (catalog != null) {
            CatalogDoneDetails = catalog.getDo();
            if (CatalogDoneDetails) {
                MaxSumPrice = catalog.getMaxSumPrice();
                MinSumPrice = catalog.getMinSumPrice();
                DefaultDiscount = catalog.getMaxDiscountPercent1();
                MaxPaymentDay = catalog.getMaxDayPayment();
                MinPaymentDay = catalog.getMinDayPayment();
                DefPaymentDay = catalog.getDefaultPayment();
            }
        }
        //endregion Cast Catalog Information


        //region Config RecyclerView
        ModelCatalogPageItem.deleteAll(ModelCatalogPageItem.class);
        ordDetailList = new ArrayList<>();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new OrderAdapter(getActivity(), ordDetailList, sharedPreferences, OrderId, from);
        binding.recyclerView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Calculate();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                Calculate();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                Calculate();
            }
        });
        adapter.OnItemChange(() -> Calculate());
        //endregion Config RecyclerView


        //region Action btnRegister
        binding.btnRegisterOrder.setOnClickListener(view1 -> {
            try {


            List<OrdDetail> orderDetailList = Select.from(OrdDetail.class).where("O =" + OrderId).list();
            if (orderDetailList.size() == 0) {
                ShowMessageWarning("هیچ سفارشی ثبت نشده است");

                return;
            }
            ArrayList<OrdDetail> resultOrdDtl = new ArrayList<>(orderDetailList);
            CollectionUtils.filter(resultOrdDtl, r -> r.getAmount1() == 0);
            if (resultOrdDtl.size() > 0) {
                ShowMessageWarning("برای ردیف های سفارش مقدار وارد کنید.");

                return;
            } else if (Constant.GetAccountRemaining(sharedPreferences) && !cashCredit.equals("0.0") && TotalPurePrice > Double.parseDouble(cashCredit) - Double.parseDouble(accountRemaining)) {
                double RialOrder = Double.parseDouble(cashCredit) - Double.parseDouble(accountRemaining);
                String rial;
                try {
                    rial = formatter.format(RialOrder);
                } catch (Exception ignored) {
                    rial = String.valueOf(RialOrder);
                }
                ShowMessageWarning("حداکثر مبلغ سفارش برای این مشتری " + rial + " ریال می باشد");
                return;
            } else if (CatalogDoneDetails && TotalPurePrice > MaxSumPrice && orderDetailList.size() > 0) {
                String max;

                try {
                    max = formatter.format(MaxSumPrice);
                } catch (Exception ignored) {
                    max = String.valueOf(MaxSumPrice).replace(".0", "");
                }
                ShowMessageWarning("حداکثر مقدار خرید " + max + "ریال می باشد. ");
                return;
            } else if (CatalogDoneDetails && TotalPurePrice < MinSumPrice && orderDetailList.size() > 0) {
                String min;
                try {
                    min = formatter.format(MinSumPrice);
                } catch (Exception ignored) {
                    min = String.valueOf(MinSumPrice).replace(".0", "");
                }

                ShowMessageWarning("حداقل مقدار خرید " + min + "ریال می باشد. ");
                return;

            } else if (!Constant.connectedToInternet(getActivity())) {

                ShowMessageWarning("اتصال به اینترنت با خطا مواجه شد , لطفا اتصال اینترنت خود را فعال نمایید.");

                return;
            }
            if (checkAccountingRefresh && Constant.GetReturnedCheque(sharedPreferences)
                    && Double.parseDouble(returnedCheque) > 0
                    || Double.parseDouble(returnedChequeRial) > 0) {


                ShowMessageWarning("مشتری چک برگشتی دارد ،  ثبت سفارش برای این مشتری ممکن نیست.");

                return;
            } else if (
                    checkAccountingRefresh
                    && Constant.GetChequeNotPassed(sharedPreferences)
                    && (Double.parseDouble(chequeNotPass) > Double.parseDouble(chequeCredit) )
                    && (Double.parseDouble(cashCredit)<Double.parseDouble(accountRemaining))
            ) {


                ShowMessageWarning(  "مبلغ چک های پاس نشده این مشتری بیشتر از اعتبار چکی وی می باشد لذا ثبت سفارش برای این مشتری ممکن نیست.");
                return;

            } else if (checkAccountingRefresh && Constant.GetAccountRemaining(sharedPreferences) && Double.parseDouble(accountRemaining) > 0 && Double.parseDouble(accountRemaining) > Double.parseDouble(cashCredit)) {
                ShowMessageWarning( "مانده حساب این مشتری بیشتر از اعتبار نقدی وی می باشد لذا ثبت سفارش برای این مشتری ممکن نیست.");
                return;


            }


            if (Constant.GetSignatureSt(sharedPreferences))
                SIG = true;

            ArrayList<OrdDetail> resultList = new ArrayList<>(orderDetailList);
            CollectionUtils.filter(resultList, r -> !r.getGift());
            for (int i = 0; i < resultList.size(); i++) {
                if (resultList.get(i).getAmount1() != resultList.get(i).getIAM() && !resultList.get(i).edit) {
                    resultList.get(i).setAmount1(resultList.get(i).getIAM());
                }
                if (!resultList.get(i).editDiscount && resultList.get(i).getDiscountPercent() >= resultList.get(i).getDiscountPercentP()) {
                    resultList.get(i).setDiscountPercent(resultList.get(i).getDiscountPercent() - resultList.get(i).getDiscountPercentP());
                }
            }


            customDialog.showDialog(getActivity(), "آیا مایل به ارسال سفارش هستید؟", true, "بستن", "ارسال سفارش", SIG,true,true);
            }catch (Exception ignored){}

        });
        //endregion Action btnRegister


        //region Settlement Day

       binding.edtSettlement.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               s = Constant.toEnglishNumber(s.toString());

               if (!s.toString().isEmpty()) {

                   float i = 0;
                   try {
                       i = Float.parseFloat(s.toString());
                   } catch (Exception ignored) {
                   }
                   if (i > MaxPaymentDay) {
                       ShowMessageWarning("حدااکثر تعداد روز تسویه برابر است با : " + MaxPaymentDay);
                       binding.edtSettlement.setText("");


                   } else if (i < MinPaymentDay) {

                       ShowMessageWarning("حداقل تعداد روز تسویه برابر است با : " + MinPaymentDay);
                       binding.edtSettlement.setText("");

                   }


               }

           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
        int pDay=0;
       try {
            pDay = (int) Double.parseDouble(String.valueOf(DefPaymentDay));
        } catch (Exception ignored) {}


        if (CatalogDoneDetails && pDay <= MaxPaymentDay && pDay >= MinPaymentDay) {
            binding.edtSettlement.setText(String.valueOf(pDay));
        }

        //endregion Settlement Day





        customDialog.setOnClickClearButton((active, SIG) -> {
            activeSendOrder = active;
            signature = SIG;
        });

        customDialog.setOnClickNegativeButton(() -> {
            customDialog.hideProgress();
            Navigation.findNavController(binding.getRoot()).popBackStack();
            Navigation.findNavController(binding.getRoot()).popBackStack();
        });


        customDialog.setOnClickPositiveButton(() -> {
            customDialog.hideProgress();
            if (!activeSendOrder && Constant.GetSignatureSt(sharedPreferences)) {

               ShowMessageWarning("لطفا در محل مربوطه امضا بزنید ");
                return;
            }

            List<OrdDetail> orderDetailList = Select.from(OrdDetail.class).where("O =" + OrderId).list();
            Date todayDate = Calendar.getInstance().getTime();
            @SuppressLint("SimpleDateFormat") DateFormat dateFormats =
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String DateNow = dateFormats.format(todayDate);
            Ord ord3 = Ord.findById(Ord.class, OrderId);
            ord3.setPch("0");
            ord3.setCntid(sharedPreferences.getString("contactId", ""));
            ord3.setConf("0");
            ord3.setItype("2");
            ord3.setPricetype("1");
            ord3.setDate(DateNow);
            ord3.setPday("");
            ord3.setDesc("");
            ord3.setDisc1(DefaultDiscount != null ? DefaultDiscount.toString() : "0.0");
            ord3.setDisc2("");
            ord3.setDisc3("");
            ord3.setSumpprice(Double.toString(SumPrices));
            ord3.setSumdprice(String.valueOf(SumDiscountPercent));
            ord3.setSumnprice(String.valueOf(TotalPurePrice));
            ord3.setPaytype("");
            ord3.setSig(signature);
            ord3.setDesc(binding.edtDescription.getText().toString());
            ord3.setAcid(customerId);
            ord3.setApp("");
            ord3.setPday(binding.edtSettlement.getText().toString().equals("") ? "0" : binding.edtSettlement.getText().toString());

            CollectionUtils.filter(orderDetailList, o -> o.getGifttype() != 3);
            OrdDetail.saveInTx(orderDetailList);

            List<Ord> ords = new ArrayList<>();
            ords.add(ord3);
            List<OrdDetail> ordDtls = Select.from(OrdDetail.class).where("O =" + OrderId).list();
            List<ModelGift> modelGifts = new ArrayList<>();
            customProgress.showProgress(getContext(), "در حال ارسال سفارش...", false);
            myViewModel.sendOrder(userName, passWord, ords, ordDtls, modelGifts,getActivity());
        });

        binding.ivBack.setOnClickListener(view12 -> Navigation.findNavController(binding.getRoot()).popBackStack());

        binding.btnEdit.setOnClickListener(view13 -> {
            NavDirections action = OrderFragmentDirections.actionGoToCatalogFragment(catId,OrderId);
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });


        binding.btnDelete.setOnClickListener(view13 -> {
            Ord ord3 = Ord.findById(Ord.class, OrderId);
            binding.progressBar.setVisibility(View.VISIBLE);
            myViewModel.deleteOrder(userName, passWord, ord3.getGid(),getActivity());
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            myViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
            myViewModel.getResultMessage().setValue(null);
            binding.progressBar.setVisibility(View.VISIBLE);
            if (!Constant.connectedToInternet(requireActivity())) {
                myViewModel.getResultMessage().setValue(new Message(-1, "اتصال به اینترنت برقرار نیست", ""));

            }
            ArrayList<OrdDetail> resultTemp = new ArrayList<>();

            //region Get Data Of Customer From Server
            Ord order = Ord.findById(Ord.class, OrderId);
            if (order != null && order.getDeleted() && from == 2) {
                binding.btnRegisterOrder.setVisibility(View.GONE);
                binding.edtDescription.setEnabled(false);
                binding.edtSettlement.setEnabled(false);
                binding.txtDeleteAll.setVisibility(View.GONE);
                myViewModel.getCustomerList(userName, passWord, account.getM(),getActivity());
            }
            //endregion Get Data Of Customer FromServer


            //region GetData From Local
            else {
                getProduct(resultTemp);
            }
            //endregion GetData From Local


            myViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {

                if (result==null)
                    return;

                binding.progressBar.setVisibility(View.GONE);
                customDialog.hideProgress();
                customProgress.hideProgress();
                ShowMessageWarning(result.getDescription());



            });

            myViewModel.getResultOrder().observe(getViewLifecycleOwner(), result -> {

                if (result == null)
                    return;

                JsonArray orderDetail = result.getAsJsonObject().get("OrderDetail").getAsJsonArray();
                Gson gson = new Gson();
                Type typeProduct = new TypeToken<List<OrdDetail>>() {
                }.getType();
                List<OrdDetail> orderDetailsLists = gson.fromJson(orderDetail.toString(), typeProduct);
                CollectionUtils.filter(orderDetailsLists, o -> o.getOi().equals(OrdGid));
                for (int i = 0; i < orderDetailsLists.size(); i++) {
                    orderDetailsLists.get(i).setIdOrders(OrderId);
                }

                OrdDetail.saveInTx(orderDetailsLists);
                getProduct(resultTemp);
                myViewModel.getResultOrder().setValue(null);
            });
            myViewModel.getResultCatalogPageItem().observe(getViewLifecycleOwner(), result -> {

                if (result == null)
                    return;

                if (result.size() > 0)
                    ModelCatalogPageItem.save(result.get(0));

                if (resultTemp.size() > 0 && Select.from(ModelCatalogPageItem.class).list().size() == resultTemp.size()) {
                    ordDetailList.clear();
                    ordDetailList.addAll(ordDetailListTemp);
                    ordDetailListTemp.clear();
                    try {
                        adapter.notifyDataSetChanged();
                    }catch (Exception ignored){}
                }
                binding.progressBar.setVisibility(View.GONE);
                myViewModel.getResultCatalogPageItem().setValue(null);

            });
            myViewModel.getResultSendOrder().observe(getViewLifecycleOwner(), result -> {
                binding.progressBar.setVisibility(View.GONE);
                customProgress.hideProgress();
                if (result == null)
                    return;
                if (result.get(0).getMessage() == 4) {
                    myViewModel.getResultSendOrder().setValue(null);
                    List<OrdDetail> orderDetailList = Select.from(OrdDetail.class).where("O =" + OrderId).list();
                    Ord ord = Ord.findById(Ord.class, OrderId);

                    OrdDetail.deleteInTx(orderDetailList);
                    Ord.deleteInTx(ord);


                    customDialog.showDialog(getActivity(), "با موفقیت ارسال شد", false, "بازگشت", "", false,false,true);


                } else {
                    myViewModel.getResultMessage().setValue(new Message(-1, "خطا در ارسال سفارش" + "\n" + result.get(0).getCurrent(), ""));
                }

            });
            myViewModel.getResultDeleteOrder().observe(getViewLifecycleOwner(), result -> {
                binding.progressBar.setVisibility(View.GONE);
                if(result==null)
                    return;
                myViewModel.getResultDeleteOrder().setValue(null);

                if (result.get(0).getMessage()==4) {

                Navigation.findNavController(binding.getRoot()).popBackStack();

                }else
                    myViewModel.getResultMessage().setValue(new Message(-1,"خطا در حذف سفارش",""));

            });
            myViewModel.getResultCustomerList().observe(getViewLifecycleOwner(), result -> {

                if (result == null)
                    return;

                myViewModel.getResultCustomerList().setValue(null);

                JsonArray c = result.getAsJsonObject().get("Customer").getAsJsonArray();
                Gson gson = new Gson();
                Type typeCustomer = new TypeToken<List<Customer>>() {
                }.getType();
                List<Customer> customers1 = gson.fromJson(c.toString(), typeCustomer);



                if (customers1.size() > 0)
                    CollectionUtils.filter(customers1, r -> r.getI().equals(customerId));
                if (customers1.size() > 0) {
                    Customer.saveInTx(customers1.get(0));

                    customer = customers1.get(0);

                    if (customer.getRc() != null && customer.getRr() != null && customer.getNpr() != null && customer.getHc() != null && customer.getRm() != null && customer.getCc() != null) {
                        checkAccountingRefresh = true;
                    }

                    accountRemaining = customer.getRm() != null ? customer.getRm() : "0";
                    cashCredit = customer.getCc() != null ? customer.getCc() : "0";
                    returnedCheque = customer.getRc() != null ? customer.getRc() : "0";
                    returnedChequeRial = customer.getRr() != null ? customer.getRr() : "0";
                    chequeNotPass = customer.getNpr() != null ? customer.getNpr() : "0";
                    chequeCredit = customer.getHc() != null ? customer.getHc() : "0";
                    myViewModel.getOrder(userName, passWord, sharedPreferences.getString("contactId", ""),getActivity());

                } else {
                ShowMessageWarning("این مشتری در سرور وجود ندارد.");
                }



            });


        } catch (Exception e) {}

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    //region Method


    @SuppressLint("SetTextI18n")
    private void Calculate() {
        List<OrdDetail> list = Select.from(OrdDetail.class).where("O =" + OrderId).list();
        CollectionUtils.filter(list, l -> l.getGifttype() == 4);
        float sumPrice = 0;
        float sumDiscount = 0;
        float sumDiscountOrder = 0;
        float totalPrice = 0;
        for (int i = 0; i < list.size(); i++) {
            float sumPriceRow = list.get(i).getAmount1() * list.get(i).getPrice1();
            float sumDiscountRow = sumPriceRow * (list.get(i).getDiscountPercent() / 100);
            float totalPriceRow = sumPriceRow - sumDiscountRow;
            sumPrice = sumPrice + sumPriceRow;
            sumDiscount = sumDiscount + sumDiscountRow;
            totalPrice = totalPrice + totalPriceRow;
        }

        if (DefaultDiscount != null)
            sumDiscountOrder = totalPrice * (DefaultDiscount / 100);


        totalPrice = totalPrice - sumDiscountOrder;
        binding.tvSumPrice.setText(formatter.format(sumPrice));
        binding.tvSumDiscount.setText(formatter.format(sumDiscount));
        binding.txtDiscountOrder.setText("تخفیف فاکتور" + " " + (DefaultDiscount != null ? formatter.format(DefaultDiscount) : "0") + "%");
        binding.tvDiscountOrder.setText(formatter.format(sumDiscountOrder));
        binding.tvTotalPrice.setText(formatter.format(totalPrice));

        SumDiscountPercent = sumDiscountOrder;
        TotalPurePrice = totalPrice;
        SumPrices = sumPrice;


    }

    private void getProduct(ArrayList<OrdDetail> resultTemp) {
        ordDetailListTemp = Select.from(OrdDetail.class).where("O =" + OrderId).list();
        resultTemp.clear();
        resultTemp.addAll(ordDetailListTemp);
        if (ordDetailListTemp.size() == 0) {
            binding.progressBar.setVisibility(View.GONE);
            ShowMessageWarning("هیچ سفارشی ثبت نشده است");

            return;
        }
        CollectionUtils.filter(resultTemp, r -> r.getPldi() != null && !r.getPldi().equals(""));
        for (int i = 0; i < resultTemp.size(); i++) {
            myViewModel.getCatalogPageItem(userName, passWord, resultTemp.get(i).getPldi(),getActivity());
        }

    }

    private void ShowMessageWarning(String error) {
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }
        snackBar.showSnack(getActivity(), binding.getRoot(), error);

    }
    //endregion Method

}
