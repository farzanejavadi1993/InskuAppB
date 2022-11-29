package ir.kitgroup.inskuappb.ConnectServer;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ir.kitgroup.inskuappb.classes.AccountFilter;

import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.BusinessR;
import ir.kitgroup.inskuappb.dataBase.City;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Guild;
import ir.kitgroup.inskuappb.dataBase.State;
import ir.kitgroup.inskuappb.model.Advertise;
import ir.kitgroup.inskuappb.model.AdvertisementStatus;
import ir.kitgroup.inskuappb.model.AppDetail;
import ir.kitgroup.inskuappb.model.CompanyStatus;
import ir.kitgroup.inskuappb.model.Log;
import retrofit2.http.Body;

public class MainRepository {

    private final APIMain api;

    @Inject
    public MainRepository(APIMain api) {
        this.api = api;
    }

    public Observable<List<Log>> getSmsLogin(String message, String mobile) {
        return api.getSmsLogin(message, mobile, 2);
    }

    public Observable<List<Log>> addAccountToServer(String accounts) {
        return api.addAccountToServer("", accounts);
    }

    public Observable<List<AppDetail>> getApp(String id) {
        return api.getApp(id);
    }

    public Observable<List<Account>> getCustomerFromServer(String mobile) {
        return api.getCustomerFromServer(mobile);
    }

    public Observable<List<Company>> getAllCompany(String text, int page, AccountFilter accountFilter) {
        return api.getAllCompany("", text, accountFilter, 2, page);
    }

    public Observable<List<Company>> getCompany(String id) {
        return api.getCompany(id);
    }

    public Observable<List<BusinessR>> getBusinessRelations(@Body AccountFilter accountFilter) {
        return api.getBusinessRelations();
    }

    public Observable<List<Guild>> getGuild() {
        return api.getGuild();
    }

    public Observable<List<State>> getState() {
        return api.getState();
    }

    public Observable<List<City>> getCity(ArrayList<String> list) {
        return api.getCity(list);
    }

    public Observable<List<Advertise>> getAllAdvertisement(AccountFilter accountFilter, String text, int page) {
        return api.getAllAdvertisement(accountFilter, text, page);
    }

    public Observable<List<Advertise>> getCompanyAdvertisement(List<String> companiesId, int page) {
        return api.getCompanyAdvertisement(companiesId, page);
    }

    public Observable<List<Advertise>> getAdvertisement(String advertisementId) {
        return api.getAdvertisement(advertisementId);
    }

    public Observable<List<Company>> getMyAccount(String customerId) {
        return api.getMyCompany(customerId);
    }

    public Observable<List<Advertise>> getMyAdvertisement(String customerId) {
        return api.getMyAdvertisement(customerId);
    }

    public Observable<List<Log>> setFirebaseToken(String appId, String customerId, String imei, String token) {
        return api.setFirebaseToken(appId, customerId, imei, token);
    }

    public Observable<List<AdvertisementStatus>> getWannaAdvertisementStatus(String customerId, String AdvertisementId) {
        return api.getWannaAdvertisementStatus(customerId, AdvertisementId);
    }
    public Observable<List<AdvertisementStatus>> setAdvertisementStatus(String customerId, String AdvertisementId, boolean StatusWanna) {
        return api.setAdvertisementStatus(customerId, AdvertisementId,StatusWanna);
    }



    public Observable<List<CompanyStatus>> getCallMeStatus(String customerId, String companyId) {
        return api.getCallMeStatus(customerId, companyId);
    }
    public Observable<List<CompanyStatus>> setStatusCompany(String customerId, String AdvertisementId, boolean StatusCallMe) {
        return api.setStatusCompany(customerId, AdvertisementId,StatusCallMe);
    }

    public Observable<List<Log>> addNewCity(String cityName, String stateId) {
        return api.addNewCity(cityName, stateId);
    }
    public Observable<List<Log>> addMyCompany(String customerId, String accountId) {
        return api.addMyCompany(customerId, accountId);
    }


    public Observable<JsonElement> getFilterMessage(String target, String customerId, String appId) {
        return api.getFilterMessage(target,customerId, appId);
    }
    public Observable<List<Log>> addToMyAdvertisement(String customerId, String advertisementId) {
        return api.addToMyAdvertisement(customerId, advertisementId);
    }

    public Observable<List<Log>> DeleteMyAccount(String customerId, String accountId) {
        return api.DeleteMyAccount(customerId, accountId);
    }


    public Observable<List<Log>> DeleteMyAdvertisement(String customerId, String advertisementId) {
        return api.DeleteMyAdvertisement(customerId, advertisementId);
    }

    public Observable<List<Log>> CreateVisitAdvertisement(String jsonObject) {
        return api.CreateVisitAdvertisement(jsonObject);
    }
}