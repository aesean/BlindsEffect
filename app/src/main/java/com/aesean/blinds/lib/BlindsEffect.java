package com.aesean.blinds.lib;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 百叶窗特效
 * Author: xl
 * Date: 16/4/19
 */
public class BlindsEffect extends EffectView {

    private static final int DEFAULT_COLUMN = 6;

    private int oldImage;
    private int newImage;

    private View mOldView;
    private View mNewView;

    private LinearLayout mContainer;

    private int mColumn = DEFAULT_COLUMN;

    /**
     * 百叶窗特效
     *
     * @param container 特效展示的位置
     * @param oldImage  旧图片
     * @param newImage  新图片
     */
    public BlindsEffect(LinearLayout container, @DrawableRes int oldImage, @DrawableRes int newImage) {
        this.oldImage = oldImage;
        this.newImage = newImage;
        mContainer = container;
    }

    /**
     * 百叶窗特效
     *
     * @param container 特效展示的位置
     * @param oldView   旧View,以View作为切换图片
     * @param newView   新View,以View作为切换图片
     */
    public BlindsEffect(LinearLayout container, View oldView, View newView) {
        this.mOldView = oldView;
        this.mNewView = newView;
        mContainer = container;
    }

    /**
     * 获取叶片数量
     *
     * @return 数量
     */
    public int getColumn() {
        return mColumn;
    }

    /**
     * 设置叶片数量
     *
     * @param column 数量
     */
    public void setColumn(int column) {
        this.mColumn = column;
    }

    @Override
    public void start() {
        mContainer.removeAllViews();
        mContainer.setOrientation(LinearLayout.HORIZONTAL);
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

    /**
     * 将View添加到LinearLayout
     *
     * @param imageView 构建出来的ImageView
     * @param index     imageView在数组里的位置
     * @param type      类型
     */
    @Override
    protected void addToViewGroup(ImageView imageView, int index, int type) {
        mContainer.addView(imageView);
    }

    @Override
    protected ImageView createImageView(Bitmap bitmap) {
        Context context = mContainer.getContext();
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(bitmap);
        // 这里注意设置weight=1,宽度为0
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return imageView;
    }

    @Override
    public Bitmap getBitmap(int type) {
        if (mOldView == null || mNewView == null) {
            Resources resources = mContainer.getContext().getResources();
            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.outWidth = 400;
//            options.outHeight = 200;
//            options.inJustDecodeBounds = false;
            int res = type == TYPE_OLD_VIEW ? oldImage : newImage;
            return BitmapFactory.decodeResource(resources, res, options);
        }
        return type == TYPE_OLD_VIEW ? createBitmapByView(mOldView) : createBitmapByView(mNewView);
    }

    @Override
    protected Bitmap[] cutBitmaps(Bitmap bitmap, int type) {
        Log.e("debug", "裁剪Bitmap" + (type == TYPE_OLD_VIEW ? "旧View" : "新View"));
        // 这里可以对Bitmap做下缩放裁剪
        int partX = bitmap.getWidth() / mColumn;
        int height = bitmap.getHeight();
        Bitmap[] bitmaps = new Bitmap[mColumn];
        for (int i = 0; i < mColumn; i++) {
            bitmaps[i] = Bitmap.createBitmap(bitmap, partX * i, 0, partX, height);
        }
        Log.e("debug", "View宽度=" + partX);
        return bitmaps;
    }

    @Override
    protected Animator setAnimator(View view, int index, int type) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotationY",
                type == TYPE_OLD_VIEW ? 0 : -90, type == TYPE_OLD_VIEW ? 90 : 0);
        objectAnimator.setDuration(800);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        return objectAnimator;
    }
}
