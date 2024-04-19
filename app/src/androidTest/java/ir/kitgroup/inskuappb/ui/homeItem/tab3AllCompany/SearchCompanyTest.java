package ir.kitgroup.inskuappb.ui.homeItem.tab3AllCompany;

import static junit.framework.TestCase.assertNotNull;

import android.content.SharedPreferences;
import android.view.View;
import androidx.fragment.app.testing.FragmentScenario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.ui.launcher.homeItem.SearchCompany;

@RunWith(MockitoJUnitRunner.class)
public class SearchCompanyTest {

    @Mock
    SharedPreferences mockSharedPreferences;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInitialization() {
        FragmentScenario<SearchCompany> scenario = FragmentScenario.launchInContainer(SearchCompany.class);
        scenario.onFragment(fragment -> {
            // Verify that fragment is not null
            assertNotNull(fragment);



            // Verify that the view is inflated
            assertNotNull(fragment.getView());

            // Verify that the ProgressBar is present
            View progressBar = fragment.requireView().findViewById(R.id.progress);
            assertNotNull(progressBar);
        });
    }

    // Add more test cases as needed to cover specific functionalities of the fragment
}

