package ir.kitgroup.inskuappb.data.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Keep
public class CustomerCatalog {
    @SerializedName("A")
    @Expose
    private String a;
    @SerializedName("C")
    @Expose
    private String c;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

}
