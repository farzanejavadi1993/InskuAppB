package ir.kitgroup.inskuappb.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdvertisementStatus {
    @SerializedName("CustomerId")
    @Expose
    private String customerId;
    @SerializedName("AdvertisementId")
    @Expose
    private String advertisementId;
    @SerializedName("statuswana")
    @Expose
    private boolean statuswana;

    @SerializedName("Type")
    @Expose
    private Integer type;
    //        جدید = 0
    //        درحالبررسی = 1
    //        تکمیلشده = 2
    //        انصراف از درخواست = 4
    //        لغوشده = 23
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    public boolean getStatuswana() {
        return statuswana;
    }

    public void setStatuswana(boolean statuswana) {
        this.statuswana = statuswana;
    }



}
