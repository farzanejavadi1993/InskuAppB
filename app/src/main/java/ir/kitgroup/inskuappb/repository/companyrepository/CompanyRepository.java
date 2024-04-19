package ir.kitgroup.inskuappb.repository.companyrepository;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ir.kitgroup.inskuappb.data.api.APICompany;
import ir.kitgroup.inskuappb.dataBase.Ord;
import ir.kitgroup.inskuappb.dataBase.OrdDetail;
import ir.kitgroup.inskuappb.dataBase.ModelCatalog;
import ir.kitgroup.inskuappb.data.model.ContactId;
import ir.kitgroup.inskuappb.data.model.Log;
import ir.kitgroup.inskuappb.data.model.ModelCatalogPage;
import ir.kitgroup.inskuappb.dataBase.ModelCatalogPageItem;
import ir.kitgroup.inskuappb.data.model.ModelGift;
import ir.kitgroup.inskuappb.data.model.ModelSetting;
import ir.kitgroup.inskuappb.data.model.ClsJsonObject;

public class CompanyRepository  implements CRepository{

    private final APICompany api;

    @Inject
    public CompanyRepository(APICompany api) {
        this.api = api;
    }

    public Observable<List<ModelCatalog>> getCatalog(String userName, String passWord) {
        return api.getCatalog(userName, passWord);
    }


    public Observable<List<ModelCatalogPage>> getCatalogPage(String userName, String passWord, String catalogId) {
        return api.getCatalogPage(userName, passWord, catalogId);
    }


    public Observable<List<ModelCatalogPageItem>> getCatalogPageItemList(String userName, String passWord, String catalogId, String pageId) {
        return api.getCatalogPageItemList(userName, passWord, catalogId, pageId);
    }

    public Observable<List<ModelCatalogPageItem>> getCatalogPageItem(String userName, String passWord, String itemId) {
        return api.getCatalogPageItem(userName, passWord, itemId);
    }

    public Observable<List<ModelSetting>> getSetting(String userName, String passWord) {
        return api.getSetting(userName, passWord);
    }

    public Observable<JsonElement> getCustomerList(String userName, String passWord, String mobile) {
        return api.getCustomerList(userName, passWord, mobile);
    }

    public Observable<List<Log>> sendOrder(String userName,
                                           String passWord,
                                           List<Ord> ords,
                                           List<OrdDetail> ordDetails,
                                           List<ModelGift> modelGifts) {
        Gson gson = new Gson();


        ClsJsonObject clsJsonObject = new ClsJsonObject();
        clsJsonObject.Order = ords;
        clsJsonObject.OrderDetail = ordDetails;
        clsJsonObject.Scenario = modelGifts;
        Type typeJsonObject = new TypeToken<ClsJsonObject>() {
        }.getType();

        return api.sendOrder(userName, passWord, gson.toJson(clsJsonObject, typeJsonObject));
    }

    public Observable<List<ContactId>> getContactId(String userName, String passWord, String mobile) {
        return api.getContactId(userName, passWord, mobile);
    }
    public Observable<JsonElement> getOrder(String userName, String passWord, String contactId) {
        return api.getOrder(userName, passWord, contactId);
    }


    public Observable<List<Log>> deleteOrder(String userName, String passWord, String id) {
        return api.deleteOrder(userName, passWord, id);
    }
}
