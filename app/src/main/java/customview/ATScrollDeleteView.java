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
import android.os.Handler;
import android.os.Message;
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
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;


/**
 * Created by jsion on 16/7/27.
 */
public class ATScrollDeleteView extends RelativeLayout {
    private static final float TOUCH_SCROLL_SCALE = 1 / 5.F;
    private static final int ANIMATION_TIME = 300;
    private static final int READY_SET_DATA = 111;
    private int topLayerColor;
    private int topLayerIcon;
    private String topLayerDesc;
    private int topLayerDescColor;
    private int topLayerDescAnotherSize;
    private int topLayerDescAnotherColor;
    private int topLayerDescSize;
    private int topLayerDescMargin;
    private int topLayerIconMarginDesc;
    private int topLayerIconMarginLeft;
    private int underLayerColor;
    private int underLayerIcon;

    private int viewWidth;
    private int viewHeight;

    private ImageView underIconView;
    private LinearLayout topLayerParent;
    private int viewState;
    private String[] mDesc;
    private OnScrollDeleteListener scrollDeleteListener;
    private LinearLayout descParent;
    private ImageView topIconView;
    private WeakRefHander weakRefHander;

    public ATScrollDeleteView(Context context) {
        this(context, null);
    }

    public ATScrollDeleteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ATScrollDeleteView(Context context, AttributeSet attrs, int defStyleAttr) {
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
                case R.styleable.ATScrollDeleteView_top_layer_desc_another_color:
                    topLayerDescAnotherColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATScrollDeleteView_top_layer_desc_another_size:
                    topLayerDescAnotherSize = typedArray.getInteger(attr, 0);
                    break;
                case R.styleable.ATScrollDeleteView_top_layer_icon_margin_left:
                    topLayerIconMarginLeft = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.ATScrollDeleteView_top_layer_icon_margin_desc:
                    topLayerIconMarginDesc = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.ATScrollDeleteView_top_layer_desc_margin:
                    topLayerDescMargin = typedArray.getDimensionPixelOffset(attr, 0);
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
        weakRefHander = new WeakRefHander(this);
        weakRefHander.sendEmptyMessage(READY_SET_DATA);
    }

    private int calculateOffSideDistance() {
        Bitmap topLayerBitmap = BitmapFactory.decodeResource(getResources(), topLayerIcon);
        if (null != topLayerBitmap) {
            return topLayerBitmap.getWidth() + topLayerIconMarginLeft;
        }
        return 0;
    }

    private void addViewListener() {
        topIconView.setOnClickListener(new OnClickListener() {
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

        descParent.setClickable(false);
        ObjectAnimator topTextAnim = ObjectAnimator.ofFloat(descParent, "translationX", topTextStartX, topTextoffSide);
        topTextAnim.setDuration(ANIMATION_TIME);
        topTextAnim.setInterpolator(new LinearInterpolator());
        topTextAnim.start();
        topTextAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                descParent.setClickable(true);
            }
        });


        ObjectAnimator topIconViewAni = ObjectAnimator.ofFloat(topIconView, "translationX", topTextStartX, topTextoffSide);
        topIconViewAni.setDuration(ANIMATION_TIME);
        topIconViewAni.setInterpolator(new LinearInterpolator());
        topIconViewAni.start();

    }

    private void addTopLayerView() {
        topLayerParent = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        topLayerParent.setLayoutParams(params);
        topLayerParent.setBackgroundColor(topLayerColor);

        topIconView = new ImageView(getContext());
        topIconView.setImageResource(topLayerIcon);
        LinearLayout.LayoutParams topIconParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        topIconParam.gravity = Gravity.CENTER_VERTICAL;
        topIconParam.leftMargin = topLayerIconMarginLeft;
        topIconView.setLayoutParams(topIconParam);

        descParent = new LinearLayout(getContext());
        descParent.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams descParentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        descParentParams.gravity = Gravity.CENTER_VERTICAL;
        descParentParams.leftMargin = topLayerIconMarginDesc;
        descParent.setLayoutParams(descParentParams);

        topLayerParent.addView(topIconView);
        topLayerParent.addView(descParent);
        addView(topLayerParent);
    }

    private void addUnderIconView(int w) {
        underIconView = new ImageView(getContext());
        underIconView.setImageResource(underLayerIcon);
        underIconView.setScaleType(ImageView.ScaleType.CENTER);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, (int) (viewWidth * TOUCH_SCROLL_SCALE));
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

    public void setScrollDeleteDesc(String... descs) {
        Logger.e("--->>descParent" + descParent);
        this.mDesc = descs;
    }

    private TextView createTextView(String desc, int textSize, int textColor, int topMargin) {
        TextView topDescView = new TextView(getContext());
        topDescView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        topDescView.setTextColor(textColor);
        topDescView.setText(desc);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = topMargin;
        topDescView.setLayoutParams(lp);
        return topDescView;
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
        Logger.e("--->>mDesc" + mDesc);
        setDataAndListener();

    }

    private void setDataAndListener() {
        if (null != descParent) {
            descParent.removeAllViews();
            if (null != mDesc && mDesc.length > 0) {
                int length = mDesc.length;
                for (int i = 0; i < length; i++) {
                    String currentDesc = mDesc[i];
                    TextView currentTextView;
                    if (0 == i) {
                        currentTextView = createTextView(currentDesc, topLayerDescSize, topLayerDescColor, 0);
                    } else {
                        currentTextView = createTextView(currentDesc, topLayerDescAnotherSize, topLayerDescAnotherColor, topLayerDescMargin);
                    }
                    descParent.addView(currentTextView);
                }
            }
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

    private static class WeakRefHander extends Handler {
        WeakReference<ATScrollDeleteView> mHomeFragmentWeakReference;

        WeakRefHander(ATScrollDeleteView atHomeFragment) {
            mHomeFragmentWeakReference = new WeakReference<ATScrollDeleteView>(atHomeFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final ATScrollDeleteView atHomeFragment = mHomeFragmentWeakReference.get();
            if (atHomeFragment != null) {
                switch (msg.what) {
                    case READY_SET_DATA:
                        if (null != atHomeFragment.descParent) {
                            atHomeFragment.descParent.removeAllViews();
                            if (null != atHomeFragment.mDesc && atHomeFragment.mDesc.length > 0) {
                                int length = atHomeFragment.mDesc.length;
                                for (int i = 0; i < length; i++) {
                                    String currentDesc = atHomeFragment.mDesc[i];
                                    TextView currentTextView;
                                    if (0 == i) {
                                        currentTextView = new TextView(atHomeFragment.getContext());
                                        currentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, atHomeFragment.topLayerDescSize);
                                        currentTextView.setTextColor(atHomeFragment.topLayerDescColor);
                                        currentTextView.setText(currentDesc);
                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        lp.topMargin = 0;
                                        currentTextView.setLayoutParams(lp);
                                    } else {
                                        currentTextView = new TextView(atHomeFragment.getContext());
                                        currentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, atHomeFragment.topLayerDescAnotherSize);
                                        currentTextView.setTextColor(atHomeFragment.topLayerDescAnotherColor);
                                        currentTextView.setText(currentDesc);
                                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        lp.topMargin = atHomeFragment.topLayerDescMargin;
                                        currentTextView.setLayoutParams(lp);
                                    }
                                    atHomeFragment.descParent.addView(currentTextView);
                                }
                            }
                        }
                        if (null != atHomeFragment.underIconView) {
                            atHomeFragment.underIconView.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (null != atHomeFragment.scrollDeleteListener) {
                                        atHomeFragment.scrollDeleteListener.deleteAction();
                                    }
                                }
                            });
                        }
                        break;
                }
            }
        }
    }

}
