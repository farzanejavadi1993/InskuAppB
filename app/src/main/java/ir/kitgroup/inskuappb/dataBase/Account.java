package ir.kitgroup.inskuappb.dataBase;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.util.List;

import ir.kitgroup.inskuappb.data.model.AppDetail;

@Keep
public class Account extends SugarRecord {
    @SerializedName("I")
    @Expose
    private String i;
    @SerializedName("N")
    @Expose
    private String n;
    @SerializedName("LN")
    @Expose
    private String ln;
    @SerializedName("M")
    @Expose
    private String m;
    @SerializedName("P1")
    @Expose
    private String p1;
    @SerializedName("ADR")
    @Expose
    private String adr;
    @SerializedName("ADR2")
    @Expose
    private Object adr2;
    @SerializedName("PM")
    @Expose
    private String pm;

    @SerializedName("GuildId")
    @Expose
    private String guildId;

    @SerializedName("StateId")
    @Expose
    private String stateId;

    @SerializedName("GuildName")
    @Expose
    private String guildName;

    @SerializedName("CityName")
    @Expose
    private String cityName;


    @SerializedName("StateName")
    @Expose
    private String stateName;
    @SerializedName("SHN")
    @Expose
    private String shn;

    @SerializedName("CityId")
    @Expose
    private String cityId;

    @SerializedName("Relation")
    @Expose
    private List<String> relation;

    @SerializedName("APP")
    @Expose
    private List<AppDetail> apps = null;
    @SerializedName("IMEI")
    @Expose
    private String imei;
    @SerializedName("APPID")
    @Expose
    private String appId;

    public int getTab() {
      return tab;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    private int tab=-1;




    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }



    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getShn() {
        return shn;
    }

    public void setShn(String shn) {
        this.shn = shn;
    }

    public List<String> getRelation() {
        return relation;
    }

    public void setRelation(List<String> relation) {
        this.relation = relation;
    }

    public String getGuildId() {
        if (guildId != null && guildId.equals(""))
            return null;
        else
            return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public String getStateId() {
        if (stateId != null && stateId.equals(""))
            return null;
        else
            return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCityId() {
        if (cityId != null && cityId.equals(""))
            return null;
        else
            return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public List<AppDetail> getApps() {
        return apps;
    }

    public void setApps(List<AppDetail> apps) {
        this.apps = apps;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private String version;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
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

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public Object getAdr2() {
        return adr2;
    }

    public void setAdr2(Object adr2) {
        this.adr2 = adr2;
    }

}
