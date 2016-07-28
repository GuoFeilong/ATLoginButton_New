package customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atloginbutton.R;

/**
 * Created by jsion on 16/7/27.
 */
public class ATScrollDeleteTouchView extends RelativeLayout {
    private static final float TOUCH_SCROLL_SCALE = 1 / 5.F;
    private static final int ANIMATION_TIME = 300;
    private int topLayerColor;
    private int topLayerIcon;
    private String topLayerDesc;
    private int topLayerDescColor;
    private int topLayerDescSize;
    private int topLayerIconMarginDesc;
    private int topLayerIconMarginLeft;
    private int underLayerColor;
    private int underLayerIcon;

    private int viewWidth;
    private int viewHeight;

    private ImageView underIconView;
    private TextView topDescView;
    private LinearLayout topLayerParent;
    private int viewState;
    private String desc;
    private OnScrollDeleteListener scrollDeleteListener;

    public ATScrollDeleteTouchView(Context context) {
        this(context, null);
    }

    public ATScrollDeleteTouchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ATScrollDeleteTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ATScrollDeleteView, defStyleAttr, R.style.def_scroll_delete_style);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ATScrollDeleteView_top_layer_color:
                    topLayerColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATScrollDeleteView_top_layer_icon:
                    topLayerIcon = typedArray.getResourceId(attr, 0);
                    break;
                case R.styleable.ATScrollDeleteView_top_layer_desc:
                    topLayerDesc = typedArray.getString(attr);
                    break;
                case R.styleable.ATScrollDeleteView_top_layer_desc_color:
                    topLayerDescColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATScrollDeleteView_top_layer_desc_size:
                    topLayerDescSize = typedArray.getInteger(attr, 0);
                    break;
                case R.styleable.ATScrollDeleteView_top_layer_icon_margin_left:
                    topLayerIconMarginLeft = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.ATScrollDeleteView_top_layer_icon_margin_desc:
                    topLayerIconMarginDesc = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.ATScrollDeleteView_under_layer_color:
                    underLayerColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATScrollDeleteView_under_layer_icon:
                    underLayerIcon = typedArray.getResourceId(attr, 0);
                    break;
            }
        }
        typedArray.recycle();
        setBackgroundColor(underLayerColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWidth = w;
        viewState = ViewState.FOLD;
        addUnderIconView(w);
        addTopLayerView();
        calculateOffSideDistance();
        addViewListener();
    }

    private int calculateOffSideDistance() {
        Bitmap topLayerBitmap = BitmapFactory.decodeResource(getResources(), topLayerIcon);
        if (null != topLayerBitmap) {
            return topLayerBitmap.getWidth() + topLayerIconMarginLeft;
        }
        return 0;
    }

    private void addViewListener() {
        topDescView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFoldOrNot();
            }
        });

        topLayerParent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ViewState.FOLD == viewState) {
                    return;
                }
                viewFoldOrNot();
            }
        });
    }

    private void viewFoldOrNot() {
        if (ViewState.FOLD == viewState) {
            showOrHideUnderLayer(true);
            viewState = ViewState.UN_FOLD;
        } else {
            showOrHideUnderLayer(false);
            viewState = ViewState.FOLD;
        }
    }

    private void showOrHideUnderLayer(boolean isShow) {
        int startX = isShow ? 0 : (int) (-viewWidth * TOUCH_SCROLL_SCALE);
        int offSide = (int) (isShow ? -viewWidth * TOUCH_SCROLL_SCALE : 0);
        topLayerParent.setClickable(false);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(topLayerParent, "translationX", startX, offSide);
        objectAnimator.setDuration(ANIMATION_TIME);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                topLayerParent.setClickable(true);
            }
        });

        int topTextStartX = isShow ? 0 : (int) (viewWidth * TOUCH_SCROLL_SCALE - calculateOffSideDistance());
        int topTextoffSide = isShow ? (int) (viewWidth * TOUCH_SCROLL_SCALE - calculateOffSideDistance()) : 0;

        topDescView.setClickable(false);
        ObjectAnimator topTextAnim = ObjectAnimator.ofFloat(topDescView, "translationX", topTextStartX, topTextoffSide);
        topTextAnim.setDuration(ANIMATION_TIME);
        topTextAnim.setInterpolator(new LinearInterpolator());
        topTextAnim.start();
        topTextAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                topDescView.setClickable(true);
            }
        });
    }

    private void addTopLayerView() {
        topLayerParent = new LinearLayout(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        topLayerParent.setLayoutParams(params);
        topLayerParent.setBackgroundColor(topLayerColor);
        topDescView = new TextView(getContext());
        topDescView.setTextSize(TypedValue.COMPLEX_UNIT_SP, topLayerDescSize);
        topDescView.setTextColor(topLayerDescColor);
        //  TODO: 16/7/28 提供方法给外界调用
        topDescView.setText(topLayerDesc);
        topDescView.setCompoundDrawablePadding(topLayerIconMarginDesc);
        LinearLayout.LayoutParams descLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        descLp.gravity = Gravity.CENTER_VERTICAL;
        descLp.leftMargin = topLayerIconMarginLeft;
        topDescView.setGravity(Gravity.CENTER);
        topDescView.setLayoutParams(descLp);
        Drawable topIconDrawable = getResources().getDrawable(topLayerIcon);
        if (null != topIconDrawable) {
            topIconDrawable.setBounds(0, 0, topIconDrawable.getMinimumWidth(), topIconDrawable.getMinimumHeight());
            topDescView.setCompoundDrawables(topIconDrawable, null, null, null);
        }
        topLayerParent.addView(topDescView);
        addView(topLayerParent);
    }

    private void addUnderIconView(int w) {
        underIconView = new ImageView(getContext());
        underIconView.setImageResource(underLayerIcon);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        Bitmap underBitmap = BitmapFactory.decodeResource(getResources(), underLayerIcon);
        int underBitmapWidth = 0;
        if (null != underBitmap) {
            underBitmapWidth = underBitmap.getWidth();
        }
        params.rightMargin = (int) (w * TOUCH_SCROLL_SCALE / 2) - underBitmapWidth / 2;
        underIconView.setLayoutParams(params);
        addView(underIconView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private static class ViewState {
        private static final int UN_FOLD = 11;
        private static final int FOLD = 12;
    }

    public void setScrollDeleteDesc(String desc) {
        this.desc = desc;
    }

    public interface OnScrollDeleteListener {
        void deleteAction();
    }

    public void setScrollDeleteListener(final OnScrollDeleteListener scrollDeleteListener) {
        this.scrollDeleteListener = scrollDeleteListener;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (null != topDescView) {
            topDescView.setText(desc);
        }
        if (null != underIconView) {
            underIconView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != scrollDeleteListener) {
                        scrollDeleteListener.deleteAction();
                    }
                }
            });
        }
    }
}
