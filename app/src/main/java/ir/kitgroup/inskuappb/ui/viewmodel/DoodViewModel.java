package ir.kitgroup.inskuappb.ui.viewmodel;


import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ir.kitgroup.inskuappb.component.NetWorkHelper1;
import ir.kitgroup.inskuappb.data.model.CompanyMessage;
import ir.kitgroup.inskuappb.data.model.Message;
import ir.kitgroup.inskuappb.data.model.Messages;
import ir.kitgroup.inskuappb.repository.DoodRepository;


@HiltViewModel
public class DoodViewModel extends ViewModel {

    private final DoodRepository myRepository;
    private final SharedPreferences sharedPreferences;
    private final NetWorkHelper1 networkHelper;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<List<CompanyMessage>> resultGetAllMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Messages>> resultGetMessages = new MutableLiveData<>();
    private final MutableLiveData<String> resultReadMessages = new MutableLiveData<>();
    private final MutableLiveData<String> resultSetRespond = new MutableLiveData<>();
    private final MutableLiveData<Integer> resultGetNewMessagesCount = new MutableLiveData<>();
    private final MutableLiveData<Message> eMessage = new MutableLiveData<>();

    @Inject
    public DoodViewModel(DoodRepository myRepository, SharedPreferences sharedPreferences, NetWorkHelper1 networkHelper) {

        this.myRepository = myRepository;
        this.sharedPreferences = sharedPreferences;
        this.networkHelper=networkHelper;
    }



    public MutableLiveData<Message> getResultMessage() {
        return eMessage;
    }


    public void getAllCompanyWithMessages(String userId, String appId,int pageNumber,int pageSize) {


        compositeDisposable.add(
                myRepository.getAllCompanyWithMessages(userId, appId,pageNumber,pageSize)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(jsonElement -> {

                            try {
                                JsonArray companies = jsonElement.getAsJsonObject().get("Companies").getAsJsonArray();

                                Gson gson = new Gson();
                                Type type = new TypeToken<List<CompanyMessage>>() {
                                }.getType();
                                List<CompanyMessage> companyMessages = gson.fromJson(companies.toString(), type);
                                resultGetAllMessage.setValue(companyMessages);

                            }catch (Exception ignored){

                                ArrayList<CompanyMessage> companyMessages=new ArrayList<>();
                                resultGetAllMessage.setValue(companyMessages);
                            }

                            }, throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت پیام ها"));
                                }
                                ));}
    public MutableLiveData<List<CompanyMessage>> getResultAllMessage() {
        return resultGetAllMessage;
    }


    public void getCompanyMessages(String userId, String appId, String companyId, int pageNumber, int pageSize) {

        compositeDisposable.add(
                myRepository.GetCompanyMessages(userId, appId, companyId, pageNumber, pageSize)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(jsonElement -> {

                            try {
                                JsonArray mssg = jsonElement.getAsJsonObject().get("Messages").getAsJsonArray();
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<Messages>>() {
                                }.getType();
                                List<Messages> messages = gson.fromJson(mssg.toString(), type);
                                resultGetMessages.setValue(messages);
                            }catch (Exception ignored){
                                ArrayList<Messages> list=new ArrayList<>();
                                resultGetMessages.setValue(list);
                            }

                                }, throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));

                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت پبام ها"));
                                }
                        ));
    }
    public MutableLiveData<List<Messages>> getResultCompanyMessages() {
        return resultGetMessages;
    }



    public void ReadCompanyMessages(String userId, String appId, String companyId,int pageNumber,int pageSize) {

        compositeDisposable.add(
                myRepository.ReadCompanyMessages(userId, appId, companyId,pageNumber,pageSize)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(jsonElement -> {
                                    resultReadMessages.setValue(jsonElement);
                                }, throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در ارتباط با سرور "));
                                }
                        ));
    }
    public MutableLiveData<String> getResultReadMessages() {
        return resultReadMessages;
    }


    public void setMessageRespond(String userId, String appId, String messageId, String respondId) {

        compositeDisposable.add(
                myRepository.setMessageRespond(userId, appId, messageId, respondId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(jsonElement -> {
                                    resultSetRespond.setValue(jsonElement);
                                }, throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در ارسال پاسخ "));
                                }
                        ));
    }
    public MutableLiveData<String> getResultSetRespond() {
        return resultSetRespond;
    }



    public void getNewMessagesCount(String userId, String appId) {

        compositeDisposable.add(
                myRepository.getNewMessagesCount(userId, appId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(jsonElement -> {
                                    resultGetNewMessagesCount.setValue(jsonElement);
                                }, throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", ""));
                                }
                        ));
    }

    public MutableLiveData<Integer> getResultNewMessagesCount() {
        return resultGetNewMessagesCount;
    }
}
