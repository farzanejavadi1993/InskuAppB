package ir.kitgroup.inskuappb.component;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import ir.kitgroup.inskuappb.R;

@RunWith(AndroidJUnit4.class)
public class CreateCityDialogTest {

    @Test
    public void testShowDialog() {
        Context context = ApplicationProvider.getApplicationContext();
        CreateCityDialog createCityDialog = CreateCityDialog.getInstance();

        // Mock the interface click listeners
        CreateCityDialog.ClickPositiveButton mockPositiveButton = mock(CreateCityDialog.ClickPositiveButton.class);
        CreateCityDialog.ClickNegativeButton mockNegativeButton = mock(CreateCityDialog.ClickNegativeButton.class);

        // Set the mock listeners
        createCityDialog.setOnClickPositiveButton(mockPositiveButton);
        createCityDialog.setOnClickNegativeButton(mockNegativeButton);

        // Call the showDialog method
        createCityDialog.showDialog(context, "Test City", "Test message", false);

        // Verify that the dialog is not null
        Dialog dialog = createCityDialog.mDialog;
        assertNotNull(dialog);

        // Verify that the dialog is showing
        assertTrue(dialog.isShowing());

        // Verify the content of the dialog
        TextView txtMessage = dialog.findViewById(R.id.txtMessage);
        EditText edtAddCity = dialog.findViewById(R.id.edtAddCity);
        RelativeLayout rlOk = dialog.findViewById(R.id.rl_ok);
        RelativeLayout rlCancel = dialog.findViewById(R.id.rl_cancel);

        assertNotNull(txtMessage);
        assertEquals("Test message", txtMessage.getText().toString());

        assertNotNull(edtAddCity);
        assertEquals("Test City", edtAddCity.getText().toString());

        assertNotNull(rlOk);
        rlOk.performClick();
        // Verify that the positive button click listener is called
        verify(mockPositiveButton).onClick("Test City");

        assertNotNull(rlCancel);
        rlCancel.performClick();
        // Verify that the negative button click listener is called
        verify(mockNegativeButton).onClick();

        // Dismiss the dialog
        createCityDialog.hideProgress();
        assertFalse(dialog.isShowing());
    }
}

