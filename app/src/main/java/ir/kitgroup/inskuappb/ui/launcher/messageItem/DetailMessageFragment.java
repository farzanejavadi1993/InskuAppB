package ir.kitgroup.inskuappb.ui.launcher.messageItem;

import static com.google.android.flexbox.JustifyContent.FLEX_START;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.card.MaterialCardView;
import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import org.apache.commons.collections4.CollectionUtils;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.ConnectServer.DoodViewModel;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;
import ir.kitgroup.inskuappb.classes.CustomDialog;
import ir.kitgroup.inskuappb.classes.EndlessParentScrollListener;
import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.classes.dialog.PictureDialog;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Files;
import ir.kitgroup.inskuappb.databinding.DetailMessageFragmentBinding;

import ir.kitgroup.inskuappb.model.Messages;
import ir.kitgroup.inskuappb.model.Survey;
import ir.kitgroup.inskuappb.util.Constant;


@AndroidEntryPoint
public class DetailMessageFragment extends Fragment {

    @Inject
    SharedPreferences sharedPreferences;
    private MainViewModel mainViewModel;
    private DoodViewModel doodViewModel;

    private DetailMessageFragmentBinding binding;

    private UniversalAdapter2 adapter2;
    private ArrayList<Messages> list;
    private int pageMain;
    private EndlessParentScrollListener endlessParentScrollListener;
    private int oldSizeLadderList = 0;
    private int positionMessage;
    private int positionSurvey;
    private RelativeLayout cardSurvey;
    private PictureDialog pictureDialog;
    private LinearLayout cardAnswer;
    private TextView txtAnswer;


    private String companyGuid;
    private CustomSnackBar snackBar;


    private Account account;
    public static int countRead;
    private CustomDialog customDialog;
    private String companyName = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DetailMessageFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundle();
        init();
        initAnimation();
        setUpDialog();
        setUpRecyclerView();

    }


    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pageMain = 1;
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        doodViewModel = new ViewModelProvider(this).get(DoodViewModel.class);
        mainViewModel.getResultMessage().setValue(null);
        doodViewModel.getResultMessage().setValue(null);


        if (pageMain * 10 > list.size() && pageMain == 1) {
            binding.progressBar.setVisibility(View.VISIBLE);
            doodViewModel.getCompanyMessages(account.getI(), Constant.APPLICATION_ID, companyGuid, pageMain, 10);
        }

        doodViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            binding.progressCompany.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
            binding.progressBar22.setVisibility(View.GONE);

            if (result.getCode() == 3) {
                ShowMessageWarning(result.getDescription());
            } else {
                binding.tvError5.setText(result.getDescription());
                binding.cardError5.setVisibility(View.VISIBLE);
            }


        });
        doodViewModel.getResultCompanyMessages().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;


            binding.progressBar22.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
            doodViewModel.getResultCompanyMessages().setValue(null);

            if (result.size() > 0) {
                if (pageMain == 1)
                    list.clear();

                list.addAll(result);
                if (pageMain == 1)
                    adapter2.notifyDataSetChanged();
                else
                    adapter2.notifyItemRangeChanged(oldSizeLadderList, list.size() - 1);




                ArrayList<Messages> res = new ArrayList<>(list);
                CollectionUtils.filter(res, r -> !r.getIsRead());
                if (res.size() > 0) {
                    countRead = res.size();//it is dirty code
                    doodViewModel.ReadCompanyMessages(account.getI(), Constant.APPLICATION_ID, companyGuid, pageMain, 10);
                }

            } else {
                if (list.size() == 0)
                    binding.layoutNotFoundDetailMessage.setVisibility(View.VISIBLE);
                else
                    binding.layoutNotFoundDetailMessage.setVisibility(View.GONE);
            }

        });
        doodViewModel.getResultReadMessages().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            doodViewModel.getResultReadMessages().setValue(null);

        });
        doodViewModel.getResultSetRespond().observe(getViewLifecycleOwner(), result -> {
            if (result == null)
                return;
            customDialog.hideProgress();
            doodViewModel.getResultSetRespond().setValue(null);
            txtAnswer.setTextColor(getActivity().getResources().getColor(R.color.teal_200));
            cardAnswer.setVisibility(View.GONE);
            txtAnswer.setVisibility(View.VISIBLE);
            txtAnswer.setText("پاسخ شما به این نظرسنجی : " + list.get(positionMessage).getSurvays().get(positionSurvey).getTitle());
            txtAnswer.setBackground(null);
            txtAnswer.setEnabled(false);
            list.get(positionMessage).getSurvays().get(positionSurvey).setChoose(true);


        });
        mainViewModel.getResultCompany().observe(getViewLifecycleOwner(), result -> {

            binding.progressCompany.setVisibility(View.GONE);
            if (result != null) {
                mainViewModel.getResultCompany().setValue(null);

                if (result.size() > 0) {

                    Company.deleteAll(Company.class);
                    Company.saveInTx(result.get(0));
                    Files.deleteAll(Files.class);
                    Files.saveInTx(result.get(0).getFiles());
                    NavDirections action = DetailMessageFragmentDirections.actionGoToDetailFragment();
                    Navigation.findNavController(binding.getRoot()).navigate(action);

                }

            }

        });
    }


    private void reloadMessages() {
        binding.layoutNotFoundDetailMessage.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.cardError5.setVisibility(View.GONE);
        doodViewModel.getCompanyMessages(account.getI(), Constant.APPLICATION_ID, companyGuid, 1, 10);
    }

    private void ShowMessageWarning(String error) {
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }
        snackBar.showSnack(getActivity(), binding.getRoot(), error);

    }

    private void initAnimation() {
        binding.progressBar22.setAnimation("loading.json");
        binding.progressBar22.loop(true);
        binding.progressBar22.setSpeed(2f);
        binding.progressBar22.playAnimation();
        binding.progressBar22.setVisibility(View.VISIBLE);
    }

    private void getBundle() {
        companyGuid = DetailMessageFragmentArgs.fromBundle(getArguments()).getGID();
        companyName = DetailMessageFragmentArgs.fromBundle(getArguments()).getName();
    }

    private void init() {
        countRead = 0;
        pictureDialog = PictureDialog.getInstance();
        snackBar = new CustomSnackBar();
        account = Select.from(Account.class).first();

        Picasso.get()
                .load("http://" + Constant.Main_URL_IMAGE + "/GetCompanyImage?id=" +
                        companyGuid + "&width=120&height=120")
                .error(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .into(binding.imgProfileChat1);


        binding.tvNameCompany.setText(companyName);

        binding.cardError5.setOnClickListener(view1 -> reloadMessages());

        binding.cardCompany.setOnClickListener(view12 -> {
            binding.progressCompany.setVisibility(View.VISIBLE);
            mainViewModel.getCompany(companyGuid);
        });
        binding.ivBack1.setOnClickListener(view1 -> Navigation.findNavController(binding.getRoot()).popBackStack());

    }

    private void setUpDialog() {
        customDialog = CustomDialog.getInstance();

        customDialog.setOnClickNegativeButton(() -> customDialog.hideProgress());

        customDialog.setOnClickPositiveButton(() -> {
                    doodViewModel.setMessageRespond(account.getI(), Constant.APPLICATION_ID, list.get(positionMessage).getId(), list.get(positionMessage).getSurvays().get(positionSurvey).getId());
                }
        );
    }

    private void setUpRecyclerView() {
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());

        if (endlessParentScrollListener == null) {
            endlessParentScrollListener = new EndlessParentScrollListener(manager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    if (page + 1 <= pageMain)
                        return;
                    loadMore();
                }
            };
        }
        binding.nested.setOnScrollChangeListener(endlessParentScrollListener);
        binding.recycleMessage.setLayoutManager(manager);
        adapter2 = new UniversalAdapter2(R.layout.message_detail_item, list, BR.detailMessage);
        binding.recycleMessage.setAdapter(adapter2);

        adapter2.setOnItemBindListener((bin, position) -> {
            try {
                View view1 = bin.getRoot();

                LinearLayout cardMessage = view1.findViewById(R.id.cardMessage);
                RoundedImageView roundImageView = view1.findViewById(R.id.ivMessage);

                cardAnswer = view1.findViewById(R.id.card_answer);
                cardAnswer.setVisibility(View.GONE);

                txtAnswer = view1.findViewById(R.id.txtAnswer);
                txtAnswer.setText("پاسخ ها");
                txtAnswer.setEnabled(true);
                txtAnswer.setTextColor(getActivity().getResources().getColor(R.color.normal_color));
                txtAnswer.setBackgroundResource(R.drawable.message_answer_background);
                txtAnswer.setVisibility(View.GONE);


                roundImageView.setOnClickListener(view2 -> pictureDialog.showDialog(getActivity(), "", account.getI(), list.get(position).getId()));

                if (list.get(position).getHasPicture()) {
                    roundImageView.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(Constant.Dood_IMAGE + "/GetMessagePicture?userId=" + account.getI() + "&appId=" + Constant.APPLICATION_ID + "&messageId=" + list.get(position).getId() + "&width=800&height=600")
                            .error(R.drawable.loading)
                            .placeholder(R.drawable.loading)
                            .into(roundImageView);
                } else
                    roundImageView.setVisibility(View.GONE);


                if (list.get(position).getIsRead())
                    cardMessage.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.linear_back_recieve_message_white));
                else
                    cardMessage.setBackgroundResource(R.drawable.linear_back_receive_message_green);


                if (list.get(position).getSurvays().size() > 0)
                    txtAnswer.setVisibility(View.VISIBLE);


                for (int j = 0; j < list.get(position).getSurvays().size(); j++) {
                    if (list.get(position).getAnswerId() != null
                            && list.get(position).getAnswerId().equals(list.get(position).getSurvays().get(j).getId())
                            || list.get(position).getSurvays().get(j).isChoose()) {
                        txtAnswer.setText("پاسخ شما به این نظرسنجی : " + list.get(position).getSurvays().get(j).getTitle());
                        txtAnswer.setTextColor(getActivity().getResources().getColor(R.color.teal_200));
                        txtAnswer.setBackground(null);
                        txtAnswer.setEnabled(false);
                    }
                }


                txtAnswer.setOnClickListener(view22 -> {
                    ArrayList<Survey> surveyList = new ArrayList<>();
                    surveyList.clear();
                    surveyList.addAll(list.get(position).getSurvays());
                    if (surveyList.size() > 0) {
                        txtAnswer = view1.findViewById(R.id.txtAnswer);
                        txtAnswer.setVisibility(View.GONE);

                        cardAnswer = view1.findViewById(R.id.card_answer);
                        cardAnswer.setVisibility(View.VISIBLE);

                        RecyclerView recyclerSurvey = view1.findViewById(R.id.recycler_survey);

                        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getActivity());
                        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
                        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
                        flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
                        flexboxLayoutManager.setAlignItems(AlignItems.BASELINE);
                        UniversalAdapter2 adapterSurvey = new UniversalAdapter2(R.layout.survey_item, surveyList, BR.survey);
                        recyclerSurvey.setLayoutManager(flexboxLayoutManager);
                        recyclerSurvey.setAdapter(adapterSurvey);


                        adapterSurvey.setOnItemClickListener((bind, pos) ->
                        {
                            try {
                                View v = bind.getRoot();
                                cardSurvey = v.findViewById(R.id.cardSurvey);


                                ArrayList<Survey> resSurvey = new ArrayList<>(list.get(position).getSurvays());
                                CollectionUtils.filter(resSurvey, Survey::isChoose);

                                if (resSurvey.size() == 0 && !list.get(position).getSurvays().get(pos).isChoose()) {

                                    positionMessage = position;
                                    positionSurvey = pos;
                                    customDialog.showDialog(getActivity(), "پاسخ شما به این نظرسنجی " + "<<" + list.get(position).getSurvays().get(pos).getTitle() + ">>" + " می باشد از ارسال آن اطمینان دارید؟", true, "خیر", "بله", false, true, true);
                                }
                            } catch (Exception ignored) {
                            }
                        });
                    }
                });

            } catch (Exception ignored) {
            }
        });

    }

    private void loadMore() {
        oldSizeLadderList = list.size();
        binding.progressBar22.setVisibility(View.VISIBLE);
        pageMain++;
        doodViewModel.getCompanyMessages(account.getI(), Constant.APPLICATION_ID, companyGuid, pageMain, 10);
    }

}
