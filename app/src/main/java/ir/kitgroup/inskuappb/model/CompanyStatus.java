package ir.kitgroup.inskuappb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyStatus {
    @SerializedName("CustomerId")
    @Expose
    private String customerId;
    @SerializedName("CompanyIdId")
    @Expose
    private String companyId;

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

    public boolean isCallStatus() {
        return callStatus;
    }

    public void setCallStatus(boolean callStatus) {
        this.callStatus = callStatus;
    }

    @SerializedName("CallStatus")
    @Expose
    private boolean callStatus;


}
