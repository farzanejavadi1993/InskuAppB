package ir.kitgroup.inskuappb.component.verify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyMobileNumber implements Verify{
    @Override
    public boolean valid(String mobileNumber) {
        Pattern p = Pattern.compile("(0/9)?[0-9]{11}");
        Matcher m = p.matcher(mobileNumber);
        if (!mobileNumber.equals("") && mobileNumber.length() ==11)
            return (m.find() && m.group().equals(mobileNumber));

        else
            return false;


    }
}
