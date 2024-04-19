package ir.kitgroup.inskuappb.ui.viewmodel;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import ir.kitgroup.inskuappb.component.AccountFilter;
import ir.kitgroup.inskuappb.component.NetWorkHelper1;
import ir.kitgroup.inskuappb.data.model.Message;
import ir.kitgroup.inskuappb.dataBase.BusinessR;
import ir.kitgroup.inskuappb.repository.mainrepository.MainRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;

public class MainViewModelTest {

    private MainViewModel mainViewModel;

    @Mock
    private MainRepository mainRepository;

    @Mock
    private NetWorkHelper1 networkHelper;

    @Mock
    private SharedPreferences sharedPreferences;
    @Mock
    private MutableLiveData<List<BusinessR>> resultBusinessRsTest;

    @Mock
    private MutableLiveData<Message> eMessageTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        // Setup RxJavaSchedulers
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());

        mainViewModel = new MainViewModel(mainRepository, sharedPreferences, networkHelper);


        mainViewModel.resultBusinessRs = resultBusinessRsTest;
        mainViewModel.eMessage = eMessageTest;
    }

    @After
    public void tearDown() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    @Test
    public void testGetBusinessRelations() {
        // Mock network connectivity
        when(networkHelper.isNetworkConnected1()).thenReturn(true);

        // Mock data
        List<BusinessR> mockBusinessRs = new ArrayList<>();
        mockBusinessRs.add(new BusinessR());

        // Mock repository response
        when(mainRepository.getBusinessRelations(any(AccountFilter.class))).thenReturn(Observable.just(mockBusinessRs));

        // Call method
        mainViewModel.getBusinessRelations(new AccountFilter());

        // Verify repository method is called
        verify(mainRepository).getBusinessRelations(any(AccountFilter.class));

        // Verify the result is set correctly
        verify(resultBusinessRsTest).setValue(mockBusinessRs);
    }
}
