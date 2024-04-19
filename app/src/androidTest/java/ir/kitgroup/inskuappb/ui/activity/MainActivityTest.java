package ir.kitgroup.inskuappb.ui.activity;

import android.content.SharedPreferences;
import android.os.PowerManager;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import ir.kitgroup.inskuappb.ui.activities.MainActivity;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest {

    @Mock
    NavController mockNavController;

    @Mock
    NavHostFragment mockNavHostFragment;

    @Mock
    FragmentManager mockFragmentManager;

    @Mock
    NavDestination mockNavDestination;

    @Mock
    SharedPreferences mockSharedPreferences;

    @Mock
    PowerManager mockPowerManager;

    @Mock
    MainActivity mockActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockActivity.getSystemService(MainActivity.POWER_SERVICE)).thenReturn(mockPowerManager);
        when(mockActivity.getSharedPreferences(anyString(), anyInt())).thenReturn(mockSharedPreferences);
        when(mockActivity.getSupportFragmentManager()).thenReturn(mockFragmentManager);
        when(mockFragmentManager.getPrimaryNavigationFragment()).thenReturn(mockNavHostFragment);
        when(mockNavHostFragment.getChildFragmentManager()).thenReturn(mockFragmentManager);

    }

    @Test
    public void testBackPressed() {
        when(mockNavDestination.getDisplayName()).thenReturn("LoginFragment");
        when(mockNavController.getBackQueue().get(1).getDestination()).thenReturn(mockNavDestination);
        mockActivity.onBackPressed();
        verify(mockNavController, times(1)).popBackStack();
    }

    @Test
    public void testExitApp() {
        mockActivity.onBackPressed();
        verify(mockActivity, times(1)).finishApp();
    }

    @Test
    public void testPowerManagement() {
        when(mockPowerManager.isScreenOn()).thenReturn(true);
        when(mockSharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(true);
        mockActivity.onPause();
        verify(mockActivity, times(1)).finish();
    }


}
