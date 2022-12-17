package ir.kitgroup.inskuappb.classes;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;


import javax.inject.Inject;
import javax.inject.Singleton;


import ir.kitgroup.inskuappb.dataBase.Company;
import ir.kitgroup.inskuappb.dataBase.StoreChangeAdvertise;


@Singleton
public class SharedPrefrenceValue {

    SharedPreferences sharedPreferences;

    @Inject
    public SharedPrefrenceValue(SharedPreferences preferenceHelper){
        this.sharedPreferences = preferenceHelper;

    }

    public void saveValueInSharedPrefrence(String id, boolean save, int count) {
        String storedHashMap = sharedPreferences.getString("storeHashMap", "");

        ArrayList<StoreChangeAdvertise> storeChangeAdvertises;
        Gson gson = new Gson();


        if (!storedHashMap.equals("")) {
            java.lang.reflect.Type type = new TypeToken<HashMap<String, ArrayList<StoreChangeAdvertise>>>() {
            }.getType();
            HashMap<String, ArrayList<StoreChangeAdvertise>> hashMap = gson.fromJson(storedHashMap, type);


            storeChangeAdvertises = hashMap.get("simpleChange");

            ArrayList<StoreChangeAdvertise> res = new ArrayList<>(storeChangeAdvertises);
            CollectionUtils.filter(res, s -> s.getI().equals(id));

            if (res.size() > 0) {
                res.get(0).setSave(save);
                res.get(0).setCount(count);
            } else {
                StoreChangeAdvertise strChangeAdv = new StoreChangeAdvertise();
                strChangeAdv.setSave(save);
                strChangeAdv.setCount(count);
                strChangeAdv.setI(id);
                storeChangeAdvertises.add(strChangeAdv);
            }

        } else {
            storeChangeAdvertises = new ArrayList<>();
            StoreChangeAdvertise strChangeAdv = new StoreChangeAdvertise();
            strChangeAdv.setSave(save);
            strChangeAdv.setCount(count);
            strChangeAdv.setI(id);
            storeChangeAdvertises.add(strChangeAdv);
        }

        HashMap<String, ArrayList<StoreChangeAdvertise>> hashMapStore = new HashMap<>();
        hashMapStore.put("simpleChange", storeChangeAdvertises);
        String storeHashMap = gson.toJson(hashMapStore);
        sharedPreferences.edit().putString("storeHashMap", storeHashMap).apply();
    }

    public ArrayList<StoreChangeAdvertise> useDataToSetInList(String storedHashMap) {
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken<HashMap<String, ArrayList<StoreChangeAdvertise>>>() {
        }.getType();
        HashMap<String, ArrayList<StoreChangeAdvertise>> hashMap = gson.fromJson(storedHashMap, type);

        ArrayList<StoreChangeAdvertise> storeChangeAdvertises = hashMap.get("simpleChange");
        return storeChangeAdvertises;
    }

    public void addToMyAccount(Company company) {
        String storeCompany = sharedPreferences.getString("storeCompany", "");

        ArrayList<Company> storeChangeCompany = null;
        Gson gson = new Gson();

        if (!storeCompany.equals("")) {
            java.lang.reflect.Type type = new TypeToken<HashMap<String, ArrayList<Company>>>() {
            }.getType();
            HashMap<String, ArrayList<Company>> hashMap = gson.fromJson(storeCompany, type);

            storeChangeCompany = hashMap.get("changeCompany");
            company.setSave(true);
            storeChangeCompany.add(company);
        }
        HashMap<String, ArrayList<Company>> hashMapStore = new HashMap<>();
        hashMapStore.put("changeCompany", storeChangeCompany);
        String storeHashMap = gson.toJson(hashMapStore);
        sharedPreferences.edit().putString("storeCompany", storeHashMap).apply();
    }

    public void deleteFromMyCompany(String id) {
        String storeCompany = sharedPreferences.getString("storeCompany", "");

        ArrayList<Company> storeChangeCompany = null;
        Gson gson = new Gson();

        if (!storeCompany.equals("")) {
            java.lang.reflect.Type type = new TypeToken<HashMap<String, ArrayList<Company>>>() {
            }.getType();
            HashMap<String, ArrayList<Company>> hashMap = gson.fromJson(storeCompany, type);

            storeChangeCompany = hashMap.get("changeCompany");

            ArrayList<Company> res = new ArrayList<>(storeChangeCompany);
            CollectionUtils.filter(res, s -> s.getI().equals(id));

            if (res.size() > 0)
                storeChangeCompany.remove(res.get(0));


        }
        HashMap<String, ArrayList<Company>> hashMapStore = new HashMap<>();
        hashMapStore.put("changeCompany", storeChangeCompany);
        String storeHashMap = gson.toJson(hashMapStore);
        sharedPreferences.edit().putString("storeCompany", storeHashMap).apply();
    }


    public ArrayList<Company> getListFromSharedPrefrence() {
        String storeCompany = sharedPreferences.getString("storeCompany", "");
        ArrayList<Company> changeCompany = new ArrayList<>();

        if (!storeCompany.equals("")) {
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<HashMap<String, ArrayList<Company>>>() {
            }.getType();
            HashMap<String, ArrayList<Company>> hashMap = gson.fromJson(storeCompany, type);
            changeCompany = hashMap.get("changeCompany");

        }
        return changeCompany;
    }
}
