package ir.kitgroup.inskuappb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WantAdvertisement {
    @SerializedName("RequestId")
    @Expose
    private String requestId;
    @SerializedName("CompanyId")
    @Expose
    private String companyId;
    @SerializedName("CustomerId")
    @Expose
    private String customerId;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("AdvertisementId")
    @Expose
    private String advertisementId;
    @SerializedName("CustomerMobile")
    @Expose
    private Object customerMobile;
    @SerializedName("CustomerName")
    @Expose
    private Object customerName;
    @SerializedName("AdvertisementTitle")
    @Expose
    private String advertisementTitle;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("CallMeStatusMobile")
    @Expose
    private CallMeStatusMobile callMeStatusMobile;
    @SerializedName("RequestCehckStatus")
    @Expose
    private Object requestCehckStatus;
    @SerializedName("statuswana")
    @Expose
    private Boolean statuswana;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    public Object getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(Object customerMobile) {
        this.customerMobile = customerMobile;
    }

    public Object getCustomerName() {
        return customerName;
    }

    public void setCustomerName(Object customerName) {
        this.customerName = customerName;
    }

    public String getAdvertisementTitle() {
        return advertisementTitle;
    }

    public void setAdvertisementTitle(String advertisementTitle) {
        this.advertisementTitle = advertisementTitle;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public CallMeStatusMobile getCallMeStatusMobile() {
        return callMeStatusMobile;
    }

    public void setCallMeStatusMobile(CallMeStatusMobile callMeStatusMobile) {
        this.callMeStatusMobile = callMeStatusMobile;
    }

    public Object getRequestCehckStatus() {
        return requestCehckStatus;
    }

    public void setRequestCehckStatus(Object requestCehckStatus) {
        this.requestCehckStatus = requestCehckStatus;
    }

    public Boolean getStatuswana() {
        return statuswana;
    }

    public void setStatuswana(Boolean statuswana) {
        this.statuswana = statuswana;
    }
}
