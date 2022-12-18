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
import androidx.navigation.NavDirections;
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
import ir.kitgroup.inskuappb.databinding.WantAdvertisementFragmentBinding;
import ir.kitgroup.inskuappb.model.WantAdvertisement;
import ir.kitgroup.inskuappb.ui.launcher.homeItem.tab1Advertise.AdvertiseFragmentDirections;

@AndroidEntryPoint
public class WantAdvertisementFragment extends Fragment {

    private MainViewModel mainViewModel;
    private WantAdvertisementFragmentBinding binding;

    private UniversalAdapter2 adapter;
    private final ArrayList<WantAdvertisement> wantAdvertisements = new ArrayList<>();

    private Account account;


    private ProgressBar progressBarCompany;


    private TextView tvRequest;

    private CustomSnackBar snackBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WantAdvertisementFragmentBinding.inflate(getLayoutInflater());
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


        });
        mainViewModel.getResultCustomerWantAdv().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            binding.progress.setVisibility(View.GONE);

            mainViewModel.getResultCustomerCallRequest().setValue(null);


            wantAdvertisements.clear();

            if (result.size() > 0) {
                wantAdvertisements.addAll(result);
                binding.layoutNotFound.setVisibility(View.GONE);
            } else
                binding.layoutNotFound.setVisibility(View.VISIBLE);


            adapter.notifyDataSetChanged();

        });



    }

    private void init() {

        account = Select.from(Account.class).first();
        binding.ivBack.setOnClickListener(view -> Navigation.findNavController(binding.getRoot()).popBackStack());
    }

    private void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new UniversalAdapter2(R.layout.want_advertisement_item, wantAdvertisements, BR.wantAdv);

        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemBindListener((bin, position) -> {
            View view = bin.getRoot();


            progressBarCompany = view.findViewById(R.id.progress_company);

            tvRequest = view.findViewById(R.id.tv_request);

            int type = wantAdvertisements.get(position).getCallMeStatusMobile().getType();
            String name = wantAdvertisements.get(position).getCallMeStatusMobile().getName();

            tvRequest.setText(name);
            if (type == 0)
                tvRequest.setTextColor(getActivity().getResources().getColor(R.color.green_button));
             else if (type == 1)
                tvRequest.setTextColor(getActivity().getResources().getColor(R.color.purple));
            else if (type == 2)
                tvRequest.setTextColor(getActivity().getResources().getColor(R.color.teal_50));
            else if (type == 3)
                tvRequest.setTextColor(getActivity().getResources().getColor(R.color.red));
            else if (type == 4)
                tvRequest.setTextColor(getActivity().getResources().getColor(R.color.gray_button_full));


        });

        adapter.setOnItemClickListener((binding, position) -> {
            progressBarCompany = binding.getRoot().findViewById(R.id.progress_company);
            navigateToDetailAdvertisement(wantAdvertisements.get(position).getAdvertisementId());
        });
    }

    private void getFirstRequest() {
        binding.progress.setVisibility(View.VISIBLE);
        mainViewModel.getCustomerWantAdv(account.getI());
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

    }

    private void navigateToDetailAdvertisement(String id) {
        NavDirections action = AdvertiseFragmentDirections.actionGoToDetailAdvertiseFragment(id);
        Navigation.findNavController(binding.getRoot()).navigate(action);
    }
}
