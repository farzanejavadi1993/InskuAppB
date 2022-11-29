package ir.kitgroup.inskuappb.ui.orders;


import static ir.kitgroup.inskuappb.util.Constant.toEnglishNumber;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import es.dmoral.toasty.Toasty;
import ir.kitgroup.inskuappb.dataBase.ModelCatalogPageItem;
import ir.kitgroup.inskuappb.R;

import com.orm.query.Select;

import org.apache.commons.collections4.CollectionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ir.kitgroup.inskuappb.dataBase.OrdDetail;
import ir.kitgroup.inskuappb.util.Constant;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.viewHolder> {
    private final List<OrdDetail> list;
    private final Activity context;
    private final DecimalFormat formatter;
    private final DecimalFormat df;
    private final long OrderId;
    private  int from;//1 create order         2 show order

    private final SharedPreferences sharedPreferences;


    //regionInterfaceCalculateFinalPrice
    public interface ChangeItem {
        void onChange();
    }

    public ChangeItem changeItem;

    public void OnItemChange(ChangeItem changeItem) {
        this.changeItem = changeItem;
    }
    //endregionInterfaceCalculateFinalPrice


    public OrderAdapter(Activity context, List<OrdDetail> list, SharedPreferences sharedPreferences, long orderId,int from) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.list = list;
        this.from=from;
        this.OrderId = orderId;
        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        formatter = new DecimalFormat("#,###,###,###");
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycle_orderdetail, parent, false);
        return new viewHolder(view);
    }


    @SuppressLint({"ResourceType", "UseCompatLoadingForDrawables", "SetTextI18n"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {

        try {


            if (from==2){
                holder.amount1.setEnabled(false);
                holder.ivMax.setVisibility(View.GONE);
                holder.ivMin.setVisibility(View.GONE);
                holder.discount.setText("");
                holder.DeleteBtn.setVisibility(View.GONE);
            }else
            {
                holder.amount1.setEnabled(true);
                holder.ivMax.setVisibility(View.VISIBLE);
                holder.ivMin.setVisibility(View.VISIBLE);
                holder.discount.setText("");
            }



            OrdDetail orderTemp = Select.from(OrdDetail.class).where("PRODUCT ='" + list.get(holder.getAdapterPosition()).getProducts() + "' AND GIFTTYPE =" + list.get(holder.getAdapterPosition()).getGifttype() + " AND O =" + OrderId).first();

            holder.products = Select.from(ModelCatalogPageItem.class).where("G ='" + orderTemp.getProducts() + "'").first();


            if (holder.products != null) {
                orderTemp.setName(holder.products.getPn());
                orderTemp.setUnit(holder.products.getPu1());
                orderTemp.setPrice1(holder.products.getPrice());
            }
            //region Check Remain
            float amountGift = 0;
            ArrayList<OrdDetail> ordDetGiftResult = new ArrayList<>(list);
            CollectionUtils.filter(ordDetGiftResult, od -> od.getProducts().equals(orderTemp.getProducts()) && od.getGift() && !orderTemp.getGift());
            if (ordDetGiftResult.size() > 0) {
                amountGift = ordDetGiftResult.get(0).getAmount1();
            }


            if (!Constant.GetCheckAmountSt(sharedPreferences) &&
                    Constant.GetOnLineOrder(sharedPreferences) &&
                    holder.products.getInventory() < orderTemp.getAmount1() +
                            amountGift) {
                holder.imgWarning.setVisibility(View.VISIBLE);
                orderTemp.warning = true;
            } else {
                holder.imgWarning.setVisibility(View.GONE);
                orderTemp.warning = false;
            }


            //endregion Check Remain

            //regionBackGroundColor

            if (orderTemp.getGift() && orderTemp.getGifttype() == 1) {
                holder.cardLLView.setBackgroundResource(R.color.selected_item_color);

                holder.name.setTextColor(0xFFFFFFFF);
                holder.unit1.setTextColor(0xFFFFFFFF);
                holder.amount1.setTextColor(0xFFFFFFFF);
                holder.price.setTextColor(0xFFFFFFFF);
                holder.totalPrice.setTextColor(0xFFFFFFFF);

            } else if (holder.products != null && holder.products.getDo() && holder.products.getDefaultDiscount() > 0) {
                holder.cardLLView.setBackgroundResource(R.color.selected_item_color_yellow);
                holder.discount.setText(formatter.format(holder.products.getDefaultDiscount()) + "%");
                holder.name.setTextColor(0xFFFFFFFF);
                holder.unit1.setTextColor(0xFFFFFFFF);
                holder.amount1.setTextColor(0xFFFFFFFF);
                holder.price.setTextColor(0xFFFFFFFF);
                holder.totalPrice.setTextColor(0xFFFFFFFF);
            } else if (orderTemp.getGifttype() == 3)
                holder.cardLLView.setBackgroundResource(R.color.selected_item_color_pink);

            //endregionBackGroundColor

            //regionRowGift
            if (orderTemp.getGift()) {
                holder.amount1.setEnabled(false);
                holder.ivMax.setVisibility(View.GONE);
                holder.ivMin.setVisibility(View.GONE);
                holder.DeleteBtn.setVisibility(View.GONE);
                orderTemp.setTotalPrice(0);
                orderTemp.setDiscountPercent(0);
                orderTemp.setPrice1(0);
                orderTemp.setPRICE2("0");
            }
            //endregionRowGift

            //regionRowDiscountPercent
            if (holder.products != null && holder.products.getDo() && holder.products.getDefaultDiscount() > 0)
                orderTemp.setDiscountPercent(holder.products.getDefaultDiscount());
            //endregionRowDiscountPercent


            //regionTextWatcherAmount

            if (holder.textWatcherAmount == null) {

                holder.textWatcherAmount = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                        OrdDetail ordDtl = Select.from(OrdDetail.class).where("PRODUCT ='" + list.get(holder.getAdapterPosition()).getProducts() + "' AND GIFTTYPE =" + list.get(holder.getAdapterPosition()).getGifttype() + " AND O =" + OrderId).first();


                        ModelCatalogPageItem product = Select.from(ModelCatalogPageItem.class).where("G ='" + ordDtl.getProducts() + "'").first();


                        ordDtl.edit = true;

                        s = toEnglishNumber(s.toString());

                        //regionEmptyS
                        if (!((String) s).isEmpty()) {
                            if (s.toString().contains(".") &&
                                    s.toString().indexOf(".") == s.toString().length() - 1) {
                                return;
                            } else if (s.toString().contains("٫") &&
                                    s.toString().indexOf("٫") == s.toString().length() - 1) {
                                return;
                            }
                        } else {

                            float oldAmount = ordDtl.getAmount1();


                            ordDtl.setAmount1(0);
                            ordDtl.setAmount2(0);
                            ordDtl.setTotalPrice(0);

                            ordDtl.save();
                            holder.amount1.removeTextChangedListener(this);
                            holder.amount1.setText("");
                            holder.amount1.addTextChangedListener(this);
                            holder.totalPrice.setText(formatter.format(0));


                            //regionDeleteRow
                            deleteRowGift(oldAmount, product);
                            //endregionDeleteRow

                            changeItem.onChange();

                            return;
                        }

                        //endregionEmptyS

                        //regionChangesAmount1
                        if (toEnglishNumber(holder.amount1.getText().toString()).hashCode() == s.hashCode()) {

                            s = ((String) s).contains("٫") ? ((String) s).replace("٫", ".") : s;
                            if (!((String) s).isEmpty()) {
                                if (s.toString().contains(".") &&
                                        s.toString().indexOf(".") == s.toString().length() - 1) {
                                    return;
                                } else if (s.toString().contains("٫") &&
                                        s.toString().indexOf("٫") == s.toString().length() - 1) {
                                    return;
                                }
                            }


                            final double Price;
                            float amount;
                            try {
                                amount = Float.parseFloat(s.toString());
                            } catch (Exception e) {
                                return;
                            }


                            //regionCheckInventory
                            if (!sharedPreferences.getBoolean("no_check_amount_switch", false)) {
                                if (amount > holder.products.getInventory()) {
                                    Toasty.warning(context, "بیشتر از موجودی انبار است.", Toast.LENGTH_SHORT).show();
                                    holder.amount1.setText("");
                                    ordDtl.setAmount1(0);
                                    ordDtl.setAmount2(0);
                                    ordDtl.save();
                                    changeItem.onChange();
                                    return;
                                }
                            }
                            //endregionCheckInventory

                            //regionCheckAmountZero
                            if (amount == 0) {
                                return;
                            }
                            //endregionCheckAmountZero

                            //regionCheckMaxAmount

                            if (holder.products.getDo() && amount > holder.products.getMaxAmount()) {
                                String maxAmounttt = formatter.format(holder.products.getMaxAmount());
                                Toasty.warning(context, "حداکثر مقدار انتخاب برابر است با : " + maxAmounttt, Toast.LENGTH_SHORT).show();
                                ordDtl.setAmount1(0);
                                ordDtl.setAmount2(0);
                                ordDtl.save();
                                changeItem.onChange();
                                holder.amount1.setText("");


                                return;
                            }
                            //endregionCheckMaxAmount

                            //regionCheckSales
                            if (holder.products.getDo() && holder.products.getSc() > 0 && amount % holder.products.getSc() != 0) {
                                String SC = formatter.format(holder.products.getSc());
                                Toasty.warning(context, "مقدار یک وارد شده باید ضریبی از  " + SC + " باشد ", Toast.LENGTH_SHORT).show();
                                holder.amount1.setText("");
                                ordDtl.setAmount1(0);
                                ordDtl.setAmount2(0);
                                ordDtl.save();
                                changeItem.onChange();


                                return;
                            }
                            //endregionCheckSales

                            //regionCalculateAmount2WhitCheckLarger

                            if (holder.products.getIsUnit()) {
                                ordDtl.setAmount2(amount * holder.products.getCoefficient());
                            } else {
                                ordDtl.setAmount2(amount / holder.products.getCoefficient());
                            }
                            //endregionCalculateAmount2WhitCheckLarger


                            //regionOtherChangesRowWhitChaneAmount1

                            Price = calcute1(amount, ordDtl.getPrice1(), ordDtl.getDiscountPercent());

                            float oldPrice = ordDtl.getAmount1();
                            ordDtl.setAmount1(amount);


                            if (ordDtl.getGift()) {
                                ordDtl.setTotalPrice(0);
                                holder.totalPrice.setText(formatter.format(0));
                            } else {
                                ordDtl.setTotalPrice((float) Price);
                                holder.totalPrice.setText(formatter.format((float) Price));
                            }


                            ordDtl.save();
                            //regionAddGiftRow
                            addRowGift(amount, product, ordDtl);
                            //endregion


                            //regionDeleteGift
                            deleteRowGift(oldPrice, product);
                            //endregion


                            changeItem.onChange();
                            //endregionOtherChangesRowWhitChaneAmount1


                        }
                        //endregionChangesAmount1


                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };
            }

            //endregionTextWatcherAmount


            //region Set Data in Variable


            //region Action ivMax
            holder.ivMax.setOnClickListener(view -> {
                OrdDetail orderDtl = Select.from(OrdDetail.class).where("PRODUCT ='" + list.get(holder.getAdapterPosition()).getProducts() + "' AND GIFTTYPE =" + list.get(holder.getAdapterPosition()).getGifttype() + " AND O =" + OrderId).first();


                float amount = orderDtl.getAmount1();
                if (amount < holder.products.getInventory()) {

                    if (holder.products.getDo() && holder.products.getSc() != 0)
                        amount = amount + holder.products.getSc();
                    else
                        amount = amount + 1;


                    orderDtl.setAmount1(amount);
                    if (holder.products.getIsUnit()) {
                        orderDtl.setAmount2(amount * holder.products.getCoefficient());
                    } else {
                        orderDtl.setAmount2(amount / holder.products.getCoefficient());
                    }
                    holder.amount1.removeTextChangedListener(holder.textWatcherAmount);
                    holder.amount1.setText(df.format(amount));
                    holder.amount1.addTextChangedListener(holder.textWatcherAmount);
                    float TotalPrice=amount* orderDtl.getPrice1();
                    holder.totalPrice.setText(formatter.format(TotalPrice));
                    orderDtl.save();

                    changeItem.onChange();
                } else {
                    Toasty.warning(context, "حداکثر مقدار انتخاب برابر است با : " + holder.products.getInventory(), Toast.LENGTH_SHORT).show();
                }


            });
            //endregion Action ivMax


            //region Action ivMin
            holder.ivMin.setOnClickListener(view -> {
                OrdDetail ordrDtl = Select.from(OrdDetail.class).where("PRODUCT ='" + list.get(holder.getAdapterPosition()).getProducts() + "' AND GIFTTYPE =" + list.get(holder.getAdapterPosition()).getGifttype() + " AND O =" + OrderId).first();


                float amount = ordrDtl.getAmount1();

                if (holder.products.getDo() && holder.products.getSc() != 0)
                    amount = amount - holder.products.getSc();
                else
                    amount = amount - 1;



                if (amount >= 1) {
                    ordrDtl.setAmount1(amount);
                    if (holder.products.getIsUnit()) {
                        ordrDtl.setAmount2(amount * holder.products.getCoefficient());
                    } else {
                        ordrDtl.setAmount2(amount / holder.products.getCoefficient());
                    }


                    holder.amount1.removeTextChangedListener(holder.textWatcherAmount);
                    holder.amount1.setText(df.format(amount));
                    holder.amount1.addTextChangedListener(holder.textWatcherAmount);
                    float TotalPrice=amount* ordrDtl.getPrice1();
                    holder.totalPrice.setText(formatter.format(TotalPrice));

                    ordrDtl.save();
                    changeItem.onChange();
                }


            });
            //endregion Action ivMin
            holder.amount1.removeTextChangedListener(holder.textWatcherAmount);


            float amount2 = 0;
            if (holder.products != null && holder.products.getIsUnit()) {
                amount2 = orderTemp.getAmount1() * holder.products.getCoefficient();
            } else if (holder.products != null) {
                amount2 = orderTemp.getAmount1() / holder.products.getCoefficient();
            }

            orderTemp.setAmount2(amount2);
            float totalPrice = orderTemp.getAmount1() * orderTemp.getPrice1();
            orderTemp.setTotalPrice(totalPrice);


            if (orderTemp.getGift() && orderTemp.getGifttype() == 1) {
                holder.price.setText("");
                holder.totalPrice.setText("این کالا هدیه محصولات انتخابی شماست");
            } else {
                holder.price.setText(formatter.format(orderTemp.getPrice1()));
                holder.totalPrice.setText(formatter.format(totalPrice));
            }
            holder.name.setText(holder.getAdapterPosition() + 1 + "_" + orderTemp.getName());
            holder.unit1.setText(orderTemp.getUnit());
            holder.amount1.setText(df.format(orderTemp.getAmount1()));
            holder.amount1.addTextChangedListener(holder.textWatcherAmount);


            orderTemp.save();


            //endregion Set Data in Variable


            //region Action DeleteButton

            holder.DeleteBtn.setOnClickListener(v -> {
                try {


                    holder.DeleteBtn.setEnabled(false);
                    holder.DeleteBtn.setVisibility(View.GONE);
                    int index;
                    int amountGift1;

                    orderTemp.warning = false;

                    //region delete row gift manual
                    ArrayList<OrdDetail> ordDetailsList1 = new ArrayList<>(list);
                    CollectionUtils.filter(ordDetailsList1, o -> o.getProducts().equals(orderTemp.getProducts()) && o.getGifttype() == 2 && o.getGift());

                    if (ordDetailsList1.size() > 0) {
                        OrdDetail ordDtl = Select.from(OrdDetail.class).where("PRODUCT ='" + ordDetailsList1.get(0).getProducts() + "' AND GIFTTYPE =" + ordDetailsList1.get(0).getGifttype() + " AND O=" + OrderId).first();
                        index = (list.indexOf(ordDetailsList1.get(0)));
                        notifyItemRemoved(index);
                        list.remove(list.get(index));
                        ordDtl.delete();

                    }


                    //endregion delete row gift manual

                    ArrayList<OrdDetail> ordDetailsList2 = new ArrayList<>(list);
                    CollectionUtils.filter(ordDetailsList2, o -> o.getProducts().equals(orderTemp.getProducts()) && o.getGifttype() == 4 && !o.getGift());


                    if (ordDetailsList2.size() > 0) {
                        OrdDetail orderDtl = Select.from(OrdDetail.class).where("PRODUCT ='" + ordDetailsList2.get(0).getProducts() + "' AND GIFTTYPE =" + ordDetailsList2.get(0).getGifttype() + " AND O=" + OrderId).first();

                        String I = "";
                        if (holder.products.getDo() && holder.products.getAmountForGift() > 0 && holder.products.getGiftAmount() > 0) {
                            if (!holder.products.getGp().equals("0"))
                                I = holder.products.getGp();
                            else
                                I = holder.products.getG();
                        }


                        amountGift1 = (int) (ordDetailsList2.get(0).getAmount1() / holder.products.getAmountForGift()) * holder.products.getGiftAmount();


                        //region delete gift row automatic

                        ArrayList<OrdDetail> ordDetailsList3 = new ArrayList<>(list);
                        String finalI = I;
                        CollectionUtils.filter(ordDetailsList3, o -> o.getProducts().equals(finalI) && o.getGifttype() == 1 && o.getGift());


                        if (ordDetailsList3.size() > 0) {

                            OrdDetail ordDtl = Select.from(OrdDetail.class).where("PRODUCT ='" + ordDetailsList3.get(0).getProducts() + "' AND GIFTTYPE =" + ordDetailsList3.get(0).getGifttype() + " AND O=" + OrderId).first();
                            index = (list.indexOf(ordDetailsList3.get(0)));
                            if (amountGift1 == ordDtl.getAmount1()) {

                                notifyItemRemoved(index);
                                list.remove(list.get(index));
                                ordDtl.delete();

                            } else {
                                ordDtl.setAmount1(-amountGift1);
                                ordDtl.save();
                            }

                        }
                        //endregion delete gift row automatic

                        //region delete  row

                        index = (list.indexOf(ordDetailsList2.get(0)));
                        notifyItemRemoved(index);
                        list.remove(list.get(index));

                        orderDtl.delete();


                        holder.DeleteBtn.setEnabled(true);
                        //endregion delete  row
                    }

                    holder.DeleteBtn.setEnabled(true);
                    holder.DeleteBtn.setVisibility(View.VISIBLE);
                    changeItem.onChange();


                } catch (Exception ignored) {
                }
            });


            //endregion Action DeleteButton


            //region Action Warning
            holder.imgWarning.setOnClickListener(v -> Toasty.warning(context, "موجودی کالا برابر است با " + holder.products.getInventory() + "، مقدار وارد شده بیشتر از موجودی انبار است", Toast.LENGTH_LONG).show());
            //endregion Action Warning


        } catch (Exception e) {
            Toasty.warning(context, "به دلیل عدم وجود کالایی در این سفارش امکان ویرایش سفارش وجود ندارد", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        //regionVariable

        public ImageView DeleteBtn;
        public ImageView imgWarning;
        public ImageView ivMax;
        public ImageView ivMin;
        public TextView name;
        public TextView unit1;
        public EditText amount1;
        public TextView totalPrice;
        public TextWatcher textWatcherAmount;
        public LinearLayout cardLLView;

        public ModelCatalogPageItem products;
        public TextView price;
        public TextView discount;
        //endregionVariable


        public viewHolder(View itemView) {
            super(itemView);

            //region CastVariable
            name = itemView.findViewById(R.id.txt_name_item_recycle_order);
            price = itemView.findViewById(R.id.price_item_recycle_order);
            amount1 = itemView.findViewById(R.id.amount1_item_recycle_order);
            unit1 = itemView.findViewById(R.id.unit1_item_recycle_order);
            totalPrice = itemView.findViewById(R.id.totalPrice_item_recycle_order);
            DeleteBtn = itemView.findViewById(R.id.orderProductDeleteBtn);
            imgWarning = itemView.findViewById(R.id.orderProductWarning);
            ivMax = itemView.findViewById(R.id.iv_max_invoice);
            ivMin = itemView.findViewById(R.id.iv_minus_invoice);
            textWatcherAmount = null;
            cardLLView = itemView.findViewById(R.id.cardLan);
            discount = itemView.findViewById(R.id.order_list_item_recycle_txt_discount);
            //endregion CastVariable


        }
    }

    //region Method
    public Double calcute1(float i1, float price, float discountPercent) {

        discountPercent = discountPercent / 100;

        double p;
        double tp = i1 * price;

        double dtp = tp * (discountPercent);
        p = tp - dtp;


        return p;

    }

    public void addRowGift(float i, ModelCatalogPageItem products, OrdDetail OrdDetail2) {
        int a1;

        if (products.getDo() && products.getAmountForGift() > 0 && products.getGiftAmount() > 0) {
            a1 = (int) (i / products.getAmountForGift()) * products.getGiftAmount();


            if (i >= products.getAmountForGift()) {
                int amount1 = (int) ((OrdDetail2.getAmount1() / products.getAmountForGift()) * products.getGiftAmount());

                ArrayList<OrdDetail> result2 = new ArrayList<>(list);
                if (products.getGp().equals("0")) {
                    CollectionUtils.filter(result2, p -> p.getProducts().equals(products.getG()) && p.getGift() && p.getGifttype() == 1);
                } else {
                    CollectionUtils.filter(result2, p -> p.getProducts().equals(products.getGp()) && p.getGift() && p.getGifttype() == 1);
                }

                //update row gift
                if (result2.size() > 0) {

                    OrdDetail ordDtl = Select.from(OrdDetail.class).where("PRODUCT ='" + result2.get(0).getProducts() + "' AND GIFTTYPE =" + result2.get(0).getGifttype() + " AND O=" + OrderId).first();
                    ordDtl.setAmount1(ordDtl.getAmount1() + a1);
                    ordDtl.setIAM(ordDtl.getAmount1() + a1);
                    ordDtl.save();


                }
                //create row gift
                else {
                    OrdDetail OrdDetail1 = new OrdDetail();
                    OrdDetail1.setAmount1(amount1);
                    OrdDetail1.setIAM(amount1);


                    OrdDetail1.setGift(true);
                    OrdDetail1.setGifttype(1);
                    if (products.getGp().equals("0")) {
                        OrdDetail1.setProducts(products.getG());

                    } else {
                        OrdDetail1.setProducts(products.getGp());

                    }
                    OrdDetail1.setName(products.getGpn());
                    OrdDetail1.setUnit(products.getGpu1());

                    OrdDetail1.setIdOrders(OrderId);

                    OrdDetail1.setPrice1(0);

                    OrdDetail1.setDisc(String.valueOf(0));


                    ArrayList<OrdDetail> rst = new ArrayList<>(list);
                    CollectionUtils.filter(rst, p -> p.getProducts().equals(products.getG()));

                    if (rst.size() > 0) {
                        int indexOfProduct = list.indexOf(rst.get(0));
                        list.add(indexOfProduct + 1, OrdDetail1);
                        notifyItemInserted(indexOfProduct + 1);
                        OrdDetail1.save();
                    }

                }


            }

        }

    }

    public void deleteRowGift(float k, ModelCatalogPageItem products) {

        int amountGift;

        if (products != null && products.getDo() && products.getAmountForGift() > 0 && products.getGiftAmount() > 0) {
            amountGift = (int) (k / products.getAmountForGift()) * products.getGiftAmount();


            ArrayList<OrdDetail> arrayList = new ArrayList<>(list);
            if (products.getGp().equals("0")) {
                CollectionUtils.filter(arrayList, a -> a.getProducts().equals(products.getG()) && a.getGifttype() == 1 && a.getGift());
            } else {
                CollectionUtils.filter(arrayList, a -> a.getProducts().equals(products.getGp()) && a.getGifttype() == 1 && a.getGift());
            }


            if (arrayList.size() > 0) {

                OrdDetail ordDtl = Select.from(OrdDetail.class).where("PRODUCT ='" + arrayList.get(0).getProducts() + "' AND GIFTTYPE =" + arrayList.get(0).getGifttype() + " AND O=" + OrderId).first();
                //DELETE ROW
                if (amountGift == ordDtl.getAmount1()) {
                    ordDtl.delete();
                    notifyItemRemoved(list.indexOf(arrayList.get(0)));
                    list.remove(arrayList.get(0));


                }
                //UPDATE WHIT SUBMISSION AMOUNT
                else {
                    ordDtl.setAmount1(ordDtl.getAmount1() - amountGift);
                    ordDtl.save();
                    notifyItemChanged(list.indexOf(arrayList.get(0)));
                }


            }


        }

    }
    //endregion Method

}



