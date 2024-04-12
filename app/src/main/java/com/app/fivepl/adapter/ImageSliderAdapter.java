package com.app.fivepl.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {
    private List<String > images;
    Activity activity;

    public ImageSliderAdapter(List<String> images, Activity activity) {
        this.images = images;
        this.activity=activity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        Glide.with(activity).load(images.get(position)).into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
