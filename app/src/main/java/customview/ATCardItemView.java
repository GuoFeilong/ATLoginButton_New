package customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.atloginbutton.R;

/**
 * Created by jsion on 16/7/20.
 */
public class ATCardItemView extends View {

    private static final float CIRCLE_RADIUS_SCALE = 1 / 20.F;
    private static final float TRIANGLE_SCALE_H = 1 / 4.5F;
    private static final float UP_DOWN_SCALE = 81 / 118.F;
    private static final float DE_H = 118.F;
    private static final float DE_W = 332.F;

    private int cardColor;
    private int cardStrockColor;
    private int textHighLightColor;
    private int textNormalColor;
    private int triangleColor;
    private int triangleFlagColor;
    private int dashLineColor;
    private int cardRoundSize;
    private int strockSize;
    private boolean isSelected = true;


    private Paint cardItemBgPaint, cardItemStrockPaint, dashLinePaint, circlePaint, trianglePaint;
    private RectF cardBgRectF, cardStrockRectF;
    private int cardWidth, cardHeight;
    private Path triangelPath;
    private Paint textFlagPaint;
    private Path dashLinePath;

    public ATCardItemView(Context context) {
        this(context, null);
    }

    public ATCardItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ATCardItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ATCardItemView, defStyleAttr, R.style.def_card_item_style);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ATCardItemView_cardColor:
                    cardColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATCardItemView_cardStrockColor:
                    cardStrockColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATCardItemView_textHighLightColor:
                    textHighLightColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATCardItemView_textNormalColor:
                    textNormalColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATCardItemView_triangleColor:
                    triangleColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATCardItemView_triangleFlagColor:
                    triangleFlagColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATCardItemView_dashLineColor:
                    dashLineColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATCardItemView_cardRoundSize:
                    cardRoundSize = typedArray.getDimensionPixelSize(attr, 2);
                    break;
                case R.styleable.ATCardItemView_strockSize:
                    strockSize = typedArray.getDimensionPixelSize(attr, 2);
                    break;
            }
        }
        init();
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize;
        int heightSize;

        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DE_W, getResources().getDisplayMetrics());
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }

        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DE_H, getResources().getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cardWidth = w;
        cardHeight = h;
        cardStrockRectF = new RectF(0, 0, cardWidth, cardHeight);
        cardBgRectF = new RectF(strockSize, strockSize, cardWidth - strockSize, cardHeight - strockSize);
        // triange path
        triangelPath = new Path();
        triangelPath.moveTo(cardWidth - (cardHeight * TRIANGLE_SCALE_H), 0);
        triangelPath.lineTo(cardWidth, 0);
        triangelPath.lineTo(cardWidth, cardHeight * TRIANGLE_SCALE_H);
        triangelPath.close();
        // dashline path
        dashLinePath = new Path();
        int dashLineStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getResources().getDisplayMetrics());
        int dashLineY = (int) (cardHeight * UP_DOWN_SCALE);
        dashLinePath.moveTo(dashLineStart, dashLineY);
        dashLinePath.lineTo(cardWidth - dashLineStart, dashLineY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCardStrock(canvas, cardStrockRectF);
        drawCardBg(canvas, cardBgRectF);
        drawDashLine(canvas);
        drawHalfCirlces(canvas);
        if (isSelected) {
            drawTriangle(canvas, triangelPath);
            drawTriangleFlag(canvas, "√");
        }
    }

    private void init() {
        cardItemBgPaint = creatPaint(cardColor, 0, Paint.Style.FILL, 0);
        cardItemStrockPaint = creatPaint(cardStrockColor, 0, Paint.Style.FILL, 0);
        dashLinePaint = creatPaint(dashLineColor, 0, Paint.Style.STROKE, 2);
        circlePaint = creatPaint(getResources().getColor(android.R.color.darker_gray), 0, Paint.Style.FILL, 0);
        trianglePaint = creatPaint(triangleColor, 0, Paint.Style.FILL, 0);
        textFlagPaint = creatPaint(triangleFlagColor, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()), Paint.Style.FILL, 2);
    }

    private Paint creatPaint(int paintColor, int textSize, Paint.Style style, int lineWidth) {
        Paint paint = new Paint();
        paint.setColor(paintColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(lineWidth);
        paint.setDither(true);
        paint.setTextSize(textSize);
        paint.setStyle(style);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }

    private void drawTriangleFlag(Canvas canvas, String triangeleFlag) {
        Paint.FontMetrics fm = textFlagPaint.getFontMetrics();
        int textH = (int) Math.ceil(fm.descent - fm.top);
        int textW = (int) textFlagPaint.measureText(triangeleFlag);

        canvas.drawText(triangeleFlag, cardWidth - (cardHeight * TRIANGLE_SCALE_H) / 2 + textW / 2, (cardHeight * TRIANGLE_SCALE_H) / 2, textFlagPaint);
    }

    private void drawTriangle(Canvas canvas, Path triangelPath) {
        canvas.drawPath(triangelPath, trianglePaint);
    }

    private void drawHalfCirlces(Canvas canvas) {
        int cirlceY = (int) (cardHeight * UP_DOWN_SCALE);
        canvas.drawCircle(0, cirlceY, cardHeight * CIRCLE_RADIUS_SCALE, cardItemStrockPaint);
        canvas.drawCircle(cardWidth, cirlceY, cardHeight * CIRCLE_RADIUS_SCALE, cardItemStrockPaint);

        canvas.drawCircle(0, cirlceY, cardHeight * CIRCLE_RADIUS_SCALE - strockSize, circlePaint);
        canvas.drawCircle(cardWidth, cirlceY, cardHeight * CIRCLE_RADIUS_SCALE - strockSize, circlePaint);
    }

    private void drawDashLine(Canvas canvas) {
        PathEffect effects = new DashPathEffect(new float[]{10, 10, 10, 10}, 1);
        dashLinePaint.setPathEffect(effects);
        canvas.drawPath(dashLinePath, dashLinePaint);
    }

    private void drawCardStrock(Canvas canvas, RectF cardStrockRectF) {
        canvas.drawRoundRect(cardStrockRectF, cardRoundSize, cardRoundSize, cardItemStrockPaint);
    }

    private void drawCardBg(Canvas canvas, RectF cardBgRectF) {
        canvas.drawRoundRect(cardBgRectF, cardRoundSize, cardRoundSize, cardItemBgPaint);
    }

    public void setCardSelectState(boolean isSelected) {
        this.isSelected = isSelected;
        invalidate();
    }

}
