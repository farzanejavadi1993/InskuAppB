package ir.kitgroup.inskuappb.ui.intros;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.databinding.IntroFragmentBinding;

import java.util.ArrayList;
import java.util.List;


public class IntroFragment extends Fragment {

    //region Parameter

    private IntroFragmentBinding binding;
    //endregion Parameter


    //region Override Method
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = IntroFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView();

    }
    //endregion Override Method


    //region Method
    @SuppressLint("SetTextI18n")
    private void btn_pre() {
        int num = binding.viewpagerIntro.getCurrentItem() + 1;
        binding.textPage.setText(" " + num + " ");
        if (binding.viewpagerIntro.getCurrentItem() != 0) {
            binding.viewpagerIntro.setCurrentItem(binding.viewpagerIntro.getCurrentItem() - 1);
        }
    }

    @SuppressLint("SetTextI18n")
    private void btn_next() {
        int num = binding.viewpagerIntro.getCurrentItem() + 1;
        binding.textPage.setText(" " + num + " ");
        if (binding.viewpagerIntro.getCurrentItem() == 2) {

            try {
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.actionGoToLoginFragment);
            }catch (Exception ignored){}

        } else {
            binding.viewpagerIntro.setCurrentItem(binding.viewpagerIntro.getCurrentItem() + 1);
        }
    }

    private void setUpView() {
        binding.btnPre.setVisibility(View.GONE);
        IntroPageAdapter adapter = new IntroPageAdapter(getChildFragmentManager());
        adapter.addFragment(new FirstIntroFragment(),"");
        adapter.addFragment(new SecondIntroFragment(),"");
        adapter.addFragment(new ThirdIntroFragment(),"");
        binding.viewpagerIntro.setAdapter(adapter);
        binding.indicator.setViewPager(binding.viewpagerIntro);
        binding.viewpagerIntro.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onPageSelected(int position) {
                int num = binding.viewpagerIntro.getCurrentItem() + 1;
                binding.textPage.setText(" " + num + " ");
                if (num > 1)
                    binding.btnPre.setVisibility(View.VISIBLE);

                else if (num == 1)
                    binding.btnPre.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.btnNext.setOnClickListener(view1 -> btn_next());
        binding.btnPre.setOnClickListener(view1 -> btn_pre());
    }


    private static class IntroPageAdapter extends FragmentPagerAdapter {
        private final List<Fragment> myFragments = new ArrayList<>();
        private final List<String> myFragmentTitles = new ArrayList<>();

        public IntroPageAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            myFragments.add(fragment);
            myFragmentTitles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return myFragments.get(position);
        }

        @Override
        public int getCount() {
            return myFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return myFragmentTitles.get(position);
        }
    }
    //endregion Method


}
