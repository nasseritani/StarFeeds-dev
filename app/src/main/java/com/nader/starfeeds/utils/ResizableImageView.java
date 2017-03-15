package com.nader.starfeeds.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nader.starfeeds.R;

public class ResizableImageView extends ImageView {
    //default aspect ratios
    final double standardWidth = 1;
    final double standardHeight = 0.85;
    String aspectRatio;

    public ResizableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.aspectRatioText);
        aspectRatio = typedArray.getString(R.styleable.aspectRatioText_aspectRatio);
    }

    @Override
    public void setMinimumHeight(int minHeight) {
        super.setMinimumHeight(minHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        double widthRatio;
        double heightRatio;
        if (aspectRatio != null) {
            String aspectRatios[] = aspectRatio.split(":");
            widthRatio = Double.valueOf(aspectRatios[0]);
            heightRatio = Double.valueOf(aspectRatios[1]);
        } else {
            widthRatio = standardWidth;
            heightRatio = standardHeight;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = (int) (width * (heightRatio / widthRatio));
        setMeasuredDimension(width, height);
    }

}