package ir.kitgroup.inskuappb.ConnectServer;


import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ir.kitgroup.inskuappb.classes.AccountFilter;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.BusinessR;
import ir.kitgroup.inskuappb.dataBase.City;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Guild;
import ir.kitgroup.inskuappb.dataBase.ModelCatalog;
import ir.kitgroup.inskuappb.dataBase.ModelCatalogPageItem;
import ir.kitgroup.inskuappb.dataBase.State;
import ir.kitgroup.inskuappb.model.Advertise;
import ir.kitgroup.inskuappb.model.AdvertisementStatus;
import ir.kitgroup.inskuappb.model.AppDetail;
import ir.kitgroup.inskuappb.model.CompanyStatus;
import ir.kitgroup.inskuappb.model.ContactId;
import ir.kitgroup.inskuappb.model.Log;
import ir.kitgroup.inskuappb.model.ModelCatalogPage;
import ir.kitgroup.inskuappb.model.ModelSetting;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface APIMain {

    @POST("GetApp")
    Observable<List<AppDetail>> getApp(@Query("id") String id);

    @GET("GetCustomerFromServer")
    Observable<List<Account>> getCustomerFromServer(@Query("mobile") String mobile);

    @POST("SendSmsFromServer")
    Observable<List<Log>> getSmsLogin(@Query("message") String message, @Query("mobile") String mobile, @Query("task") Integer task);

    @POST("AddCustomerToServer")
    Observable<List<Log>> addAccountToServer(@Query("companyId") String companyId
            , @Body() String jsonObject);

    @GET("GetBusinessRelation")
    Observable<List<BusinessR>> getBusinessRelations();

    @GET("GetGuild")
    Observable<List<Guild>> getGuild();

    @GET("GetState")
    Observable<List<State>> getState();

    @POST("GetCity")
    Observable<List<City>> getCity(@Body() ArrayList<String> list);

    @POST("GetAdvertisements")
    Observable<List<Advertise>> getAllAdvertisement(@Body() AccountFilter accountFilter,@Query("text") String text,@Query("page") int page);

    @POST("GetAdvertisement")
    Observable<List<Advertise>> getAdvertisement( @Query("advertisementId") String advertisementId);

    @POST("GetAdvertisement")
    Observable<List<Advertise>> getCompanyAdvertisement(@Body List<String> companiesId, @Query("page") int page);

    @POST("AddToMyAdvertisement")
    Observable<List<Log>> addToMyAdvertisement(@Query("customerId") String customerId, @Query("advertisementId") String advertisementId);

    @POST("DeleteMyAdvertisement")
    Observable<List<Log>> DeleteMyAdvertisement(@Query("customerId") String customerId, @Query("advertisementId") String advertisementId);

    @GET("GetMyAdvertisement")
    Observable<List<Advertise>> getMyAdvertisement(@Query("customerId") String customerId);

    @POST("GetAccount")
    Observable<List<Company>> getAllCompany(@Query("parentAccountId") String parentAccountId,@Query("text") String text, @Body AccountFilter accountFilter, @Query("appType") int appType, @Query("page") int page);

    @POST("GetAccount")
    Observable<List<Company>> getCompany(@Query("id") String id);

    @GET("GetMyAccount")
    Observable<List<Company>> getMyCompany(@Query("customerId") String customerId);

    @POST("AddToMyAccount")
    Observable<List<Log>> addMyCompany(@Query("customerId") String customerId, @Query("accountId") String accountId);

    @POST("DeleteMyAccount")
    Observable<List<Log>> DeleteMyAccount(@Query("customerId") String customerId, @Query("accountId") String accountId);

    @POST("CreateVisitAdvertisement")
    Observable<List<Log>> CreateVisitAdvertisement(@Body() String jsonVisitAdvertisement);

    @POST("SetFirebaseToken")
    Observable<List<Log>> setFirebaseToken(@Query("appId") String appId, @Query("customerId") String customerId, @Query("imei") String imei, @Query("token") String token);


    @GET("GetWannaAdvertisementStatus")
    Observable<List<AdvertisementStatus>> getWannaAdvertisementStatus( @Query("CustomerId") String customerId, @Query("AdvertisementId") String AdvertisementId);

    @POST("SetWannaAdvertisementStatus")
    Observable<List<AdvertisementStatus>> setAdvertisementStatus(@Query("CustomerId") String customerId, @Query("AdvertisementId") String AdvertisementId, @Query("StatusWanna") boolean StatusWanna);


    @GET("GetCallMeStatus")
    Observable<List<CompanyStatus>> getCallMeStatus(@Query("CustomerId") String customerId, @Query("CompanyId") String CompanyId);

    @POST("SetCallMeStatus")
    Observable<List<CompanyStatus>> setStatusCompany( @Query("CustomerId") String customerId, @Query("CompanyId") String CompanyId,@Query("StatusCallMe") boolean StatusCallMe);

    @POST("AddNewCity")
    Observable<List<Log>> addNewCity( @Query("CityName") String cityName, @Query("StateId") String stateId);

    @GET("DoodCustomerFilter")
    Observable<JsonElement> getFilterMessage(@Query("Target") String target, @Query("CustomerId") String CompanyId,@Query("AppId") String appId);
}