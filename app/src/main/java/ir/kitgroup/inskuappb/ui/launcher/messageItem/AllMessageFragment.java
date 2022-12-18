package ir.kitgroup.inskuappb.ui.launcher.messageItem;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.orm.query.Select;


import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ConnectServer.DoodViewModel;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.activities.MainActivity;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.classes.EndlessParentScrollListener;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.databinding.AllCompanyMessageBinding;
import ir.kitgroup.inskuappb.model.CompanyMessage;


import ir.kitgroup.inskuappb.util.Constant;

@AndroidEntryPoint
public class AllMessageFragment extends Fragment {

    @Inject
    SharedPreferences sharedPreferences;


    private DoodViewModel doodViewModel;
    private MainViewModel mainViewModel;
    private AllCompanyMessageBinding binding;


    private UniversalAdapter2 adapter2;
    private final ArrayList<CompanyMessage> messages = new ArrayList<>();
    private EndlessParentScrollListener endlessParentScrollListener;
    private int pageMain = 1;
    private int oldSizeLadderList = 0;


    private int positionNavigate = -1;
    private Account account;


    private String wordSearch="";
    private boolean firstSync;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AllCompanyMessageBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initSearch();
        setPropertyToAccount();
        initAnimation();
        setUpRecyclerView();
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        doodViewModel = new ViewModelProvider(this).get(DoodViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        nullTheMutable();

        if (!firstSync) {
            binding.progressBar.setVisibility(View.VISIBLE);
            doodViewModel.getAllCompanyWithMessages(account.getI(), Constant.APPLICATION_ID, pageMain, 10);
        }
        else {
            try {
                if (positionNavigate != -1 && DetailMessageFragment.countRead > 0) {
                    int amount = Math.max(messages.get(positionNavigate).getNewCount() - DetailMessageFragment.countRead, 0);
                    messages.get(positionNavigate).setNewCount(amount);

                    int counter = ((MainActivity) getActivity()).getCounter();
                    if (counter - DetailMessageFragment.countRead > 0)
                        ((MainActivity) getActivity()).setCounterOrder(counter - DetailMessageFragment.countRead);
                    else
                        ((MainActivity) getActivity()).setClearCounterOrder();


                    positionNavigate = -1;
                }
            } catch (Exception ignored) {}
        }

        doodViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                binding.progressSearch.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.progressBar22.setVisibility(View.GONE);
                binding.tvError4.setText(result.getDescription());
                binding.cardError4.setVisibility(View.VISIBLE);
            }
        });

        mainViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                binding.progressSearch.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.progressBar22.setVisibility(View.GONE);
                binding.tvError4.setText(result.getDescription());
                binding.cardError4.setVisibility(View.VISIBLE);
            }
        });

        doodViewModel.getResultAllMessage().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            firstSync=true;
            doodViewModel.getResultAllMessage().setValue(null);
            binding.progressBar.setVisibility(View.GONE);
            binding.progressSearch.setVisibility(View.GONE);
            if (pageMain!=1)
            binding.progressBar22.setVisibility(View.VISIBLE);


            if (result.size() > 0) {
                if (pageMain == 1)
                    messages.clear();
                messages.addAll(result);
            if (pageMain==1)
                adapter2.notifyDataSetChanged();
            else
                adapter2.notifyItemRangeChanged(oldSizeLadderList, messages.size() - 1);


            } else {
                if (messages.size() == 0)
                    binding.layoutNotFoundMessage.setVisibility(View.VISIBLE);
                else
                    binding.layoutNotFoundMessage.setVisibility(View.GONE);
            }

            binding.progressBar22.setVisibility(View.GONE);

        });

        mainViewModel.getResultFilterMessage().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;

            mainViewModel.getResultFilterMessage().setValue(null);
            binding.progressBar.setVisibility(View.GONE);
            binding.progressSearch.setVisibility(View.GONE);
            if (pageMain!=1)
                binding.progressBar22.setVisibility(View.VISIBLE);


            if (result.size() > 0) {
                if (pageMain == 1)
                    messages.clear();
                messages.addAll(result);
                if (pageMain==1)
                    adapter2.notifyDataSetChanged();
                else
                    adapter2.notifyItemRangeChanged(oldSizeLadderList, messages.size() - 1);


            } else {
                if (messages.size() == 0)
                    binding.layoutNotFoundMessage.setVisibility(View.VISIBLE);
                else
                    binding.layoutNotFoundMessage.setVisibility(View.GONE);
            }

            binding.progressBar22.setVisibility(View.GONE);

        });

    }

    //region Method
    public void reloadMessages() {
        pageMain = 1;
        oldSizeLadderList = messages.size();
        binding.progressBar22.setVisibility(View.VISIBLE);
        binding.layoutNotFoundMessage.setVisibility(View.GONE);
        binding.cardError4.setVisibility(View.GONE);
        doodViewModel.getAllCompanyWithMessages(account.getI(), Constant.APPLICATION_ID, pageMain, 10);
    }

    private void initAnimation() {
        binding.progressBar22.setAnimation("loading.json");
        binding.progressBar22.loop(true);
        binding.progressBar22.setSpeed(2f);
        binding.progressBar22.playAnimation();
        binding.progressBar22.setVisibility(View.VISIBLE);
    }

    private void init() {
        sharedPreferences.edit().putBoolean("loginSuccess", true).apply();
        setSearchViewConfig(binding.searchViewMessage);
        binding.ivBack.setOnClickListener(view1 -> Navigation.findNavController(binding.getRoot()).popBackStack());
        binding.cardError4.setOnClickListener(view12 -> reloadMessages());
    }

    private void setUpRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());

        if (endlessParentScrollListener == null) {
            endlessParentScrollListener = new EndlessParentScrollListener(manager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    if (page + 1 <= pageMain || !wordSearch.equals(""))
                        return;
                    loadMore();
                }
            };
        }

        binding.nested.setOnScrollChangeListener(endlessParentScrollListener);
        binding.recycleListChat.setLayoutManager(manager);
        adapter2 = new UniversalAdapter2(R.layout.message_row, messages, BR.message);
        binding.recycleListChat.setAdapter(adapter2);

        adapter2.setOnItemClickListener((bind, position) -> {

                Bundle bundle = new Bundle();
                bundle.putString("GID", messages.get(position).getId());
                bundle.putString("name", messages.get(position).getName());
                positionNavigate = position;
                Navigation.findNavController(getView()).navigate(R.id.DetailMessageFragment, bundle);

        });
    }

    private void loadMore() {
        oldSizeLadderList = messages.size();
        binding.progressBar22.setVisibility(View.VISIBLE);
        pageMain++;
        doodViewModel.getAllCompanyWithMessages(account.getI(), Constant.APPLICATION_ID, pageMain, 10);
    }

    private void setPropertyToAccount(){
        account = Select.from(Account.class).first();
        account.setTab(2);
        account.save();
    }


    private void setSearchViewConfig(SearchView searchView) {
        try {

            searchView.setIconifiedByDefault(false);
            searchView.setQueryHint("جستجوی پیام");
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setBackgroundColor(Color.TRANSPARENT);
            searchView.setQuery("", false);
            searchView.clearFocus();



            searchView.setBackground(null);

            LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
            LinearLayout linearLayout2;
            try {
                linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
            } catch (Exception ignore) {
                linearLayout2 = (LinearLayout) linearLayout1.getChildAt(0);
            }

            LinearLayout linearLayout3;
            try {
                linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
            } catch (Exception ignore) {
                linearLayout3 = (LinearLayout) linearLayout2.getChildAt(0);
            }


            AutoCompleteTextView autoComplete;
            try {
                autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
            } catch (Exception ignore) {
                autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(1);
            }
            autoComplete.setTextSize(12);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                autoComplete.setTextColor(getActivity().getColor(R.color.teal_200));
                autoComplete.setHintTextColor(getActivity().getColor(R.color.grey_font));

            }
            Typeface iranSansBold = Typeface.createFromAsset(getActivity().getAssets(), "iransans.ttf");
            autoComplete.setTypeface(iranSansBold);


        } catch (Exception ignore) {}
    }

    private void initSearch() {
        binding.searchViewMessage.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextSubmit(String query) {

                binding.searchViewMessage.clearFocus();
                if (!wordSearch.equals(query.trim()))
                    search(query);
                return true;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!wordSearch.equals(newText.trim()))
                    search(newText);

                return false;

            }
        });
    }

    private void nullTheMutable(){
        doodViewModel.getResultMessage().setValue(null);
        mainViewModel.getResultMessage().setValue(null);
        doodViewModel.getResultAllMessage().setValue(null);
        mainViewModel.getResultFilterMessage().setValue(null);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void search(String text) {
        binding.cardError4.setVisibility(View.GONE);
        binding.layoutNotFoundMessage.setVisibility(View.GONE);
        wordSearch = text.trim();
        oldSizeLadderList = 0;
        messages.clear();
        adapter2.notifyDataSetChanged();
        binding.progressBar.setVisibility(View.VISIBLE);

        pageMain = 1;

        if (wordSearch.trim().equals(""))
            doodViewModel.getAllCompanyWithMessages(account.getI(), Constant.APPLICATION_ID, pageMain, 10);
        else if (wordSearch.length()==1)
            mainViewModel.clearRequest();
        else if (wordSearch.length()>1) {
            binding.progressSearch.setVisibility(View.VISIBLE);
            mainViewModel.getFilterMessage(wordSearch, account.getI(), Constant.APPLICATION_ID);
        }

    }
    //endregion Method
}
