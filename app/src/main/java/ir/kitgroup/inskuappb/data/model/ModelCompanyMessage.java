package ir.kitgroup.inskuappb.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelCompanyMessage {
    @SerializedName("Companies")
    @Expose
    private List<CompanyMessage> companies = null;
    @SerializedName("PageNumber")
    @Expose
    private Integer pageNumber;
    @SerializedName("PageSize")
    @Expose
    private Integer pageSize;
    @SerializedName("Count")
    @Expose
    private Integer count;

    public List<CompanyMessage> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyMessage> companies) {
        this.companies = companies;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


}
