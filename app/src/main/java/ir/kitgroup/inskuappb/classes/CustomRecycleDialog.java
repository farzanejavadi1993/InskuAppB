package ir.kitgroup.inskuappb.classes;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ir.kitgroup.inskuappb.BR;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.adapter.UniversalAdapter2;

public class CustomRecycleDialog  {


    private RecyclerView recyclerView;
    private  UniversalAdapter2 adapter2;


    //region Interface Search Item Recycle
    public interface Search {
        void onSearch(String word);
    }

    private Search search;

    public void SearchListener(Search search) {
        this.search = search;
    }
    //endregion Interface Item Recycle




    //region Interface Item Recycle
    public interface ClickItemRecycle {
        void onClick(int position);
    }

    private ClickItemRecycle clickItemRecycle;

    public void setOnClickItemRecycle(ClickItemRecycle clickItemRecycle) {
        this.clickItemRecycle = clickItemRecycle;
    }
    //endregion Interface Item Recycle


    private static CustomRecycleDialog customRecycleDialog = null;
    private Dialog mDialog;


    public static CustomRecycleDialog getInstance() {
        if (customRecycleDialog == null) {
            customRecycleDialog = new CustomRecycleDialog();
        }
        return customRecycleDialog;
    }

    public void
    showDialog(Activity context, boolean visibleEditText, String message, boolean cancelable, String textNegative, int type, List<?> list) {
        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDialog.setContentView(R.layout.custom_recycle_dialog);


        recyclerView = mDialog.findViewById(R.id.recyclerViewDialog);
        TextView tvMessage = mDialog.findViewById(R.id.tvMessage);
        EditText edtSearch = mDialog.findViewById(R.id.edt_search_name_catalogpage);
        RelativeLayout rlButton = mDialog.findViewById(R.id.layout);
        //1 Choose Catalog

        if (type == 1)
        adapter2 = new UniversalAdapter2(R.layout.name_catalog_item, list, BR.catalog);

        //2 Choose CatalogPage
        else if (type == 2)
            adapter2 = new UniversalAdapter2(R.layout.name_catalog_page_item, list, BR.catalogPage);




        DisplayMetrics dm = new DisplayMetrics();
        Objects.requireNonNull(context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        if (visibleEditText) {
            mDialog.getWindow().setLayout((int) (dm.widthPixels * .9), LinearLayout.LayoutParams.WRAP_CONTENT);
            edtSearch.setVisibility(View.VISIBLE);
            tvMessage.setVisibility(View.GONE);
            FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(context);
            flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
            flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
            flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
            flexboxLayoutManager.setAlignItems(AlignItems.BASELINE);
            recyclerView.setLayoutManager(flexboxLayoutManager);
        } else {
            //mDialog.getWindow().setLayout(dm.widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT);
            rlButton.setVisibility(View.GONE);
            tvMessage.setVisibility(View.VISIBLE);
            edtSearch.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

        }


        recyclerView.setAdapter(adapter2);
        adapter2.setOnItemClickListener((binding, position) ->
                {
                    try{
                        clickItemRecycle.onClick(position);
                    }catch (Exception ignored){}

                }

        );

        TextView tvNegative = mDialog.findViewById(R.id.tv_negative);
        RelativeLayout rlCancel = mDialog.findViewById(R.id.rl_cancel);

        tvMessage.setText(message);
        tvNegative.setText(textNegative);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().equals(""))
             search.onSearch(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);

        rlCancel.setOnClickListener(view -> hideDialog());

        mDialog.show();
    }

    public void hideDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public void notifyDataSetChanged(){

        try {
            adapter2.notifyDataSetChanged();
        }catch (Exception ignored){}

    }

}