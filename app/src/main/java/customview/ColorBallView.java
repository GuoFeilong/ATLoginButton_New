package customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.atloginbutton.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jsion on 16/8/5.
 */

public class ColorBallView extends View {
    private static final int STRETCHING_X = 150;
    private static final int STRETCHING_Y = 80;
    private static final float BALL_MAX = 1 / 10F;
    private static final float DE_VIEW_SIZE = 120F;
    private static final long ANIMATION_TIME = 1200;
    private static final long INTERVAL_TIME = 400;
    private final int DE_BALL_SIZE = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
    private Paint mPaint;
    private int mFirstBallColor;
    private int mSecondBallColor;
    private int mThirdBallColor;
    private int mBallRadius;

    private int mWidth;
    private int mHeight;

    private int cx, cy, cx1, cy1, cx2, cy2;
    private int offsetY, offsetY1, offsetY2;
    private boolean isDrawSecond, isDrawThird;

    public ColorBallView(Context context) {
        this(context, null);
    }

    public ColorBallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorBallView, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ColorBallView_ballSize:
                    mBallRadius = typedArray.getDimensionPixelSize(attr, DE_BALL_SIZE);
                    break;
                case R.styleable.ColorBallView_firstBallColor:
                    mFirstBallColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ColorBallView_secondColor:
                    mSecondBallColor = typedArray.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.ColorBallView_thirdBallColor:
                    mThirdBallColor = typedArray.getColor(attr, Color.YELLOW);
                    break;
            }
        }
        typedArray.recycle();
        init();
        startAllBallAnimation();
    }

    private void startAllBallAnimation() {
        Observable.interval(INTERVAL_TIME, INTERVAL_TIME, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        int i = aLong.intValue();
                        if (i == 0) {
                            initAnimation();

                        } else if (i == 1) {
                            initAnimation1();
                        } else if (i == 2) {
                            initAnimation2();
                            this.unsubscribe();
                            onCompleted();
                        }
                    }
                });
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize;
        int heightSize;

        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DE_VIEW_SIZE, getResources().getDisplayMetrics());
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }

        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DE_VIEW_SIZE, getResources().getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mBallRadius = (int) ((int) (Math.min(Math.min(w, h) * BALL_MAX, mBallRadius)) * .5F);

        cx = mBallRadius;
        cy = mHeight / 2;
        cx1 = cx;
        cx2 = cx;
        cy1 = cy;
        cy2 = cy;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBall(canvas);
    }

    private void drawBall(Canvas canvas) {
        mPaint.setColor(mFirstBallColor);
        canvas.drawCircle(cx, cy + offsetY, mBallRadius, mPaint);
        if (isDrawSecond) {
            mPaint.setColor(mSecondBallColor);
            canvas.drawCircle(cx1, cy1 + offsetY1, mBallRadius, mPaint);
        }
        if (isDrawThird) {
            mPaint.setColor(mThirdBallColor);
            canvas.drawCircle(cx2, cy2 + offsetY2, mBallRadius, mPaint);
        }

    }

    private void initAnimation() {
        ValueAnimator valueAnimator = getValueAni();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float aFloat = Float.valueOf(animation.getAnimatedValue().toString());
                cx = (int) (aFloat * STRETCHING_X) + mBallRadius;
                offsetY = (int) ((float) Math.sin(2 * Math.PI * aFloat) * STRETCHING_Y);
                invalidate();
            }
        });
        valueAnimator.start();
    }


    private void initAnimation1() {
        ValueAnimator valueAnimator = getValueAni();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float aFloat = Float.valueOf(animation.getAnimatedValue().toString());
                cx1 = (int) (aFloat * STRETCHING_X) + mBallRadius;
                offsetY1 = (int) ((float) Math.sin(2 * Math.PI * aFloat) * STRETCHING_Y);
                isDrawSecond = true;
                invalidate();
            }
        });
        valueAnimator.start();
    }


    private void initAnimation2() {
        ValueAnimator valueAnimator = getValueAni();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float aFloat = Float.valueOf(animation.getAnimatedValue().toString());
                cx2 = (int) (aFloat * STRETCHING_X) + mBallRadius;
                offsetY2 = (int) ((float) Math.sin(2 * Math.PI * aFloat) * STRETCHING_Y);
                isDrawThird = true;
                invalidate();
            }
        });
        valueAnimator.start();
    }

    private ValueAnimator getValueAni() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1.1f);
        valueAnimator.setDuration(ANIMATION_TIME);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);

        return valueAnimator;
    }
}
