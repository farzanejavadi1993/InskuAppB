package ir.kitgroup.inskuappb.component;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NetWorkHelper1Test {

    @Mock
    Context mockContext;

    @Mock
    ConnectivityManager mockConnectivityManager;

    @Mock
    NetworkInfo mockMobileNetworkInfo;

    @Mock
    NetworkInfo mockWifiNetworkInfo;

    private NetWorkHelper1 netWorkHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE))
                .thenReturn(mockConnectivityManager);
        netWorkHelper = new NetWorkHelper1(mockContext);
    }

    @Test
    public void testIsNetworkConnected1_WithMobileConnected() {
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE))
                .thenReturn(mockMobileNetworkInfo);
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI))
                .thenReturn(mockWifiNetworkInfo);

        when(mockMobileNetworkInfo.getState()).thenReturn(NetworkInfo.State.CONNECTED);
        when(mockWifiNetworkInfo.getState()).thenReturn(NetworkInfo.State.DISCONNECTED);

        assertTrue(netWorkHelper.isNetworkConnected1());
    }

    @Test
    public void testIsNetworkConnected1_WithWifiConnected() {
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE))
                .thenReturn(mockMobileNetworkInfo);
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI))
                .thenReturn(mockWifiNetworkInfo);

        when(mockMobileNetworkInfo.getState()).thenReturn(NetworkInfo.State.DISCONNECTED);
        when(mockWifiNetworkInfo.getState()).thenReturn(NetworkInfo.State.CONNECTED);

        assertTrue(netWorkHelper.isNetworkConnected1());
    }

    @Test
    public void testIsNetworkConnected1_WithNoNetworkConnected() {
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE))
                .thenReturn(mockMobileNetworkInfo);
        when(mockConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI))
                .thenReturn(mockWifiNetworkInfo);

        when(mockMobileNetworkInfo.getState()).thenReturn(NetworkInfo.State.DISCONNECTED);
        when(mockWifiNetworkInfo.getState()).thenReturn(NetworkInfo.State.DISCONNECTED);

        assertFalse(netWorkHelper.isNetworkConnected1());
    }
}

