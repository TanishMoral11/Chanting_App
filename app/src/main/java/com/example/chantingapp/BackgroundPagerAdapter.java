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

    // Context to access application resources
    private Context context;

    // List of background images (resource IDs)
    private List<Integer> backgroundImages;

    // Listener interface for handling image clicks
    private OnImageClickListener imageClickListener;

    // Interface definition for a callback to be invoked when an image is clicked
    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    // Constructor to initialize context, background images, and the click listener
    public BackgroundPagerAdapter(Context context, List<Integer> backgroundImages, OnImageClickListener listener) {
        this.context = context;
        this.backgroundImages = backgroundImages;
        this.imageClickListener = listener;
    }

    // Return the count of items, simulating infinite scrolling
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    // Determine whether a page View is associated with a specific key object
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    // Create and return a page view for the given position
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // Inflate the layout for each page
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_background, container, false);

        // Find the ImageView in the inflated layout
        ImageView imageView = itemView.findViewById(R.id.imageView);

        // Calculate the actual position of the image in the list
        int actualPosition = position % backgroundImages.size();
        // Set the image resource for the ImageView
        imageView.setImageResource(backgroundImages.get(actualPosition));

        // Set a click listener on the ImageView to handle image clicks
        imageView.setOnClickListener(v -> {
            if (imageClickListener != null) {
                imageClickListener.onImageClick(actualPosition);
            }
        });

        // Add the page view to the container
        container.addView(itemView);
        return itemView;
    }

    // Remove a page view from the container
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
