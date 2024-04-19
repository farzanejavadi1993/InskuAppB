package ir.kitgroup.inskuappb.data.api;




import com.google.gson.JsonElement;

import java.util.List;

import io.reactivex.Observable;
import ir.kitgroup.inskuappb.dataBase.ModelCatalog;
import ir.kitgroup.inskuappb.dataBase.ModelCatalogPageItem;
import ir.kitgroup.inskuappb.data.model.ContactId;
import ir.kitgroup.inskuappb.data.model.Log;
import ir.kitgroup.inskuappb.data.model.ModelCatalogPage;
import ir.kitgroup.inskuappb.data.model.ModelSetting;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface APICompany {

    @GET("GetCatalog")
    Observable<List<ModelCatalog>> getCatalog(@Query("userName") String userName, @Query("passWord") String passWord);

    @GET("GetCatalogPage")
    Observable<List<ModelCatalogPage>> getCatalogPage(@Query("userName") String userName, @Query("passWord") String passWord, @Query("catalogId") String catId);


    @GET("GetCatalogPageItem")
    Observable<List<ModelCatalogPageItem>> getCatalogPageItemList(@Query("userName") String userName, @Query("passWord") String passWord, @Query("catalogId") String catId, @Query("pageId") String pageId);


    @GET("GetSetting")
    Observable<List<ModelSetting>> getSetting(@Query("userName") String userName, @Query("passWord") String passWord);


    @GET("GetAccount")
    Observable<JsonElement> getCustomerList(@Query("userName") String userName, @Query("passWord") String passWord, @Query("mobile") String mobile);


    @GET("GetCatalogPageItem")
    Observable<List<ModelCatalogPageItem>> getCatalogPageItem(@Query("userName") String userName, @Query("passWord") String passWord, @Query("itemId") String itemId);


    @GET("GetContactId")
    Observable<List<ContactId>> getContactId(@Query("userName") String userName, @Query("passWord") String passWord, @Query("mobile") String mobile);



    @GET("GetOrderByContactId")
    Observable<JsonElement> getOrder(@Query("userName") String userName,
                                     @Query("password") String password,
                                     @Query("contactId") String contactId);


    @POST("SendOrder")
    Observable<List<Log>> sendOrder(@Query("userName") String userName,
                                    @Query("password") String password,
                                    @Body() String jsonObject);

    @POST("deleteOrder")
    Observable<List<Log>> deleteOrder(@Query("userName") String userName,
                                      @Query("password") String password,
                                      @Query("id") String id);

}
