package ir.kitgroup.inskuappb.ui.orders;

import static net.servicestack.func.Func.filter;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import org.apache.commons.collections4.CollectionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import ir.kitgroup.inskuappb.ConnectServer.CompanyViewModel;
import ir.kitgroup.inskuappb.ConnectServer.MainViewModel;
import ir.kitgroup.inskuappb.classes.dialog.CustomSnackBar;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Customer;
import ir.kitgroup.inskuappb.dataBase.Ord;
import ir.kitgroup.inskuappb.dataBase.OrdDetail;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.classes.CustomOrderDialog;
import ir.kitgroup.inskuappb.classes.CustomRecycleDialog;
import ir.kitgroup.inskuappb.databinding.CatalogActivityBinding;

import ir.kitgroup.inskuappb.model.Message;
import ir.kitgroup.inskuappb.model.ModelCatalogPage;
import ir.kitgroup.inskuappb.dataBase.ModelCatalogPageItem;
import ir.kitgroup.inskuappb.util.Constant;

@AndroidEntryPoint
public class CatalogFragment extends Fragment implements Filterable {

    //region Parameter
    @Inject
    SharedPreferences sharedPreferences;


    private CustomSnackBar snackBar;
    private CatalogActivityBinding binding;
    private Company company;
    private String userName = "";
    private String passWord = "";
    private CompanyViewModel myViewModel;


    //region Dialog Parameter
    private CustomRecycleDialog searchDialog;
    private ArrayList<ModelCatalogPage> nameCatalogList;
    private ArrayList<ModelCatalogPage> allNameCatalogPage;
    //endregion Dialog Parameter

    //region OrderDialog Parameter
    private CustomOrderDialog orderDialog;
    //endregion OrderDialog Parameter


    //region getFromBundle Parameter
    private String CatId = "";
    public long OrderId;
    //endregion getFromBundle Parameter


    //region Item Product Parameter
    private GestureDetector gestureDetector = null;
    private ArrayList<LinearLayout> layouts;
    private ArrayList<String> pageIdList;
    private int currPosition = 0;

    private List<ModelCatalogPage> catalogPages;
    private GradientDrawable bgDark;
    private int fontSize = 0;
    private Typeface fontIRANSANS;
    private DecimalFormat formatter;
    private DecimalFormat format;
    private GradientDrawable bgLight;
    private int width;
    private int height;
    //endregion Item Product Parameter


    //endregion Parameter


    private final Object mLock = new Object();
    private ArrayList<ModelCatalogPage> mOriginalValues;

    //region Override Method

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CatalogActivityBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @SuppressLint({"ClickableViewAccessibility", "NotifyDataSetChanged"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        closeKeyboard();
        snackBar=new CustomSnackBar();
        fontIRANSANS = Typeface.createFromAsset(getActivity().getAssets(), "iransans.ttf");
        format = new DecimalFormat("###.###");
        formatter = new DecimalFormat("#,###,###,###");
        orderDialog = CustomOrderDialog.getInstance();
        orderDialog.hideDialog();
        company = Select.from(Company.class).first();
        userName = company.getUser();
        passWord = company.getPass();
        catalogPages = new ArrayList<>();

        //region Create Size
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        height = dm.widthPixels;
        width = dm.heightPixels;
        double x = Math.pow(height / dm.xdpi, 2);
        double y = Math.pow(width / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        if (screenInches < 7)
            fontSize = 10;
        else
            fontSize = 12;
        bgLight = new GradientDrawable();
        bgLight.setColor(0xFFFFFFFF);
        bgDark = new GradientDrawable();
        bgDark.setColor(0xFFCCFFCC);
        //endregion Create Size

        //region Create Order
        CatId = CatalogFragmentArgs.fromBundle(getArguments()).getCat();
        OrderId = CatalogFragmentArgs.fromBundle(getArguments()).getOrderId();
        Ord ord;
        if (OrderId > 0) //Edit
            ord = Select.from(Ord.class).where("CAT ='" + CatId + "'AND ACID ='" + Select.from(Customer.class).first().getI() + "' AND id =" + OrderId).first();
        else//Create
            ord = Select.from(Ord.class).where("CAT ='" + CatId + "'AND ACID ='" + Select.from(Customer.class).first().getI() + "'").first();


        if (ord == null) {
            Ord order = new Ord();
            order.setGid(UUID.randomUUID().toString());
            order.setType(0);
            order.setDate("-");
            order.setCntid(sharedPreferences.getString("contactId", ""));
            order.setPdate("-");
            order.setAcid(Select.from(Customer.class).first().getI());
            order.setCat(CatId);
            order.setDeleted(false);
            OrderId = Ord.save(order);
        } else
            OrderId = ord.getId();

        //endregion Create Order

        //endregion Config


        //region cast ItemProduct

        //region Gravity
        binding.catalogTopLayout.getLayoutParams().width = height;
        binding.catalogTopLayout.getLayoutParams().height = (int) (width * .6);
        binding.catalogCounterLayout.getLayoutParams().width = height;
        binding.catalogCounterLayout.getLayoutParams().height = (int) (width * .05);
        binding.catalogBottomLayout.setPadding((int) (height * .005), (int) (width * .005),
                (int) (height * .005), (int) (width * .005));
        binding.catalogBottomLayout.getLayoutParams().width = height;
        binding.catalogBottomLayout.getLayoutParams().height = (int) (width * .35);
        //endregion Gravity

        gestureDetector = new GestureDetector(new MyGestureDetector());
        layouts = new ArrayList<>();
        pageIdList = new ArrayList<>();

        //region Build Down Layouts

        //enter in this place


        //endregion

        //region Dialog Search Name
        allNameCatalogPage = new ArrayList<>();
        nameCatalogList = new ArrayList<>();

        searchDialog = CustomRecycleDialog.getInstance();
        searchDialog.setOnClickItemRecycle(position -> {
            searchDialog.hideDialog();
            ArrayList<ModelCatalogPage> result = new ArrayList<>(catalogPages);
            CollectionUtils.filter(result, r -> r.getI().equals(nameCatalogList.get(position).getI()));
            if (result.size() > 0) {
                currPosition = catalogPages.indexOf(result.get(0));
                binding.counterCurrentCatalog.setText(String.valueOf(currPosition + 1));
                ShowPageWithNumber(currPosition, pageIdList.get(currPosition), false);
                binding.catalogBottomInnerLayout.removeAllViews();
                binding.progress.setVisibility(View.VISIBLE);
                myViewModel.getCatalogPageItemList(userName, passWord, CatId, result.get(0).getP(),getActivity());
            }
            //getData(currPosition, getActivity());

        });
        binding.imgMenu.setOnClickListener(v -> {
                    nameCatalogList.clear();
                    nameCatalogList.addAll(allNameCatalogPage);
                    searchDialog.showDialog(getActivity(),
                            true,
                            "",
                            true,
                            "بستن",
                            2,
                            nameCatalogList);
                }
        );

        searchDialog.SearchListener(word -> getFilter().filter(word));

        //endregion Dialog Search Name

        //region Actions
        binding.basket.setOnClickListener(view12 -> {
            NavDirections action = CatalogFragmentDirections.actionGoToOrderFragment(CatId, OrderId, "", "", false);
            Navigation.findNavController(binding.getRoot()).navigate(action);
        });
        binding.catalogPrevBtn.setVisibility(View.INVISIBLE);
        binding.catalogNextBtn.setVisibility(View.INVISIBLE);
        binding.catalogHSV.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        binding.catalogCounterLayout.setOnClickListener(v -> {

            try {
                AlertDialog.Builder pageCountBuilder = new AlertDialog.Builder(getActivity());
                pageCountBuilder.setTitle("شماره صفحه");
                pageCountBuilder.setCancelable(true);

                final EditText catalogPageNumber = new EditText(getActivity());
                catalogPageNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                catalogPageNumber.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
                catalogPageNumber.setFocusable(true);
                pageCountBuilder.setView(catalogPageNumber);

                pageCountBuilder.setPositiveButton("برو", (dialog, which) -> {


                    if (catalogPageNumber.getText() != null && !catalogPageNumber.getText().toString().equals("")) {

                        if (Integer.parseInt(catalogPageNumber.getText().toString()) > 0 &&
                                Integer.parseInt(catalogPageNumber.getText().toString()) <= layouts.size()) {

                            currPosition = Integer.parseInt(catalogPageNumber.getText().toString()) - 1;


                            binding.counterCurrentCatalog.setText(String.valueOf(currPosition + 1));


                            ShowPageWithNumber(currPosition, pageIdList.get(currPosition), true);
                            binding.catalogBottomInnerLayout.removeAllViews();


                        } else {
                            ShowMessageWarning("صفحه موجود نیست!");

                        }
                    }
                });

                pageCountBuilder.show();
            }
            catch (Exception es) {}
        });
        //endregion


        //endregion cast ItemProduct


        binding.ivBack.setOnClickListener(view1 -> Navigation.findNavController(binding.getRoot()).popBackStack());

        binding.cardError23.setOnClickListener(view13 -> {
            binding.cardError23.setVisibility(View.GONE);
            binding.progress.setVisibility(View.VISIBLE);
            myViewModel.getCatalogPage(userName, passWord, CatId,getActivity());
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myViewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        myViewModel.getResultMessage().setValue(null);

        myViewModel.getCatalogPage(userName, passWord, CatId,getActivity());
        myViewModel.getResultCatalogPage().observe(getViewLifecycleOwner(), result -> {

            if (result == null)
                return;


            int count = 0;
            RelativeLayout relativeLayout;
            ImageView imageView;

            for (int i = 0; i < result.size(); i++) {
                ArrayList<ModelCatalogPage> resultCtP = new ArrayList<>(catalogPages);
                int finalI = i;
                CollectionUtils.filter(resultCtP, c -> c.getP().equals(result.get(finalI).getP()));
                if (resultCtP.size() == 0) {
                    catalogPages.add(result.get(i));
                    if (result.get(i).getN() != null && !result.get(i).getN().equals("-")) {
                        nameCatalogList.add(result.get(i));
                    }
                    LinearLayout detailLayout = new LinearLayout(getActivity());
                    detailLayout.setLayoutParams(
                            new LinearLayout.LayoutParams(height, LinearLayout.LayoutParams.WRAP_CONTENT));
                    detailLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    detailLayout.setOrientation(LinearLayout.VERTICAL);

                    relativeLayout = new RelativeLayout(getActivity());
                    relativeLayout.setLayoutParams(
                            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                                    RelativeLayout.LayoutParams.MATCH_PARENT));
                    detailLayout.addView(relativeLayout);


                    imageView = new ImageView(getActivity());
                    imageView.setId(count);
                    imageView.setLayoutParams(
                            new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                                    LinearLayout.LayoutParams.FILL_PARENT));
                    relativeLayout.addView(imageView);
                    relativeLayout.setPadding((int) (height * .01), (int) (width * .01),
                            (int) (height * .01), (int) (width * .01));


                    pageIdList.add(result.get(i).getP());
                    layouts.add(count, detailLayout);
                    binding.catalogTopInnerLay.addView(detailLayout);
                    /*if (count == pageIdList.size() - 1 && pageIdList.size() > currPosition)
                        Picasso.get()
                                .load("http://" + company.getIp1() + "/GetImage?userName=" + userName + "&&passWord=" + passWord + "&&Id=" + pageIdList.get(currPosition) + "&&width=" + height + "&&height=" + width)
                                .error(R.drawable.loading)
                                .placeholder(R.drawable.loading)
                                .into(imageView);*/


                    count++;


                }


            }

            allNameCatalogPage.clear();
            allNameCatalogPage.addAll(nameCatalogList);


            nameCatalogList.clear();
            nameCatalogList.addAll(allNameCatalogPage);
            searchDialog.showDialog(getActivity(),
                    true,
                    "",
                    true,
                    "بستن",
                    2,
                    nameCatalogList);


            binding.progress.setVisibility(View.VISIBLE);
            myViewModel.getCatalogPageItemList(userName, passWord, CatId, pageIdList.get(currPosition),getActivity());
            binding.counterAllCatalog.setText(String.valueOf(count));
            binding.counterCurrentCatalog.setText(String.valueOf(currPosition + 1));
            ImageView imageView1 = requireActivity().findViewById(0);
            Picasso.get()
                    .load("http://" + company.getIp1() + "/GetImage?userName=" + userName + "&&passWord=" + passWord + "&&Id=" + pageIdList.get(currPosition) + "&&width=" + height + "&&height=" + width)
                    .error(R.drawable.loading)
                    .placeholder(R.drawable.loading)
                    .into(imageView1);

        });
        myViewModel.getResultCatalogPageItemList().observe(getViewLifecycleOwner(), result -> {
            binding.progress.setVisibility(View.GONE);
            if (result == null)
                return;

            myViewModel.getResultCatalogPage().setValue(null);
            myViewModel.getResultCatalogPageItemList().setValue(null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getData(getActivity(), result);
            }

        });
        myViewModel.getResultMessage().observe(getViewLifecycleOwner(), result -> {
            binding.progress.setVisibility(View.GONE);
            if (result != null){
                binding.cardError23.setVisibility(View.VISIBLE);
                binding.tvError23.setText(result.getDescription());

            }


        });


    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<ModelCatalogPage> tempList = new ArrayList();

                if (mOriginalValues == null) {
                    synchronized (mLock) {
                        mOriginalValues = new ArrayList(allNameCatalogPage);
                    }
                }

                if (!constraint.toString().equals("") && constraint != null && allNameCatalogPage != null) {

                    String[] tempSearch = constraint.toString().trim().split(" ");

                    int counter;
                    int searchSize = tempSearch.length;

                    for (ModelCatalogPage item : mOriginalValues) {


                        counter = 0;
                        for (String searchItem : tempSearch) {

                            if (item.getN().contains(searchItem)) {
                                counter++;
                            }
                        }

                        if (counter == searchSize)
                            tempList.add(item);
                    }

                    filterResults.values = tempList;
                    filterResults.count = tempList.size();
                } else {
                    synchronized (mLock) {
                        tempList = new ArrayList(mOriginalValues);
                    }

                    filterResults.values = tempList;
                    filterResults.count = tempList.size();
                }

                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                nameCatalogList.clear();
                nameCatalogList.addAll((Collection<? extends ModelCatalogPage>) results.values);
                if (nameCatalogList.size() == 0)
                    myViewModel.getResultMessage().setValue(new Message(40, "هیچ نامی وجود ندارد", ""));

                try {
                    searchDialog.notifyDataSetChanged();
                }catch (Exception ignored){}


            }
        };
    }


    //endregion Override Method


    //region Functions
    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            binding.txtError.setVisibility(View.GONE);

            boolean last = false, first = false;
            if (currPosition == layouts.size() - 1) {
                last = true;
            } else if (currPosition == 0) {
                first = true;
            }

            if (e1.getX() < e2.getX()) {
                currPosition = getVisibleViews("left");
            } else {
                currPosition = getVisibleViews("right");
            }

            if (currPosition == 0 && first) {
                currPosition = layouts.size() - 1;
            } else if (currPosition == layouts.size() - 1 && last) {
                currPosition = 0;
            }


            binding.counterCurrentCatalog.setText(String.valueOf(currPosition + 1));


            binding.progress.setVisibility(View.VISIBLE);
            myViewModel.getCatalogPageItemList(userName, passWord, CatId, pageIdList.get(currPosition),getActivity());
            ShowPageWithNumber(currPosition, pageIdList.get(currPosition), false);
            binding.catalogBottomInnerLayout.removeAllViews();

            //getData(currPosition, getActivity());


            return true;
        }
    }

    public int getVisibleViews(String direction) {
        Rect hitRect = new Rect();
        int position = 0;
        int rightCounter = 0;

        for (int i = 0; i < layouts.size(); i++) {
//            if (layouts.get(i).getLocalVisibleRect(hitRect)) {
            if (currPosition == i) {
//                if (direction.equals("left") && i < currPosition) {
//                    continue;
//                }else if (direction.equals("right") && i > currPosition){
//
//                }

                position = i;

                if (direction.equals("left")) {
                    if (i < layouts.size() - 1)
                        position = i + 1;

                    break;
                } else if (direction.equals("right")) {
                    rightCounter++;
                    if (i > 0)
                        position = i - 1;

                    if (rightCounter == 2)
                        break;
                }
            }
        }
        return position;
    }

    public void ShowPageWithNumber(Integer currentPosition, String pageId, boolean getProduct) {

        currPosition = currentPosition;

        if (currPosition == 0) {
            binding.catalogPrevBtn.setVisibility(View.INVISIBLE);

            if (catalogPages.size() > 1) {
                binding.catalogNextBtn.setVisibility(View.VISIBLE);
                ImageView imageViewAfter = requireActivity().findViewById(currPosition + 1);
                imageViewAfter.setImageDrawable(null);
            }
        } else if (currPosition == layouts.size() - 1) {
            binding.catalogNextBtn.setVisibility(View.INVISIBLE);

            ImageView imageViewBack = requireActivity().findViewById(currPosition - 1);
            imageViewBack.setImageDrawable(null);
        } else {
            binding.catalogPrevBtn.setVisibility(View.VISIBLE);
            binding.catalogNextBtn.setVisibility(View.VISIBLE);

            ImageView imageViewBack = requireActivity().findViewById(currPosition - 1);
            ImageView imageViewAfter = getActivity().findViewById(currPosition + 1);
            imageViewBack.setImageDrawable(null);
            imageViewAfter.setImageDrawable(null);
        }

        binding.catalogHSV.smoothScrollTo(layouts.get(currPosition).getLeft(), 0);
        ImageView imageView = requireActivity().findViewById(currPosition);
        Picasso.get()
                .load("http://" + company.getIp1() + "/GetImage?userName=" + userName + "&&passWord=" + passWord + "&&Id=" + pageId + "&&width=" + height + "&&height=" + (int) (width * .6))
                .error(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .into(imageView);

        if (getProduct) {
            binding.progress.setVisibility(View.VISIBLE);
            myViewModel.getCatalogPageItemList(userName, passWord, CatId, pageIdList.get(currPosition),getActivity());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"NewApi", "Range", "SetTextI18n", "UseCompatLoadingForDrawables", "ObsoleteSdkInt"})
    public void getData(Context context, List<ModelCatalogPageItem> list) {


        //  if (Constant.GetCheckAmountSt(sharedPreferences))
        CollectionUtils.filter(list, l -> l.getPrice() > 0 && l.getCoefficient() > 0);
        // else
        //   CollectionUtils.filter(list, l -> l.getPrice() > 0 && l.getCoefficient() > 0 && l.getInventory() > 0);


        if (list.size() == 0)
            binding.txtError.setVisibility(View.VISIBLE);
        else
            binding.txtError.setVisibility(View.GONE);


        for (int i = 0; i < list.size(); i++) {

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 0, 20);

            LinearLayout bottomRow = new LinearLayout(getActivity());
            bottomRow.setLayoutParams(layoutParams);
            bottomRow.setPadding((int) (height * .01), (int) (width * .01),
                    (int) (height * .01), (int) (width * .01));


            bottomRow.setOrientation(LinearLayout.HORIZONTAL);
            bottomRow.setBackground(bgLight);

            TextView pageId = new TextView(getActivity());
            pageId.setText(list.get(i).getP());
            pageId.setVisibility(View.GONE);


            TextView addedItem = new TextView(getActivity());
            addedItem.setText(String.valueOf(false));
            addedItem.setVisibility(View.GONE);


            TextView addedIndex = new TextView(getActivity());
            addedIndex.setVisibility(View.GONE);


            TextView rowNumber = new TextView(getActivity());
            rowNumber.setWidth((int) (height * .025));
            rowNumber.setGravity(Gravity.CENTER);
            rowNumber.setTextSize(fontSize);
            rowNumber.setTextColor(Color.BLACK);
            rowNumber.setTypeface(fontIRANSANS, Typeface.BOLD);
            rowNumber.setText("_" + (i + 1));


            TextView productName = new TextView(getActivity());
            productName.setWidth((int) (height * .515));
            productName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            String name = list.get(i).getPn() + " ( " + "ناموجود" + " )";
            if (name.length() > 48)
                productName.setTextSize(fontSize - 1);
            else
                productName.setTextSize(fontSize);


            productName.setTextColor(Color.BLACK);
            productName.setTypeface(fontIRANSANS, Typeface.BOLD);
            productName.setText(list.get(i).getPn());


            TextView productUnit1 = new TextView(getActivity());
            productUnit1.setWidth((int) (height * .085));
            productUnit1.setGravity(Gravity.CENTER);
            productUnit1.setTextSize(fontSize);
            productUnit1.setTextColor(Color.BLACK);
            productUnit1.setTypeface(fontIRANSANS, Typeface.BOLD);
            productUnit1.setText(list.get(i).getPu1());


           /* TextView productRatio = new TextView(getActivity());
            productRatio.setWidth((int) (height * .05));
            productRatio.setGravity(Gravity.CENTER);
            productRatio.setTextSize(fontSize);
            productRatio.setTextColor(Color.BLACK);
            productRatio.setTypeface(fontIRANSANS, Typeface.BOLD);
            productRatio.setText(format.format(list.get(i).getCoefficient()));*/


            TextView productPrice = new TextView(getActivity());
            productPrice.setWidth((int) (height * .145));
            productPrice.setGravity(Gravity.CENTER);
            productPrice.setTextSize(fontSize);
            productPrice.setTextColor(Color.BLACK);
            productPrice.setTypeface(fontIRANSANS, Typeface.BOLD);
            productPrice.setText(formatter.format(list.get(i).getPrice()) + "ریال");


           /* TextView productUnit2 = new TextView(getActivity());
            productUnit2.setWidth((int) (height * .075));
            productUnit2.setTextSize(fontSize);
            productUnit2.setGravity(Gravity.CENTER);
            productUnit2.setTextColor(Color.BLACK);
            productUnit2.setTypeface(fontIRANSANS, Typeface.BOLD);
            productUnit2.setText(list.get(i).getPu2());*/


            TextView customerPrice = new TextView(getActivity());
            customerPrice.setWidth((int) (height * .145));
            customerPrice.setGravity(Gravity.CENTER);
            customerPrice.setTextSize(fontSize);
            customerPrice.setTextColor(Color.BLACK);
            customerPrice.setTypeface(fontIRANSANS, Typeface.BOLD);
            customerPrice.setText(formatter.format(list.get(i).getPriceCustomer()) + "ریال");


            TextView addButton = new TextView(getActivity());
            addButton.setWidth((int) (height * .035));
            addButton.setGravity(Gravity.CENTER);
            addButton.setTextSize(30);
            addButton.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);


            if (!Constant.GetCheckAmountSt(sharedPreferences) && list.get(i).getInventory() <= 0) {
                rowNumber.setBackgroundResource(R.color.red);
                String description = list.get(i).getPn().trim() + " (" + "ناموجود" + ")";
                Constant.setTextFontToSpecialPart(description, fontIRANSANS, 3, 10, Color.BLACK,  productName);



            } else if (list.get(i).getGiftAmount() > 0
                    && list.get(i).getAmountForGift() > 0
                    && list.get(i).getDo() &&
                    list.get(i).getDefaultDiscount() > 0) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    rowNumber.setBackgroundDrawable(context.getDrawable(R.drawable.ic_circle_blue));
                } else {
                    rowNumber.setBackgroundResource(R.color.colorRedGray800);
                }

            } else if (list.get(i).getGiftAmount() > 0
                    && list.get(i).getAmountForGift() > 0
                    && list.get(i).getDo()) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    rowNumber.setBackgroundDrawable(context.getDrawable(R.drawable.ic_circle_green));
                } else {
                    rowNumber.setBackgroundResource(R.color.selected_item_color);
                }


            } else if (list.get(i).getAmountForGift() == 0
                    && list.get(i).getGiftAmount() == 0
                    && (list.get(i).getDo()
                    && list.get(i).getDefaultDiscount() > 0)) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    rowNumber.setBackgroundDrawable(context.getDrawable(R.drawable.ic_circle_yellow));
                } else {
                    rowNumber.setBackgroundResource(R.color.selected_item_color_yellow);
                }
            }


            final TextView finalAddedItem = addedItem;
            final LinearLayout finalRow = bottomRow;
            final TextView finalAddButton = addButton;


            //region OnClick Row


            List<OrdDetail> ordDetails = Select.from(OrdDetail.class).list();

            int finalI1 = i;
            bottomRow.setOnClickListener(v -> {
                try {
                    if (Boolean.valueOf(finalAddedItem.getText().toString())) {

                        int index;
                        int amountGift;

                        ArrayList<OrdDetail> resultOrdDtl = new ArrayList<>(ordDetails);
                        CollectionUtils.filter(resultOrdDtl, o -> o.O == OrderId && o.getProducts().equals(list.get(finalI1).getG()) && o.getGifttype() == 4 && !o.getGift());


                        if (resultOrdDtl.size() > 0) {

                            String I = "";
                            if (list.get(finalI1).getDo()
                                    &&
                                    list.get(finalI1).getAmountForGift() > 0 && list.get(finalI1).getGiftAmount() > 0
                            ) {
                                if (!list.get(finalI1).getGp().equals("0"))
                                    I = list.get(finalI1).getGp();
                                else
                                    I = list.get(finalI1).getG();

                            }

                            amountGift = (int) (resultOrdDtl.get(0).getAmount1() / list.get(finalI1).getAmountForGift()) * list.get(finalI1).getGiftAmount();


                            //region delete gift row automatic

                            ArrayList<OrdDetail> ordDetailsAutomaticRowGift = new ArrayList<>(ordDetails);
                            String finalI = I;
                            CollectionUtils.filter(ordDetailsAutomaticRowGift, o -> o.O == OrderId && o.getProducts().equals(finalI) && o.getGifttype() == 1 && o.getGift());

                            if (ordDetailsAutomaticRowGift.size() > 0) {
                                index = (ordDetails.indexOf(ordDetailsAutomaticRowGift.get(0)));
                                if (amountGift == ordDetails.get(index).getAmount1()) {
                                    OrdDetail.delete(ordDetailsAutomaticRowGift);
                                } else {
                                    ordDetails.get(index).setAmount1(ordDetails.get(index).getAmount1() - amountGift);
                                    ordDetails.get(index).setIAM(ordDetails.get(index).getAmount1() - amountGift);
                                    OrdDetail.saveInTx(ordDetails);
                                }

                            }
                            //endregion delete gift row automatic


                            //region delete  row
                            if (ordDetailsAutomaticRowGift.size() > 0) {
                                index = (ordDetails.indexOf(ordDetailsAutomaticRowGift.get(0)));
                                OrdDetail.delete(ordDetails.get(index));
                            }
                            OrdDetail.delete(ordDetails.get(ordDetails.indexOf(resultOrdDtl.get(0))));


                            //endregion delete  row
                        }


                        finalAddedItem.setText(String.valueOf(false));

                        finalAddButton.setText("+");
                        finalAddButton.setTextColor(Color.GREEN);
                        finalRow.setBackground(bgLight);
                    } else {


                        if (Constant.GetCatalogAmountSt(sharedPreferences)) {

                            String cofficient;
                            if (list.get(finalI1).getDo() && list.get(finalI1).getSc() > 0) {
                                cofficient = "مقدار  وارد شده باید ضریبی از " + formatter.format(list.get(finalI1).getSc()) + " باشد .";
                            } else
                                cofficient = "";


                            String gift;
                            if (list.get(finalI1).getDo() && list.get(finalI1).getAmountForGift() > 0 && list.get(finalI1).getGiftAmount() > 0) {
                                String nameG;
                                String unitG;
                                if (list.get(finalI1).getGp().equals("0")) {

                                    nameG = list.get(finalI1).getPn();
                                    unitG = list.get(finalI1).getPu1();

                                } else {
                                    nameG = list.get(finalI1).getGpn();
                                    unitG = list.get(finalI1).getGpu1();

                                }
                                gift = " به ازای خرید" + " " + list.get(finalI1).getAmountForGift() + " " + unitG + " " + "از این کالا" + " " + list.get(finalI1).getGiftAmount() + " " + unitG + " " + "از کالای" + " " + nameG + " " + "جایزه بگیرید.";
                            } else
                                gift = "";


                            String discount;
                            if (list.get(finalI1).getDo() && list.get(finalI1).getDefaultDiscount() > 0) {
                                discount = "تخفیف پیش فرض : " + String.valueOf(list.get(finalI1).getDefaultDiscount()).replace(".0", "") + "\n" +
                                        "  حدااکثر تخفیف : " + String.valueOf(list.get(finalI1).getMaxDiscount()).replace(".0", "") + "\n" +
                                        "  حدااقل تخفیف : " + String.valueOf(list.get(finalI1).getMinDiscount()).replace(".0", "");
                            } else
                                discount = "";


                            String lower;
                            String upper;

                            if (list.get(finalI1).getDo()) {
                                lower = String.valueOf(list.get(finalI1).getMinAmount()).replace(".0", "") + "";
                                upper = String.valueOf(list.get(finalI1).getMaxAmount()).replace(".0", "") + "";


                            } else {
                                lower = "";
                                upper = "";
                            }


                            String remain;
                            remain = formatter.format(list.get(finalI1).getInventory());


                            String textDescription = "هر " + formatter.format(list.get(finalI1).getCoefficient()) + " " + list.get(finalI1).getPu1() + " " + "از این کالا برابر با یک " + list.get(finalI1).getPu2() + " می باشد . ";


                            orderDialog.hideDialog();
                            orderDialog.showOrderDialog(getActivity(), list.get(finalI1).getPn(), cofficient, gift, discount, lower, upper, remain, textDescription);


                            orderDialog.setOnClickPositiveButton(amount -> {

                                if (amount == 0)
                                    return;
                                if (Constant.GetCatalogAmountSt(sharedPreferences)) {

                                    float AvailAmount = list.get(finalI1).getInventory();


                                    if (!Constant.GetCheckAmountSt(sharedPreferences))
                                        if (amount > AvailAmount) {
                                            ShowMessageWarning("مقدار واردشده از موجودی کالا بیشتر است!");

                                            return;
                                        }

                                    if (list.get(finalI1).getDo()) {

                                        if (list.get(finalI1).getSc() != 0 && amount % list.get(finalI1).getSc() != 0) {
                                            ShowMessageWarning("مقدار یک وارد شده باید ضریبی از " + formatter.format(list.get(finalI1).getSc()) + " باشد ");
                                            return;
                                        }

                                        if (list.get(finalI1).getMinAmount() != 0 && amount >= list.get(finalI1).getMinAmount()) {

                                            if (list.get(finalI1).getMaxAmount() != 0 && amount <= list.get(finalI1).getMaxAmount()) {

                                            } else {


                                                ShowMessageWarning("مقدار واردشده از مقدار مجاز بیشتر است! حداکثر: " + list.get(finalI1).getMaxAmount());
                                                return;

                                            }
                                        } else {

                                            ShowMessageWarning("مقدار واردشده از مقدار مجاز کمتر است! حداقل: " + list.get(finalI1).getMinAmount());

                                            return;
                                        }
                                    }

                                    //region ایجاد  ردیف سفارش
                                    OrdDetail orderDtlail = Select.from(OrdDetail.class).where("PRODUCT ='" + list.get(finalI1).getG() + "' AND O =" + OrderId + " AND GIFTTYPE =" + 4).first();
                                    if (orderDtlail == null) {
                                        OrdDetail ordDtl = new OrdDetail();
                                        ordDtl.setPLDI(list.get(finalI1).getPldi());
                                        ordDtl.setAmount1(Float.parseFloat(String.valueOf(amount)));
                                        ordDtl.setIAM(Float.parseFloat(String.valueOf(amount)));
                                        ordDtl.setGift(false);
                                        ordDtl.setGifttype(4);
                                        ordDtl.setI(UUID.randomUUID().toString());
                                        ordDtl.setProducts(list.get(finalI1).getG());
                                        ordDtl.setIdOrders(OrderId);
                                        ordDtl.setPrice1(list.get(finalI1).getPrice());
                                        ordDtl.setUnit(list.get(finalI1).getPu1());
                                        ordDtl.setDiscountPercent(list.get(finalI1).getDo() ? list.get(finalI1).getDefaultDiscount() : 0);
                                        ir.kitgroup.inskuappb.dataBase.OrdDetail.saveInTx(ordDtl);
                                    } else {
                                        orderDtlail.setAmount1(Float.parseFloat(String.valueOf(amount)));
                                        orderDtlail.setIAM(Float.parseFloat(String.valueOf(amount)));
                                        orderDtlail.save();
                                    }


                                    //endregion ایجاد  ردیف سفارش


                                    //region چنانچه به ردیف ایجاد شده جایزه تعلق بگیرد
                                    if (list.get(finalI1).getDo() && list.get(finalI1).getAmountForGift() > 0 && list.get(finalI1).getGiftAmount() > 0) {


                                        if (amount >= list.get(finalI1).getAmountForGift()) {

                                            int amountGift = (int) ((amount / list.get(finalI1).getAmountForGift()) * list.get(finalI1).getGiftAmount());


                                            //region ایجاد ردیف جایزه

                                            String GProductId;
                                            if (list.get(finalI1).getGp().equals("0"))
                                                GProductId = list.get(finalI1).getG();
                                            else
                                                GProductId = list.get(finalI1).getGp();


                                            OrdDetail GordDtl = Select.from(OrdDetail.class).where("PRODUCT ='" + GProductId + "' AND O =" + OrderId + " AND GIFTTYPE =" + 1).first();

                                            if (GordDtl == null) {
                                                OrdDetail ordDtlG = new OrdDetail();
                                                ordDtlG.setAmount1(amountGift);
                                                ordDtlG.setIAM(amountGift);
                                                ordDtlG.setGift(true);
                                                ordDtlG.setGifttype(1);
                                                ordDtlG.setProducts(GProductId);
                                                ordDtlG.setIdOrders(OrderId);
                                                ordDtlG.setPRICE2(String.valueOf(0));
                                                ordDtlG.setPrice1(0);
                                                ordDtlG.setDiscountPercent(0);
                                                ordDtlG.setName(list.get(finalI1).getGpn());
                                                ordDtlG.setUnit(list.get(finalI1).getGpu1());
                                                OrdDetail.saveInTx(ordDtlG);
                                            } else {
                                                GordDtl.setAmount1(GordDtl.getAmount1() + amountGift);
                                                GordDtl.setIAM(GordDtl.getAmount1() + amountGift);
                                                GordDtl.save();
                                            }


                                            //endregion ایجاد ردیف جایزه


                                        }
                                    }
                                    //endregion چنانچه به ردیف ایجاد شده جایزه تعلق بگیرد

                                    finalAddedItem.setText(String.valueOf(true));
                                    finalAddButton.setText("-");
                                    finalAddButton.setTextColor(Color.RED);
                                    finalRow.setBackground(bgDark);


                                }


                                List ordD = Select.from(OrdDetail.class).where("O =" + OrderId).list();
                                if (ordD.size() > 0)
                                    binding.badge.setVisibility(View.VISIBLE);
                                else
                                    binding.badge.setVisibility(View.GONE);
                                binding.badge.setText(String.valueOf(ordD.size()));
                            });


                            orderDialog.setOnClickMaxButton((amount, edtAmount) -> {
                                if (amount < list.get(finalI1).getInventory()) {
                                    if (list.get(finalI1).getDo() && list.get(finalI1).getSc() != 0) {
                                        if (amount != 0) {
                                            double rest = amount % list.get(finalI1).getSc();
                                            if (rest != 0)
                                                amount = amount - rest;
                                        }
                                        amount = amount + list.get(finalI1).getSc();
                                    } else
                                        amount = amount + 1;

                                    orderDialog.setAmount(edtAmount, amount);
                                }
                            });


                            orderDialog.setOnClickMinButton((amount, edtAmount) -> {
                                if (amount >= 1) {
                                    if (list.get(finalI1).getDo() && list.get(finalI1).getSc() != 0) {
                                        if (amount != 0 && amount <= list.get(finalI1).getSc())
                                            amount = 0;

                                        else if (amount != 0 && amount > list.get(finalI1).getSc()) {
                                            double rest = amount % list.get(finalI1).getSc();
                                            if (rest != 0)
                                                amount = amount - rest;
                                            amount = amount - list.get(finalI1).getSc();
                                        }

                                    }

                                    else
                                        amount = amount - 1;


                                    orderDialog.setAmount(edtAmount, amount);
                                }
                            });


                        } else {

                            OrdDetail OrdDetail = new OrdDetail();
                            OrdDetail.setPLDI(list.get(finalI1).getPldi());
                            OrdDetail.setAmount1(Float.parseFloat("0"));
                            OrdDetail.setIAM(Float.parseFloat("0"));
                            OrdDetail.setGift(false);
                            OrdDetail.setGifttype(4);
                            OrdDetail.setIdOrders(OrderId);
                            OrdDetail.setProducts(list.get(finalI1).getG());
                            ir.kitgroup.inskuappb.dataBase.OrdDetail.save(OrdDetail);


                            finalAddedItem.setText(String.valueOf(true));

                            finalAddButton.setText("-");
                            finalAddButton.setTextColor(Color.RED);
                            finalRow.setBackground(bgDark);
                        }

                    }

                    List ordD = Select.from(OrdDetail.class).where("O =" + OrderId).list();
                    if (ordD.size() > 0)
                        binding.badge.setVisibility(View.VISIBLE);
                    else
                        binding.badge.setVisibility(View.GONE);
                    binding.badge.setText(String.valueOf(ordD.size()));
                } catch (Exception ignored) {

                }
            });


            List<OrdDetail> currOrdDet = filter(ordDetails, p -> p.O == OrderId && p.getProducts().equals(list.get(finalI1).getG()) && !p.getGift());


            if (currOrdDet.size() > 0) {
                addButton.setText("-");
                addButton.setTextColor(Color.RED);
                addedItem.setText(String.valueOf(true));
                addedIndex.setText(String.valueOf(currOrdDet.get(0).getId()));
                bottomRow.setBackground(bgDark);
            } else {
                addButton.setText("+");
                addButton.setTextColor(Color.GREEN);
            }

            //endregion

            List ordD = Select.from(OrdDetail.class).where("O =" + OrderId).list();
            if (ordD.size() > 0)
                binding.badge.setVisibility(View.VISIBLE);
            else
                binding.badge.setVisibility(View.GONE);
            binding.badge.setText(String.valueOf(ordD.size()));


            bottomRow.addView(addButton);
            bottomRow.addView(customerPrice);
            //bottomRow.addView(productUnit2);
            bottomRow.addView(productPrice);
            // bottomRow.addView(productRatio);
            bottomRow.addView(productUnit1);
            bottomRow.addView(productName);
            bottomRow.addView(rowNumber);
            bottomRow.addView(addedIndex);
            bottomRow.addView(addedItem);


            binding.catalogBottomInnerLayout.addView(bottomRow);

            if (list.get(finalI1).getStp() != null && list.get(finalI1).getStp().equals("1"))
                bottomRow.setVisibility(View.GONE);


        }
    }



    private void closeKeyboard()
    {
        try {
            InputMethodManager manager
                    = (InputMethodManager)
                    getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            binding.getRoot().getWindowToken(), 0);
        }catch (Exception ignored){}


    }


    private void ShowMessageWarning(String error) {
        try {
            snackBar.hide();
        } catch (Exception ignored) {
        }
        snackBar.showSnack(getActivity(), binding.getRoot(), error);

    }


//endregion
}
