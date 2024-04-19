package ir.kitgroup.inskuappb.repository.companyrepository;

import com.google.gson.JsonElement;

import java.util.List;

import io.reactivex.Observable;
import ir.kitgroup.inskuappb.data.api.APICompany;
import ir.kitgroup.inskuappb.data.model.ContactId;
import ir.kitgroup.inskuappb.data.model.Log;
import ir.kitgroup.inskuappb.data.model.ModelCatalogPage;
import ir.kitgroup.inskuappb.data.model.ModelGift;
import ir.kitgroup.inskuappb.data.model.ModelSetting;
import ir.kitgroup.inskuappb.dataBase.ModelCatalog;
import ir.kitgroup.inskuappb.dataBase.ModelCatalogPageItem;
import ir.kitgroup.inskuappb.dataBase.Ord;
import ir.kitgroup.inskuappb.dataBase.OrdDetail;

public interface CRepository {


    Observable<List<ModelCatalog>> getCatalog(String userName, String passWord);


    Observable<List<ModelCatalogPage>> getCatalogPage(String userName, String passWord, String catalogId);


    Observable<List<ModelCatalogPageItem>> getCatalogPageItemList(String userName, String passWord, String catalogId, String pageId);

    Observable<List<ModelCatalogPageItem>> getCatalogPageItem(String userName, String passWord, String itemId);

    Observable<List<ModelSetting>> getSetting(String userName, String passWord);

    Observable<JsonElement> getCustomerList(String userName, String passWord, String mobile);

    Observable<List<Log>> sendOrder(String userName,
                                    String passWord,
                                    List<Ord> ords,
                                    List<OrdDetail> ordDetails,
                                    List<ModelGift> modelGifts)
            ;

    Observable<List<ContactId>> getContactId(String userName, String passWord, String mobile);

    Observable<JsonElement> getOrder(String userName, String passWord, String contactId);


    Observable<List<Log>> deleteOrder(String userName, String passWord, String id);
}


