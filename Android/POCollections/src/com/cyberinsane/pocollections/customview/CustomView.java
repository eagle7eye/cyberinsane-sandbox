package com.cyberinsane.pocollections.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.cyberinsane.pocollections.R;

public class CustomView extends View {

    private boolean mShowText;
    private int mTextPos;

    private float angle = 0f;
    private float theta_old = 0f;

    private int mRadius = 0;
    private int mCircleThickness = 50;
    private Paint mPaintCircle;
    private int mCircleColor = Color.parseColor("#000000");

    private Paint mPaintText;
    private int mTextColor = Color.parseColor("#000000");

    // Holder
    private Paint mPaintHolder;
    private int mHolderColor = Color.parseColor("#cdcdcd");
    private int mHolderRadius = mCircleThickness / 2;

    private GestureDetector mDetector;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);

        try {
            mShowText = typedArray.getBoolean(R.styleable.CustomView_showText, false);
            mTextPos = typedArray.getInt(R.styleable.CustomView_labelPosition, 0);

        } finally {
            typedArray.recycle();
        }

        init();
    }

    private void init() {

        mPaintCircle = new Paint();
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setColor(mCircleColor);
        mPaintCircle.setStrokeWidth(mCircleThickness);

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(mTextColor);

        mPaintHolder = new Paint();
        mPaintHolder.setAntiAlias(true);
        mPaintHolder.setColor(mHolderColor);

        setOnTouchListener(onTouchListener);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));

        int minh = w + getPaddingBottom() + getPaddingTop();
        int h = Math.min(MeasureSpec.getSize(heightMeasureSpec), minh);

        mRadius = w / 2;
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.rotate(angle, getWidth() / 2, getHeight() / 2);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius - mCircleThickness, mPaintCircle);

        canvas.drawCircle(getWidth() / 2, mCircleThickness, mHolderRadius + 10, mPaintHolder);

        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setTextSize(24);
        canvas.drawText("HUHA", getHeight() / 2, getWidth() / 2, mPaintText);
    }

    OnTouchListener onTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            int actionCode = action & MotionEvent.ACTION_MASK;
            if (actionCode == MotionEvent.ACTION_POINTER_DOWN) {
                float x = event.getX(0);
                float y = event.getY(0);
                theta_old = getTheta(x, y);
            } else if (actionCode == MotionEvent.ACTION_MOVE) {
                invalidate();

                float x = event.getX(0);
                float y = event.getY(0);

                float theta = getTheta(x, y);
                float delta_theta = theta - theta_old;

                theta_old = theta;

                int direction = (delta_theta > 0) ? 1 : -1;
                angle += 3 * direction;

                System.out.println(angle);
                invalidate();
                requestLayout();
            }

            return true;
        }
    };

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
    }

    public boolean isShowText() {
        return mShowText;
    }

    public void setShowText(boolean showText) {
        mShowText = showText;
        invalidate();
        requestLayout();
    }

    private float getTheta(float x, float y) {
        float sx = x - (getWidth() / 2.0f);
        float sy = y - (getHeight() / 2.0f);

        float length = (float) Math.sqrt(sx * sx + sy * sy);
        float nx = sx / length;
        float ny = sy / length;
        float theta = (float) Math.atan2(ny, nx);

        final float rad2deg = (float) (180.0 / Math.PI);
        float theta2 = theta * rad2deg;

        return (theta2 < 0) ? theta2 + 360.0f : theta2;
    }

}
