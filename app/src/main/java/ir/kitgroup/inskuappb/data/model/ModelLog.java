package ir.kitgroup.inskuappb.data.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
@Keep
public class ModelLog {
    @SerializedName("Log")
    @Expose
    private List<Log> logs = null;
    public List<Log> getLogs() {
        return logs;
    }

}
