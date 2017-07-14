package com.example.android.popularmovies.displayUtilities;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;



public class MoviesImageView extends ImageView {
    public MoviesImageView(Context context) {
        super(context);
    }

    public MoviesImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoviesImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }
}
