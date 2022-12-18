package ir.kitgroup.inskuappb.ConnectServer;


import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ir.kitgroup.inskuappb.classes.AccountFilter;

import ir.kitgroup.inskuappb.classes.NetWorkHelper1;
import ir.kitgroup.inskuappb.dataBase.BusinessR;
import ir.kitgroup.inskuappb.dataBase.City;
import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.Guild;
import ir.kitgroup.inskuappb.dataBase.State;
import ir.kitgroup.inskuappb.model.Advertise;
import ir.kitgroup.inskuappb.model.AdvertisementStatus;
import ir.kitgroup.inskuappb.model.AppDetail;
import ir.kitgroup.inskuappb.model.CallMe;
import ir.kitgroup.inskuappb.model.CompanyMessage;
import ir.kitgroup.inskuappb.model.CompanyStatus;
import ir.kitgroup.inskuappb.model.ContactId;
import ir.kitgroup.inskuappb.model.Log;
import ir.kitgroup.inskuappb.model.Message;
import ir.kitgroup.inskuappb.dataBase.Account;
import ir.kitgroup.inskuappb.dataBase.ModelCatalog;
import ir.kitgroup.inskuappb.model.ModelCatalogPage;
import ir.kitgroup.inskuappb.dataBase.ModelCatalogPageItem;
import ir.kitgroup.inskuappb.model.ModelSetting;
import ir.kitgroup.inskuappb.model.WantAdvertisement;
import ir.kitgroup.inskuappb.util.Constant;


@HiltViewModel
public class MainViewModel extends ViewModel {

    private final MainRepository myRepository;
    private final SharedPreferences sharedPreferences;
    private final NetWorkHelper1 networkHelper;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<List<Log>> resultSendSms = new MutableLiveData<>();
    private final MutableLiveData<List<Log>> resultAddAccount = new MutableLiveData<>();
    private final MutableLiveData<List<Account>> resultCustomerFromServer = new MutableLiveData<>();
    private final MutableLiveData<List<Company>> resultAllCompanies = new MutableLiveData<>();
    private final MutableLiveData<List<Company>> resultSearchCompanies = new MutableLiveData<>();
    private final MutableLiveData<List<Company>> resultCompany = new MutableLiveData<>();
    private final MutableLiveData<List<ModelCatalog>> resultCatalog = new MutableLiveData<>();
    private final MutableLiveData<List<ModelCatalogPage>> resultCatalogPage = new MutableLiveData<>();
    private final MutableLiveData<List<ModelCatalogPageItem>> resultCatalogPageItem = new MutableLiveData<>();
    private final MutableLiveData<JsonElement> resultCustomer = new MutableLiveData<>();
    private final MutableLiveData<List<ContactId>> resultContactId = new MutableLiveData<>();
    private final MutableLiveData<List<AppDetail>> resultApp = new MutableLiveData<>();
    private final MutableLiveData<JsonElement> resultOrder = new MutableLiveData<>();
    private final MutableLiveData<List<Log>> resultDeleteOrder = new MutableLiveData<>();
    private final MutableLiveData<List<BusinessR>> resultBusinessRs = new MutableLiveData<>();
    private final MutableLiveData<List<Guild>> resultGuild = new MutableLiveData<>();
    private final MutableLiveData<List<State>> resultState = new MutableLiveData<>();
    private final MutableLiveData<List<City>> resultCity = new MutableLiveData<>();
    private final MutableLiveData<List<Advertise>> resultBillboardAdvertisement = new MutableLiveData<>();
    private final MutableLiveData<List<Advertise>> resultSearchAdvertisement = new MutableLiveData<>();
    private final MutableLiveData<List<Advertise>> resultVipAdvertisement = new MutableLiveData<>();
    private final MutableLiveData<List<Advertise>> resultSimpleAdvertisement = new MutableLiveData<>();

    private final MutableLiveData<List<Advertise>> resultCompanyAdvertise = new MutableLiveData<>();
    private final MutableLiveData<List<Advertise>> resultAdvertise = new MutableLiveData<>();
    private final MutableLiveData<List<Company>> resultMyCompany = new MutableLiveData<>();
    private final MutableLiveData<List<Advertise>> resultMyAdvertisement = new MutableLiveData<>();
    private final MutableLiveData<List<Log>> resultAddMyCompany = new MutableLiveData<>();
    private final MutableLiveData<List<Log>> resultAddMyAdvertisement = new MutableLiveData<>();
    private final MutableLiveData<List<Log>> resultDeleteMyAccount = new MutableLiveData<>();
    private final MutableLiveData<List<Log>> resultDeleteMyAdvertisement = new MutableLiveData<>();
    private final MutableLiveData<List<Log>> resultCreateVisitAdvertise = new MutableLiveData<>();
    private final MutableLiveData<List<ModelSetting>> resultSetting = new MutableLiveData<>();
    private final MutableLiveData<List<Log>> resultSendOrder = new MutableLiveData<>();
    private final MutableLiveData<List<Log>> resultSetToken = new MutableLiveData<>();
    private final MutableLiveData<List<AdvertisementStatus>> resultAdvertisementStatus = new MutableLiveData<>();
    private final MutableLiveData<List<AdvertisementStatus>> resultSetAdvertisementStatus = new MutableLiveData<>();
    private final MutableLiveData<List<CompanyStatus>> resultCompanyStatus = new MutableLiveData<>();
    private final MutableLiveData<List<CallMe>> resultCallMe = new MutableLiveData<>();
    private final MutableLiveData<List<WantAdvertisement>> resultWantAdvertisement = new MutableLiveData<>();
    private final MutableLiveData<List<CompanyStatus>> resultSetCompanyStatus = new MutableLiveData<>();
    private final MutableLiveData<List<Log>> resultAddCity = new MutableLiveData<>();
    private final MutableLiveData<List<CompanyMessage>> resultGetAllMessage = new MutableLiveData<>();
    private final MutableLiveData<Message> eMessage = new MutableLiveData<>();



    @Inject
    public MainViewModel(MainRepository myRepository, SharedPreferences sharedPreferences,NetWorkHelper1 networkHelper) {
        this.myRepository = myRepository;
        this.sharedPreferences = sharedPreferences;
        this.networkHelper=networkHelper;
    }



    //region Filters'Method
    public void getGuild() {
        compositeDisposable.add(
                myRepository.getGuild()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(

                                resultGuild::setValue,
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت اطلاعات"));
                                }

                        )
        );
    }

    public MutableLiveData<List<Guild>> getResultGuild() {
        return resultGuild;
    }


    public void getState() {
        compositeDisposable.add(
                myRepository.getState()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultState::setValue,
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت استان ها"));
                                }
                        )
        );
    }

    public MutableLiveData<List<State>> getResultState() {
        return resultState;
    }


    public void getCity(ArrayList<String> list) {
        compositeDisposable.clear();
        compositeDisposable.add(
                myRepository.getCity(list)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultCity::setValue,
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در دریافت شهر"));
                                }
                        )
        );
    }

    public MutableLiveData<List<City>> getResultCity() {
        return resultCity;
    }


    public void getBusinessRelations(AccountFilter accountFilter) {

        compositeDisposable.add(
                myRepository.getBusinessRelations(accountFilter)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(

                                resultBusinessRs::setValue,
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در دریافت نوع پخش"));
                                }

                        )
        );
    }

    public MutableLiveData<List<BusinessR>> getResultBusinessRelations() {
        return resultBusinessRs;
    }

    //endregion Filters'Method

    //region Advertisement' Method
    public void getBillboardAdvs(AccountFilter accountFilter,String appId, String customerId) {
        compositeDisposable.clear();
        Gson gson = new Gson();
        Type typeAccount = new TypeToken<AccountFilter>() {
        }.getType();
        gson.toJson(accountFilter, typeAccount);

        compositeDisposable.add(
                myRepository.getBillboardAdvs(accountFilter,appId,customerId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultBillboardAdvertisement::setValue,
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت آگهی "));
                                }
                        )
        );
    }
    public MutableLiveData<List<Advertise>> getResultBillboardAdvs() {
        return resultBillboardAdvertisement;
    }


    public void getSearchAdv(AccountFilter accountFilter,String appId, String customerId,String text,int page) {
        compositeDisposable.clear();
        Gson gson = new Gson();
        Type typeAccount = new TypeToken<AccountFilter>() {
        }.getType();
        gson.toJson(accountFilter, typeAccount);

        compositeDisposable.add(
                myRepository.getSearchAdvertisement(accountFilter,appId,customerId,text,page)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultSearchAdvertisement::setValue,
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت آگهی "));
                                }
                        ));
    }

    public MutableLiveData<List<Advertise>> getResultSearchAdvs() {
        return resultSearchAdvertisement;
    }

    public void getVipAdvertisements(AccountFilter accountFilter,String appId) {
        compositeDisposable.add(
                myRepository.getVipAdvertisements(accountFilter,appId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultVipAdvertisement::setValue,
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت آگهی "));
                                }
                        )
        );
    }
    public MutableLiveData<List<Advertise>> getResultVipAdvertisements() {
        return resultVipAdvertisement;
    }


    public void getSimpleAdvertisements(AccountFilter accountFilter,String appId,String customerId,int page) {
        compositeDisposable.add(
                myRepository.getSimpleAdvertisements(accountFilter,appId,customerId,page)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultSimpleAdvertisement::setValue,
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت آگهی "));
                                }
                        )
        );
    }
    public MutableLiveData<List<Advertise>> getResultSimpleAdvertisements() {
        return resultSimpleAdvertisement;
    }





    public void getCompanyAdvertisement(List<String> companiesId,int page,String appId,String customerId) {
//        compositeDisposable.clear();
        compositeDisposable.add(
                myRepository.getCompanyAdvertisement(companiesId,page,appId,customerId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                jsonElement -> resultCompanyAdvertise.setValue(jsonElement)
                                , throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت آگهی "));
                                }

                        )
        );
    }
    public MutableLiveData<List<Advertise>> getResultCompanyAdvertisement() {
        return resultCompanyAdvertise;
    }


    public void getAdvertisement(String advertisementId,String customerId) {

        compositeDisposable.add(
                myRepository.getAdvertisement(advertisementId,customerId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                jsonElement -> resultAdvertise.setValue(jsonElement)
                                ,throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت آگهی "));
                                }

                        )
        );
    }
    public MutableLiveData<List<Advertise>> getResultAdvertisement() {
        return resultAdvertise;
    }







    public void getMyAdvertisement(String customerId,String appId) {
        compositeDisposable.clear();
        compositeDisposable.add(
                myRepository.getMyAdvertisement(customerId,appId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                jsonElement -> resultMyAdvertisement.setValue(jsonElement)
                              ,  throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت آگهی ها"));
                                }

                        ));
    }

    public MutableLiveData<List<Advertise>> getResultMyAdvertisement() {
        return resultMyAdvertisement;
    }


    public void addToMyAdvertisement(String customerId, String advertiseId) {
        compositeDisposable.add(
                myRepository.addToMyAdvertisement(customerId, advertiseId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(

                                resultAddMyAdvertisement::setValue,
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در ذخیره آگهی"));
                                }

                        )
        );
    }

    public MutableLiveData<List<Log>> getResultAddMyAdvertisement() {
        return resultAddMyAdvertisement;
    }


    public void deleteMyAdvertisement(String customerId, String advertisementId) {
        compositeDisposable.add(
                myRepository.DeleteMyAdvertisement(customerId, advertisementId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(

                                resultDeleteMyAdvertisement::setValue,
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در حذف آگهی از لیست ذخیره"));
                                }

                        )
        );
    }

    public MutableLiveData<List<Log>> getResultDeleteMyAdvertisement() {
        return resultDeleteMyAdvertisement;
    }


    public void getCompany(String id) {

        compositeDisposable.add(
                myRepository.getCompany(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultCompany::setValue
                                , throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در دریافت شرکت ها"));
                                }
                        ));
    }

    public MutableLiveData<List<Company>> getResultCompany() {
        return resultCompany;
    }
    //endregion Advertisement' Method

    //region AllCompany And MyCompanies Method
    public void getAllCompany(AccountFilter accountFilter,String text,String appId,String customerId,int page) {
        compositeDisposable.clear();
        Gson gson = new Gson();
        Type typeAccount = new TypeToken<AccountFilter>() {
        }.getType();
        gson.toJson(accountFilter, typeAccount);
        compositeDisposable.add(
                myRepository.getAllCompany(accountFilter,text,appId,customerId,page)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                jsonElement -> resultAllCompanies.setValue(jsonElement)
                                , throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(10, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت شرکت ها"));
                                }
                        ));
    }

    public MutableLiveData<List<Company>> getResultAllCompany() {
        return resultAllCompanies;
    }


    public void getMyCompany(String customerId,String appId) {

        compositeDisposable.add(
                myRepository.getMyAccount(customerId,appId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultMyCompany::setValue, throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(10, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت شرکت ها"));
                                }

                        ));
    }

    public MutableLiveData<List<Company>> getResultMyCompany() {
        return resultMyCompany;
    }


    public void addMyCompany(String customerId, String accountId,String appId) {
        compositeDisposable.add(
                myRepository.addMyCompany(customerId, accountId,appId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(

                                resultAddMyCompany::setValue,
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در ذخیره شرکت"));
                                }

                        )
        );
    }

    public MutableLiveData<List<Log>> getResultAddMyCompany() {
        return resultAddMyCompany;
    }


    public void deleteMyAccount(String customerId, String accountId,String appId) {
        compositeDisposable.add(
                myRepository.DeleteMyAccount(customerId, accountId,appId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(

                                resultDeleteMyAccount::setValue,
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در حذف شرکت از لیست ذخیره"));
                                }

                        )
        );
    }

    public MutableLiveData<List<Log>> getResultDeleteMyAccount() {
        return resultDeleteMyAccount;
    }
    //endregion AllCompany And MyCompanies Method


    public void getSmsLogin(String message, String mobile) {

        compositeDisposable.add(
                myRepository.getSmsLogin(message, mobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(jsonElement -> resultSendSms.setValue(jsonElement),
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(
                                                new Message(1, "",
                                                        "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(
                                                new Message(2, "",
                                                        "خطا در ارسال پیامک"));
                                }
                        ));
    }
    public MutableLiveData<List<Log>> getResultSmsLogin() {
        return resultSendSms;
    }


    public void addAccountToServer(Constant.JsonObjectAccount accounts) {
        Gson gson = new Gson();
        Type typeAccount = new TypeToken<Constant.JsonObjectAccount>() {
        }.getType();


        compositeDisposable.add(
                myRepository.addAccountToServer(gson.toJson(accounts, typeAccount))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultAddAccount::setValue,
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(-1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در ثبت اطلاعات"));

                                }
                        ));
    }
    public MutableLiveData<List<Log>> getResultAddAccount() {
        return resultAddAccount;
    }

    public void getApp(String id) {


        compositeDisposable.add(
                myRepository.getApp(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                jsonElement -> resultApp.setValue(jsonElement),
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در ارتباط با سرور "));
                                }


                        ));
    }
    public MutableLiveData<List<AppDetail>> getResultgetApp() {
        return resultApp;
    }

    public void getCustomerFromServer(String mobile) {
        compositeDisposable.clear();
        compositeDisposable.add(
                myRepository.getCustomerFromServer(mobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                                .
                        subscribe(
                                resultCustomerFromServer::setValue,

                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت اطلاعات کاربر"));
                                }
                        ));
    }
    public MutableLiveData<List<Account>> getResultCustomerFromServer() {
        return resultCustomerFromServer;
    }
    public MutableLiveData<Message> getResultMessage() {
        return eMessage;
    }

    public void CreateVisitAdvertisement(Constant.JsonObjectVisitAdvertisement jsonObject) {
        Gson gson = new Gson();
        Type typeAccount = new TypeToken<Constant.JsonObjectVisitAdvertisement>() {
        }.getType();
        gson.toJson(jsonObject, typeAccount);
        compositeDisposable.add(
                myRepository.CreateVisitAdvertisement(gson.toJson(jsonObject, typeAccount))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(

                                resultCreateVisitAdvertise::setValue,
                                throwable ->
                                        eMessage.setValue(new Message(12, "", ""))
                        )
        );
    }

    public MutableLiveData<List<Log>> getResultCreateVisitAdvertise() {
        return resultCreateVisitAdvertise;
    }



    public void setFirebaseToken(String appId, String customerId, String imei, String token) {

        compositeDisposable.add(
                myRepository.setFirebaseToken(appId, customerId, imei, token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(jsonElement ->
                                        resultSetToken.setValue(jsonElement),
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در ارتباط با سرور "));
                                }
                        ));
    }

    public  MutableLiveData<List<Log>> getResultFirebaseToken(){
        return resultSetToken;
    }


    public void getWannaAdvertisementStatus(String customerId, String AdvertisementId) {

        compositeDisposable.add(
                myRepository.getWannaAdvertisementStatus(customerId, AdvertisementId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(jsonElement ->
                                        resultAdvertisementStatus.setValue(jsonElement),
                                throwable ->{
                                    int p=0;
                                }));
    }
    public  MutableLiveData<List<AdvertisementStatus>> getResultAdvertisementStatus(){
        return resultAdvertisementStatus;
    }



    public void setWannaAdvertisementStatus(String customerId, String AdvertisementId,boolean StatusWanna) {
        compositeDisposable.add(
                myRepository.setAdvertisementStatus(customerId, AdvertisementId,StatusWanna)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultSetAdvertisementStatus::setValue,
                                throwable ->{
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در ارسال درخواست "));
                                }));
    }
    public  MutableLiveData<List<AdvertisementStatus>> getResultSetAdvertisementStatus(){
        return resultSetAdvertisementStatus;
    }




    public void getCallMeStatus(String customerId, String companyId) {
        compositeDisposable.add(
                myRepository.getCallMeStatus(customerId, companyId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultCompanyStatus::setValue,
                                throwable ->{
                                    int p=0;
                                }));
    }
    public  MutableLiveData<List<CompanyStatus>> getResultCallMeStatus(){
        return resultCompanyStatus;
    }



    public void getCustomerCallRequest(String customerId) {
        compositeDisposable.add(
                myRepository.getCustomerCallRequest(customerId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {})
                        .subscribe(jsonElement ->
                                resultCallMe.setValue(jsonElement),
                                throwable ->{
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در دریافت اطلاعات"));
                                }));
    }
    public  MutableLiveData<List<CallMe>> getResultCustomerCallRequest(){return resultCallMe;
    }


    public void getCustomerWantAdv(String customerId) {
        compositeDisposable.add(
                myRepository.getCustomerWantAdv(customerId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {})
                        .subscribe(jsonElement ->
                                        resultWantAdvertisement.setValue(jsonElement),
                                throwable ->{
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در دریافت اطلاعات"));
                                }));
    }
    public  MutableLiveData<List<WantAdvertisement>> getResultCustomerWantAdv(){return resultWantAdvertisement;}


    public void setStatusCompany(String customerId, String companyId,boolean StatusCallMe) {
        compositeDisposable.add(
                myRepository.setStatusCompany(customerId, companyId,StatusCallMe)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultSetCompanyStatus::setValue,
                                throwable ->{
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در ارسال درخواست "));
                                }));
    }
    public  MutableLiveData<List<CompanyStatus>> getResultSetCompanyStatus(){
        return resultSetCompanyStatus;
    }


    public void getFilterMessage(String target, String customerId,String appId) {

        compositeDisposable.clear();
        compositeDisposable.add(
                myRepository.getFilterMessage(target,customerId, appId)
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
    public MutableLiveData<List<CompanyMessage>> getResultFilterMessage() {
        return resultGetAllMessage;
    }


    public void addNewCity(String cityName, String stateId) {

        compositeDisposable.add(
                myRepository.addNewCity(cityName, stateId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                jsonElement -> resultAddCity.setValue(jsonElement),
                                throwable ->
                                {
                                    if (!networkHelper.isNetworkConnected1())
                                        eMessage.setValue(
                                                new Message(4, "",
                                                        "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(
                                                new Message(4, "",
                                                        "خطا در ثبت شهر"));
                                }
                        ));
    }
    public MutableLiveData<List<Log>> getResultAddCity() {
        return resultAddCity;
    }
    public void clearRequest() {
        compositeDisposable.clear();

    }

}