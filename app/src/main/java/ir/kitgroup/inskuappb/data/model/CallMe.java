package ir.kitgroup.inskuappb.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallMe {
    @SerializedName("CallRequestId")
    @Expose
    private String callRequestId;
    @SerializedName("CustomerId")
    @Expose
    private String customerId;
    @SerializedName("CompanyId")
    @Expose
    private String companyId;
    @SerializedName("CustomerName")
    @Expose
    private Object customerName;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("CustomerMobile")
    @Expose
    private Object customerMobile;
    @SerializedName("CallRequestStatus")
    @Expose
    private Object callRequestStatus;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("CallMeStatusMobile")
    @Expose
    private CallMeStatusMobile callMeStatusMobile;
    @SerializedName("CallStatus")
    @Expose
    private Boolean callStatus;

    public String getCallRequestId() {
        return callRequestId;
    }

    public void setCallRequestId(String callRequestId) {
        this.callRequestId = callRequestId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Object getCustomerName() {
        return customerName;
    }

    public void setCustomerName(Object customerName) {
        this.customerName = customerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Object getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(Object customerMobile) {
        this.customerMobile = customerMobile;
    }

    public Object getCallRequestStatus() {
        return callRequestStatus;
    }

    public void setCallRequestStatus(Object callRequestStatus) {
        this.callRequestStatus = callRequestStatus;
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

    public Boolean getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(Boolean callStatus) {
        this.callStatus = callStatus;
    }
}
