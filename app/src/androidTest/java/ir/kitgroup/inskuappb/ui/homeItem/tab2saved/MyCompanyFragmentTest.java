package ir.kitgroup.inskuappb.ui.homeItem.tab2saved;

import static junit.framework.TestCase.assertNotNull;

import android.content.SharedPreferences;
import android.view.View;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.ui.launcher.homeItem.tab2Saved.MyCompanyFragment;

@RunWith(MockitoJUnitRunner.class)
public class MyCompanyFragmentTest {

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
        FragmentScenario<MyCompanyFragment> scenario = FragmentScenario.launchInContainer(MyCompanyFragment.class);
        scenario.onFragment(fragment -> {
            // Verify that fragment is not null
            assertNotNull(fragment);


            // Verify that the view is inflated
            assertNotNull(fragment.getView());

            // Verify that the RecyclerView is present
            RecyclerView recyclerView = fragment.requireView().findViewById(R.id.recycler_my_company);
            assertNotNull(recyclerView);

            // Verify that the ProgressBar is present
            View progressBar = fragment.requireView().findViewById(R.id.progress);
            assertNotNull(progressBar);
        });
    }

    // Add more test cases as needed to cover specific functionalities of the fragment
}

