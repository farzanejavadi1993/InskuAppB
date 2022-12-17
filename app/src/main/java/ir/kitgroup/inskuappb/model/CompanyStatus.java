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
