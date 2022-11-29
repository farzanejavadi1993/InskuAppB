package ir.kitgroup.inskuappb.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Keep
public class ModelGift {
    @SerializedName("I")
    @Expose
    private String i;

    @SerializedName("SI")
    @Expose
    private String  si;
    @SerializedName("PI")
    @Expose
    private String  pi;
    @SerializedName("AM")
    @Expose
    private String  am;

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getSi() {
        return si;
    }

    public void setSi(String si) {
        this.si = si;
    }

    public String getPi() {
        return pi;
    }

    public void setPi(String pi) {
        this.pi = pi;
    }

    public String getAm() {
        return am;
    }

    public void setAm(String am) {
        this.am = am;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    @SerializedName("A")
    @Expose
    private String  a;




}
