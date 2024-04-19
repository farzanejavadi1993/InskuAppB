package ir.kitgroup.inskuappb.component.verify;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VerifyMobileNumberTest {

    Pattern p;

    @Before
    public  void setUp(){
         p = Pattern.compile("(0/9)?[0-9]{11}");
    }
    @Test
    public void emptyMobileNumber_IsNotValid(){
        String mobileNumber ="";
        Matcher m = p.matcher(mobileNumber);
        assertFalse(m.find() && m.group().equals(mobileNumber));
    }

    @Test
    public void MobileNumberWithLessThan11digit_IsNotValid(){
        String mobileNumber="0915046916" ;
        Matcher m = p.matcher(mobileNumber);
        assertFalse(m.find() && m.group().equals(mobileNumber));
    }

    @Test
    public void MobileNumberWithJust11Digit_IsValid(){
        String mobileNumber="09150469164" ;
        Matcher m = p.matcher(mobileNumber);
        assertTrue(m.find() && m.group().equals(mobileNumber));

    }
}
