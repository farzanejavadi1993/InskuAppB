package ir.kitgroup.inskuappb.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import ir.kitgroup.inskuappb.model.DShowIn;
import ir.kitgroup.inskuappb.model.DType;

@Keep
public class Advertise  extends SugarRecord {
    @SerializedName("Id")
    @Expose
    private String I;
    @SerializedName("CompanyId")
    @Expose
    private String companyId;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Link")
    @Expose
    private String link;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("ExpirationDate")
    @Expose
    private String expirationDate;
    @SerializedName("DShowIn")
    @Expose
    private DShowIn dShowIn;
    @SerializedName("ShowIn")
    @Expose
    private Integer showIn;
    @SerializedName("DType")
    @Expose
    private DType dType;

    @SerializedName("Type")
    @Expose
    private Integer type;
    @SerializedName("File")
    @Expose
    private Object file;
    @SerializedName("Count")
    @Expose
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    private boolean save;

    public String getI() {
        return I;
    }

    public void setId(String id) {
        this.I = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public DShowIn getDShowIn() {
        return dShowIn;
    }

    public void setDShowIn(DShowIn dShowIn) {
        this.dShowIn = dShowIn;
    }

    public Integer getShowIn() {
        return showIn;
    }

    public void setShowIn(Integer showIn) {
        this.showIn = showIn;
    }

    public DType getDType() {
        return dType;
    }


    public void setDType(DType dType) {
        this.dType = dType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getFile() {
        return file;
    }

    public void setFile(Object file) {
        this.file = file;
    }

}
