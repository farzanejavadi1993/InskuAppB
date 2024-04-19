package ir.kitgroup.inskuappb.ui.viewmodel;



import android.app.Activity;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonElement;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ir.kitgroup.inskuappb.dataBase.Ord;
import ir.kitgroup.inskuappb.dataBase.OrdDetail;
import ir.kitgroup.inskuappb.data.model.ContactId;
import ir.kitgroup.inskuappb.data.model.Log;
import ir.kitgroup.inskuappb.data.model.Message;
import ir.kitgroup.inskuappb.dataBase.ModelCatalog;
import ir.kitgroup.inskuappb.data.model.ModelCatalogPage;
import ir.kitgroup.inskuappb.dataBase.ModelCatalogPageItem;
import ir.kitgroup.inskuappb.data.model.ModelGift;
import ir.kitgroup.inskuappb.data.model.ModelSetting;
import ir.kitgroup.inskuappb.repository.companyrepository.CRepository;
import ir.kitgroup.inskuappb.util.Constant;


@HiltViewModel
public class CompanyViewModel extends ViewModel {

    private final CRepository myRepository;
    private final SharedPreferences sharedPreferences;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();




    private final MutableLiveData<List<ModelCatalog>> resultCatalog = new MutableLiveData<>();
    private final MutableLiveData<List<ModelCatalogPage>> resultCatalogPage = new MutableLiveData<>();
    private final MutableLiveData<List<ModelCatalogPageItem>> resultCatalogPageItem = new MutableLiveData<>();
    private final MutableLiveData<JsonElement> resultCustomer = new MutableLiveData<>();
    private final MutableLiveData<List<ContactId>> resultContactId = new MutableLiveData<>();
    private final MutableLiveData<JsonElement> resultOrder = new MutableLiveData<>();
    private final MutableLiveData<List<Log>> resultDeleteOrder = new MutableLiveData<>();
    private final MutableLiveData<List<ModelSetting>> resultSetting = new MutableLiveData<>();
    private final MutableLiveData<List<Log>> resultSendOrder = new MutableLiveData<>();
    private final MutableLiveData<Message> eMessage = new MutableLiveData<>();

    @Inject
    public CompanyViewModel(CRepository myRepository, SharedPreferences sharedPreferences) {
        this.myRepository = myRepository;
        this.sharedPreferences = sharedPreferences;
    }

    //region Catalog's Method
    public void getCatalogPage(String userName, String passWord, String catalogId,Activity activity) {
        compositeDisposable.clear();
        compositeDisposable.add(
                myRepository.getCatalogPage(userName, passWord, catalogId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultCatalogPage::setValue,
                                throwable ->
                                {
                                    if (!Constant.connectedToInternet(activity))
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت  صفحات کاتالوگ"));
                                }));
    }
    public MutableLiveData<List<ModelCatalogPage>> getResultCatalogPage() {
        return resultCatalogPage;
    }



    public void getCatalogPageItemList(String userName, String passWord, String catalogId, String pageId,Activity activity) {
        compositeDisposable.clear();
        compositeDisposable.add(
                myRepository.getCatalogPageItemList(userName, passWord, catalogId, pageId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultCatalogPageItem::setValue,
                                throwable ->
                                {
                                    if (!Constant.connectedToInternet(activity))
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت کالاها "));
                                }
                        ));
    }

    public MutableLiveData<List<ModelCatalogPageItem>> getResultCatalogPageItemList() {
        return resultCatalogPageItem;
    }

    //endregion Catalog's Method

    //region DetailCompanies'Method
    public void getCustomerList(String userName, String passWord, String mobile,Activity activity) {
        compositeDisposable.clear();
        compositeDisposable.add(
                myRepository.getCustomerList(userName, passWord, mobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(jsonElement -> {

                                    resultCustomer.setValue(jsonElement);
                                },
                                throwable ->
                                {
                                    if (!Constant.connectedToInternet(activity))
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(
                                                new Message(-1, throwable.getMessage(), "خطا در ارتباط با سرور"));
                                }));
    }
    public MutableLiveData<JsonElement> getResultCustomerList() {
        return resultCustomer;
    }


    public void getContactId(String userName, String passWord, String mobile,Activity activity) {
        compositeDisposable.clear();
        compositeDisposable.add(
                myRepository.getContactId(userName, passWord, mobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultContactId::setValue,
                                throwable ->
                                {
                                    if (!Constant.connectedToInternet(activity))
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(
                                                new Message(-2, "", "خطا در ارتباط با سرور"));
                                }));
    }
    public MutableLiveData<List<ContactId>> getResultContactId() {
        return resultContactId;
    }


    public void getSetting(String userName, String passWord,Activity activity) {
        compositeDisposable.clear();
        compositeDisposable.add(
                myRepository.getSetting(userName, passWord)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultSetting::setValue,
                                throwable ->
                                {
                                    if (!Constant.connectedToInternet(activity))
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(
                                                new Message(-3, "", "خطا در ارتباط با سرور"));}

                        ));
    }
    public MutableLiveData<List<ModelSetting>> getResultSetting() {
        return resultSetting;
    }


    public void getCatalog(String userName, String passWord,Activity activity) {
        compositeDisposable.clear();
        compositeDisposable.add(
                myRepository.getCatalog(userName, passWord)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultCatalog::setValue,

                                throwable ->
                                {
                                    if (!Constant.connectedToInternet(activity))
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(
                                                new Message(3, "", "خطا در دریافت کاتالوگ"));
                                }
                        ));
    }
    public MutableLiveData<List<ModelCatalog>> getResultCatalog() {
        return resultCatalog;
    }
    //endregion DetailCompanies'Method

    //region Orders'Method
    public void getCatalogPageItem(String userName, String passWord, String itemId,Activity activity) {
        compositeDisposable.add(
                myRepository.getCatalogPageItem(userName, passWord, itemId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultCatalogPageItem::setValue,
                                throwable ->
                                {
                                    if (!Constant.connectedToInternet(activity))
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت کالاها"));
                                }

                        ));

    }
    public MutableLiveData<List<ModelCatalogPageItem>> getResultCatalogPageItem() {
        return resultCatalogPageItem;
    }



    public void sendOrder(String userName, String passWord, List<Ord> ords, List<OrdDetail> ordDetails, List<ModelGift> modelGifts,Activity activity) {
        compositeDisposable.add(
                myRepository.sendOrder(userName, passWord, ords, ordDetails, modelGifts)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(jsonElement -> {

                                    resultSendOrder.setValue(jsonElement);
                                },
                                throwable ->
                                {
                                    if (!Constant.connectedToInternet(activity))
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در ارسال سفارش"));
                                }
                        ));
    }
    public MutableLiveData<List<Log>> getResultSendOrder() {
        return resultSendOrder;
    }


    public void deleteOrder(String userName, String passWord, String id,Activity activity) {

        compositeDisposable.add(
                myRepository.deleteOrder(userName, passWord, id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                resultDeleteOrder::setValue,
                                throwable ->
                                {

                                    if (!Constant.connectedToInternet(activity))
                                        eMessage.setValue(new Message(3, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(3, "", "خطا در حذف سفارش"));

                                }


                        ));
    }

    public MutableLiveData<List<Log>> getResultDeleteOrder() {
        return resultDeleteOrder;
    }


    public void getOrder(String userName, String passWord, String contactId,Activity activity) {
        compositeDisposable.clear();
        compositeDisposable.add(
                myRepository.getOrder(userName, passWord, contactId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                        })
                        .subscribe(
                                jsonElement -> resultOrder.setValue(jsonElement),
                                throwable ->
                                {

                                    if (!Constant.connectedToInternet(activity))
                                        eMessage.setValue(new Message(1, "", "خطا در اتصال اینترنت"));
                                    else
                                        eMessage.setValue(new Message(1, "", "خطا در دریافت سفارش"));

                                }
                        )
        );
    }
    public MutableLiveData<JsonElement> getResultOrder() {
        return resultOrder;
    }
    //endregion Orders'Method


    public MutableLiveData<Message> getResultMessage() {
        return eMessage;
    }







}