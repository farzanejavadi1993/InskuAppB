package ir.kitgroup.inskuappb.model;

public class SliderData {


    public String getI() {
        return I;
    }

    public void setI(String i) {
        I = i;
    }

    private String I;
    private String imgUrl;
    private String name;
    private DType type;

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    private boolean save;
    private int view;

    public String getType() {

        String s1 = type.get1() != null ?
                type.get1() :
                type.get2() != null ?
                        type.get2() :
                        type.get3() != null ?
                                type.get3() :
                                type.get4() != null ?
                                        type.get4() : null;


        if (s1 == null)
            s1="";
        else
            s1="( " +
                s1
                + " )";
        return s1;

    }

    public void setType(DType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }


    // Constructor method.
    public SliderData(String imgUrl, String name, DType type,String I, int view,boolean save) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.type = type;
        this.view = view;
        this.I=I;
        this.save=save;
    }

    // Getter method
    public String getImgUrl() {
        return imgUrl;
    }

    // Setter method
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}