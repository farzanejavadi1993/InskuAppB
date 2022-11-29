package ir.kitgroup.inskuappb.classes.pdf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class PdfTools {
    private static final String GOOGLE_DRIVE_PDF_READER_PREFIX = "http://drive.google.com/viewer?url=";
    private static final String HTML_MIME_TYPE = "text/html";

    public void showPDFUrl(final Context context, final String pdfUrl,SharedPreferences sharedPreferences) {
        openPDFThroughGoogleDrive(context, pdfUrl,sharedPreferences);
    }

    public void openPDFThroughGoogleDrive(final Context context, final String pdfUrl, final SharedPreferences sharedPreferences) {
        sharedPreferences.edit().putBoolean("loginSuccess", false).apply();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse(GOOGLE_DRIVE_PDF_READER_PREFIX+pdfUrl), HTML_MIME_TYPE);
        context.startActivity(i);
    }
}
