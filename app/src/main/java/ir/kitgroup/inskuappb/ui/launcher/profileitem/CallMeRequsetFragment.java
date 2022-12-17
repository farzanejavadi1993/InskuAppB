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

import com.orm.query.Select;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.databinding.CallMeRequsetFragmentBinding;
import ir.kitgroup.inskuappb.model.CallMe;

@AndroidEntryPoint
public class CallMeRequsetFragment extends Fragment {

    private MainViewModel mainViewModel;
    private CallMeRequsetFragmentBinding binding;

    private UniversalAdapter2 adapter;
    private final ArrayList<CallMe> callMeRequests = new ArrayList<>();

    private Account account;
    private boolean firstSync;


    private ProgressBar progressBarRequset;
    private boolean requestingCall;
    private TextView tvRequest;

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
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        getFirstRequest();

        mainViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

        });

        mainViewModel.getResultCustomerCallRequest().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            mainViewModel.getResultCustomerCallRequest().setValue(null);

            firstSync = true;

            callMeRequests.clear();

            if (result.size() > 0) {
                callMeRequests.addAll(result);
            }

            adapter.notifyDataSetChanged();

        });


    }

    private void init() {
        account = Select.from(Account.class).first();
    }

    private void initRecyclerView() {
        adapter = new UniversalAdapter2(R.layout.call_me_request_item, callMeRequests, BR.callMe);

        adapter.setOnItemBindListener((bin, position) -> {
            View view = bin.getRoot();
            progressBarRequset = view.findViewById(R.id.progress_requset);
            tvRequest = view.findViewById(R.id.tv_request);

            tvRequest.setOnClickListener(view1 -> {
                if (!requestingCall) {
                    requestingCall = true;


                }
            });


        });

    }

    private void getFirstRequest() {
        if (!firstSync) {
            binding.progress.setVisibility(View.VISIBLE);
            mainViewModel.getCustomerCallRequest(account.getI());

        } else {


        }
    }


}
