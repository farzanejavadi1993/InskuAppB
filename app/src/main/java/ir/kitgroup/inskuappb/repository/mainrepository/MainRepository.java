package ir.kitgroup.inskuappb.repository.mainrepository;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import ir.kitgroup.inskuappb.component.AccountFilter;

import ir.kitgroup.inskuappb.data.api.APIMain;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.BusinessR;
import ir.kitgroup.inskuappb.dataBase.City;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Guild;
import ir.kitgroup.inskuappb.dataBase.State;
import ir.kitgroup.inskuappb.data.model.Advertise;
import ir.kitgroup.inskuappb.data.model.AdvertisementStatus;
import ir.kitgroup.inskuappb.data.model.AppDetail;
import ir.kitgroup.inskuappb.data.model.CallMe;
import ir.kitgroup.inskuappb.data.model.CompanyStatus;
import ir.kitgroup.inskuappb.data.model.Log;
import ir.kitgroup.inskuappb.data.model.WantAdvertisement;
import retrofit2.http.Body;

public class MainRepository  {

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

    public Observable<List<Company>> getAllCompany(AccountFilter accountFilter,String text,String appId,String customerId ,int page) {
        return api.getAllCompany(accountFilter,"", text,appId,customerId, page);
    }

    public Observable<List<Company>> getCompanyDetailById(String id) {
        return api.getCompanyDetailById(id);
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

    public Observable<List<Advertise>> getBillboardAdvs(AccountFilter accountFilter,String appId,String customerId) {
        return api.getBillboardAdvs(accountFilter,appId,customerId);
    }

    public Observable<List<Advertise>> searchAdvertisement(AccountFilter accountFilter, String appId, String customerId, String text, int page) {
        return api.searchAdvertisement(accountFilter,appId,customerId,text,page);
    }


    public Observable<List<Advertise>> getVipAdvertisements(AccountFilter accountFilter,String appId) {
        return api.getVipAdvertisements(accountFilter,appId);
    }

    public Observable<List<Advertise>> getSimpleAdvertisements(AccountFilter accountFilter,String appId,String customerId,int page) {
        return api.getSimpleAdvertisements(accountFilter,appId,customerId,page);
    }



    public Observable<List<Advertise>> getAdvsByCompanyId(List<String> companiesId, int page, String appId, String customerId) {
        return api.getAdvsByCompanyId(companiesId, page,appId,customerId);
    }

    public Observable<List<Advertise>> getAdvertisementById(String advertisementId, String customerId) {
        return api.getAdvertisementById(advertisementId,customerId);
    }

    public Observable<List<Company>> getMyAccount(String customerId,String appId) {
        return api.getMyCompany(customerId,appId);
    }

    public Observable<List<Advertise>> getCustomerSavedAdvs(String customerId, String appId) {
        return api.getCustomerSavedAdvs(customerId,appId);
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

    public Observable<List<CallMe>> getCustomerCallRequest(String customerId) {
        return api.getCustomerCallRequest(customerId);
    }

    public Observable<List<WantAdvertisement>> getCustomerWantAdv(String customerId) {
        return api.getCustomerWantAdv(customerId);
    }

    public Observable<List<CompanyStatus>> setStatusCompany(String customerId, String AdvertisementId, boolean StatusCallMe) {
        return api.setStatusCompany(customerId, AdvertisementId,StatusCallMe);
    }

    public Observable<List<Log>> addNewCity(String cityName, String stateId) {
        return api.addNewCity(cityName, stateId);
    }
    public Observable<List<Log>> addToSavedAccounts(String customerId, String accountId, String appId) {
        return api.addToSavedAccounts(customerId, accountId,appId);
    }


    public Observable<JsonElement> getFilterMessage(String target, String customerId, String appId) {
        return api.getFilterMessage(target,customerId, appId);
    }
    public Observable<List<Log>> addToSavedAdvs(String customerId, String advertisementId) {
        return api.addToSavedAdvs(customerId, advertisementId);
    }

    public Observable<List<Log>> removeFromSavedAccounts(String customerId, String accountId, String appId) {
        return api.removeFromSavedAccounts(customerId, accountId,appId);
    }


    public Observable<List<Log>> removeFromSavedAdvs(String customerId, String advertisementId) {
        return api.removeFromSavedAdvs(customerId, advertisementId);
    }

    public Observable<List<Log>> CreateVisitAdvertisement(String jsonObject) {
        return api.CreateVisitAdvertisement(jsonObject);
    }
}
