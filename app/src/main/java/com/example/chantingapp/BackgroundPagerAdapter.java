package com.example.chantingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import java.util.List;

public class BackgroundPagerAdapter extends PagerAdapter {
    private Context context;
    private List<Integer> backgroundImages;
    private OnImageClickListener imageClickListener;

    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    public BackgroundPagerAdapter(Context context, List<Integer> backgroundImages, OnImageClickListener listener) {
        this.context = context;
        this.backgroundImages = backgroundImages;
        this.imageClickListener = listener;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE; // Simulate infinite scrolling
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_background, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        // Calculate the actual position
        int actualPosition = position % backgroundImages.size();
        imageView.setImageResource(backgroundImages.get(actualPosition));

        imageView.setOnClickListener(v -> {
            if (imageClickListener != null) {
                imageClickListener.onImageClick(actualPosition);
            }
        });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
