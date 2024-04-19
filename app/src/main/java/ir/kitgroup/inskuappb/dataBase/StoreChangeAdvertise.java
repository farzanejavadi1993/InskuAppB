package ir.kitgroup.inskuappb.dataBase;

import com.orm.SugarRecord;

public class StoreChangeAdvertise  extends SugarRecord {
    private String I;
    private int count;
    private boolean save;

  public   StoreChangeAdvertise(){}
    public StoreChangeAdvertise(String i,boolean save, int cont){
        this.I=i;
        this.save=save;
        this.count=cont;
    }

    public String getI() {
        return I;
    }

    public void setI(String i) {
        I = i;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }
}
