package ir.kitgroup.inskuappb.data.model;

import androidx.annotation.Keep;

import java.util.List;

import ir.kitgroup.inskuappb.dataBase.Ord;
import ir.kitgroup.inskuappb.dataBase.OrdDetail;

@Keep
public class ClsJsonObject {
    public List<Ord> Order ;
    public List<OrdDetail> OrderDetail ;
    public List<ModelGift> Scenario ;
}
