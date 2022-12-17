package ir.kitgroup.inskuappb.dataBase;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;


import java.util.List;

@Keep
public class Company extends SugarRecord {
    @SerializedName("I")
    @Expose
    public String i;
    @SerializedName("N")
    @Expose
    public String n;
    @SerializedName("PI")
    @Expose
    public String pi;
    @SerializedName("T1")
    @Expose
    public String t1;
    @SerializedName("Issaved")
    @Expose
    public boolean issaved;
    @SerializedName("M1")
    @Expose
    public String m1;
    @SerializedName("LONG")
    @Expose
    public String _long;
    @SerializedName("LAT")
    @Expose
    public String lat;
    @SerializedName("IP1")
    @Expose
    public String ip1;
    @SerializedName("USER")
    @Expose
    public String user;
    @SerializedName("PASS")
    @Expose
    public String pass;
    @SerializedName("DESC")
    @Expose
    public String desc;
    @SerializedName("INSK_ID")
    @Expose
    public String inskId;
    @SerializedName("COUNTRY")
    @Expose
    public String country;
    @SerializedName("STATE")
    @Expose
    public String state;
    @SerializedName("CITY")
    @Expose
    public String city;
    @SerializedName("ABUS")
    @Expose
    public String abus;
    @SerializedName("HTG")
    @Expose
    public String htg;
    @SerializedName("TXT1")
    @Expose
    public String txt1;
    @SerializedName("TELG")
    @Expose
    public String telg;
    @SerializedName("WHATA")
    @Expose
    public String whata;
    @SerializedName("INSTA")
    @Expose
    public String insta;
    @SerializedName("EMAIL")
    @Expose
    public String email;
    @SerializedName("WEBS")
    @Expose
    public String webs;
    @SerializedName("ADDR")
    @Expose
    public String addr;

    @SerializedName("COUNT")
    @Expose
    public Integer count;
    @SerializedName("BR")
    @Expose
    public String br;
    @SerializedName("BRN")
    @Expose
    public String brn;
    @SerializedName("Guilds")
    @Expose
    public List<Guild> guilds = null;
    @SerializedName("States")
    @Expose
    public List<State> states = null;
    @SerializedName("Cities")
    @Expose
    public List<City> cities = null;
    @SerializedName("Files")
    @Expose
    public List<Files> files = null;
    @SerializedName("AC_AREA")
    @Expose
    private int ac_area ;


     public int getAc_area() {
        return ac_area;
        //1سراسرس
        //2منطقه ای
        //3استانی
        //4محلی
    }

    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    public String getINSTA() {
        return insta;
    }

    public List<Files> getFiles() {
        return files;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getPi() {
        return pi;
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getM1() {
        return m1;
    }

    public void setM1(String m1) {
        this.m1 = m1;
    }

    public String getLong() {
        return _long;
    }

    public String getLat() {
        return lat;
    }

    public String getIp1() {
        return ip1;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getInskId() {
        return inskId;
    }


    public String getAbus() {
        return abus;
    }

    public void setAbus(String abus) {
        this.abus = abus;
    }

    public String getTelg() {
        return telg;
    }

    public String getWhata() {
        return whata;
    }

    public String getEmail() {
        return email;
    }

    public String getWebs() {
        return webs;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }


    public boolean isSave() {
         return issaved;
    }

    public void setSave(boolean save) {
         this.issaved = save;
    }




}
