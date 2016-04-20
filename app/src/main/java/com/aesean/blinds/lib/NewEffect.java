package com.aesean.blinds.lib;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.aesean.blinds.R;

/**
 * Author: xl
 * Date: 16/4/20
 */
public class NewEffect extends EffectView {
    private int mOldImage;
    private int mNewImage;

    private View mOldView;
    private View mNewView;

    private GridLayout mContainer;

    private static final int DEFAULT_COLUMN_COUNT = 6;
    private static final int DEFAULT_ROW_COUNT = 6;

    private int mColumnCount = DEFAULT_COLUMN_COUNT;
    private int mRowCount = DEFAULT_ROW_COUNT;

    public NewEffect(GridLayout container, @DrawableRes int oldImage, @DrawableRes int newImage) {
        this.mContainer = container;
        this.mOldImage = oldImage;
        this.mNewImage = newImage;
        setContainer(DEFAULT_COLUMN_COUNT, DEFAULT_ROW_COUNT);
    }

    public NewEffect(GridLayout container, View oldView, View newView) {
        this.mContainer = container;
        this.mOldView = oldView;
        this.mNewView = newView;
        setContainer(DEFAULT_COLUMN_COUNT, DEFAULT_ROW_COUNT);
    }

    public void setColumnCount(int columnCount) {
        this.mColumnCount = columnCount;
        mContainer.setColumnCount(mColumnCount);
    }

    public void setRowCount(int rowCount) {
        this.mRowCount = rowCount;
        mContainer.setRowCount(mRowCount);
    }

    private void setContainer(int columnCount, int rowCount) {
        setColumnCount(columnCount);
        setRowCount(rowCount);
    }

    @Override
    public void start() {
        mContainer.removeAllViews();
        mContainer.setBackgroundColor(Color.argb(0, 0, 0, 0));
        super.start();
    }

    @Override
    protected void animatorFinish(int type) {
        super.animatorFinish(type);
        // 如果是旧的View,则移除所有View
        if (type == TYPE_OLD_VIEW) {
            mContainer.removeAllViews();
        }
    }

    @Override
    protected void addToViewGroup(ImageView imageView, int index, int type) {
//        GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(index, GridLayout.UNDEFINED, 1),
//                GridLayout.spec(index, GridLayout.UNDEFINED, 1));
        mContainer.addView(imageView);
    }

    @Override
    protected ImageView createImageView(Bitmap bitmap, int type) {
        Context context = mContainer.getContext();
//        View view = LayoutInflater.from(context).inflate(R.layout.grid_image_view, mContainer, false);
        ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.grid_image_view, mContainer, false);
        imageView.setImageBitmap(bitmap);
        // 这里注意设置weight=1,宽度为0,设置ScaleType为FitXY来修正显示问题
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (type == TYPE_NEW_VIEW) {
            imageView.setVisibility(View.INVISIBLE);
        }
        return imageView;
    }

    @Override
    public Bitmap getBitmap(int type) {
        if (mOldView == null || mNewView == null) {
            Resources resources = mContainer.getContext().getResources();
            BitmapFactory.Options options = new BitmapFactory.Options();
            int res = type == TYPE_OLD_VIEW ? mOldImage : mNewImage;
            return BitmapFactory.decodeResource(resources, res, options);
        }
        return type == TYPE_OLD_VIEW ? createBitmapByView(mOldView) : createBitmapByView(mNewView);
    }

    @Override
    protected Bitmap[] cutBitmaps(Bitmap bitmap, int type) {
        Log.e("debug", "裁剪Bitmap" + (type == TYPE_OLD_VIEW ? "旧View" : "新View"));
        // 这里可以对Bitmap做下缩放裁剪,这里如果bitmap大小不是partX整数倍的话,这里会出现裁剪不完整的问题
        // 这里如果计算差值太麻烦了,其实也没必要,效果也不好,所以直接在getImageView的时候设置ImageView的scaleType为fitXY
        // 来修正显示问题,这样图像会有非常微小的拉伸.
        int partX = bitmap.getWidth() / mColumnCount;
        // X方向的差值
        int patchX = bitmap.getWidth() - partX * mColumnCount;
        Log.e("debug", "patchX=" + patchX);
        int partY = bitmap.getHeight() / mRowCount;
        // Y方向的差值
        int patchY = bitmap.getHeight() - partY * mRowCount;
        Log.e("debug", "patchY=" + patchY);
        int sum = mColumnCount * mRowCount;
        Bitmap[] bitmaps = new Bitmap[sum];
        for (int i = 0; i < mRowCount; i++) {
            for (int j = 0; j < mColumnCount; j++) {
                bitmaps[i * mColumnCount + j] = Bitmap.createBitmap(bitmap, partX * j,
                        partY * i, partX, partY);
            }
        }
        Log.e("debug", "View宽度=" + partX);
        return bitmaps;
    }

    @Override
    protected Animator setAnimator(View view, int index, int type) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotationX",
                type == TYPE_OLD_VIEW ? 0 : -90, type == TYPE_OLD_VIEW ? 90 : 0);
        objectAnimator.setDuration(1600);
        int[] convert = convert(index);
        objectAnimator.setStartDelay(convert[0] * 100 + convert[1] * 120);
        if (type == TYPE_OLD_VIEW) {
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        } else {
            objectAnimator.setInterpolator(new DecelerateInterpolator());
        }
        return objectAnimator;
    }

    private int[] convert(int index) {
        int[] result = new int[2];
        result[0] = index / mColumnCount;
        result[1] = index % mRowCount;

        return result;
    }
}
