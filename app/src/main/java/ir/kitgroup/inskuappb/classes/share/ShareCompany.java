package ir.kitgroup.inskuappb.classes.share;

import android.app.Activity;
import android.content.Intent;

public class ShareCompany implements Share {



    @Override
    public void onShare(Activity activity, String N, String D, String Mobile, String link) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String my_string = N + "\n" + D + "\n" + Mobile + "\n" + "مشاهده جزییات و ارسال سفارش در این شرکت ، از طریق نصب اپلیکیشن پخش یاب" + "\n" +
                "" + "\n" +
                link;
        intent.putExtra(Intent.EXTRA_TEXT, my_string);
        activity.startActivity(Intent.createChooser(intent, "اشتراک این برنامه با"));
    }
}
