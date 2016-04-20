package com.aesean.blinds.lib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.widget.ImageView;

/**
 * 将所有从一个Bitmap切换成另一个Bitmap,同事在切换过程中需要裁剪Bitmap来添加一些特殊动画的情况,抽象了一个特效类.
 * <p/>
 * Bitmap可以通过View来生成,提供了一个通过View来生成Bitmap的方法{@link #createBitmapByView(View)}.
 * 这样的话,继承实现这个类就可以达到多种不同的切换特效了.
 * 整体没有很难,思路很简单.就是通过旧View和新View拿到两个Bitmap,然后对两个Bitmap进行裁剪,
 * 然后创建ImageView,然后添加到指定的容器,然后通过属性动画完成切换效果
 * Author: xl
 * Date: 16/4/19
 */
@SuppressWarnings("unused")
public abstract class EffectView {

    public static final int TYPE_OLD_VIEW = 0;
    public static final int TYPE_NEW_VIEW = 1;

    /**
     * 开车了
     */
    public void start() {
        final Bitmap[] oldBitmaps = cutBitmaps(getBitmap(TYPE_OLD_VIEW), TYPE_OLD_VIEW);
        final Bitmap[] newBitmaps = cutBitmaps(getBitmap(TYPE_NEW_VIEW), TYPE_NEW_VIEW);

        for (int i = 0; i < oldBitmaps.length; i++) {
            final int j = i;
            final ImageView imageView = createImageView(oldBitmaps[i], TYPE_OLD_VIEW);
            addToViewGroup(imageView, i, TYPE_OLD_VIEW);
            Animator animator = setAnimator(imageView, i, TYPE_OLD_VIEW);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (j == oldBitmaps.length - 1) {
                        animatorFinish(TYPE_OLD_VIEW);
                        animatorEnd(newBitmaps);
                    }
                }
            });
            animator.start();
        }
    }

    /**
     * 动画结束回调
     *
     * @param type 类型
     */
    protected void animatorFinish(int type) {

    }

    /**
     * 第一个动画结束,开始第二个动画
     *
     * @param bitmaps bitmaps
     */
    protected void animatorEnd(final Bitmap[] bitmaps) {
        for (int i = 0; i < bitmaps.length; i++) {
            final int j = i;
            // 这里如果newBitmap的length和oldBitmaps不一致的时候会有问题
            final ImageView newImageView = createImageView(bitmaps[i], TYPE_NEW_VIEW);
            addToViewGroup(newImageView, i, TYPE_NEW_VIEW);
            Animator animator = setAnimator(newImageView, i, TYPE_NEW_VIEW);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    newImageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (j == bitmaps.length - 1) {
                        animatorFinish(TYPE_NEW_VIEW);
                    }
                }

            });
            animator.start();
        }
    }

    /**
     * 将构建出来的{@link View}添加到容器中,这里添加到不同的{@link android.view.ViewGroup}可以有不同的效果.
     * 最简单的添加到{@link android.widget.LinearLayout}中可以实现百叶窗效果.
     * 这里具体添加到什么样的容器里根据需要自己实现.
     *
     * @param imageView 构建出来的ImageView
     */

    protected abstract void addToViewGroup(ImageView imageView, int index, int type);

    /**
     * 通过Bitmap创建ImageView,这个方法写成抽象主要是当前类没有Context
     *
     * @param bitmap bitmap
     * @return ImageView
     */
    protected abstract ImageView createImageView(Bitmap bitmap, int type);

    /**
     * 通过View创建Bitmap
     *
     * @param view View
     * @return 返回创建的Bitmap
     */
    protected Bitmap createBitmapByView(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        // 注意Config如果设置的不带有Alpha类型,那背景无法透明
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        if (bitmap != null) {
            // 将view绘制到canvas上,拿到bitmap
            Canvas canvas = new Canvas();
            canvas.setBitmap(bitmap);
            view.draw(canvas);
            canvas.setBitmap(null);
        }
        return bitmap;
    }

    /**
     * 得到Bitmap
     * 参考{@link #createBitmapByView(View)}
     *
     * @param type View类型,是新View还是旧View
     * @return bitmap
     */
    public abstract Bitmap getBitmap(int type);

    /**
     * 裁剪Bitmap,这里可以根据不同需要,裁剪出不同的形状
     *
     * @param bitmap 需要裁剪的Bitmap
     * @param type   类型,参考{@link #TYPE_NEW_VIEW}和{@link #TYPE_OLD_VIEW}
     * @return 裁剪结果
     */
    protected abstract Bitmap[] cutBitmaps(Bitmap bitmap, int type);

    /**
     * 设置属性动画
     *
     * @param view  需要设置的View
     * @param index View在cut后的Bitmap数组中的位置
     * @param type  是新View还是旧View
     * @return 动画
     */
    protected abstract Animator setAnimator(View view, int index, int type);

}
