package com.cyberinsane.pocollections.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;

import com.cyberinsane.pocollections.R;

public class BobbleHeadView extends View {

    protected static final String DEBUG_TAG = "BobbleHeadView";

    private Matrix transform = new Matrix();
    private Vector2D position = new Vector2D();

    private boolean isInitialized = false;

    private int mWidth;
    private int mHeight;

    int mInterpolatorStartTime;
    private CycleInterpolator mInterpolator;

    private int mDuration = 2000;
    private float mDurationReciprocal = 1 / (float) mDuration;

    private float mTotalDistance = 0;

    private Bitmap doggieHead;

    Paint paint;

    private GestureDetector mGestureDetector;

    private int timePassed;

    public BobbleHeadView(Context context) {
        super(context);
    }

    public BobbleHeadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public BobbleHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        doggieHead = BitmapFactory.decodeResource(getResources(), R.drawable.bullseye_head);
        mWidth = doggieHead.getWidth();
        mHeight = doggieHead.getHeight();

        initDetectors(context);
        mInterpolator = new CycleInterpolator(10f);

        paint = new Paint();

    }

    @SuppressWarnings("synthetic-access")
    private void initDetectors(Context context) {

        mGestureDetector = new GestureDetector(context, new MyGestureListener());
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean retVal = mGestureDetector.onTouchEvent(event);
        return retVal || super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));

    }

    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas c) {

        if (!isInitialized) {
            int w = getWidth();
            int h = getHeight();
            position.set(w / 2, h / 2);
            isInitialized = true;
        }

        transform.reset();
        transform.postTranslate((getWidth() - mWidth) / 2, (getHeight() - mHeight) / 2);
        
        timePassed = (int) (AnimationUtils.currentAnimationTimeMillis() - mInterpolatorStartTime);

        if (timePassed < mDuration) {

            shakeItShakeItDoggie();
        }

        c.drawBitmap(doggieHead, transform, paint);
    }
    
    private void shakeItShakeItDoggie() {
        
        float x = timePassed * mDurationReciprocal;
        x = mInterpolator.getInterpolation(x);

        transform.postRotate(x * 200f / timePassed, getWidth() / 2 + (x*100f), getHeight() / 2 + (x*100f));

        invalidate();
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {

            return DoggieAnimStart();
        }

        private boolean DoggieAnimStart() {
            mInterpolatorStartTime = (int) AnimationUtils.currentAnimationTimeMillis();
            postInvalidate();
            return true;
        }

    }

}
