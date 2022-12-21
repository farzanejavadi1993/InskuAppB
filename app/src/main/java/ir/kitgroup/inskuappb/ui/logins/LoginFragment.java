package ir.kitgroup.inskuappb.ui.logins;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.R;

import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;

import ir.kitgroup.inskuappb.classes.verify.VerifyMobileNumber;
import ir.kitgroup.inskuappb.databinding.LoginFragmentBinding;
import ir.kitgroup.inskuappb.util.Constant;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    //region Parameter
    @Inject
    Typeface typeface;


    public Typeface iranSansFont;
    private LoginFragmentBinding binding;
    private MainViewModel myViewModel;

    private Integer code;
    private String phoneNumber = "";
    private VerifyMobileNumber verifyMobileNumber;

    private CustomSnackBar snackbar;

    //endregion Parameter


    //region Override Method
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LoginFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //binding.loginTvAppName.setTypeface(typeface);
        iranSansFont = Typeface.createFromAsset(getActivity().getAssets(), "iransans.ttf");
        getObject();
        setTextFontToTvRule();

        //region Listener Accept Rule
        binding.checkbox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b)
                binding.btnEnter.setVisibility(View.VISIBLE);
            else
                binding.btnEnter.setVisibility(View.GONE);
        });
        //endregion Listener Accept Rule


        //region Listener Login Button
        binding.btnEnter.setOnClickListener(v -> {
            phoneNumber = Objects.requireNonNull(binding.edtNumber.getText()).toString();
            checkVerifyNumber();
        });
        //endregion Listener Login Button


        //region Listener Show Rule In RuleFragment By Click TextView Of Rule(loginTvRules)
        binding.layoutRule.setOnClickListener(view1 -> {
            NavDirections action = LoginFragmentDirections.actionGoToRuleFragment();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });
        //region Listener Show Rule In RuleFragment By Click TextView Of Rule(loginTvRules)

        binding.edtNumber.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.edtNumber, 0);




        binding.edtNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==11)
                    Constant.hideKeyBoard(getActivity(),binding.edtNumber);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        myViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        myViewModel.getResultMessage().setValue(null);


        myViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result == null) return;
                changeUi(View.GONE, getResources().getColor(R.color.color_primary), true);
                ShowMessageWarning(result.getDescription());

        });


        myViewModel.getResultSmsLogin().observe(getViewLifecycleOwner(), result -> {
            changeUi(View.GONE, getResources().getColor(R.color.color_primary), true);

            if (result == null)
                return;
            myViewModel.getResultSmsLogin().setValue(null);


            //region SuccessFull Result
            if (result.get(0).getMessage() == 4) {
                NavDirections action = LoginFragmentDirections.actionGoToVerifyFragment(String.valueOf(code), phoneNumber);
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
            //endregion SuccessFull Result
            else
                ShowMessageWarning(result.get(0).getDescription());


        });
    }
    //endregion Override Method

    //region Custom Method
    private void checkVerifyNumber() {
        if (verifyMobileNumber.valid(phoneNumber)) {
            code = new Random(System.nanoTime()).nextInt(89000) + 10000;
            changeUi(View.VISIBLE, getResources().getColor(R.color.bottom_background_inActive_color), false);
            myViewModel.getSmsLogin(String.valueOf(code), phoneNumber);

        } else
            ShowMessageWarning(getString(R.string.wrong_phone_number));


    }

    private void changeUi(int visibility, @ColorInt int color, boolean enable) {
        binding.progressBar.setVisibility(visibility);
        binding.btnEnter.setBackgroundColor(color);
        binding.btnEnter.setEnabled(enable);
    }

    private void getObject() {
        snackbar=new CustomSnackBar();
        verifyMobileNumber = new VerifyMobileNumber();

    }

    private void setTextFontToTvRule() {
        // Set Text Font To Special Part Of TextRule(پخش یاب)
        String description1 = getActivity().getResources().getString(R.string.rule1);
        Constant.setTextFontToSpecialPart(description1, typeface, 14, 21, Color.BLACK, binding.loginTvRules1);
        String description2 = getActivity().getResources().getString(R.string.rule2);
        Constant.setTextFontToSpecialPart(description2, iranSansFont, 0, 15, R.color.teal_700, binding.loginTvRules2);
    }


    private void ShowMessageWarning(String error) {
        try {
            snackbar.hide();
        } catch (Exception ignored) {
        }
        snackbar.showSnack(getActivity(), binding.getRoot(), error);

    }
    //region Custom Method
}
