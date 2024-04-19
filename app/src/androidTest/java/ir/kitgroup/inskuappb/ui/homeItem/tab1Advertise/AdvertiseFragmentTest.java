package ir.kitgroup.inskuappb.ui.homeItem.tab1Advertise;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ir.kitgroup.inskuappb.ui.launcher.homeItem.tab1Advertise.AdvertiseFragment;

public class AdvertiseFragmentTest {



    AdvertiseFragment advertiseFragment =new AdvertiseFragment();
    @Test
    public void sampleAndVipAreTrueAndBannerIsFalse_returnFalse() {

        boolean result =advertiseFragment.completeSync(false,true,true);
        assertFalse(result);
    }

    private void vipAndBannerAreTrueAndSampleIsFalse_returnFalse() {
        boolean result =advertiseFragment.completeSync(true,false,true);
        assertFalse(result);
    }

    private void sampleAndBannerAreTrueAndVipIsFalse_returnFalse() {

        boolean result =advertiseFragment.completeSync(true,true,false);
        assertFalse(result);
    }


    private void allOfThemAreFalse_returnFalse() {

        boolean result =advertiseFragment.completeSync(false,false,false);
        assertFalse(result);
    }

    private void allOfThemAreTrue_returnFalse() {
        boolean result =advertiseFragment.completeSync(true,true,true);
        assertTrue(result);
    }

}
