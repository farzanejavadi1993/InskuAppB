package ir.kitgroup.inskuappb.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ir.kitgroup.inskuappb.dataBase.Customer;
@Keep
public class ModelCustomer {
    @SerializedName("Customer")
    @Expose
    private List<Customer> customer = null;
    @SerializedName("CustomerCatalog")
    @Expose
    private List<CustomerCatalog> customerCatalog = null;

    public List<Customer> getCustomer() {
        return customer;
    }

    public void setCustomer(List<Customer> customer) {
        this.customer = customer;
    }

    public List<CustomerCatalog> getCustomerCatalog() {
        return customerCatalog;
    }

    public void setCustomerCatalog(List<CustomerCatalog> customerCatalog) {
        this.customerCatalog = customerCatalog;
    }
}
