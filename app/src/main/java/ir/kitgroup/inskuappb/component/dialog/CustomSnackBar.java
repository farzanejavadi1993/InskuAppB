package ir.kitgroup.inskuappb.component.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import ir.kitgroup.inskuappb.R;

public class CustomSnackBar  {
     Snackbar snackbar;

    public void showSnack(Activity activity, View v,String message) {
         snackbar = Snackbar.make(v, "", Snackbar.LENGTH_LONG);
        View customSnackView = activity.getLayoutInflater().inflate(R.layout.custom_snackbar_view, null);
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setPadding(0, 0, 0, 0);
        TextView close = customSnackView.findViewById(R.id.close);
        TextView tvError = customSnackView.findViewById(R.id.textView1);
        tvError.setText(message);
        close.setOnClickListener(v1 -> snackbar.dismiss());
        snackbarLayout.addView(customSnackView, 0);
        snackbar.show();

    }

    public void hide(){
        snackbar.dismiss();
    }
}
