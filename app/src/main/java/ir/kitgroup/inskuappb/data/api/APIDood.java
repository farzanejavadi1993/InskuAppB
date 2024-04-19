package ir.kitgroup.inskuappb.data.api;


import com.google.gson.JsonElement;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface APIDood {

    @POST("GetAllCompanyWithMessages")
    Observable<JsonElement> getAllCompanyWithMessages(@Query("userId") String userId, @Query("appId") String appId, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    @POST("GetCompanyMessages")
    Observable<JsonElement> GetCompanyMessages(@Query("userId") String userId, @Query("appId") String appId, @Query("companyId") String companyId, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    @POST("ReadCompanyMessages")
    Observable<String> ReadCompanyMessages(@Query("userId") String userId, @Query("appId") String appId, @Query("companyId") String companyId, @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);

    @POST("SetMessageRespond")
    Observable<String> setMessageRespond(@Query("userId") String userId, @Query("appId") String appId, @Query("messageId") String messageId, @Query("respondId") String respondId);


    @POST("GetNewMessagesCount")
    Observable<Integer> getNewMessagesCount(@Query("userId") String userId, @Query("appId") String appId);

}