package ir.kitgroup.inskuappb.dataBase;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.util.Comparator;
@Keep
public class OrdDetail extends SugarRecord {
    @SerializedName("I")
    @Expose
    private String i;
    @SerializedName("OI")
    @Expose
    private String oi;
//    @SerializedName("O")
//    @Expose
//    private Integer o;
    @SerializedName("PRODUCT")
    @Expose
    private String product;
    @SerializedName("PRICE1")
    @Expose
    private String price1;
    @SerializedName("PRICE2")
    @Expose
    private String price2;
    @SerializedName("QUAN1")
    @Expose
    private String quan1;
    @SerializedName("QUAN2")
    @Expose
    private String quan2;
    @SerializedName("IAM")
    @Expose
    private String iam;
    @SerializedName("SUMPRICE")
    @Expose
    private String sumprice;
    @SerializedName("DISC")
    @Expose
    private String disc;
    @SerializedName("DISCP")
    @Expose
    private String discp;
    @SerializedName("GIFT")
    @Expose
    private Boolean gift;
    @SerializedName("GIFTTYPE")//4 ردیف معمولی     1 ردیف جایزه اتوماتیک
    @Expose
    private Integer gifttype;
    @SerializedName("DESC")
    @Expose
    private String desc;
    @SerializedName("PLDI")
    @Expose
    private String pldi;

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getOi() {
        return oi;
    }

    public void setOi(String oi) {
        this.oi = oi;
    }


    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getQuan1() {
        return quan1;
    }

    public void setQuan1(String quan1) {
        this.quan1 = quan1;
    }

    public String getQuan2() {
        return quan2;
    }

    public void setQuan2(String quan2) {
        this.quan2 = quan2;
    }

    public String getSumprice() {
        return sumprice;
    }

    public void setSumprice(String sumprice) {
        this.sumprice = sumprice;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getDiscp() {
        return discp;
    }

    public void setDiscp(String discp) {
        this.discp = discp;
    }

    public Boolean getGift() {
        return gift;
    }

    public void setGift(Boolean gift) {
        this.gift = gift;
    }

    public Integer getGifttype() {
        return gifttype;
    }

    public void setGifttype(Integer gifttype) {
        this.gifttype = gifttype;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getPRICE2() {
        return price2;
    }

    public void setPRICE2(String PRICE2) {
        this.price2 = PRICE2;
    }

    public String Name;
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }






    public String getPldi() {
        return pldi;
    }
    public void setPLDI(String PLDI) {
        this.pldi = PLDI;
    }

    public boolean edit;
    public boolean editDiscount;
    public boolean warning;


    public OrdDetail() {
    }



    public void setTotalPrice(float totalPrice) {
        this.sumprice = String.valueOf(totalPrice);
    }




    public float getDiscountPercentP() {

        float discP = 0;
        try {
            if (discp!= null && discp.contains("/")){
                discP=Float.parseFloat(  discp.replace("/","."));
            }

            else if (discp!= null){
                discP=Float.parseFloat(discp);
            }
        }catch (Exception e){
            int y=0;
        }
        return discP;
    }
    public void setAmount2(float amount2) {
        this.quan2 = String.valueOf(amount2);
    }




    public void setDiscountPercent(float discountPercent) {
        this.disc = String.valueOf(discountPercent);
    }
    public float getDiscountPercent()
    {

        float dis;
        try {
            dis=Float.parseFloat(disc != null && !disc.equals("") ? disc : "0");
        }catch (Exception e){
            dis=0;
        }
        return  dis;
        // return Float.parseFloat(DISC);
    }




    public long O;
    public void setIdOrders(long idOrders) {
        this.O = idOrders;
    }




    public String Unit;
    public String getUnit() {
        return Unit;
    }
    public void setUnit(String unit) {
        Unit = unit;
    }




    public float getAmount1() {

        float amount1 = 0;
        try {
            if (quan1!= null && quan1.contains("/")){
                amount1=Float.parseFloat(  quan1.replace("/","."));
            }

            else if (quan1!= null){
                amount1=Float.parseFloat(quan1);
            }
        }catch (Exception e){
            int y=0;
        }
        return amount1;

    }
    public void setAmount1(float amount1) {
        this.quan1 = String.valueOf(amount1);
    }



    public String getProducts() {
        return product;
    }
    public void setProducts(String products) {
        this.product = products;
    }




    public float getIAM() {

        float iAm = 0;
        try {
            if (iam!= null && iam.contains("/")){
                iAm=Float.parseFloat(  iam.replace("/","."));
            }

            else {
                iAm=Float.parseFloat(iam);
            }
        }catch (Exception e){
            int y=0;
        }
        return iAm;

    }
    public void setIAM(float iAm) {
        this.iam = String.valueOf(iAm);
    }



    public  float getPrice1() {
       float price=0;
       try {
           price= Float.parseFloat(price1);

       }catch (Exception e){
           price=0;
       }
        return price;
    }
    public void setPrice1(float price) {
        this.price1 = String.valueOf(price);
    }
}