package ir.kitgroup.inskuappb.component;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ir.kitgroup.inskuappb.dataBase.StoreChangeAdvertise;

@RunWith(MockitoJUnitRunner.class)
public class SharedPrefrenceValueTest {

    @Mock
    SharedPreferences sharedPreferences;

    private SharedPrefrenceValue sharedPrefrenceValue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sharedPrefrenceValue = new SharedPrefrenceValue(sharedPreferences);
    }

    @Test
    public void saveValueInSharedPrefrenceTest() {
        // Mock the behavior of SharedPreferences
        when(sharedPreferences.getString(anyString(), anyString())).thenReturn("");
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);

        // Call the method to test
        sharedPrefrenceValue.saveValueInSharedPrefrence("1", true, 5);

        // Verify that the correct methods of SharedPreferences.Editor were called
        verify(editor).putString(anyString(), anyString());
        verify(editor).apply();
    }

    @Test
    public void useDataToSetInListTest() {
        // Create a Gson object for serialization
        Gson gson = new Gson();

        // Create a HashMap representing the stored data
        HashMap<String, ArrayList<StoreChangeAdvertise>> dataMap = new HashMap<>();
        ArrayList<StoreChangeAdvertise> dataList = new ArrayList<>();
        dataList.add(new StoreChangeAdvertise("1", true, 5));
        dataMap.put("simpleChange", dataList);

        // Convert the dataMap to JSON
        String jsonData = gson.toJson(dataMap);

        // Mock the behavior of SharedPreferences
        when(sharedPreferences.getString(anyString(), anyString())).thenReturn(jsonData);

        // Call the method to test
        ArrayList<StoreChangeAdvertise> result = sharedPrefrenceValue.useDataToSetInList(jsonData);

        // Verify that the correct data was returned
        assertEquals(dataList, result);
    }

}
