package ir.kitgroup.inskuappb.ui.launcher.homeItem;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.orm.query.Select;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ui.viewmodel.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;


import ir.kitgroup.inskuappb.component.AccountFilter;
import ir.kitgroup.inskuappb.component.CreateCityDialog;

import ir.kitgroup.inskuappb.component.dialog.CustomSnackBar;


import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.BusinessR;
import ir.kitgroup.inskuappb.dataBase.City;
import ir.kitgroup.inskuappb.dataBase.Guild;
import ir.kitgroup.inskuappb.dataBase.State;
import ir.kitgroup.inskuappb.databinding.FilterFragmentBinding;

import ir.kitgroup.inskuappb.util.Constant;

@AndroidEntryPoint
public class FilterFragment extends Fragment implements Filterable {

    //region Parameter
    @Inject
    SharedPreferences sharedPreferences;

    private FilterFragmentBinding binding;
    private MainViewModel myViewModel;
    private String appVersion;


    //region Variable BusinessRelation
    private List<BusinessR> selectedBusinessItems;//لیست ارتباط تجاری هایی که از قبل سیو شده
    private ArrayList<BusinessR> chooseBusinessRs;//لیست ارتباط تجاری هایی که انتخاب میشوند
    private ArrayList<BusinessR> businessRs;//لیست ارتباط تجاری محدود
    private ArrayList<BusinessR> allBusinessRs;//همه لیست ارتباط تجاری
    private UniversalAdapter2 adapterBusinessR;
    private boolean showBusinessR = false;
    //endregion Variable BusinessRelation


    //region Variable Guild
    private List<Guild> selectedGuildItems;
    private ArrayList<Guild> choseGuilds;
    private ArrayList<Guild> guilds;
    private ArrayList<Guild> allGuilds;
    private UniversalAdapter2 adapterGuild;
    private TextWatcher textWatcherGuild;

    //endregion Variable Guild


    //region Variable State
    private final Object mLock = new Object();
    private ArrayList<Guild> mOriginalValuesGuild;
    private ArrayList<State> mOriginalValuesState;
    private ArrayList<City> mOriginalValuesCity;
    private ArrayList<String> guidStates;
    private List<State> selectedStateItems;
    private ArrayList<State> chooseStates;
    private ArrayList<State> states;
    private ArrayList<State> allStates;
    private UniversalAdapter2 adapterState;
    private TextWatcher textWatcherState;

    //endregion Variable State

    private List<Account> accountList;
    //region Variable City

    private ArrayList<City> chooseCities;
    private ArrayList<City> cities;
    private ArrayList<City> allCities;
    private UniversalAdapter2 adapterCity;
    private TextWatcher textWatcherCity;
    private boolean showCreateCityDialog = false;
    private String addCity = "";
    private CreateCityDialog createCityDialog;

    //endregion Variable City


    private boolean guild = false;
    private boolean state = false;

    private String wordSearchCity;


    private boolean guildSearch = false;
    private boolean stateSearch = false;
    private boolean citySearch = true;


    //endregion Parameter


    private String phoneNumber = "";
    private String from = "";
    private CustomSnackBar snackBar;


    //region Override Method

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FilterFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            getBundle();
            init();
            initCreateCityDialog();
            getAppVersion();
            initGuildRecyclerView();
            initStateRecyclerView();
            initCityRecyclerView();
            initBusinessRelationRecyclerView();
        } catch (Exception ignored) {}


    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        myViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        nullTheMutable();


        myViewModel.getGuild();
        myViewModel.getState();

        if (from.equals("home"))
            myViewModel.getBusinessRelations(getAccountFilter());



        myViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;

            binding.btnDoFilter.setBackgroundColor(getResources().getColor(R.color.color_primary_dark));
            binding.btnDoFilter.setEnabled(true);
            binding.progress.setVisibility(View.GONE);


            if (result.getCode() == 3)
                ShowMessageWarning(result.getDescription());
            else if (result.getCode() == 4) {
                createCityDialog.showDialog(getActivity(), addCity, result.getDescription() + "\n" + "دوباره سعی کنید.", true);
            } else {
                binding.tvError8.setText(result.getDescription());
                binding.cardError8.setVisibility(View.VISIBLE);
            }
            binding.progress.setVisibility(View.GONE);


        });
        myViewModel.getResultAddCity().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;
            binding.progress.setVisibility(View.GONE);

            myViewModel.getResultAddCity().setValue(null);
            if (result.get(0).getMessage() == 3) {
                createCityDialog.showDialog(getActivity(), addCity, result.get(0).getDescription(), true);
            } else
                binding.edtCity.removeTextChangedListener(textWatcherCity);
            binding.edtCity.setText(addCity);
            binding.edtCity.addTextChangedListener(textWatcherCity);
            chooseCities.clear();
            City city = new City();
            city.setName(addCity);
            city.setI(result.get(0).getCurrent());
            chooseCities.add(0, city);


        });
        myViewModel.getResultGuild().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;

            myViewModel.getResultGuild().setValue(null);
            guild = true;
            if (state) {
                binding.cardContent.setVisibility(View.VISIBLE);
                binding.progress.setVisibility(View.GONE);
            }
            if (result.size() > 0) {
                allGuilds.clear();
                allGuilds.addAll(result);
                if (allGuilds.size() > 0)
                    binding.cardGuild.setVisibility(View.VISIBLE);
                guilds.clear();
                guilds.addAll(allGuilds);
                binding.recyclerGuild.setVisibility(View.GONE);
            }

        });
        myViewModel.getResultState().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;

            myViewModel.getResultState().setValue(null);
            state = true;
            if (guild) {
                binding.cardContent.setVisibility(View.VISIBLE);
                binding.progress.setVisibility(View.GONE);
            }
            if (result.size() > 0) {
                allStates.clear();
                allStates.addAll(result);
                states.clear();
                states.addAll(allStates);
                binding.recyclerState.setVisibility(View.GONE);
            }
            if (!phoneNumber.equals("") && guidStates.size() > 0 && accountList.size() > 0 && accountList.get(0).getCityId() == null) {
                allCities.clear();
                cities.clear();
                adapterCity.notifyDataSetChanged();
                myViewModel.getCity(guidStates);
            }

        });
        myViewModel.getResultBusinessRelations().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            binding.progress.setVisibility(View.GONE);
            myViewModel.getResultBusinessRelations().setValue(null);
            if (result.size() > 0) {

                allBusinessRs.clear();
                allBusinessRs.addAll(result);

                for (BusinessR slcItem : selectedBusinessItems) {
                    ArrayList<BusinessR> bsrList = new ArrayList<>(allBusinessRs);
                    CollectionUtils.filter(bsrList, s -> s.getI().equals(slcItem.getI()));
                    if (bsrList != null)
                        allBusinessRs.get(allBusinessRs.indexOf(bsrList.get(0))).setSave(true);

                }


                //region Manage BusinessR Adapter
                businessRs.clear();
                ArrayList<BusinessR> resBussinesRSave = new ArrayList<>(allBusinessRs);
                CollectionUtils.filter(resBussinesRSave, BusinessR::isSave);

                ArrayList<BusinessR> resBussinesRNotSave = new ArrayList<>(allBusinessRs);
                CollectionUtils.filter(resBussinesRNotSave, r -> !r.isSave());


                allBusinessRs.clear();
                allBusinessRs.addAll(resBussinesRSave);
                allBusinessRs.addAll(resBussinesRNotSave);


                if (allBusinessRs.size() > 0 && phoneNumber.equals(""))
                    binding.cardBusinessR.setVisibility(View.VISIBLE);
                if (allBusinessRs.size() > 3) {
                    businessRs.addAll(allBusinessRs.subList(0, 3));
                    binding.btnShowBusinessR.setVisibility(View.VISIBLE);
                } else
                    businessRs.addAll(allBusinessRs);

                try {
                    adapterBusinessR.notifyDataSetChanged();
                } catch (Exception ignored) {
                }
                //endregion Manage BusinessR Adapter
            } else
                ShowMessageWarning("هیچ نوع پخشی وجود ندارد");

        });
        myViewModel.getResultCity().observe(getViewLifecycleOwner(), result -> {
            binding.progress.setVisibility(View.GONE);
            if (result == null)
                return;

            myViewModel.getResultCity().setValue(null);

            if (result.size() > 0) {
                allCities.clear();
                allCities.addAll(result);
                binding.recyclerCity.setVisibility(View.VISIBLE);
                cities.clear();
                cities.addAll(allCities);
                try {
                    adapterCity.notifyDataSetChanged();
                } catch (Exception ignored) {
                }
                if (wordSearchCity != null && wordSearchCity.length() > 1)
                    getFilter().filter(wordSearchCity);
            } else {
                allCities.clear();
                cities.clear();
                adapterCity.notifyDataSetChanged();


                if (!showCreateCityDialog) {
                    showCreateCityDialog = true;
                    createCityDialog.showDialog(getActivity(), addCity, getActivity().getResources().getString(R.string.add_city), false);
                }
//                ShowMessageWarning("هیچ شهری یافت نشد.");
            }

        });
        myViewModel.getResultAddAccount().observe(getViewLifecycleOwner(), result -> {
            binding.btnDoFilter.setBackgroundColor(getResources().getColor(R.color.color_primary_dark));
            binding.btnDoFilter.setEnabled(true);
            binding.progress.setVisibility(View.GONE);

            if (result == null)
                return;

            myViewModel.getResultAddAccount().setValue(null);
            if (result.get(0).getMessage() == 1) {
                Account.deleteAll(Account.class);
                accountList.get(0).setI(result.get(0).getCurrent());
                Account.saveInTx(accountList);
                if (from.equals("profile")) {
                    Toasty.success(getActivity(), "اطلاعات شما با موفقیت بروز رسانی شد", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(binding.getRoot()).popBackStack();
                } else {
                    NavDirections action = ir.kitgroup.inskuappb.ui.launcher.homeItem.FilterFragmentDirections.actionGoToHomeFragment();
                    Navigation.findNavController(binding.getRoot()).navigate(action);
                }

            } else
                Toasty.error(requireActivity(), result.get(0).getMessage(), Toast.LENGTH_SHORT, true).show();
        });



    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                ArrayList tempList = null;
                if (guildSearch)
                    tempList = filterGuild(allGuilds, constraint);
                else if (stateSearch)
                    tempList = filterState(allStates, constraint);
                else if (citySearch)
                    tempList = filterCity(allCities, constraint);


                filterResults.values = tempList;
                filterResults.count = tempList.size();
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {

                if (citySearch) {

                    cities.clear();
                    cities.addAll((Collection<? extends City>) results.values);

                    if (cities.size() == 0) {
                        if (!showCreateCityDialog) {
                            showCreateCityDialog = true;
                            createCityDialog.showDialog(getActivity(), "", getActivity().getResources().getString(R.string.add_city), false);
                        }

                    }

//                        myViewModel.getResultMessage().setValue(new Message(3, "", "هیچ شهری برای استان مورد نظرشما یافت نشد."));
                    try {
                        adapterCity.notifyDataSetChanged();
                    } catch (Exception ignored) {
                    }
                } else if (stateSearch) {
                    states.clear();
                    states.addAll((Collection<? extends State>) results.values);
                    try {
                        adapterState.notifyDataSetChanged();
                    } catch (Exception ignored) {
                    }
                    binding.recyclerState.setVisibility(View.VISIBLE);
                } else if (guildSearch) {
                    guilds.clear();
                    guilds.addAll((Collection<? extends Guild>) results.values);
                    try {
                        adapterGuild.notifyDataSetChanged();
                    } catch (Exception ignored) {
                    }
                    binding.recyclerGuild.setVisibility(View.VISIBLE);
                }


            }
        };
    }

    //endregion Override Method


    //region Method

    private void nullTheMutable() {
        myViewModel.getResultMessage().setValue(null);
        myViewModel.getResultAddCity().setValue(null);
        myViewModel.getResultGuild().setValue(null);
        myViewModel.getResultState().setValue(null);
        myViewModel.getResultBusinessRelations().setValue(null);
        myViewModel.getResultCity().setValue(null);
        myViewModel.getResultAddAccount().setValue(null);
    }
    //region BusinessR Method
    private void setSaveBussines(int position, boolean save) {
        for (BusinessR m : chooseBusinessRs) {
            if (m.getI().equals(allBusinessRs.get(position).getI()))
                chooseBusinessRs.get(chooseBusinessRs.indexOf(m)).setSave(save);
        }
    }

    private void setTextBtnShowBussinesR() {
        ArrayList<BusinessR> res = new ArrayList<>(chooseBusinessRs);
        CollectionUtils.filter(res, BusinessR::isSave);
        if (showBusinessR)
            binding.btnShowBusinessR.setText(res.size() > 0 ? "بستن" + " ( " + (res.size()) + " انتخاب )" : "نمایش همه");
        else
            binding.btnShowBusinessR.setText(res.size() > 0 ? "نمایش همه" + " ( " + (res.size()) + " انتخاب )" : "نمایش همه");
    }
    //endregion BusinessR Method


    private ArrayList filterGuild(ArrayList<Guild> allGuilds, CharSequence constraint) {

        ArrayList tempList = new ArrayList();

        if (mOriginalValuesGuild == null) {
            synchronized (mLock) {
                mOriginalValuesGuild = new ArrayList(allGuilds);
            }
        }

        if (!constraint.toString().equals("") && constraint != null && allGuilds != null) {

            String[] tempSearch = constraint.toString().trim().split(" ");

            int counter;
            int searchSize = tempSearch.length;

            for (Guild item : mOriginalValuesGuild) {


                counter = 0;
                for (String searchItem : tempSearch) {

                    if (item.getName().contains(searchItem)) {
                        counter++;
                    }
                }

                if (counter == searchSize)
                    tempList.add(item);
            }


        } else {
            synchronized (mLock) {
                tempList = new ArrayList(mOriginalValuesGuild);
            }


        }
        return tempList;
    }

    private ArrayList filterState(ArrayList<State> allGuilds, CharSequence constraint) {

        ArrayList tempList = new ArrayList();

        if (mOriginalValuesState == null) {
            synchronized (mLock) {
                mOriginalValuesState = new ArrayList(allGuilds);
            }
        }

        if (!constraint.toString().equals("") && constraint != null && allGuilds != null) {

            String[] tempSearch = constraint.toString().trim().split(" ");

            int counter;
            int searchSize = tempSearch.length;

            for (State item : mOriginalValuesState) {


                counter = 0;
                for (String searchItem : tempSearch) {

                    if (item.getName().contains(searchItem)) {
                        counter++;
                    }
                }

                if (counter == searchSize)
                    tempList.add(item);
            }


        } else {
            synchronized (mLock) {
                tempList = new ArrayList(mOriginalValuesState);
            }


        }
        return tempList;
    }

    private ArrayList filterCity(ArrayList<City> allCity, CharSequence constraint) {

        ArrayList tempList = new ArrayList();

        if (mOriginalValuesCity == null) {
            synchronized (mLock) {
                mOriginalValuesCity = new ArrayList(allCity);
            }
        }

        if (!constraint.toString().equals("") && constraint != null && allCity != null) {

            String[] tempSearch = constraint.toString().trim().split(" ");

            int counter;
            int searchSize = tempSearch.length;

            for (City item : mOriginalValuesCity) {


                counter = 0;
                for (String searchItem : tempSearch) {

                    if (item.getName().contains(searchItem)) {
                        counter++;
                    }
                }

                if (counter == searchSize)
                    tempList.add(item);
            }


        } else {
            synchronized (mLock) {
                tempList = new ArrayList(mOriginalValuesCity);
            }


        }
        return tempList;
    }

    private void ShowMessageWarning(String error) {
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }
        snackBar.showSnack(getActivity(), binding.getRoot(), error);

    }

    private AccountFilter getAccountFilter() {
        AccountFilter accountFilter = new AccountFilter();

        ArrayList<String> cityIdList = new ArrayList<>();
        if (chooseCities.size() > 0)
            cityIdList.add(chooseCities.get(0).getI());


        ArrayList<String> stateIdList = new ArrayList<>();
        if (chooseStates.size() > 0)
            stateIdList.add(chooseStates.get(0).getI());

        ArrayList<String> guildIdList = new ArrayList<>();
        if (choseGuilds.size() > 0)
            guildIdList.add(choseGuilds.get(0).getI());

        accountFilter.City = cityIdList;
        accountFilter.State = stateIdList;
        accountFilter.Guild = guildIdList;

        return accountFilter;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void callBusinessR() {
        businessRs.clear();
        allBusinessRs.clear();
        adapterBusinessR.notifyDataSetChanged();
        binding.btnShowBusinessR.setVisibility(View.GONE);
        myViewModel.getBusinessRelations(getAccountFilter());
    }

    private void init() {
        accountList = Select.from(Account.class).list();
        snackBar = new CustomSnackBar();
        if (accountList.size() > 0) {

            binding.edtMobile.setText(accountList.get(0).getM());
            binding.edtMobileM.setText(accountList.get(0).getPm());
            binding.edtName.setText(accountList.get(0).getN());
            binding.edtLastName.setText(accountList.get(0).getLn());
            binding.edtNameStore.setText(accountList.get(0).getShn());

            if (from.equals("splash") || from.equals("verify")) {

                if (accountList.get(0).getPm() != null && !accountList.get(0).getPm().equals(""))
                    binding.edtMobileM.setEnabled(false);

                if (accountList.get(0).getN() != null && !accountList.get(0).getN().equals("")) {
                    binding.txtStarName.setVisibility(View.GONE);
                    binding.edtName.setEnabled(false);
                }

                if (accountList.get(0).getLn() != null && !accountList.get(0).getLn().equals("")) {
                    binding.txtStarLastName.setVisibility(View.GONE);
                    binding.edtLastName.setEnabled(false);
                }

                if (accountList.get(0).getShn() != null && !accountList.get(0).getShn().equals("")) {
                    binding.edtNameStore.setEnabled(false);
                    binding.tvStarNameStore.setVisibility(View.GONE);
                }
            }

        } else
            binding.edtMobile.setText(phoneNumber);


        binding.cardError8.setOnClickListener((v) -> {
            binding.cardError8.setVisibility(View.GONE);
            binding.progress.setVisibility(View.VISIBLE);
            if (from.equals("home"))
                callBusinessR();
            myViewModel.getGuild();
            myViewModel.getState();
        });
        binding.btnDoFilter.setOnClickListener(view13 -> {
            boolean name = true;
            boolean lastName = true;
            boolean guild = true;
            boolean state = true;
            boolean city = true;
            boolean nameStore = true;


            if (!from.equals("home") && binding.edtName.getText().toString().equals("")) {
                binding.edtName.setBackgroundResource(R.drawable.background_card_error);
                name = false;
            }

            if (!from.equals("home") && binding.edtLastName.getText().toString().equals("")) {
                binding.edtLastName.setBackgroundResource(R.drawable.background_card_error);
                lastName = false;
            }


            if (choseGuilds.size() == 0) {
                binding.cardGuild1.setBackgroundResource(R.drawable.background_card_error);
                guild = false;

            }

            if (chooseStates.size() == 0) {
                binding.cardState1.setBackgroundResource(R.drawable.background_card_error);
                state = false;
            }

            if (!from.equals("home") && chooseCities.size() == 0) {
                binding.cardCity1.setBackgroundResource(R.drawable.background_card_error);
                city = false;
            }

            if (!from.equals("home") && binding.edtNameStore.getText().toString().isEmpty()) {
                binding.edtNameStore.setBackgroundResource(R.drawable.background_card_error);
                nameStore = false;
            }


            if ((!from.equals("home")) && (!lastName || !name || !city || !state || !guild || !nameStore))
                return;


            ArrayList<BusinessR> resultB = new ArrayList<>(chooseBusinessRs);
            CollectionUtils.filter(resultB, r -> !r.isSave());
            CollectionUtils.filter(chooseBusinessRs, BusinessR::isSave);
            for (int i = 0; i < resultB.size(); i++) {
                BusinessR bs = Select.from(BusinessR.class).where("I ='" + resultB.get(i).getI() + "'").first();
                if (bs != null)
                    bs.delete();
            }

            Guild.deleteAll(Guild.class);
            State.deleteAll(State.class);
            City.deleteAll(City.class);


            Guild.saveInTx(choseGuilds);
            State.saveInTx(chooseStates);
            BusinessR.saveInTx(chooseBusinessRs);
            if (from.equals("home")) {
                NavDirections action = FilterFragmentDirections.actionGoToHomeFragment();
                Navigation.findNavController(binding.getRoot()).navigate(action);

            } else {
                binding.btnDoFilter.setBackgroundColor(getResources().getColor(R.color.bottom_background_inActive_color));
                binding.btnDoFilter.setEnabled(false);
                binding.progress.setVisibility(View.VISIBLE);


                //region Create JsonAccount
                Account account;
                if (accountList.size() > 0)
                    account = accountList.get(0);
                else
                    account = new Account();

                String IMEI = Constant.getAndroidID(getActivity());
                account.setM(binding.edtMobile.getText().toString());
                account.setPm(binding.edtMobileM.getText().toString());
                account.setN(binding.edtName.getText().toString());
                account.setLn(binding.edtLastName.getText().toString());
                account.setStateId(chooseStates.get(0).getI());
                account.setStateName(chooseStates.get(0).getName());
                account.setGuildId(choseGuilds.get(0).getI());
                account.setGuildName(choseGuilds.get(0).getName());
                account.setCityId(chooseCities.get(0).getI());
                account.setCityName(chooseCities.get(0).getName());
                account.setShn(binding.edtNameStore.getText().toString());
                account.setAppId(Constant.APPLICATION_ID);
                account.setVersion(appVersion);
                account.setImei(IMEI);

                accountList.clear();
                accountList.add(account);
                Constant.JsonObjectAccount jsonObjectAcc = new Constant.JsonObjectAccount();
                jsonObjectAcc.Account = accountList;
                myViewModel.addAccountToServer(jsonObjectAcc);
                //endregion Create JsonAccount

            }


        });

        binding.edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.edtName.setBackgroundResource(R.drawable.background_card);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.edtLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.edtLastName.setBackgroundResource(R.drawable.background_card);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.edtNameStore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.edtNameStore.setBackgroundResource(R.drawable.background_card);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        binding.ivBack.setOnClickListener(view14 -> Navigation.findNavController(binding.getRoot()).popBackStack());
        binding.txtDeleteAll.setOnClickListener(view -> {

            BusinessR.deleteAll(BusinessR.class);
            chooseBusinessRs.clear();
            for (BusinessR b : allBusinessRs) {
                if (b.isSave())
                    b.setSave(false);
            }
            try {
                adapterBusinessR.notifyDataSetChanged();
            } catch (Exception ignored) {
            }

            Guild.deleteAll(Guild.class);
            choseGuilds.clear();
            guilds.clear();
            try {
                adapterGuild.notifyDataSetChanged();
            } catch (Exception ignored) {
            }


            State.deleteAll(State.class);
            chooseStates.clear();
            states.clear();
            try {
                adapterState.notifyDataSetChanged();
            } catch (Exception ignored) {
            }


            City.deleteAll(City.class);
            chooseCities.clear();
            cities.clear();
            try {
                adapterState.notifyDataSetChanged();
            } catch (Exception ignored) {
            }


            Guild guild = new Guild();
            guild.setI(accountList.get(0).getGuildId());
            guild.setName(accountList.get(0).getGuildName());
            guild.save();
            choseGuilds.add(guild);

            State state = new State();
            state.setI(accountList.get(0).getStateId());
            state.setName(accountList.get(0).getStateName());
            state.save();

            chooseStates.add(state);


            Toasty.success(requireActivity(), "با موفقیت حذف شد.", Toast.LENGTH_SHORT, true).show();


        });
    }

    private void initCreateCityDialog() {

        createCityDialog = CreateCityDialog.getInstance();
        createCityDialog.setOnClickPositiveButton((city) -> {
            showCreateCityDialog = false;
            createCityDialog.hideProgress();
            addCity = city;
            binding.progress.setVisibility(View.VISIBLE);
            myViewModel.addNewCity(city, guidStates.get(0));
        });


        createCityDialog.setOnClickNegativeButton(() -> {
            showCreateCityDialog = false;
            createCityDialog.hideProgress();
        });
    }

    private void getBundle() {
        phoneNumber = ir.kitgroup.inskuappb.ui.launcher.homeItem.FilterFragmentArgs.fromBundle(getArguments()).getPhoneNumber();
        from = FilterFragmentArgs.fromBundle(getArguments()).getFrom();

        if (!from.equals("home")) {
            binding.cardPhoneNumber.setVisibility(View.VISIBLE);
            binding.cardFullNameUser.setVisibility(View.VISIBLE);
            binding.cardNameStore.setVisibility(View.VISIBLE);
            binding.cardCity.setVisibility(View.VISIBLE);
            binding.txtDeleteAll.setVisibility(View.GONE);
            binding.cardBusinessR.setVisibility(View.GONE);

            if (from.equals("verify") || from.equals("splash")) {
                binding.tvTitle.setText("اطلاعات تکمیلی");
                binding.btnDoFilter.setText("ورود به حساب کاربری");
            } else if (from.equals("profile")) {
                binding.tvTitle.setText("ویرایش حساب کاربری");
                binding.btnDoFilter.setText("ویرایش");
            }
        } else
            binding.cardCity.setVisibility(View.GONE);

    }

    private void getAppVersion() {
        try {
            appVersion = Constant.appVersion(getActivity());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint({"NotifyDataSetChanged", "ClickableViewAccessibility"})
    private void initGuildRecyclerView() {
        textWatcherGuild = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                citySearch = false;
                guildSearch = true;
                stateSearch = false;

                if (charSequence.toString().trim().equals("")) {
                    resetGuild();
                    guilds.addAll(allGuilds);
                    binding.recyclerGuild.setVisibility(View.VISIBLE);
                    adapterGuild.notifyDataSetChanged();
                    binding.guildClose.setVisibility(View.VISIBLE);
                } else
                    getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        binding.edtGuild.addTextChangedListener(textWatcherGuild);
        binding.guildClose.setOnClickListener(view -> {
            binding.guildClose.setVisibility(View.GONE);
            resetGuild();
            adapterGuild.notifyDataSetChanged();
        });


        binding.edtGuild.setOnTouchListener((view, motionEvent) -> {
            resetGuild();
            guilds.addAll(allGuilds);
            binding.recyclerGuild.setVisibility(View.VISIBLE);
            adapterGuild.notifyDataSetChanged();
            binding.guildClose.setVisibility(View.VISIBLE);

            return false;
        });


        selectedGuildItems = Select.from(Guild.class).list();
        choseGuilds = new ArrayList<>();
        choseGuilds.addAll(selectedGuildItems);
        guilds = new ArrayList<>();
        allGuilds = new ArrayList<>();

        adapterGuild = new UniversalAdapter2(R.layout.guild_item, guilds, BR.guild);
        binding.recyclerGuild.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerGuild.setAdapter(adapterGuild);


        if (choseGuilds.size() > 0 && choseGuilds.get(0).getName() != null) {
            guild = true;

            binding.edtGuild.removeTextChangedListener(textWatcherGuild);
            binding.edtGuild.setText(choseGuilds.get(0).getName());
            binding.edtGuild.addTextChangedListener(textWatcherGuild);
            if (from.equals("verify") || from.equals("splash"))
                binding.edtGuild.setEnabled(false);

        }


        adapterGuild.setOnItemClickListener((bind, position) -> {

            binding.guildClose.setVisibility(View.GONE);
            choseGuilds.clear();
            choseGuilds.add(0, guilds.get(position));

            guilds.clear();
            guilds.addAll(allGuilds);
            adapterGuild.notifyDataSetChanged();
            binding.recyclerGuild.setVisibility(View.GONE);

            if (choseGuilds.size() > 0) {
                binding.edtGuild.removeTextChangedListener(textWatcherGuild);
                binding.edtGuild.setText(choseGuilds.get(0).getName());
                binding.edtGuild.addTextChangedListener(textWatcherGuild);
            }


            if (from.equals("home")) {
                binding.progress.setVisibility(View.VISIBLE);
                callBusinessR();
            }
        });
    }

    @SuppressLint({"NotifyDataSetChanged", "ClickableViewAccessibility"})
    private void initStateRecyclerView() {

        textWatcherState = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                citySearch = false;
                guildSearch = false;
                stateSearch = true;

                if (charSequence.toString().trim().equals("")) {
                    resetState();
                    states.addAll(allStates);
                    adapterState.notifyDataSetChanged();
                    binding.recyclerState.setVisibility(View.VISIBLE);
                    binding.stateClose.setVisibility(View.VISIBLE);
                } else
                    getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        binding.edtState.addTextChangedListener(textWatcherState);

        binding.stateClose.setOnClickListener(view -> {
            resetState();
            binding.stateClose.setVisibility(View.GONE);
            adapterState.notifyDataSetChanged();
        });


        binding.edtState.setOnTouchListener((view, motionEvent) -> {
            resetState();
            states.addAll(allStates);
            adapterState.notifyDataSetChanged();
            binding.recyclerState.setVisibility(View.VISIBLE);
            binding.stateClose.setVisibility(View.VISIBLE);
            return false;
        });

        selectedStateItems = Select.from(State.class).list();
        chooseStates = new ArrayList<>();
        chooseStates.addAll(selectedStateItems);

        guidStates = new ArrayList<>();
        for (State state : chooseStates) {
            guidStates.add(0, state.getI());
        }

        states = new ArrayList<>();
        allStates = new ArrayList<>();

        adapterState = new UniversalAdapter2(R.layout.state_item, states, BR.state);
        binding.recyclerState.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerState.setAdapter(adapterState);

        if (chooseStates.size() > 0 && chooseStates.get(0).getName() != null) {
            state = true;

            if (from.equals("verify") || from.equals("splash"))
                binding.edtState.setEnabled(false);


            binding.edtState.removeTextChangedListener(textWatcherState);
            binding.edtState.setText(chooseStates.get(0).getName());
            binding.edtState.addTextChangedListener(textWatcherState);
        }


        adapterState.setOnItemClickListener((bind, position) -> {

            binding.stateClose.setVisibility(View.GONE);


            cities.clear();
            allCities.clear();
            chooseCities.clear();
            adapterCity.notifyDataSetChanged();


            chooseStates.clear();
            guidStates.clear();

            chooseStates.add(0, states.get(position));
            guidStates.add(0, states.get(position).getI());

            states.clear();
            states.addAll(allStates);
            adapterState.notifyDataSetChanged();
            binding.recyclerState.setVisibility(View.GONE);

            if (chooseStates.size() > 0) {
                binding.edtState.removeTextChangedListener(textWatcherState);
                binding.edtState.setText(chooseStates.get(0).getName());
                binding.edtState.addTextChangedListener(textWatcherState);

                binding.edtCity.removeTextChangedListener(textWatcherCity);
                binding.edtCity.setText("");
                binding.edtCity.addTextChangedListener(textWatcherCity);
            }


            binding.progress.setVisibility(View.VISIBLE);
            if (!from.equals("home") && guidStates.size() > 0) {
                allCities.clear();
                cities.clear();
                adapterCity.notifyDataSetChanged();
                myViewModel.getCity(guidStates);
            } else if (from.equals("home"))
                callBusinessR();

        });
    }

    @SuppressLint({"NotifyDataSetChanged", "ClickableViewAccessibility"})
    private void initCityRecyclerView() {

        textWatcherCity = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                citySearch = true;
                guildSearch = false;
                stateSearch = false;

                if (guidStates.size() == 0) {
                    Toasty.error(requireActivity(), "استان مورد نظر خود را انتخاب کنید.", Toast.LENGTH_SHORT, true).show();

                } else if (!charSequence.toString().trim().equals("")) {
                    wordSearchCity = charSequence.toString();

                    if (charSequence.toString().length() > 1) {
                        if (allCities.size() == 0) {
                            binding.progress.setVisibility(View.VISIBLE);
                            allCities.clear();
                            cities.clear();
                            adapterCity.notifyDataSetChanged();
                            myViewModel.getCity(guidStates);
                        } else
                            getFilter().filter(wordSearchCity);
                    }
                } else {
                    cities.clear();
                    cities.addAll(allCities);
                    adapterCity.notifyDataSetChanged();
                    binding.recyclerCity.setVisibility(View.VISIBLE);
                    binding.cityClose.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        binding.edtCity.addTextChangedListener(textWatcherCity);

        binding.cityClose.setOnClickListener(view -> {
            binding.cityClose.setVisibility(View.GONE);
            resetCity();
            adapterCity.notifyDataSetChanged();
        });


        binding.edtCity.setOnTouchListener((view, motionEvent) -> {
            resetCity();
            cities.addAll(allCities);
            adapterCity.notifyDataSetChanged();
            binding.recyclerCity.setVisibility(View.VISIBLE);
            binding.cityClose.setVisibility(View.VISIBLE);
            return false;
        });


        List<City> selectedCityItems = Select.from(City.class).list();
        chooseCities = new ArrayList<>();
        chooseCities.addAll(selectedCityItems);
        allCities = new ArrayList<>();
        cities = new ArrayList<>();

        adapterCity = new UniversalAdapter2(R.layout.city_item, cities, BR.city);
        binding.recyclerCity.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerCity.setAdapter(adapterCity);


        if ((accountList.size() > 0 && accountList.get(0).getCityId() != null)) {
            binding.edtCity.removeTextChangedListener(textWatcherCity);
            binding.edtCity.setText(accountList.get(0).getCityName());
            binding.edtCity.addTextChangedListener(textWatcherCity);


            City city = new City();
            city.setName(accountList.get(0).getCityName());
            city.setI(accountList.get(0).getCityId());
            chooseCities.add(city);
        }


        adapterCity.setOnItemClickListener((bind, position) -> {

            binding.cityClose.setVisibility(View.GONE);
            binding.cardCity1.setBackgroundResource(R.drawable.background_card);

            chooseCities.clear();
            chooseCities.add(0, cities.get(position));
            cities.clear();
            adapterCity.notifyDataSetChanged();

            binding.recyclerCity.setVisibility(View.GONE);

            if (chooseCities.size() > 0) {
                binding.edtCity.removeTextChangedListener(textWatcherCity);
                binding.edtCity.setText(chooseCities.get(0).getName());
                binding.edtCity.addTextChangedListener(textWatcherCity);

            }
            //   binding.progress.setVisibility(View.VISIBLE);
            //callBusinessR();
        });


        //endregion Config RecyclerViewCity
    }

    private void initBusinessRelationRecyclerView() {
        //region Config RecyclerViewBusinessR
        selectedBusinessItems = Select.from(BusinessR.class).list();
        CollectionUtils.filter(selectedBusinessItems, BusinessR::isSave);
        chooseBusinessRs = new ArrayList<>();
        chooseBusinessRs.addAll(selectedBusinessItems);
        businessRs = new ArrayList<>();
        allBusinessRs = new ArrayList<>();
        adapterBusinessR = new UniversalAdapter2(R.layout.bussiness_r_item, businessRs, BR.business);

        binding.recyclerBusinessR.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerBusinessR.setAdapter(adapterBusinessR);

        adapterBusinessR.setOnItemBindListener((bin, position) -> {

            try {
                View view1 = bin.getRoot();
                CheckBox checkBox = view1.findViewById(R.id.checkBoxFilter);

                if (allBusinessRs.get(position).isSave())
                    checkBox.setChecked(true);
                else
                    checkBox.setChecked(false);

                setTextBtnShowBussinesR();

                checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b) {
                        if (!chooseBusinessRs.contains(allBusinessRs.get(position))) {
                            allBusinessRs.get(position).setSave(true);
                            chooseBusinessRs.add(allBusinessRs.get(position));
                        } else
                            setSaveBussines(position, true);

                    } else {
                        setSaveBussines(position, false);

                    }

                    setTextBtnShowBussinesR();

                });
            } catch (Exception ignored) {
            }

        });

        binding.btnShowBusinessR.setOnClickListener(view0 -> {
            businessRs.clear();
            setTextBtnShowBussinesR();

            ArrayList<BusinessR> resBussinesRSave = new ArrayList<>(allBusinessRs);
            CollectionUtils.filter(resBussinesRSave, BusinessR::isSave);

            ArrayList<BusinessR> resBussinesRNotSave = new ArrayList<>(allBusinessRs);
            CollectionUtils.filter(resBussinesRNotSave, r -> !r.isSave());


            allBusinessRs.clear();
            allBusinessRs.addAll(resBussinesRSave);
            allBusinessRs.addAll(resBussinesRNotSave);


            if (showBusinessR) {
                showBusinessR = false;
                businessRs.addAll(allBusinessRs.subList(0, 3));
            } else {
                showBusinessR = true;
                businessRs.addAll(allBusinessRs);
            }

            try {
                adapterBusinessR.notifyDataSetChanged();
            } catch (Exception ignored) {
            }
        });

        //endregion Config RecyclerViewBusinessR
    }

    @SuppressLint("NotifyDataSetChanged")
    private void resetGuild() {



        binding.cardGuild1.setBackgroundResource(R.drawable.background_card);
        binding.edtGuild.removeTextChangedListener(textWatcherGuild);
        binding.edtGuild.setText("");
        binding.edtGuild.addTextChangedListener(textWatcherGuild);
        guilds.clear();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void resetState() {

        binding.cardState1.setBackgroundResource(R.drawable.background_card);
        binding.edtState.removeTextChangedListener(textWatcherState);
        binding.edtState.setText("");
        binding.edtState.addTextChangedListener(textWatcherState);
        guidStates.clear();
        cities.clear();
        adapterCity.notifyDataSetChanged();
        states.clear();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void resetCity() {
        binding.cardCity1.setBackgroundResource(R.drawable.background_card);
        binding.edtCity.removeTextChangedListener(textWatcherCity);
        binding.edtCity.setText("");
        binding.edtCity.addTextChangedListener(textWatcherCity);
        cities.clear();
    }

    //endregion Method

}

