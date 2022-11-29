package ir.kitgroup.inskuappb.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ir.kitgroup.inskuappb.App;
import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.model.SliderData;
import ir.kitgroup.inskuappb.util.Constant;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {


    public interface ClickItem{
        void onClick1(SliderData sliderData,int position);
    }


    private ClickItem clickItem;
    public void setOnClickListener(ClickItem clickItem){
        this.clickItem=clickItem;
    }
    private final List<SliderData> mSliderItems;
    private int height;
    private Context context;


    public SliderAdapter(Activity context, ArrayList<SliderData> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;

        this.context=context;

        height=(Constant.width /3)*2;
    }


    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        final SliderData sliderItem = mSliderItems.get(position);

        Glide.with(context)
                .load("http://" + Constant.Main_URL_IMAGE + "/GetAdvertisementImage?id=" +
                        sliderItem.getImgUrl() + "&width=" + 600 + "&height=" + 400)
                .error(R.drawable.loading2)
                .placeholder(R.drawable.loading12)
                .centerInside()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(viewHolder.ivSpecial);


        viewHolder.itemView.setOnClickListener(view -> clickItem.onClick1(mSliderItems.get(position),position));
    }

    @Override
    public int getCount() {
        int size=0;
        if (mSliderItems!=null)
            size=mSliderItems.size();
        return size;
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView ivSpecial;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            ivSpecial = itemView.findViewById(R.id.iv_special);
            this.itemView = itemView;
        }
    }
}