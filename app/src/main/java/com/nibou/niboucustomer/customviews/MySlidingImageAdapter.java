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
import com.nibou.niboucustomer.R;

import java.util.List;

public class MySlidingImageAdapter extends PagerAdapter {

    List<String> mainuserPhotoList;
    private LayoutInflater inflater;
    private Context context;


    public MySlidingImageAdapter(Context context, List<String> mainuserPhotoList) {
        this.context = context;
        this.mainuserPhotoList = mainuserPhotoList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mainuserPhotoList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.layout_swipe_image, view, false);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.zoomableImageView);

        final ProgressBar progressBar = (ProgressBar) imageLayout.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        if (mainuserPhotoList.get(position) != null) {
            if (mainuserPhotoList.get(position) != null) {
                Glide.with(context).load(mainuserPhotoList.get(position))
                        .error(R.drawable.image_placeholder).into(imageView);
                progressBar.setVisibility(View.GONE);
            }
        } else {
            imageView.setImageResource(R.drawable.default_placeholder);
            progressBar.setVisibility(View.GONE);
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