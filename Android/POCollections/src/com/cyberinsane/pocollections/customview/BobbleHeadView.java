package com.cyberinsane.pocollections.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cyberinsane.pocollections.R;

public class BobbleHeadView extends View {

    protected static final String DEBUG_TAG = "BobbleHeadView";

    private Matrix transform = new Matrix();
    private Vector2D position = new Vector2D();
    private TouchManager touchManager = new TouchManager(1);

    private boolean isInitialized = false;

    private int mWidth;
    private int mHeight;
    private float mMoveXBy = 0;
    private float mMoveYBy = 0;
    private int mStifness = 2;

    private float mOldX = 0;
    private float mOldY = 0;

    private float mTotalDistance = 0;

    private View self;

    private Bitmap doggieHead;

    Paint paint;

    public BobbleHeadView(Context context) {
        super(context);
        self = this;
    }

    public BobbleHeadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public BobbleHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        self = this;
        doggieHead = BitmapFactory.decodeResource(getResources(), R.drawable.bullseye_head);
        mWidth = doggieHead.getWidth();
        mHeight = doggieHead.getHeight();

        paint = new Paint();

    }

    @SuppressLint("NewApi")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // switch (event.getAction() & MotionEvent.ACTION_MASK) {
        // case MotionEvent.ACTION_DOWN:
        // mMoveXBy = event.getX();
        // mMoveYBy = event.getY();
        // break;
        // case MotionEvent.ACTION_UP:
        // break;
        // case MotionEvent.ACTION_POINTER_DOWN:
        // break;
        // case MotionEvent.ACTION_POINTER_UP:
        // break;
        // case MotionEvent.ACTION_MOVE: {
        // invalidate();
        //
        // moveDoggiHead(event);
        // break;
        // }
        // }

        touchManager.update(event);
        position.add(touchManager.moveDelta(0));
        invalidate();

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(mWidth, mHeight);
    }

    public float getDistance(float startX, float startY, MotionEvent ev) {
        float distanceSum = 0;
        final int historySize = ev.getHistorySize();
        for (int h = 0; h < historySize; h++) {
            // historical point
            float hx = ev.getHistoricalX(0, h);
            float hy = ev.getHistoricalY(0, h);
            // distance between startX,startY and historical point
            float dx = (hx - startX);
            float dy = (hy - startY);
            distanceSum += Math.sqrt(dx * dx + dy * dy);
            // make historical point the start point for next loop iteration
            startX = hx;
            startY = hy;
        }
        // add distance from last historical point to event's point
        float dx = (ev.getX(0) - startX);
        float dy = (ev.getY(0) - startY);
        distanceSum += Math.sqrt(dx * dx + dy * dy);
        return distanceSum;
    }

    @SuppressLint("NewApi")
    private void moveDoggiHead(MotionEvent ev) {

        // final float dx = ev.getX() - mOldX;
        // final float dy = ev.getY() - mOldY;
        // final float dl = (float) Math.sqrt(dx * dx + dy * dy);
        // mTotalDistance += dl;
        // mOldX = ev.getX();
        // mOldY = ev.getY();

        // ViewPropertyAnimator animatorX =
        // com.nineoldandroids.view.ViewPropertyAnimator.animate(self).translationX(ev.getX()).setDuration(0);
        // ViewPropertyAnimator animatory =
        // com.nineoldandroids.view.ViewPropertyAnimator.animate(self).translationY(ev.getY()).setDuration(0);

        // System.out.println("HUHA :" + mTotalDistance);
        // invalidate();
        // requestLayout();

        mMoveXBy = ev.getX();
        mMoveYBy = ev.getY();
        invalidate();

    }

    @Override
    protected void onDraw(Canvas c) {

        if (!isInitialized) {
            int w = getWidth();
            int h = getHeight();
            position.set(w / 2, h / 2);
            isInitialized = true;
        }

        transform.reset();
        transform.postTranslate(-mWidth / 2.0f, -mHeight / 2.0f);
        transform.postTranslate(position.getX() / mStifness, position.getY() / mStifness);

        c.drawBitmap(doggieHead, transform, paint);
    }

}
