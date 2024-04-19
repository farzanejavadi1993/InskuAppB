package ir.kitgroup.inskuappb.data.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Keep
public class FilterModel {
    @SerializedName("I")
    @Expose
    private String i;
    @SerializedName("Name")
    @Expose
    private String name;

    public String getI() {
        return i;
    }

    public void setI(String id) {
        this.i = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
