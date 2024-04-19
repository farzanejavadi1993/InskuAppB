package ir.kitgroup.inskuappb.data.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Keep
public class ContactId {
    @SerializedName("CNTID")
    @Expose
    private String cntid;

    public String getCntid() {
        return cntid;
    }

    public void setCntid(String cntid) {
        this.cntid = cntid;
    }
}
