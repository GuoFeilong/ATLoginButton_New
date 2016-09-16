## 源代码在文章最后,应各位读者留言,奉上代码下载地址 ##
昨天偶偶然看见UI 给的一个交互的效果,原图如下
![这里写图片描述](http://img.blog.csdn.net/20160715141559136)

就是下面的loginbutton,于是大概模仿了一下,
并没有做这个UI的全部效果,有兴趣的可以完善后面展开的效果

下面是demo的button效果
![这里写图片描述](http://img.blog.csdn.net/20160715141659837)

这个View用到的知识点比较简单:

 1. view的坐标系知识,(大家没有不熟悉的吧)
 2. view的canvas基本API(画矩形,画扇形,)
 3. view的自定义属性(attr提供选项)
 4. 属性动画的知识(老生常谈的知识,ObjectAnimation和ValueAniamtion)
 

下面我们就一步步实现这个button

 - 我们写一个自定义的类继承View实现其构造,在构造函数中获取自定义属性的值
   

```
 public ATLoginButton(Context context) {
        this(context, null);
    }

    public ATLoginButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ATLoginButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性集合
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ATLoginButton, defStyleAttr, R.style.def_button_style);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ATLoginButton_button_color:
                    buttonColor = typedArray.getColor(attr, getResources().getColor(R.color.colorAccent));
                    break;
                case R.styleable.ATLoginButton_text_color:
                    textColor = typedArray.getColor(attr, getResources().getColor(R.color.colorW));
                    break;
                case R.styleable.ATLoginButton_text_size:
                    textSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.ATLoginButton_login_text:
                    loginDesc = typedArray.getString(attr);
                    break;
                case R.styleable.ATLoginButton_failed_text:
                    failDesc = typedArray.getString(attr);
                    break;
                case R.styleable.ATLoginButton_circle_loading_color:
                    circlerLoadingColor = typedArray.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.ATLoginButton_circle_loading_width:
                    circleLoadingLineWidth = typedArray.getDimensionPixelOffset(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.ATLoginButton_failed_button_color:
                    failedButtonColor = typedArray.getColor(attr, Color.GRAY);
                    break;
            }
        }
        typedArray.recycle();
        init();
    }
```

 - 重写view的onMeasue,确定和测量我们view的大小和测试模式的确定
   

```
 @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
// 主要view支持wrap_content属性,如果不处理,warpcontent和matchparent感官给我们的感觉是一样的,其实并不然,想了解的可以看下官方文档
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
```
    

 - 然后获取测量后view的宽和高
 

```
  @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        circleAndRoundSize = h / 2;
        textPoint = getTextPointInView(loginDesc);
        buttonRectF = new RectF(0, 0, mWidth, mHeight);
        mText = loginDesc;
        viewState = LoginViewState.NORMAL_STATE;
    }
```

 - 然后就是最后一步了onDraw,几分钟,我们已经完成了百分之80的工作
   最后20%就是让view的内容画到画布上,并且让其动起来就ok了
   
 
 - 画圆形的button,注意这个圆角button,动起来的时候量个半圆需要合并成一个完整的圈,所以倒角的半径就已经确定了,就是我们view高度的一半,这里需要注意下
 
 

```
//画button代码
 private void drawButton(Canvas canvas) {
        canvas.drawRoundRect(buttonRectF, circleAndRoundSize, circleAndRoundSize, buttonPaint);
    }
```

 - 画button上面的文字
 

```
 private void drawTextDesc(Canvas canvas, String textDesc) {
        canvas.drawText(textDesc, textPoint.x, textPoint.y, textPaint);
    }
```

 - 小插曲,我们在绘制文字的时候为了让文字居中,我们需要获取文字测量后的信息如下
 
 

```
// 这里我直接获取了文字的宽高然后把文字在view中的坐标信息计算并返回出去了
 private Point getTextPointInView(String textDesc) {
        Point point = new Point();
        int textW = (mWidth - (int) textPaint.measureText(textDesc)) / 2;
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        int textH = (int) Math.ceil(fm.descent - fm.top);
        point.set(textW, (mHeight + textH) / 2);
        return point;
    }

```

 - 画扇形的方法,这个方形就是我们那个loading的圆圈
 
  

```
  private void drawCircleLoading(Canvas canvas) {
        float circleSpacing = circleAndRoundSize / 4;
        float x = (mHeight - 10) / 2;
        float y = (mHeight - 10) / 2;
        canvas.translate(mWidth / 2, y);
        canvas.scale(1F, 1F);
        canvas.rotate(0);
        RectF rectF = new RectF(-x + circleSpacing, -y + circleSpacing, x - circleSpacing, y - circleSpacing);
        canvas.drawArc(rectF, -45, 270, false, circleLoadingPaint);
    }
```

 - ok到现在我们所有的图形元素都准备到位,剩下的就是提供两个方法,一个是开始登陆,button变成圆形,还有一个就是登陆的结果不管失败还是成功都要变成button,以及还有一个在变成圆球的时候旋转的动画
 
一步步来

```
    public void buttonLoginAction() {
        setClickable(false);
        buttonPaint.setColor(buttonColor);
        if (viewState != LoginViewState.NORMAL_STATE) {
            circleLoadingPaint.setColor(circlerLoadingColor);
        }
        ValueAnimator valueAnimator = getValA(0F, 1F);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
            // 这里是我们根据valueani返回的比例,确定button的新的left和right
                Float aFloat = Float.valueOf(valueAnimator.getAnimatedValue().toString());
                int left = (int) ((aFloat) * mWidth);
                int jL = mWidth / 2 - circleAndRoundSize;
                if (left >= jL) {
                //由于float不好做比价所以转成int,如果新的left坐标大于view的测量一半说明这个时候应该变成圆形了,
                // 我们手动让其变成正规圆,抛弃float带来的误差
                    buttonRectF = new RectF(jL, 0, jL + mHeight, mHeight);
                    textPaint.setColor(Color.TRANSPARENT);
                    isLoading = true;
				    // 动画取消
                    invalidate();
                    valueAnimator.cancel();
                    startLoading();
                    viewState = LoginViewState.LOADING_STATE;
                    return;
                }
                float right = (1 - aFloat) * mWidth;
                buttonRectF = new RectF(left, 0, right, mHeight);
                invalidate();
            }
        });
        valueAnimator.start();
    }

```

 - 然后就是类似的一个方法,圆圈变成button的方法
 
 

```
    public void buttonLoaginResultAciton(final boolean isSuccess) {
        viewState = isSuccess ? LoginViewState.SUCCESS_STATE : LoginViewState.FAILED_STATE;
        stopLoading();
        ValueAnimator valueAnimator = getValA(0F, 1F);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
            //依然是计算新的left
                int jL = mWidth / 2 - circleAndRoundSize;
                Float aFloat = Float.valueOf(valueAnimator.getAnimatedValue().toString());
                int left = (int) ((1 - aFloat) * jL);
                float right = jL + mHeight + jL * aFloat;
                buttonRectF = new RectF(left, 0, right, mHeight);
                textPaint.setColor(textColor);
                if (isSuccess){
                // 登陆成功,重置view的状态
                    mText = loginDesc;
                    textPoint = getTextPointInView(mText);
                    buttonPaint.setColor(buttonColor);
                    invalidate();
                    if (aFloat.intValue() == 1) {
                        setClickable(true);
                        buttonRectF = new RectF(0, 0, mWidth, mHeight);
                        invalidate();
                        valueAnimator.cancel();
                    }
                }else {
                // 登陆失败,进入颤抖动画显示失败的文字和背景
                    mText = failDesc;
                    textPoint = getTextPointInView(mText);
                    buttonPaint.setColor(failedButtonColor);
                    invalidate();
                    if (aFloat.intValue() == 1) {
                        setClickable(true);
                        buttonRectF = new RectF(0, 0, mWidth, mHeight);
                        invalidate();
                        shakeFailed();
                        valueAnimator.cancel();
                    }
                }
            }
        });
        valueAnimator.start();
    }

```

这样我们view的全部工作都做完了,剩下的就是在Mainactivity里面用一下

```
    private void addListener2Button(final ATLoginButton atLoginButton, final boolean loaginStatus) {
        atLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
	            // 这里是登陆动画,然后去请求服务器接口
                atLoginButton.buttonLoginAction();
                // 加入三秒后,登陆失败或者成功
                atLoginButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //这里调用View获取登陆状态的方法,成功或者失败
                        atLoginButton.buttonLoaginResultAciton(loaginStatus);
                        String notice = loaginStatus ? "登陆成功,重置button状态" : "登录失败,显示失败状态";
                        Toast.makeText(getApplicationContext(), notice, Toast.LENGTH_SHORT).show();
                    }
                }, 3000);
            }
        });
    }

```
由于 就一个这个demo就一个自定义view,项目就不上传了,把完整的代码给大家,有兴趣的可以放到AS里面跑一下,谢谢!
最后给大家推荐我的一个比价完整的开源项目,SoHOT链接如下,
[文章末尾有Githup免费下载地址,希望star谢谢](http://blog.csdn.net/givemeacondom/article/details/50526518)

```
public class ATLoginButton extends View {
    private static final float DE_W = 280.F;
    private static final float DE_H = 65.F;
    private static final long ANIMATION_TIME = 800;
    private int buttonColor;
    private int textColor;
    private int textSize;
    private int circlerLoadingColor;
    private int failedButtonColor;
    private int circleLoadingLineWidth;
    private String loginDesc;
    private String failDesc;
    private String mText;
    private Paint buttonPaint;
    private Paint textPaint;
    private Paint circleLoadingPaint;

    private int mHeight;
    private int mWidth;
    private int circleAndRoundSize;

    private RectF buttonRectF;
    private Point textPoint;
    private boolean isLoading;
    private RotateAnimation rotateAnimation;

    private int viewState;

    public ATLoginButton(Context context) {
        this(context, null);
    }

    public ATLoginButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ATLoginButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ATLoginButton, defStyleAttr, R.style.def_button_style);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ATLoginButton_button_color:
                    buttonColor = typedArray.getColor(attr, getResources().getColor(R.color.colorAccent));
                    break;
                case R.styleable.ATLoginButton_text_color:
                    textColor = typedArray.getColor(attr, getResources().getColor(R.color.colorW));
                    break;
                case R.styleable.ATLoginButton_text_size:
                    textSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.ATLoginButton_login_text:
                    loginDesc = typedArray.getString(attr);
                    break;
                case R.styleable.ATLoginButton_failed_text:
                    failDesc = typedArray.getString(attr);
                    break;
                case R.styleable.ATLoginButton_circle_loading_color:
                    circlerLoadingColor = typedArray.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.ATLoginButton_circle_loading_width:
                    circleLoadingLineWidth = typedArray.getDimensionPixelOffset(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.ATLoginButton_failed_button_color:
                    failedButtonColor = typedArray.getColor(attr, Color.GRAY);
                    break;
            }
        }
        typedArray.recycle();
        init();
    }

    private void init() {
        buttonPaint = creatPaint(buttonColor, 0, Paint.Style.FILL, circleLoadingLineWidth);
        circleLoadingPaint = creatPaint(circlerLoadingColor, 0, Paint.Style.STROKE, circleLoadingLineWidth);
        textPaint = creatPaint(textColor, textSize, Paint.Style.FILL, circleLoadingLineWidth);
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
        mWidth = w;
        mHeight = h;
        circleAndRoundSize = h / 2;
        textPoint = getTextPointInView(loginDesc);
        buttonRectF = new RectF(0, 0, mWidth, mHeight);
        mText = loginDesc;
        viewState = LoginViewState.NORMAL_STATE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawButton(canvas);
        drawTextDesc(canvas, mText);
        if (isLoading) {
            drawCircleLoading(canvas);
        }
    }

    private void drawCircleLoading(Canvas canvas) {
        float circleSpacing = circleAndRoundSize / 4;
        float x = (mHeight - 10) / 2;
        float y = (mHeight - 10) / 2;
        canvas.translate(mWidth / 2, y);
        canvas.scale(1F, 1F);
        canvas.rotate(0);
        RectF rectF = new RectF(-x + circleSpacing, -y + circleSpacing, x - circleSpacing, y - circleSpacing);
        canvas.drawArc(rectF, -45, 270, false, circleLoadingPaint);
    }

    private void drawTextDesc(Canvas canvas, String textDesc) {
        canvas.drawText(textDesc, textPoint.x, textPoint.y, textPaint);
    }

    private void drawButton(Canvas canvas) {
        canvas.drawRoundRect(buttonRectF, circleAndRoundSize, circleAndRoundSize, buttonPaint);
    }

    private Point getTextPointInView(String textDesc) {
        Point point = new Point();
        int textW = (mWidth - (int) textPaint.measureText(textDesc)) / 2;
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        int textH = (int) Math.ceil(fm.descent - fm.top);
        point.set(textW, (mHeight + textH) / 2);
        return point;
    }

    public void buttonLoginAction() {
        setClickable(false);
        buttonPaint.setColor(buttonColor);
        if (viewState != LoginViewState.NORMAL_STATE) {
            circleLoadingPaint.setColor(circlerLoadingColor);
        }
        ValueAnimator valueAnimator = getValA(0F, 1F);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Float aFloat = Float.valueOf(valueAnimator.getAnimatedValue().toString());
                int left = (int) ((aFloat) * mWidth);
                int jL = mWidth / 2 - circleAndRoundSize;
                if (left >= jL) {
                    buttonRectF = new RectF(jL, 0, jL + mHeight, mHeight);
                    textPaint.setColor(Color.TRANSPARENT);
                    isLoading = true;
                    invalidate();
                    valueAnimator.cancel();
                    startLoading();
                    viewState = LoginViewState.LOADING_STATE;
                    return;
                }
                float right = (1 - aFloat) * mWidth;
                buttonRectF = new RectF(left, 0, right, mHeight);
                invalidate();
            }
        });
        valueAnimator.start();
    }

    public void buttonLoaginResultAciton(final boolean isSuccess) {
        viewState = isSuccess ? LoginViewState.SUCCESS_STATE : LoginViewState.FAILED_STATE;
        stopLoading();
        ValueAnimator valueAnimator = getValA(0F, 1F);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int jL = mWidth / 2 - circleAndRoundSize;
                Float aFloat = Float.valueOf(valueAnimator.getAnimatedValue().toString());
                int left = (int) ((1 - aFloat) * jL);
                float right = jL + mHeight + jL * aFloat;
                buttonRectF = new RectF(left, 0, right, mHeight);
                textPaint.setColor(textColor);
                if (isSuccess){
                    mText = loginDesc;
                    textPoint = getTextPointInView(mText);
                    buttonPaint.setColor(buttonColor);
                    invalidate();
                    if (aFloat.intValue() == 1) {
                        setClickable(true);
                        buttonRectF = new RectF(0, 0, mWidth, mHeight);
                        invalidate();
                        valueAnimator.cancel();
                    }
                }else {
                    mText = failDesc;
                    textPoint = getTextPointInView(mText);
                    buttonPaint.setColor(failedButtonColor);
                    invalidate();
                    if (aFloat.intValue() == 1) {
                        setClickable(true);
                        buttonRectF = new RectF(0, 0, mWidth, mHeight);
                        invalidate();
                        shakeFailed();
                        valueAnimator.cancel();
                    }
                }
            }
        });
        valueAnimator.start();
    }

    private void stopLoading() {
        isLoading = false;
        circleLoadingPaint.setColor(Color.TRANSPARENT);
        if (null != rotateAnimation) {
            rotateAnimation.cancel();
        }
    }
    private void shakeFailed() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "translationX", 0, 10);
        objectAnimator.setDuration(500);
        objectAnimator.setInterpolator(new CycleInterpolator(10));
        objectAnimator.start();
    }

    private void startLoading() {
        rotateAnimation = new RotateAnimation(0F, 360F, mWidth / 2, mHeight / 2);
        rotateAnimation.setDuration(500);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        startAnimation(rotateAnimation);
    }

    private static class LoginViewState {
        static final int NORMAL_STATE = 90;
        static final int LOADING_STATE = 91;
        static final int FAILED_STATE = 92;
        static final int SUCCESS_STATE = 93;
    }
}
```

## 源代码链接 ##[button按钮下载地址外加一个标签view](https://github.com/GuoFeilong/ATLoginButton_New)

