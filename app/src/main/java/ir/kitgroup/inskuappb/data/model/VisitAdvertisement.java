package ir.kitgroup.inskuappb.data.model;

import androidx.annotation.Keep;

@Keep
public class VisitAdvertisement {
    private String AdvertisementId;
    private String CustomerId ;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    private String Id ;

    public String getAdvertisementId() {
        return AdvertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        AdvertisementId = advertisementId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }
}
