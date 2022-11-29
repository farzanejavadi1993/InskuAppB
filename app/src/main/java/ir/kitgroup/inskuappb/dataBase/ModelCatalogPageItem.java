package ir.kitgroup.inskuappb.dataBase;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
@Keep
public class ModelCatalogPageItem extends SugarRecord {
    @SerializedName("I")
    @Expose
    private String i;
    @SerializedName("P")
    @Expose
    private String p;
    @SerializedName("G")
    @Expose
    private String g;
    @SerializedName("PN")
    @Expose
    private String pn;
    @SerializedName("R")
    @Expose
    private String r;
    @SerializedName("C")
    @Expose
    private String c;
    @SerializedName("DO")
    @Expose
    private String _do;
    @SerializedName("DD")
    @Expose
    private String dd;
    @SerializedName("IDI")
    @Expose
    private String idi;
    @SerializedName("ADI")
    @Expose
    private String adi;
    @SerializedName("DA")
    @Expose
    private String da;
    @SerializedName("IA")
    @Expose
    private String ia;
    @SerializedName("AA")
    @Expose
    private String aa;
    @SerializedName("AF")
    @Expose
    private String af;
    @SerializedName("GA")
    @Expose
    private String ga;
    @SerializedName("GP")
    @Expose
    private String gp;

    public String getGpn() {
        return gpn;
    }

    public void setGpn(String gpn) {
        this.gpn = gpn;
    }

    @SerializedName("GPN")
    @Expose
    private String gpn;
    @SerializedName("SC")
    @Expose
    private String sc;

    public String getGpu1() {
        return gpu1;
    }

    public void setGpu1(String gpu1) {
        this.gpu1 = gpu1;
    }

    @SerializedName("GPU1")
    @Expose
    private String gpu1;
    @SerializedName("STP")
    @Expose
    private String stp;
    @SerializedName("PLDI")
    @Expose
    private String pldi;
    @SerializedName("PR")
    @Expose
    private String pr;
    @SerializedName("PRS")
    @Expose
    private String prs;
    @SerializedName("PU1")
    @Expose
    private String pu1;
    @SerializedName("PU2")
    @Expose
    private String pu2;


    @SerializedName("ISUNIT")
    @Expose
    private String isUnit;

    public Boolean getIsUnit() {
        return Boolean.parseBoolean(isUnit);
    }


    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getP() {
        return p;
    }


    public String getG() {
        return g;
    }


    public String getPn() {
        return pn;
    }


    public float getPrice() {

        float priceItem = 0;

        if (r != null && r.contains("/")) {

            priceItem = Float.parseFloat(r.replace("/", "."));

        } else if (r != null) {

            priceItem = Float.parseFloat(r);
        }

        return priceItem;
    }

    public float getPriceCustomer() {

        return Float.parseFloat(c != null ?
                c : "0");
    }


    public Boolean getDo() {
        boolean Do = false;
        if (_do!=null && _do.equals("1"))
            Do = true;
        return Do;
    }


    public float getMinDiscount() {

        return Float.parseFloat(idi != null ?
                idi : "0");
    }

    public float getMaxDiscount() {

        return Float.parseFloat(adi != null ?
                adi : "0");
    }


    public Float getDefaultDiscount() {
        return Float.parseFloat(dd != null ?
                dd : "0");
    }


    public int getGiftAmount() {
        return Integer.parseInt(ga != null ?
                ga : "0");
    }


    public int getAmountForGift() {
        return Integer.parseInt(af != null ?
                af : "0");
    }


    public float getMaxAmount() {
        return Float.parseFloat(aa != null ?
                aa : "0");
    }

    public float getMinAmount() {

        return Float.parseFloat(ia != null ?
                ia : "0");
    }


    public String getGp() {
        return gp;
    }


    public Float getSc() {

        return Float.parseFloat(sc != null ?
                sc : "0");
    }


    public String getStp() {
        return stp;
    }


    public String getPldi() {
        return pldi;
    }

    public String getPu1() {
        return pu1;
    }


    public String getPu2() {
        return pu2;
    }


    public int getInventory() {
        float inventory = 0;
        if (prs != null && !prs.equals("") && !prs.equals("-") && !prs.equals("null") && prs.contains("/")) {
            inventory = Float.parseFloat(prs.replace("/", "."));
        } else if (prs != null && !prs.equals("") && !prs.equals("-") && !prs.equals("null")) {
            inventory = Float.parseFloat(prs);
        }
        return (int)inventory;
    }


    public float getCoefficient() {


        float coefficient = 0;
        try {
            if (pr != null && pr.contains("/")) {
                coefficient = Float.parseFloat(pr.replace("/", "."));
            } else {
                coefficient = Float.parseFloat(pr);
            }
        } catch (Exception ignored) {

        }
        return coefficient;
    }

}