package ir.kitgroup.inskuappb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallMeStatusMobile {

    @SerializedName("Type")
    @Expose
    private Integer type;
    //        جدید = 0
    //        درحالبررسی = 1
    //        تکمیلشده = 2
    //        لغوشده = 3
    //        انصراف از درخواست = 4

    @SerializedName("Name")
    @Expose
    private String name;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
