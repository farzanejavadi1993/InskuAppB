package ir.kitgroup.inskuappb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Messages {

    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("CompanyId")
    @Expose
    private String companyId;
    @SerializedName("Desc")
    @Expose
    private String desc;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("IsRead")
    @Expose
    private Boolean isRead;

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Boolean getHasPicture() {
        return HasPicture;
    }

    public void setHasPicture(Boolean hasPicture) {
        HasPicture = hasPicture;
    }

    public String getAnswerId() {
        return AnswerId;
    }

    public void setAnswerId(String answerId) {
        AnswerId = answerId;
    }

    @SerializedName("HasPicture")
    @Expose
    private Boolean HasPicture;


    @SerializedName("AnswerId")
    @Expose
    private String AnswerId;


    @SerializedName("Survays")
    @Expose
    private List<Survey> survays = null;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public List<Survey> getSurvays() {
        return survays;
    }

    public void setSurvays(List<Survey> survays) {
        this.survays = survays;
    }

}
