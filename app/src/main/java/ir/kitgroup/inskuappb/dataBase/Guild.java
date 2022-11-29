package ir.kitgroup.inskuappb.dataBase;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
@Keep
public class Guild extends SugarRecord {
    @SerializedName("I")
    @Expose
    private String i;
    @SerializedName("Name")
    @Expose
    private String name;


    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    private boolean save;


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
