package ir.kitgroup.inskuappb.data.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class Log {

    @SerializedName("TypeClass")
    @Expose
    private String typeClass;
    @SerializedName("Current")
    @Expose
    private String current;
    @SerializedName("Message")//1 Inserted    //2dupliated    //3 error   //4 successFull
    @Expose
    private Integer message;
    @SerializedName("Description")
    @Expose
    private String description;

    public String getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(String typeClass) {
        this.typeClass = typeClass;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
