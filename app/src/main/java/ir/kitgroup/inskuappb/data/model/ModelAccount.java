package ir.kitgroup.inskuappb.data.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ir.kitgroup.inskuappb.dataBase.Account;
@Keep
public class ModelAccount {

    @SerializedName("Account")
    @Expose
    private final List<Account> AccountList=null;

    public List<Account> getAccountList() {
        return AccountList;
    }


}