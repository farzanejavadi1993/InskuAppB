package ir.kitgroup.inskuappb.ui.launcher.homeItem;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.orm.query.Select;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.databinding.HomeFragmentBinding;
import ir.kitgroup.inskuappb.ui.launcher.homeItem.tab1Advertise.AdvertiseFragment;
import ir.kitgroup.inskuappb.ui.viewmodel.DoodViewModel;
import ir.kitgroup.inskuappb.ui.viewmodel.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.ui.activities.MainActivity;
import ir.kitgroup.inskuappb.component.filterr.Filters;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.ui.launcher.homeItem.tab2Saved.MyCompanyFragment;
import ir.kitgroup.inskuappb.ui.launcher.homeItem.tab3AllCompany.AllCompanyFragment;

import ir.kitgroup.inskuappb.util.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@AndroidEntryPoint
public class LauncherFragment extends Fragment {

    //region Parameter
    @Inject
    Typeface typeface;
    @Inject
    SharedPreferences sharedPreferences;
    public static String fragment = "";

    private HomeFragmentBinding binding;
    private Account account;
    private DoodViewModel doodViewModel;
    private MainViewModel mainViewModel;
    private Filters doFilter;
    private String IMEI = "";
    private View view = null;


    //endregion Parameter

    //region Override Method
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = HomeFragmentBinding.inflate(getLayoutInflater());
        try {
            account = Select.from(Account.class).first();

            doFilter = new Filters();

            binding.viewpagerIntro.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });


            binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 2) {
                        fragment = "advertise";
                        binding.btnSearch.setVisibility(View.VISIBLE);
                    }
                    else if (tab.getPosition() == 1) {
                        fragment = "myCompany";
                        binding.btnSearch.setVisibility(View.GONE);
                    }
                    else {
                        fragment = "allCompany";
                        binding.btnSearch.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });


            binding.filter.setOnClickListener(view1 -> {
                NavDirections action =  ir.kitgroup.inskuappb.ui.launcher.homeItem.LauncherFragmentDirections.actionGoToFilterFragment("","home");
                Navigation.findNavController(binding.getRoot()).navigate(action);
            });

            binding.btnSearch.setOnClickListener(view1 -> {
                if (fragment.equals("advertise"))
                    Navigation.findNavController(getView()).navigate(R.id.SearchAdvertiseFragment);
                else
                    Navigation.findNavController(getView()).navigate(R.id.SearchCompany);
            });

            initTab();

        } catch (Exception ignored) {
        }
        return binding.getRoot();
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        doodViewModel = new ViewModelProvider(this).get(DoodViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        doodViewModel.getResultMessage().setValue(null);
        mainViewModel.getResultMessage().setValue(null);

        doodViewModel.getNewMessagesCount(account.getI(), Constant.APPLICATION_ID);
        doodViewModel.getResultNewMessagesCount().observe(getViewLifecycleOwner(), result -> {
            if (result == null) {
                return;
            }
                doodViewModel.getResultAllMessage().setValue(null);

                if (result > 0)
                    ((MainActivity) getActivity()).setCounterOrder(result);
                else
                    ((MainActivity) getActivity()).setClearCounterOrder();


        });

        mainViewModel.getResultFirebaseToken().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            mainViewModel.getResultFirebaseToken().setValue(null);
        });
    }

    //endregion Override Method

    //region Method
    private class IntroPageAdapter extends FragmentPagerAdapter {
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

    private void initTab() {

        IntroPageAdapter adapter = new IntroPageAdapter(getChildFragmentManager());

        adapter.addFragment(new AllCompanyFragment(), "همه\u200Cپخش\u200Cها");
        adapter.addFragment(new MyCompanyFragment(), "پخش\u200Cهای من");
        adapter.addFragment(new AdvertiseFragment(), "طرح ها");


        binding.viewpagerIntro.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpagerIntro);
        TabLayout.Tab tab;
        if (account != null && account.getTab() != -1)
            tab = binding.tabLayout.getTabAt(account.getTab());
        else
            tab = binding.tabLayout.getTabAt(2);

        tab.select();


        sharedPreferences.edit().putBoolean("loginSuccess", true).apply();


        //region Init Filter
        if (doFilter.getResultFilter())
            binding.btnFilters.setImageResource(R.drawable.ic_full_filter);
        else
            binding.btnFilters.setImageResource(R.drawable.ic_empty_filter);

        //endregion Init Filter
        IMEI = Constant.getAndroidID(getActivity());
        try {

            if (sharedPreferences.getString("access_token", "").equals("")) {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                return;
                            }
                            String token = task.getResult();
                            sharedPreferences.edit().putString("access_token", token).apply();
                            mainViewModel.setFirebaseToken(Constant.APPLICATION_ID, account.getI(), IMEI,
                                    token);
                        });

            } else if (!sharedPreferences.getString("new_token", "").equals("")
                    && !sharedPreferences.getString("new_token", "").equals(
                    sharedPreferences.getString("access_token", ""))) {

                String newToken = sharedPreferences.getString("new_token", "");
                sharedPreferences.edit().putString("access_token", newToken).apply();
                mainViewModel.setFirebaseToken(Constant.APPLICATION_ID, account.getI(), IMEI,
                        newToken);

            }
        } catch (Exception ignored) {
        }


    }
    //endregion Method


}
