package ir.kitgroup.inskuappb.dataBase;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

@Keep
public class ModelCatalog extends SugarRecord {

    @SerializedName("I")
    @Expose
    private String i;
    @SerializedName("N")
    @Expose
    private String n;
    @SerializedName("DO")
    @Expose
    private String _do;
    @SerializedName("TY")
    @Expose
    private String ty;
    @SerializedName("D1")
    @Expose
    private String d1;
    @SerializedName("ID1")
    @Expose
    private String id1;
    @SerializedName("AD1")
    @Expose
    private String ad1;
    @SerializedName("D2")
    @Expose
    private String d2;
    @SerializedName("ID2")
    @Expose
    private String id2;
    @SerializedName("AD2")
    @Expose
    private String ad2;
    @SerializedName("D3")
    @Expose
    private String d3;
    @SerializedName("ID3")
    @Expose
    private String id3;
    @SerializedName("AD3")
    @Expose
    private String ad3;
    @SerializedName("ISP")
    @Expose
    private String isp;
    @SerializedName("ASP")
    @Expose
    private String asp;
    @SerializedName("DP")
    @Expose
    private String dp;
    @SerializedName("IP")
    @Expose
    private String ip;
    @SerializedName("AP")
    @Expose
    private String ap;
    @SerializedName("SM")
    @Expose
    private String sm;
    @SerializedName("PT")
    @Expose
    private String pt;
    @SerializedName("CI")
    @Expose
    private String ci;
    @SerializedName("RE")
    @Expose
    private String re;
    @SerializedName("GU")
    @Expose
    private String gu;
    @SerializedName("LI")
    @Expose
    private String li;
    @SerializedName("LN")
    @Expose
    private String ln;
    @SerializedName("STS")
    @Expose
    private String sts;

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



    public void setDo(String _do) {
        this._do = _do;
    }

    public String getTy() {
        return ty;
    }

    public void setTy(String ty) {
        this.ty = ty;
    }

    public String getD1() {
        return d1;
    }

    public void setD1(String d1) {
        this.d1 = d1;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getAd1() {
        return ad1;
    }

    public void setAd1(String ad1) {
        this.ad1 = ad1;
    }

    public String getD2() {
        return d2;
    }

    public void setD2(String d2) {
        this.d2 = d2;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getAd2() {
        return ad2;
    }

    public void setAd2(String ad2) {
        this.ad2 = ad2;
    }

    public String getD3() {
        return d3;
    }

    public void setD3(String d3) {
        this.d3 = d3;
    }

    public String getId3() {
        return id3;
    }

    public void setId3(String id3) {
        this.id3 = id3;
    }

    public String getAd3() {
        return ad3;
    }

    public void setAd3(String ad3) {
        this.ad3 = ad3;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getAsp() {
        return asp;
    }

    public void setAsp(String asp) {
        this.asp = asp;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAp() {
        return ap;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }

    public String getSm() {
        return sm;
    }

    public void setSm(String sm) {
        this.sm = sm;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getRe() {
        return re;
    }

    public void setRe(String re) {
        this.re = re;
    }

    public String getGu() {
        return gu;
    }

    public void setGu(String gu) {
        this.gu = gu;
    }

    public String getLi() {
        return li;
    }

    public void setLi(String li) {
        this.li = li;
    }

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }
    public float getMaxSumPrice(){
        float max=0;
        if (asp!=null && asp.contains("/")){
            max=Float.parseFloat(asp.replace("/", "."));
        }else {
            max=Float.parseFloat(asp);
        }
        return max;
    }



    public float getMinSumPrice(){
        float min=0;
        if (isp!=null && isp.contains("/")){
            min=Float.parseFloat(isp.replace("/", "."));
        }else {
            min=Float.parseFloat(isp);
        }
        return min;
    }


    public boolean getDo(){
        if (_do.equals("0")){
            return false;
        }else {
            return true;
        }
    }

    public Float getMaxDiscountPercent1(){
        float max1=0;
        if (ad1!=null && ad1.contains("/")){
            max1=Float.parseFloat(ad1.replace("/", "."));
        }else {
            max1=Float.parseFloat(ad1);
        }
        return max1;

    }



    public Float getMinDayPayment(){
        float min=0;
        if (ip!=null && ip.contains("/")){
            min=Float.parseFloat(ip.replace("/", "."));
        }else {
            min=Float.parseFloat(ip);
        }
        return min;


    }
    public Float getMaxDayPayment(){
        float max=0;
        if (ap!=null && ap.contains("/")){
            max=Float.parseFloat(ap.replace("/", "."));
        }else {
            max=Float.parseFloat(ap);
        }
        return max;


    }
    public Float getDefaultPayment(){
        float def=0;
        if (dp!=null && dp.contains("/")){
            def=Float.parseFloat(dp.replace("/", "."));
        }else {
            def=Float.parseFloat(dp);
        }
        return def;



    }
}
