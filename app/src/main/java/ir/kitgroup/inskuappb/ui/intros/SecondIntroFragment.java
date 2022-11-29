package ir.kitgroup.inskuappb.ui.intros;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.databinding.SecondIntroFragmentBinding;
import ir.kitgroup.inskuappb.util.Constant;

@AndroidEntryPoint
public class SecondIntroFragment extends Fragment {

    @Inject
    Typeface typeface;

    private SecondIntroFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding=SecondIntroFragmentBinding.inflate(getLayoutInflater());

        String description=getActivity().getResources().getString(R.string.description_second_intro);
        Constant.setTextFontToSpecialPart(description, typeface, 3, 10, Color.BLACK,  binding.tvSecondDescription);
        return binding.getRoot();
    }
}
