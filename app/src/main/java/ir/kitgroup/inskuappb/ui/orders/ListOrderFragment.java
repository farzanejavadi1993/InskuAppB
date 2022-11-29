package ir.kitgroup.inskuappb.ui.orders;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ConnectServer.CompanyViewModel;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Ord;
import ir.kitgroup.inskuappb.dataBase.OrdDetail;
import ir.kitgroup.inskuappb.databinding.ListOrderFragmentBinding;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.orm.query.Select;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@AndroidEntryPoint
public class ListOrderFragment extends Fragment {

    //region Parameter
    @Inject
    SharedPreferences sharedPreferences;
    private ListOrderFragmentBinding binding;
    private CompanyViewModel myViewModel;
    private Account account;
    private Company company;
    private boolean enableEdit;
    private String userName;
    private String passWord;

    private UniversalAdapter2 adapter2;
    private ArrayList<Ord> list;
    private CustomSnackBar snackBar;


    //endregion Parameter


    //region Override Method
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ListOrderFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //region Config
        enableEdit = false;

        snackBar = new CustomSnackBar();
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


        company = Select.from(Company.class).first();
        userName = company.getUser();
        passWord = company.getPass();

        setUpRecyclerView();


        binding.ivBack.setOnClickListener(view1 -> Navigation.findNavController(binding.getRoot()).popBackStack());

        binding.cardError320.setOnClickListener(view12 -> {
            binding.cardError320.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
            myViewModel.getOrder(userName, passWord, sharedPreferences.getString("contactId", ""), getActivity());
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        myViewModel.getResultMessage().setValue(null);


        binding.progressBar.setVisibility(View.VISIBLE);
        myViewModel.getOrder(userName, passWord, sharedPreferences.getString("contactId", ""), getActivity());


        myViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {

            if (result != null) {
                binding.progressBar.setVisibility(View.GONE);

                binding.tvError320.setText(result.getDescription());
                binding.cardError320.setVisibility(View.VISIBLE);

            }
        });

        myViewModel.getResultOrder().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;
            JsonArray order = result.getAsJsonObject().get("Order").getAsJsonArray();
            Gson gson = new Gson();
            Type typeOrd = new TypeToken<List<Ord>>() {
            }.getType();
            list.clear();
            list.addAll(gson.fromJson(order.toString(), typeOrd));


            if (list.size() > 0)
                binding.layoutNotFound.setVisibility(View.GONE);
            else
                binding.layoutNotFound.setVisibility(View.VISIBLE);

            try {
                adapter2.notifyDataSetChanged();
            }catch (Exception ignored){}
            myViewModel.getResultOrder().setValue(null);
            binding.progressBar.setVisibility(View.GONE);


        });


    }


    //endregion Override Method


    //region Method

    private void setUpRecyclerView() {
        list = new ArrayList<>();
        adapter2 = new UniversalAdapter2(R.layout.order_item, list, BR.order);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(adapter2);


        adapter2.setOnItemClickListener((binding, position) -> {
            try {
                if (list.get(position).getConf().equals("0"))
                    enableEdit = true;

                NavDirections action = ListOrderFragmentDirections.actionGoToOrderFragment(list.get(position).getCat(), -10, list.get(position).getGid(), list.get(position).getAcid(), enableEdit);
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }catch (Exception ignored){}

        });


        adapter2.setOnItemBindListener((binding, position) -> {
            try {
                View view = binding.getRoot();
                TextView tvStatus = view.findViewById(R.id.tv_status);
                if (list.get(position).getC().equals("1")) {
                    tvStatus.setText("تکمیل شده");
                    tvStatus.setTextColor(getResources().getColor(R.color.selected_item_color));
                } else if (list.get(position).getDsc().equals("1")) {
                    tvStatus.setText(" تایید اپراتور");
                    tvStatus.setTextColor(getResources().getColor(R.color.invalid_name_color));
                } else if (list.get(position).getDc().equals("1")) {
                    tvStatus.setText(" تایید تحویل");
                    tvStatus.setTextColor(getResources().getColor(R.color.pinkRed));
                } else if (list.get(position).getSmc().equals("1")) {
                    tvStatus.setText("تایید مدیر");
                    tvStatus.setTextColor(getResources().getColor(R.color.selected_item_color_orange));
                } else if (list.get(position).getAc().equals("1")) {
                    tvStatus.setText("انتظار تایید حسابدار");
                    tvStatus.setTextColor(getResources().getColor(R.color.invite_friend_color));

                } else if (list.get(position).getSc().equals("1")) {
                    tvStatus.setText("تایید سرپرست");
                    tvStatus.setTextColor(getResources().getColor(R.color.selected_item_color_pink));
                } else if (list.get(position).getConf().equals("1")) {
                    tvStatus.setText("تایید سفارش دهنده");
                    tvStatus.setTextColor(getResources().getColor(R.color.purple));
                } else {
                    tvStatus.setText("در انتظار بررسی");
                    tvStatus.setTextColor(getResources().getColor(R.color.teal_200));
                }
            }catch (Exception ignored){}


        });
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

