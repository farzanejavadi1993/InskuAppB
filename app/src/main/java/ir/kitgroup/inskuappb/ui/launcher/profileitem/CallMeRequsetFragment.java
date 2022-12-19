package ir.kitgroup.inskuappb.ui.launcher.profileitem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.orm.query.Select;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Files;
import ir.kitgroup.inskuappb.databinding.CallMeRequsetFragmentBinding;
import ir.kitgroup.inskuappb.model.CallMe;

@AndroidEntryPoint
public class CallMeRequsetFragment extends Fragment {

    private MainViewModel mainViewModel;
    private CallMeRequsetFragmentBinding binding;

    private UniversalAdapter2 adapter;
    private final ArrayList<CallMe> callMeRequests = new ArrayList<>();

    private Account account;


    private ProgressBar progressBarCompany;

    private boolean goCompany;
    private TextView tvRequest;

    private CustomSnackBar snackBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CallMeRequsetFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initRecyclerView();
        initSnackBar();
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        getFirstRequest();
        nullTheMutable();

        mainViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            try {
                binding.progress.setVisibility(View.GONE);
                progressBarCompany.setVisibility(View.GONE);
            } catch (Exception ignored) {
            }

            if (result.getCode() == 3) {
                binding.tvErrorC.setText(result.getDescription());
                binding.cardErrorC.setVisibility(View.VISIBLE);
            } else
                ShowMessageWarning(result.getDescription());

            goCompany = false;


        });
        mainViewModel.getResultCustomerCallRequest().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            binding.progress.setVisibility(View.GONE);

            mainViewModel.getResultCustomerCallRequest().setValue(null);


            callMeRequests.clear();

            if (result.size() > 0) {
                callMeRequests.addAll(result);
                binding.layoutNotFound.setVisibility(View.GONE);
            } else
                binding.layoutNotFound.setVisibility(View.VISIBLE);


            adapter.notifyDataSetChanged();

        });
        mainViewModel.getResultCompany().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            progressBarCompany.setVisibility(View.GONE);
            mainViewModel.getResultCompany().setValue(null);


            goCompany = false;
            if (result.size() > 0)
                navigateToDetailCompany(result.get(0));

        });


    }

    private void init() {
        goCompany = false;
        account = Select.from(Account.class).first();
        binding.ivBack.setOnClickListener(view -> Navigation.findNavController(binding.getRoot()).popBackStack());
    }

    private void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new UniversalAdapter2(R.layout.call_me_request_item, callMeRequests, BR.callMe);

        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemBindListener((bin, position) -> {
            View view = bin.getRoot();


            progressBarCompany = view.findViewById(R.id.progress_company);

            tvRequest = view.findViewById(R.id.tv_request);

            int type = callMeRequests.get(position).getCallMeStatusMobile().getType();

            if (type == 0)
                tvRequest.setTextColor(getActivity().getResources().getColor(R.color.green_button));
             else if (type == 1)
                tvRequest.setTextColor(getActivity().getResources().getColor(R.color.purple));
            else if (type == 2)
                tvRequest.setTextColor(getActivity().getResources().getColor(R.color.teal_50));
            else if (type == 3)
                tvRequest.setTextColor(getActivity().getResources().getColor(R.color.red));
            else if (type == 4)
                tvRequest.setTextColor(getActivity().getResources().getColor(R.color.gray_button));


        });

        adapter.setOnItemClickListener((binding, position) -> {
            progressBarCompany = binding.getRoot().findViewById(R.id.progress_company);
            if (!goCompany) {
                goCompany = true;
                progressBarCompany.setVisibility(View.VISIBLE);
                mainViewModel.getCompanyDetailById(callMeRequests.get(position).getCompanyId());
            }
        });
    }

    private void getFirstRequest() {
        binding.progress.setVisibility(View.VISIBLE);
        mainViewModel.getCustomerCallRequest(account.getI());
    }


    private void ShowMessageWarning(String error) {
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }
        snackBar.showSnack(getActivity(), binding.getRoot(), error);
    }

    private void initSnackBar() {
        snackBar = new CustomSnackBar();
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }

    }

    private void nullTheMutable() {
        mainViewModel.getResultMessage().setValue(null);
        mainViewModel.getResultCustomerCallRequest().setValue(null);
        mainViewModel.getResultCompany().setValue(null);
    }

    private void navigateToDetailCompany(Company company) {
        Company.deleteAll(Company.class);
        Company.saveInTx(company);
        Files.deleteAll(Files.class);
        Files.saveInTx(company.getFiles());
        Navigation.findNavController(getView()).navigate(R.id.DetailCompanyFragment);
    }
}
