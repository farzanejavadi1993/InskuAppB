package ir.kitgroup.inskuappb.ui.launcher.profileitem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.databinding.RequestUserFragmentBinding;

public class RequestUserFragment  extends Fragment {

    private RequestUserFragmentBinding binding;
    private UniversalAdapter2 adapter2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding=RequestUserFragmentBinding.inflate(getLayoutInflater());
       return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void init(){}




}
