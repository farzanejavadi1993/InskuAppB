package ir.kitgroup.inskuappb.dataBase;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
@Keep
public class Files extends SugarRecord {

    @SerializedName("I")
    @Expose
    private String i;
    @SerializedName("Mime")
    @Expose
    private String mime;




    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

}
