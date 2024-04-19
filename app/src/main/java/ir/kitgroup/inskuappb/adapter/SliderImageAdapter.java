package ir.kitgroup.inskuappb.adapter;



import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.component.SliderImage;

public class SliderImageAdapter extends SliderViewAdapter<SliderImageAdapter.SliderAdapterViewHolder> {

    private final List<SliderImage> mSliderItems;
    private int height;
    private Context context;


    public SliderImageAdapter(Activity context, ArrayList<SliderImage> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        height = dm.widthPixels;
        this.context=context;
    }


    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout_image, null);
        return new SliderAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        final SliderImage sliderItem = mSliderItems.get(position);

        Glide.with(context)
                .load(sliderItem.getImgUrl())
                .error(R.drawable.loading2)
                .placeholder(R.drawable.loading12)
                .centerInside()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(viewHolder.ivCompany);


    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        ImageView ivCompany;
        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            ivCompany = itemView.findViewById(R.id.iv_company);}
    }
}
