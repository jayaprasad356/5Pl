package com.app.fivepl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.app.fivepl.R;
import com.bumptech.glide.Glide;

public class WelcomePagerAdapter extends PagerAdapter {

    private Context context;

    private int[] images = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3
    };

    private int[] headings = {
            R.string.heading_one,
            R.string.heading_two,
            R.string.heading_three
    };

    private int[] descriptions = {
            R.string.desc_one,
            R.string.desc_two,
            R.string.desc_three
    };

    public WelcomePagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView slideTitleImage = view.findViewById(R.id.titleImage);
        TextView slideHeading = view.findViewById(R.id.texttitle);
        TextView slideDescription = view.findViewById(R.id.textdeccription);


        Glide.with(context).load(images[position]).placeholder(R.drawable.sample_agri).into(slideTitleImage);

//
//        // Using Glide to load images efficiently
//        Glide.with(context)
//                .load(images[position])
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.logo) // Placeholder image while loading
//                      //  .error(R.drawable.error) // Error image if loading fails
//                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Caching
//                        .override(800, 600) // Resizing the image
//                )
//                .into(slideTitleImage);

        slideHeading.setText(headings[position]);
        slideDescription.setText(descriptions[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
