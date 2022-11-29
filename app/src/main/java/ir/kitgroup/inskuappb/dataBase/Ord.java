package ir.kitgroup.inskuappb.dataBase;
import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
@Keep
public class Ord extends SugarRecord {

    @SerializedName("GID")
    @Expose
    private String gid;
    @SerializedName("N")
    @Expose
    private String n;
    @SerializedName("ACID")
    @Expose
    private String acid;
    @SerializedName("CNTID")
    @Expose
    private Object cntid;
    @SerializedName("ACNM")
    @Expose
    private String acnm;
    @SerializedName("APP")
    @Expose
    private Object app;
    @SerializedName("CAT")
    @Expose
    private String cat;
    @SerializedName("DESC")
    @Expose
    private String desc;
    @SerializedName("PRICETYPE")
    @Expose
    private String pricetype;
    @SerializedName("TYPE")
    @Expose
    private Integer type;
    @SerializedName("DISC1")
    @Expose
    private String disc1;
    @SerializedName("DISC2")
    @Expose
    private String disc2;
    @SerializedName("DISC3")
    @Expose
    private String disc3;
    @SerializedName("DATE")
    @Expose
    private String date;
    @SerializedName("PDATE")
    @Expose
    private Object pdate;
    @SerializedName("SIG")
    @Expose
    private Object sig;
    @SerializedName("PDAY")
    @Expose
    private String pday;
    @SerializedName("NETPR")
    @Expose
    private Object netpr;
    @SerializedName("ITYPE")
    @Expose
    private String itype;
    @SerializedName("PAYTYPE")
    @Expose
    private String paytype;
    @SerializedName("ACT")
    @Expose
    private String act;
    @SerializedName("MC")
    @Expose
    private String mc;
    @SerializedName("CONF")
    @Expose
    private String conf;
    @SerializedName("SC")
    @Expose
    private String sc;
    @SerializedName("AC")
    @Expose
    private String ac;
    @SerializedName("SMC")
    @Expose
    private String smc;
    @SerializedName("DC")
    @Expose
    private String dc;
    @SerializedName("DSC")
    @Expose
    private String dsc;
    @SerializedName("C")
    @Expose
    private String c;
    @SerializedName("DELETED")
    @Expose
    private Boolean deleted;
    @SerializedName("PCH")
    @Expose
    private Object pch;
    @SerializedName("SUMDPRICE")
    @Expose
    private String sumdprice;
    @SerializedName("SUMPPRICE")
    @Expose
    private String sumpprice;
    @SerializedName("SUMNPRICE")
    @Expose
    private String sumnprice;



    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getAcid() {
        return acid;
    }

    public void setAcid(String acid) {
        this.acid = acid;
    }

    public Object getCntid() {
        return cntid;
    }

    public void setCntid(Object cntid) {
        this.cntid = cntid;
    }

    public String getAcnm() {
        return acnm;
    }

    public void setAcnm(String acnm) {
        this.acnm = acnm;
    }

    public Object getApp() {
        return app;
    }

    public void setApp(Object app) {
        this.app = app;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPricetype() {
        return pricetype;
    }

    public void setPricetype(String pricetype) {
        this.pricetype = pricetype;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDisc1() {
        return disc1;
    }

    public void setDisc1(String disc1) {
        this.disc1 = disc1;
    }

    public String getDisc2() {
        return disc2;
    }

    public void setDisc2(String disc2) {
        this.disc2 = disc2;
    }

    public String getDisc3() {
        return disc3;
    }

    public void setDisc3(String disc3) {
        this.disc3 = disc3;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Object getPdate() {
        return pdate;
    }

    public void setPdate(Object pdate) {
        this.pdate = pdate;
    }

    public Object getSig() {
        return sig;
    }

    public void setSig(Object sig) {
        this.sig = sig;
    }

    public String getPday() {
        return pday;
    }

    public void setPday(String pday) {
        this.pday = pday;
    }

    public Object getNetpr() {
        return netpr;
    }

    public void setNetpr(Object netpr) {
        this.netpr = netpr;
    }

    public String getItype() {
        return itype;
    }

    public void setItype(String itype) {
        this.itype = itype;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getSmc() {
        return smc;
    }

    public void setSmc(String smc) {
        this.smc = smc;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Object getPch() {
        return pch;
    }

    public void setPch(Object pch) {
        this.pch = pch;
    }

    public String getSumdprice() {
        return sumdprice;
    }

    public void setSumdprice(String sumdprice) {
        this.sumdprice = sumdprice;
    }

    public String getSumpprice() {
        return sumpprice;
    }

    public void setSumpprice(String sumpprice) {
        this.sumpprice = sumpprice;
    }

    public String getSumnprice() {
        return sumnprice;
    }

    public void setSumnprice(String sumnprice) {
        this.sumnprice = sumnprice;
    }

}