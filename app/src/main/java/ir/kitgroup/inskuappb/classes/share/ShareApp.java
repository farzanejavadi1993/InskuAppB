package ir.kitgroup.inskuappb.classes.share;

import android.app.Activity;
import android.content.Intent;

public class ShareApp implements Share{
    @Override
    public void onShare(Activity activity, String N, String D, String Mobile, String link) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String my_string = "سلام "+ N +" "+ D +" شما را به اپلیکیشن پخش یاب دعوت کرده است.از طریق لینک زیر برنامه را دانلود کنید. "+"\n"+
                link;
        intent.putExtra(Intent.EXTRA_TEXT, my_string);
        activity.startActivity(Intent.createChooser(intent, "اشتراک این برنامه با"));
    }
}
