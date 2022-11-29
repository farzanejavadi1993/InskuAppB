package ir.kitgroup.inskuappb.ui.logins;

import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import ir.kitgroup.inskuappb.databinding.RuleFragmentBinding;

public class RuleFragment extends Fragment {
    private RuleFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RuleFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String p = "۱_تعاریف واصطلاحات" +
                "\n" +
                " ۱.۱ پخش یاب : سیستم نرم افزاری جستجوی شرکت های توزیع پخش و اعلام طرح های فروش برای فروشگاه ها"
                + "\n" +
                "۲.۱اپلیکیشن : نرم افزار و برنامه متعلق به شرکت کوروش درستکاران پارس ( کیت گروپ )  است که به منظور استفاده از خدمات جستجو شرکت های پخش و طرح های فروش برای فروشگاه ها  طراحی شده است." +
                "\n" +
                " ۳.۱کاربر اپلیکیشن : یک فروشگاه در هر نقطه از کشور و هر نوع صنف می باشد که برنامه را نصب کرده تا از اطلاعات برنامه جهت مدیریت خرید و اطلاع رسانی استفاده کند." +
                "\n" +
                "3.2 کاربر پرتال : شرکت های وارد کننده – توزیع کننده و یا تولید کننده هستند که در پنل ثبت نام کرده و اطلاعات کامل شرکت خود را ثبت میکنند ."
                + "\n" +
                "۴.۱حساب کاربری: حسابی است که اشخاص برای استفاده از خدمات اپلیکیشن پخش یاب ایجاد میکند."
                + "\n" +
                "۲_حساب کاربری"
                + "\n" +
                "۱.۲کاربران در اپلیکیشن "
                + "\n" +
                "2.2 کاربران در پرتال  پخش یاب قوانین و مقررات را پذیرفته و با آگاهی از آن استفاده میکنند."
                + "\n" +
                "۲.۲ برای استفاده از خدمات پخش یاب لازم است کاربران اعم از حقیقی و حقوقی یک حساب کاربری در اپلیکیشن ایجاد کنند"
                + "\n" +
                "۳.۲حساب کاربری شامل نام ،شماره همراه ، کاربر می باشد."
                + "\n" +
                " ۴.۲مسئولیت همه فعالیت هایی که از طریق حساب کاربری انجام میشود و بعهده کاربر مربوطه می باشد."
                + "\n" +
                "3_هزینه ها و پرداخت"
                + "\n" +
                " 1.3 هزینه سفارش از طریق پرتال شرکت های توزیع پخش برای درج طرح ها در پخش یاب پرداخت می شود."
                + "\n" +
                "2.3 پرداخت هزینه سفارش فقط از طریق روش هایی که توسط پرتال پخش یاب " +
                "مشخص میشود امکان پذیر می باشد این روش ها شامل پرداخت از طریق کارت بانکی بصورت انلاین ، پرداخت نقدی درمحل تحویل سفارش واز طریق اعتبار حساب می باشد." +
                " 3.3کاربر می پذیرد که لغو سفارش فقط تا قبل تایید شدن سفارش از طرف پرتال پخش یاب بصورت سیستمی امکان پذیراست ویا از طریق تماس تلفنی با شماره 09153102690 مقدوراست."
                + "\n" +
                "3.4 کاربر می تواند وضعیت اخرین طرح ها را از بخش طرح ها مشاهده کند و یا پیام های ارسالی از شرکت های پخش را مشاهده نماید. "
                + "\n" +

                "4_ مسولیت های پخش یاب "
                + "\n" +
                " ۱.۴ پخش یاب موظف است از روش هایی که برای پشتیبانی تعیین کرده در دسترس کاربران باشد ونسبت به رسیدگی شکایت و نارضایتی تلاش کند."
                + "\n" +
                " ۲.۴ تمام اطلاعات کاربر بصورت محرمانه محافظت شده و دسترسی به ان توسط سایرین ممنوع می باشد."
                + "\n" +
                "۵ قطع خدمات"
                + "\n" +
                "درصورتیکه شرکت های توزیع و پخش به هر نحوی از سیستم جهت درج اگهی های نامتعارف و خلاف قوانین استفاده کند پخش یاب حساب کاربری را بلاک خواهد کرد.";


        binding.tvRule.setText(p);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.tvRule.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        }

        binding.ivBack.setOnClickListener(view1 -> Navigation.findNavController(binding.getRoot()).popBackStack());
    }
}
