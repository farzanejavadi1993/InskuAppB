package ir.kitgroup.inskuappb.model;

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
