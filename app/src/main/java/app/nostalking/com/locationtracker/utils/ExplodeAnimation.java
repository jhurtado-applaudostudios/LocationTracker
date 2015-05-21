package app.nostalking.com.locationtracker.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import app.nostalking.com.locationtracker.intefaces.AnimationListener;

/**
 * Created by Juan Hurtado on 5/21/2015.
 */
public class ExplodeAnimation extends Animation {

    private int mXparts;
    private int mYparts;
    private ViewGroup mViewParent;
    private int mMatrix;
    private TimeInterpolator mTimeInterpolator;
    private int mDuration;
    private AnimationListener mAnimationListener;

    public final static int MATRIX_1X2 = 12;
    public final static int MATRIX_1X3 = 13;
    public final static int MATRIX_2X1 = 21;
    public final static int MATRIX_2X2 = 22;
    public final static int MATRIX_2X3 = 23;
    public final static int MATRIX_3X1 = 31;
    public final static int MATRIX_3X2 = 32;
    public final static int MATRIX_3X3 = 33;

    public ExplodeAnimation(View view) {
        this.view = view;
        setExplodeMatrix(MATRIX_3X3);
        mTimeInterpolator = new AccelerateDecelerateInterpolator();
        mDuration = DURATION_LONG;
        mAnimationListener = null;
    }

    public ExplodeAnimation setExplodeMatrix(int matrix){
        this.mMatrix = matrix;
        mXparts = mMatrix / 10;
        mYparts = mMatrix % 10;
        return this;
    }


    @Override
    public void animate() {
        final LinearLayout explodeLayout = new LinearLayout(view.getContext());
        LinearLayout[] layouts = new LinearLayout[mYparts];
        mViewParent = (ViewGroup) view.getParent();
        explodeLayout.setLayoutParams(view.getLayoutParams());
        explodeLayout.setOrientation(LinearLayout.VERTICAL);
        explodeLayout.setClipChildren(false);

        view.setDrawingCacheEnabled(true);
        Bitmap viewBmp = view.getDrawingCache(true);
        int totalParts = mXparts * mYparts, bmpWidth = viewBmp.getWidth()
                / mXparts, bmpHeight = viewBmp.getHeight() / mYparts, widthCount = 0, heightCount = 0, middleXPart = (mXparts - 1) / 2;
        int[] translation = new int[2];
        ImageView[] imageViews = new ImageView[totalParts];

        for (int i = 0; i < totalParts; i++) {
            int translateX = 0, translateY = 0;
            if (i % mXparts == 0) {
                if (i != 0)
                    heightCount++;
                widthCount = 0;
                layouts[heightCount] = new LinearLayout(view.getContext());
                layouts[heightCount].setClipChildren(false);
                translation = sideTranslation(heightCount, bmpWidth, bmpHeight,
                        mXparts, mYparts);
                translateX = translation[0];
                translateY = translation[1];
            } else if (i % mXparts == mXparts - 1) {
                translation = sideTranslation(heightCount, -bmpWidth,
                        bmpHeight, mXparts, mYparts);
                translateX = translation[0];
                translateY = translation[1];
            } else {
                if (widthCount == middleXPart) {
                    if (heightCount == 0) {
                        translateX = 0;
                        if (mYparts != 1) {
                            translateY = -bmpHeight;
                        }
                    } else if (heightCount == mYparts - 1) {
                        translateX = 0;
                        translateY = bmpHeight;
                    }
                }
            }
            if (mXparts == 1) {
                translation = sideTranslation(heightCount, 0, bmpHeight,
                        mXparts, mYparts);
                translateX = translation[0];
                translateY = translation[1];
            }

            imageViews[i] = new ImageView(view.getContext());
            imageViews[i]
                    .setImageBitmap(Bitmap.createBitmap(viewBmp, bmpWidth
                                    * widthCount, bmpHeight * heightCount, bmpWidth,
                            bmpHeight));
            imageViews[i].animate().translationXBy(translateX)
                    .translationYBy(translateY).alpha(0)
                    .setInterpolator(mTimeInterpolator).setDuration(mDuration);
            layouts[heightCount].addView(imageViews[i]);
            widthCount++;
        }

        for (int i = 0; i < mYparts; i++)
            explodeLayout.addView(layouts[i]);
        final int positionView = mViewParent.indexOfChild(view);
//        mViewParent.removeView(view);
        view.setVisibility(View.INVISIBLE);
        mViewParent.addView(explodeLayout, positionView);

        ViewGroup rootView = (ViewGroup) explodeLayout.getRootView();
        while (!mViewParent.equals(rootView)) {
            mViewParent.setClipChildren(false);
            mViewParent = (ViewGroup) mViewParent.getParent();
        }
        rootView.setClipChildren(false);

        imageViews[0].animate().setListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                mViewParent = (ViewGroup) explodeLayout.getParent();
                view.setLayoutParams(explodeLayout.getLayoutParams());
                view.setVisibility(View.INVISIBLE);
                mViewParent.removeView(explodeLayout);
//                mViewParent.addView(view, positionView);
                if (getListener() != null) {
                    getListener().onAnimationEnd(ExplodeAnimation.this);
                }
            }
        });
    }

    private int[] sideTranslation(int heightCount, int bmpWidth, int bmpHeight,
                                  int xParts, int yParts) {
        int[] translation = new int[2];
        int middleYPart = (yParts - 1) / 2;
        if (heightCount == 0) {
            translation[0] = -bmpWidth;
            translation[1] = -bmpHeight;
        } else if (heightCount == yParts - 1) {
            translation[0] = -bmpWidth;
            translation[1] = bmpHeight;
        }

        if (yParts % 2 != 0) {
            if (heightCount == middleYPart) {
                translation[0] = -bmpWidth;
                translation[1] = 0;
            }
        }
        return translation;
    }

    public int getExplodeMatrix() {
        return mMatrix;
    }

    public TimeInterpolator getInterpolator() {
        return mTimeInterpolator;
    }

    public ExplodeAnimation setInterpolator(TimeInterpolator interpolator) {
        this.mTimeInterpolator = interpolator;
        return this;
    }

    public long getDuration() {
        return mDuration;
    }

    public ExplodeAnimation setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }
    public AnimationListener getListener() {
        return mAnimationListener;
    }

    public ExplodeAnimation setListener(AnimationListener listener) {
        this.mAnimationListener = listener;
        return this;
    }

}
