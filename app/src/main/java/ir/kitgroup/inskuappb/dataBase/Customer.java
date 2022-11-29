package ir.kitgroup.inskuappb.dataBase;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
@Keep
public class Customer extends SugarRecord {
    @SerializedName("I")
    @Expose
    private String i;
    @SerializedName("N")
    @Expose
    private String n;
    @SerializedName("M")
    @Expose
    private String m;
    @SerializedName("T")
    @Expose
    private String t;
    @SerializedName("MO")
    @Expose
    private String mo;
    @SerializedName("A")
    @Expose
    private String a;
    @SerializedName("LA")
    @Expose
    private String la;
    @SerializedName("LO")
    @Expose
    private String lo;
    @SerializedName("SA")
    @Expose
    private String sa;
    @SerializedName("CI")
    @Expose
    private String ci;
    @SerializedName("BL")
    @Expose
    private String bl;
    @SerializedName("GU")
    @Expose
    private String gu;
    @SerializedName("OW")
    @Expose
    private String ow;
    @SerializedName("VI")
    @Expose
    private Double vi;
    @SerializedName("ST")
    @Expose
    private String st;
    @SerializedName("CS")
    @Expose
    private String cs;
    @SerializedName("MA")
    @Expose
    private String ma;
    @SerializedName("CO")
    @Expose
    private String co;
    @SerializedName("GR")
    @Expose
    private String gr;
    @SerializedName("BO")
    @Expose
    private String bo;
    @SerializedName("DE")
    @Expose
    private String de;
    @SerializedName("RCO")
    @Expose
    private String rco;
    @SerializedName("HCM")
    @Expose
    private String hcm;
    @SerializedName("HWM")
    @Expose
    private String hwm;
    @SerializedName("HCAM")
    @Expose
    private String hcam;
    @SerializedName("MG")
    @Expose
    private String mg;
    @SerializedName("STT")
    @Expose
    private String stt;
    @SerializedName("RM")
    @Expose
    private String rm;
    @SerializedName("CC")
    @Expose
    private String cc;
    @SerializedName("RC")
    @Expose
    private String rc;
    @SerializedName("RR")
    @Expose
    private String rr;
    @SerializedName("HC")
    @Expose
    private String hc;
    @SerializedName("NPR")
    @Expose
    private String npr;

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

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getMo() {
        return mo;
    }

    public void setMo(String mo) {
        this.mo = mo;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getLa() {
        return la;
    }

    public void setLa(String la) {
        this.la = la;
    }

    public String getLo() {
        return lo;
    }

    public void setLo(String lo) {
        this.lo = lo;
    }

    public String getSa() {
        return sa;
    }

    public void setSa(String sa) {
        this.sa = sa;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getBl() {
        return bl;
    }

    public void setBl(String bl) {
        this.bl = bl;
    }

    public String getGu() {
        return gu;
    }

    public void setGu(String gu) {
        this.gu = gu;
    }

    public String getOw() {
        return ow;
    }

    public void setOw(String ow) {
        this.ow = ow;
    }

    public Double getVi() {
        return vi;
    }

    public void setVi(Double vi) {
        this.vi = vi;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getCs() {
        return cs;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getGr() {
        return gr;
    }

    public void setGr(String gr) {
        this.gr = gr;
    }

    public String getBo() {
        return bo;
    }

    public void setBo(String bo) {
        this.bo = bo;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getRco() {
        return rco;
    }

    public void setRco(String rco) {
        this.rco = rco;
    }

    public String getHcm() {
        return hcm;
    }

    public void setHcm(String hcm) {
        this.hcm = hcm;
    }

    public String getHwm() {
        return hwm;
    }

    public void setHwm(String hwm) {
        this.hwm = hwm;
    }

    public String getHcam() {
        return hcam;
    }

    public void setHcam(String hcam) {
        this.hcam = hcam;
    }

    public String getMg() {
        return mg;
    }

    public void setMg(String mg) {
        this.mg = mg;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getRm() {
        return rm;
    }

    public void setRm(String rm) {
        this.rm = rm;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getRr() {
        return rr;
    }

    public void setRr(String rr) {
        this.rr = rr;
    }

    public String getHc() {
        return hc;
    }

    public void setHc(String hc) {
        this.hc = hc;
    }

    public String getNpr() {
        return npr;
    }

    public void setNpr(String npr) {
        this.npr = npr;
    }


}
