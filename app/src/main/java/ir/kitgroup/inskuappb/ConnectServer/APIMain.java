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
import ir.kitgroup.inskuappb.dataBase.State;
import ir.kitgroup.inskuappb.model.Advertise;
import ir.kitgroup.inskuappb.model.AdvertisementStatus;
import ir.kitgroup.inskuappb.model.AppDetail;
import ir.kitgroup.inskuappb.model.CallMe;
import ir.kitgroup.inskuappb.model.CompanyStatus;
import ir.kitgroup.inskuappb.model.Log;
import ir.kitgroup.inskuappb.model.WantAdvertisement;
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

    @POST("SearchAdvertisement")
    Observable<List<Advertise>> searchAdvertisement(@Body() AccountFilter accountFilter, @Query("appId") String appId, @Query("customerId") String customerId, @Query("text") String text, @Query("page") int page);

    @POST("GetBillboardAdvs")
    Observable<List<Advertise>> getBillboardAdvs(@Body() AccountFilter accountFilter,@Query("appId") String appId, @Query("customerId") String customerId);

    @POST("GetVipAdvertisements")
    Observable<List<Advertise>> getVipAdvertisements(@Body() AccountFilter accountFilter,@Query("appId") String appId);

    @POST("GetSimpleAdvertisements")
    Observable<List<Advertise>> getSimpleAdvertisements(@Body() AccountFilter accountFilter, @Query("appId") String appId, @Query("customerId") String customerId, @Query("page") int page);

    @POST("GetAdvertisementById")
    Observable<List<Advertise>> getAdvertisementById(@Query("advertisementId") String advertisementId, @Query("customerId") String customerId);

    @POST("GetAdvsByCompanyId")
    Observable<List<Advertise>> getAdvsByCompanyId(@Body List<String> companiesId, @Query("page") int page, @Query("appId") String appId, @Query("customerId") String customerId);

    @POST("AddToSavedAdvs")
    Observable<List<Log>> addToSavedAdvs(@Query("customerId") String customerId, @Query("advertisementId") String advertisementId);

    @POST("RemoveFromSavedAdvs")
    Observable<List<Log>> removeFromSavedAdvs(@Query("customerId") String customerId, @Query("advertisementId") String advertisementId);

    @GET("GetCustomerSavedAdvs")
    Observable<List<Advertise>> getCustomerSavedAdvs(@Query("customerId") String customerId, @Query("appId") String appId);

    @POST("GetAllAccount")
    Observable<List<Company>> getAllCompany(@Body AccountFilter accountFilter,@Query("parentAccountId") String parentAccountId,@Query("text") String text, @Query("appId") String appId,@Query("customerId") String customerId,@Query("page") int page);

    @POST("GetAccountDetailById")
    Observable<List<Company>> getCompanyDetailById(@Query("id") String id);

    @GET("GetMyAccount")
    Observable<List<Company>> getMyCompany(@Query("customerId") String customerId,@Query("appId") String appId);

    @POST("AddToSavedAccounts")
    Observable<List<Log>> addToSavedAccounts(@Query("customerId") String customerId, @Query("accountId") String accountId, @Query("appId") String appId);

    @POST("RemoveFromSavedAccounts")
    Observable<List<Log>> removeFromSavedAccounts(@Query("customerId") String customerId, @Query("accountId") String accountId, @Query("appId")String appId);

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

    @GET("GetCustomerCallRequest")
    Observable<List<CallMe>> getCustomerCallRequest(@Query("CustomerId") String customerId);


    @GET("GetCustomerWantAdv")
    Observable<List<WantAdvertisement>> getCustomerWantAdv(@Query("CustomerId") String customerId);

    @POST("SetCallMeStatus")
    Observable<List<CompanyStatus>> setStatusCompany( @Query("CustomerId") String customerId, @Query("CompanyId") String CompanyId,@Query("StatusCallMe") boolean StatusCallMe);

    @POST("AddNewCity")
    Observable<List<Log>> addNewCity( @Query("CityName") String cityName, @Query("StateId") String stateId);

    @GET("DoodCustomerFilter")
    Observable<JsonElement> getFilterMessage(@Query("Target") String target, @Query("CustomerId") String CompanyId,@Query("AppId") String appId);
}