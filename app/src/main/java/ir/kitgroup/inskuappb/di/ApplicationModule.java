package ir.kitgroup.inskuappb.di;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import ir.kitgroup.inskuappb.ConnectServer.APICompany;
import ir.kitgroup.inskuappb.ConnectServer.APIDood;
import ir.kitgroup.inskuappb.ConnectServer.APIMain;
import ir.kitgroup.inskuappb.ConnectServer.HostSelectionInterceptor;
import ir.kitgroup.inskuappb.classes.SharedPrefrenceValue;
import ir.kitgroup.inskuappb.util.Constant;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class ApplicationModule {

    @Provides
    @Singleton
    SharedPreferences provideSharedPreference(@ApplicationContext Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    Context provideContext(@ApplicationContext Context context) {
        return context;
    }


    @Provides
    @Singleton
    public HostSelectionInterceptor provideHostSelectionInterceptor(SharedPreferences preferenceHelper) {
        return new HostSelectionInterceptor(preferenceHelper);
    }


    @Provides
    @Singleton
    public SharedPrefrenceValue provideSharedPrefrenceValue(SharedPreferences preferenceHelper) {
        return new SharedPrefrenceValue(preferenceHelper);
    }



    @Provides
    @Singleton
    @Named("Company")
    public OkHttpClient provideOkHttpClientCompany(@ApplicationContext Context context,HostSelectionInterceptor  hostSelectionInterceptor) {
        long cacheSize = 5 * 1024 * 1024;
        Cache mCache = new Cache(context.getCacheDir(), cacheSize);
        return new OkHttpClient().newBuilder()
                .cache(mCache)
                .retryOnConnectionFailure(true)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(hostSelectionInterceptor)
                .followRedirects(true)
                .followSslRedirects(true)
                .build();


    }



    @Provides
    @Singleton
    @Named("Main")
    public OkHttpClient provideOkHttpClient(@ApplicationContext Context context) {

        long cacheSize = 5 * 1024 * 1024;
        Cache mCache = new Cache(context.getCacheDir(), cacheSize);
        return new OkHttpClient().newBuilder()
                .cache(mCache)
                .retryOnConnectionFailure(true)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                //.addInterceptor(hostSelectionInterceptor)
                .followRedirects(false)
                .followSslRedirects(true)
                .build();
    }


    @Provides
    @Singleton
    @Named("Main")
    Retrofit provideRetrofitMain(Gson gson,@Named("Main") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.Main_URL)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    @Named("Dood")
    Retrofit provideRetrofitDood(Gson gson,@Named("Main") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.Dood_URL)
                .client(okHttpClient)
                .build();
    }


    @Provides
    @Singleton
    @Named("Company")
    Retrofit provideRetrofitCompany(Gson gson, @Named("Company") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.Dood_URL)
                .client(okHttpClient)
                .build();
    }



    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        // gsonBuilder.setLenient();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    Typeface provideTypeFaceSignature(@ApplicationContext Context context) {
        return Typeface.createFromAsset(context.getAssets(), "iransans.ttf");

    }

    @Provides
    @Singleton
    public APIMain provideAPIMain(@Named("Main") Retrofit ret) {
        return ret.create(APIMain.class);
    }

    @Provides
    @Singleton
    public APIDood provideAPIDood(@Named("Dood") Retrofit ret) {
        return ret.create(APIDood.class);
    }

    @Provides
    @Singleton
    public APICompany provideAPICompany(@Named("Company") Retrofit ret) {
        return ret.create(APICompany.class);
    }
}