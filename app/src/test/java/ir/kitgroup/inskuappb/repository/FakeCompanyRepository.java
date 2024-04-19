package ir.kitgroup.inskuappb.repository;




import com.google.gson.JsonElement;
import java.util.List;

import io.reactivex.Observable;
import ir.kitgroup.inskuappb.data.model.ContactId;
import ir.kitgroup.inskuappb.data.model.Log;
import ir.kitgroup.inskuappb.data.model.ModelCatalogPage;
import ir.kitgroup.inskuappb.data.model.ModelGift;
import ir.kitgroup.inskuappb.dataBase.ModelCatalog;
import ir.kitgroup.inskuappb.dataBase.ModelCatalogPageItem;
import ir.kitgroup.inskuappb.data.model.ModelSetting;
import ir.kitgroup.inskuappb.dataBase.Ord;
import ir.kitgroup.inskuappb.dataBase.OrdDetail;
import ir.kitgroup.inskuappb.repository.companyrepository.CRepository;

public class FakeCompanyRepository implements CRepository {

    private List<ModelCatalog> fakeCatalogResult;
    private List<ModelCatalogPage> fakeCatalogPageResult;
    private List<ModelCatalogPageItem> fakeCatalogPageItemListResult;
    private JsonElement fakeCustomerListResult;
    private List<ContactId> fakeContactIdResult;
    private JsonElement fakeOrderResult;
    private List<Log> fakeDeleteOrderResult;
    private List<ModelSetting> fakeSettingResult;
    private List<Log> fakeSendOrderResult;

    public void setFakeCatalogResult(List<ModelCatalog> fakeCatalogResult) {
        this.fakeCatalogResult = fakeCatalogResult;
    }

    public void setFakeCatalogPageResult(List<ModelCatalogPage> fakeCatalogPageResult) {
        this.fakeCatalogPageResult = fakeCatalogPageResult;
    }

    public void setFakeCatalogPageItemListResult(List<ModelCatalogPageItem> fakeCatalogPageItemListResult) {
        this.fakeCatalogPageItemListResult = fakeCatalogPageItemListResult;
    }

    public void setFakeCustomerListResult(JsonElement fakeCustomerListResult) {
        this.fakeCustomerListResult = fakeCustomerListResult;
    }

    public void setFakeContactIdResult(List<ContactId> fakeContactIdResult) {
        this.fakeContactIdResult = fakeContactIdResult;
    }

    public void setFakeOrderResult(JsonElement fakeOrderResult) {
        this.fakeOrderResult = fakeOrderResult;
    }

    public void setFakeDeleteOrderResult(List<Log> fakeDeleteOrderResult) {
        this.fakeDeleteOrderResult = fakeDeleteOrderResult;
    }

    public void setFakeSettingResult(List<ModelSetting> fakeSettingResult) {
        this.fakeSettingResult = fakeSettingResult;
    }

    public void setFakeSendOrderResult(List<Log> fakeSendOrderResult) {
        this.fakeSendOrderResult = fakeSendOrderResult;
    }

    @Override
    public Observable<List<ModelCatalog>> getCatalog(String userName, String passWord) {
        return Observable.just(fakeCatalogResult);
    }

    @Override
    public Observable<List<ModelCatalogPage>> getCatalogPage(String userName, String passWord, String catalogId) {
        return Observable.just(fakeCatalogPageResult);
    }

    @Override
    public Observable<List<ModelCatalogPageItem>> getCatalogPageItemList(String userName, String passWord, String catalogId, String pageId) {
        return Observable.just(fakeCatalogPageItemListResult);
    }

    @Override
    public Observable<List<ModelCatalogPageItem>> getCatalogPageItem(String userName, String passWord, String itemId) {
        return null;
    }

    @Override
    public Observable<List<ModelSetting>> getSetting(String userName, String passWord) {
        return Observable.just(fakeSettingResult);
    }

    @Override
    public Observable<JsonElement> getCustomerList(String userName, String passWord, String mobile) {
        return Observable.just(fakeCustomerListResult);
    }

    @Override
    public Observable<List<Log>> sendOrder(String userName, String passWord, List<Ord> ords, List<OrdDetail> ordDetails, List<ModelGift> modelGifts) {
        return Observable.just(fakeSendOrderResult);
    }

    @Override
    public Observable<List<ContactId>> getContactId(String userName, String passWord, String mobile) {
        return Observable.just(fakeContactIdResult);
    }

    @Override
    public Observable<JsonElement> getOrder(String userName, String passWord, String contactId) {
        return Observable.just(fakeOrderResult);
    }

    @Override
    public Observable<List<Log>> deleteOrder(String userName, String passWord, String id) {
        return Observable.just(fakeDeleteOrderResult);
    }
}
