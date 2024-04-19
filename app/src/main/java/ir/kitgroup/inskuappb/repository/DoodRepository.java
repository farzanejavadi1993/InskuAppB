package ir.kitgroup.inskuappb.repository;


import com.google.gson.JsonElement;

import javax.inject.Inject;

import io.reactivex.Observable;
import ir.kitgroup.inskuappb.data.api.APIDood;

public class DoodRepository {

    private final APIDood api;

    @Inject
    public DoodRepository(APIDood api) {

        this.api = api;
    }

    public Observable<JsonElement> getAllCompanyWithMessages(String userId, String appId, int pageNumber, int pageSize) {
        return api.getAllCompanyWithMessages(userId, appId, pageNumber, pageSize);
    }

    public Observable<JsonElement> GetCompanyMessages(String userId, String appId, String companyId, int pageNumber, int pageSize) {
        return api.GetCompanyMessages(userId, appId, companyId, pageNumber, pageSize);
    }

    public Observable<String> ReadCompanyMessages(String userId, String appId, String companyId, int pageNumber, int pageSize) {
        return api.ReadCompanyMessages(userId, appId, companyId, pageNumber, pageSize);
    }

    public Observable<String> setMessageRespond(String userId, String appId, String messageId, String respondId) {
        return api.setMessageRespond(userId, appId, messageId, respondId);
    }

    public Observable<Integer> getNewMessagesCount(String userId, String appId) {
        return api.getNewMessagesCount(userId, appId);
    }
}
