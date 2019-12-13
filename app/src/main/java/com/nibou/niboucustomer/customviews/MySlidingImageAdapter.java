package com.nibou.niboucustomer.customviews;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.models.ListResponseModel;

import java.util.ArrayList;

public class MySlidingImageAdapter extends PagerAdapter {

    ArrayList<ListResponseModel.Img> imgArrayList;
    private LayoutInflater inflater;
    private Context context;


    public MySlidingImageAdapter(Context context, ArrayList<ListResponseModel.Img> imgArrayList) {
        this.context = context;
        this.imgArrayList = imgArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imgArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.layout_swipe_image, view, false);
        assert imageLayout != null;

        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.zoomableImageView);

        imageView.setOnClickListener(v -> {
            if (context instanceof MyImageViewPagerActivity) {
                ((MyImageViewPagerActivity) context).handleCrossButton();
            }
        });

        final ProgressBar progressBar = (ProgressBar) imageLayout.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        if (imgArrayList.get(position).getImg() != null && !imgArrayList.get(position).getImg().isEmpty()) {
            Glide.with(context).load(imgArrayList.get(position).getImg()).error(R.drawable.empty_image_theme_color_drawable).placeholder(R.drawable.empty_image_theme_color_drawable).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(imageView);
        }
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}