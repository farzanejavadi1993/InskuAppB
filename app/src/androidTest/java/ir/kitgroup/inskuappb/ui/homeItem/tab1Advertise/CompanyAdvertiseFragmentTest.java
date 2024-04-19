package ir.kitgroup.inskuappb.ui.homeItem.tab1Advertise;

import static junit.framework.TestCase.assertNotNull;

import android.content.SharedPreferences;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.ui.launcher.homeItem.tab1Advertise.CompanyAdvertiseFragment;

@RunWith(MockitoJUnitRunner.class)
public class CompanyAdvertiseFragmentTest {

    @Mock
    SharedPreferences mockSharedPreferences;

    @Mock
    NavController mockNavController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitialization() {
        FragmentScenario<CompanyAdvertiseFragment> scenario = FragmentScenario.launchInContainer(CompanyAdvertiseFragment.class);
        scenario.onFragment(fragment -> {
            // Verify that fragment is not null
            assertNotNull(fragment);



            // Verify that the view is inflated
            assertNotNull(fragment.getView());

            // Verify that the RecyclerView is present
            RecyclerView recyclerView = fragment.requireView().findViewById(R.id.recyclerCompanyAdvertise);
            assertNotNull(recyclerView);

            // Verify that the ProgressBar is present
            View progressBar = fragment.requireView().findViewById(R.id.progressBar22);
            assertNotNull(progressBar);
        });
    }

    // Add more test cases as needed to cover specific functionalities of the fragment
}
