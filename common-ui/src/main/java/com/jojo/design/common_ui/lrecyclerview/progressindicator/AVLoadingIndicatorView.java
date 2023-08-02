package com.jojo.design.common_ui.lrecyclerview.progressindicator;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.jojo.design.common_ui.R;
import com.jojo.design.common_ui.lrecyclerview.progressindicator.indicators.LineScalePulseOutIndicator;

import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallBeat;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallClipRotate;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallClipRotateMultiple;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallClipRotatePulse;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallGridBeat;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallGridPulse;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallPulse;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallPulseRise;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallPulseSync;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallRotate;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallScale;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallScaleRipple;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallScaleRippleMultiple;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallSpinFadeLoader;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallTrianglePath;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallZigZag;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallZigZagDeflect;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.CubeTransition;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.LineScale;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.LineScaleParty;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.LineScalePulseOut;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.LineScalePulseOutRapid;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.LineSpinFadeLoader;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.Pacman;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.SemiCircleSpin;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.SquareSpin;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.BallScaleMultiple;
import static com.jojo.design.common_ui.lrecyclerview.recyclerview.ProgressStyle.TriangleSkewSpin;

public class AVLoadingIndicatorView extends View {

    private static final String TAG = "AVLoadingIndicatorView";

    private static final LineScalePulseOutIndicator DEFAULT_INDICATOR = new LineScalePulseOutIndicator();

    private static final int MIN_SHOW_TIME = 500; // ms
    private static final int MIN_DELAY = 500; // ms

    private long mStartTime = -1;

    private boolean mPostedHide = false;

    private boolean mPostedShow = false;

    private boolean mDismissed = false;

    private final Runnable mDelayedHide = new Runnable() {

        @Override
        public void run() {
            mPostedHide = false;
            mStartTime = -1;
            setVisibility(View.GONE);
        }
    };

    private final Runnable mDelayedShow = new Runnable() {

        @Override
        public void run() {
            mPostedShow = false;
            if (!mDismissed) {
                mStartTime = System.currentTimeMillis();
                setVisibility(View.VISIBLE);
            }
        }
    };

    int mMinWidth;
    int mMaxWidth;
    int mMinHeight;
    int mMaxHeight;

    private Indicator mIndicator;
    private int mIndicatorColor;

    private boolean mShouldStartAnimationDrawable;

    public AVLoadingIndicatorView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public AVLoadingIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, R.style.AVLoadingIndicatorView);
    }

    public AVLoadingIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, R.style.AVLoadingIndicatorView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AVLoadingIndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, R.style.AVLoadingIndicatorView);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mMinWidth = 24;
        mMaxWidth = 48;
        mMinHeight = 24;
        mMaxHeight = 48;

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.avLoadingIndicatorView, defStyleAttr, defStyleRes);

        mMinWidth = a.getDimensionPixelSize(R.styleable.avLoadingIndicatorView_minWidth, mMinWidth);
        mMaxWidth = a.getDimensionPixelSize(R.styleable.avLoadingIndicatorView_maxWidth, mMaxWidth);
        mMinHeight = a.getDimensionPixelSize(R.styleable.avLoadingIndicatorView_minHeight, mMinHeight);
        mMaxHeight = a.getDimensionPixelSize(R.styleable.avLoadingIndicatorView_maxHeight, mMaxHeight);
        String indicatorName = a.getString(R.styleable.avLoadingIndicatorView_indicatorName);
        mIndicatorColor = a.getColor(R.styleable.avLoadingIndicatorView_indicatorColor, getResources().getColor(R.color.color_app_yellow));
        setIndicator(indicatorName);
        if (mIndicator == null) {
            setIndicator(DEFAULT_INDICATOR);
        }
        a.recycle();
    }

    public Indicator getIndicator() {
        return mIndicator;
    }

    public void setIndicator(Indicator d) {
        if (mIndicator != d) {
            if (mIndicator != null) {
                mIndicator.setCallback(null);
                unscheduleDrawable(mIndicator);
            }

            mIndicator = d;
            //need to set indicator color again if you didn't specified when you update the indicator .
            setIndicatorColor(mIndicatorColor);
            if (d != null) {
                d.setCallback(this);
            }
            postInvalidate();
        }
    }


    /**
     * setIndicatorColor(0xFF00FF00)
     * or
     * setIndicatorColor(Color.BLUE)
     * or
     * setIndicatorColor(Color.parseColor("#FF4081"))
     * or
     * setIndicatorColor(0xFF00FF00)
     * or
     * setIndicatorColor(getResources().getColor(android.R.color.black))
     *
     * @param color
     */
    public void setIndicatorColor(int color) {
        this.mIndicatorColor = color;
        mIndicator.setColor(color);
    }


    /**
     * You should pay attention to pass this parameter with two way:
     * for example:
     * 1. Only class Name,like "SimpleIndicator".(This way would use default package name with
     * "com.wang.avi.indicators")
     * 2. Class name with full package,like "com.my.android.indicators.SimpleIndicator".
     *
     * @param indicatorName the class must be extend Indicator .
     */
    public void setIndicator(String indicatorName) {
        if (TextUtils.isEmpty(indicatorName)) {
            return;
        }
        StringBuilder drawableClassName = new StringBuilder();
        if (!indicatorName.contains(".")) {
            String defaultPackageName = getClass().getPackage().getName();
            drawableClassName.append(defaultPackageName)
                    .append(".indicators")
                    .append(".");
        }
        drawableClassName.append(indicatorName);
        try {
            Class<?> drawableClass = Class.forName(drawableClassName.toString());
            Indicator indicator = (Indicator) drawableClass.newInstance();
            setIndicator(indicator);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Didn't find your class , check the name again !" + indicatorName);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void smoothToShow() {
        startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        setVisibility(VISIBLE);
    }

    public void smoothToHide() {
        startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
        setVisibility(GONE);
    }

    public void hide() {
        mDismissed = true;
        removeCallbacks(mDelayedShow);
        long diff = System.currentTimeMillis() - mStartTime;
        if (diff >= MIN_SHOW_TIME || mStartTime == -1) {
            // The progress spinner has been shown long enough
            // OR was not shown yet. If it wasn't shown yet,
            // it will just never be shown.
            setVisibility(View.GONE);
        } else {
            // The progress spinner is shown, but not long enough,
            // so put a delayed message in to hide it when its been
            // shown long enough.
            if (!mPostedHide) {
                postDelayed(mDelayedHide, MIN_SHOW_TIME - diff);
                mPostedHide = true;
            }
        }
    }

    public void show() {
        // Reset the start time.
        mStartTime = -1;
        mDismissed = false;
        removeCallbacks(mDelayedHide);
        if (!mPostedShow) {
            postDelayed(mDelayedShow, MIN_DELAY);
            mPostedShow = true;
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == mIndicator
                || super.verifyDrawable(who);
    }

    void startAnimation() {
        if (getVisibility() != VISIBLE) {
            return;
        }

        if (mIndicator instanceof Animatable) {
            mShouldStartAnimationDrawable = true;
        }
        postInvalidate();
    }

    void stopAnimation() {
        if (mIndicator instanceof Animatable) {
            mIndicator.stop();
            mShouldStartAnimationDrawable = false;
        }
        postInvalidate();
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == GONE || v == INVISIBLE) {
                stopAnimation();
            } else {
                startAnimation();
            }
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    @Override
    public void invalidateDrawable(Drawable dr) {
        if (verifyDrawable(dr)) {
            final Rect dirty = dr.getBounds();
            final int scrollX = getScrollX() + getPaddingLeft();
            final int scrollY = getScrollY() + getPaddingTop();

            invalidate(dirty.left + scrollX, dirty.top + scrollY,
                    dirty.right + scrollX, dirty.bottom + scrollY);
        } else {
            super.invalidateDrawable(dr);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        updateDrawableBounds(w, h);
    }

    private void updateDrawableBounds(int w, int h) {
        // onDraw will translate the canvas so we draw starting at 0,0.
        // Subtract out padding for the purposes of the calculations below.
        w -= getPaddingRight() + getPaddingLeft();
        h -= getPaddingTop() + getPaddingBottom();

        int right = w;
        int bottom = h;
        int top = 0;
        int left = 0;

        if (mIndicator != null) {
            // Maintain aspect ratio. Certain kinds of animated drawables
            // get very confused otherwise.
            final int intrinsicWidth = mIndicator.getIntrinsicWidth();
            final int intrinsicHeight = mIndicator.getIntrinsicHeight();
            final float intrinsicAspect = (float) intrinsicWidth / intrinsicHeight;
            final float boundAspect = (float) w / h;
            if (intrinsicAspect != boundAspect) {
                if (boundAspect > intrinsicAspect) {
                    // New width is larger. Make it smaller to match height.
                    final int width = (int) (h * intrinsicAspect);
                    left = (w - width) / 2;
                    right = left + width;
                } else {
                    // New height is larger. Make it smaller to match width.
                    final int height = (int) (w * (1 / intrinsicAspect));
                    top = (h - height) / 2;
                    bottom = top + height;
                }
            }
            mIndicator.setBounds(left, top, right, bottom);
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTrack(canvas);
    }

    void drawTrack(Canvas canvas) {
        final Drawable d = mIndicator;
        if (d != null) {
            // Translate canvas so a indeterminate circular progress bar with padding
            // rotates properly in its animation
            final int saveCount = canvas.save();

            canvas.translate(getPaddingLeft(), getPaddingTop());

            d.draw(canvas);
            canvas.restoreToCount(saveCount);

            if (mShouldStartAnimationDrawable && d instanceof Animatable) {
                ((Animatable) d).start();
                mShouldStartAnimationDrawable = false;
            }
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dw = 0;
        int dh = 0;

        final Drawable d = mIndicator;
        if (d != null) {
            dw = Math.max(mMinWidth, Math.min(mMaxWidth, d.getIntrinsicWidth()));
            dh = Math.max(mMinHeight, Math.min(mMaxHeight, d.getIntrinsicHeight()));
        }

        updateDrawableState();

        dw += getPaddingLeft() + getPaddingRight();
        dh += getPaddingTop() + getPaddingBottom();

        final int measuredWidth = resolveSizeAndState(dw, widthMeasureSpec, 0);
        final int measuredHeight = resolveSizeAndState(dh, heightMeasureSpec, 0);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateDrawableState();
    }

    private void updateDrawableState() {
        final int[] state = getDrawableState();
        if (mIndicator != null && mIndicator.isStateful()) {
            mIndicator.setState(state);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);

        if (mIndicator != null) {
            mIndicator.setHotspot(x, y);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
        removeCallbacks();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        // This should come after stopAnimation(), otherwise an invalidate message remains in the
        // queue, which can prevent the entire view hierarchy from being GC'ed during a rotation
        super.onDetachedFromWindow();
        removeCallbacks();
    }

    private void removeCallbacks() {
        removeCallbacks(mDelayedHide);
        removeCallbacks(mDelayedShow);
    }

    public void setIndicatorId(int indicatorId) {
        String indicatorName = null;
        switch (indicatorId) {
            case BallPulse:
                indicatorName = "BallPulseIndicator";
                break;
            case BallGridPulse:
                indicatorName = "BallGridPulseIndicator";
                break;
            case BallClipRotate:
                indicatorName = "BallClipRotateIndicator";
                break;
            case BallClipRotatePulse:
                indicatorName = "BallClipRotatePulseIndicator";
                break;
            case SquareSpin:
                indicatorName = "SquareSpinIndicator";
                break;
            case BallClipRotateMultiple:
                indicatorName = "BallClipRotateMultipleIndicator";
                break;
            case BallPulseRise:
                indicatorName = "BallPulseRiseIndicator";
                break;
            case BallRotate:
                indicatorName = "BallRotateIndicator";
                break;
            case CubeTransition:
                indicatorName = "CubeTransitionIndicator";
                break;
            case BallZigZag:
                indicatorName = "BallZigZagIndicator";
                break;
            case BallZigZagDeflect:
                indicatorName = "BallZigZagDeflectIndicator";
                break;
            case BallTrianglePath:
                indicatorName = "BallTrianglePathIndicator";
                break;
            case BallScale:
                indicatorName = "BallScaleIndicator";
                break;
            case LineScale:
                indicatorName = "LineScaleIndicator";
                break;
            case LineScaleParty:
                indicatorName = "LineScalePartyIndicator";
                break;
            case BallScaleMultiple:
                indicatorName = "BallScaleMultipleIndicator";
                break;
            case BallPulseSync:
                indicatorName = "BallPulseSyncIndicator";
                break;
            case BallBeat:
                indicatorName = "BallBeatIndicator";
                break;
            case LineScalePulseOut:
                indicatorName = "LineScalePulseOutIndicator";
                break;
            case LineScalePulseOutRapid:
                indicatorName = "LineScalePulseOutRapidIndicator";
                break;
            case BallScaleRipple:
                indicatorName = "BallScaleRippleIndicator";
                break;
            case BallScaleRippleMultiple:
                indicatorName = "BallScaleRippleMultipleIndicator";
                break;
            case BallSpinFadeLoader:
                indicatorName = "BallSpinFadeLoaderIndicator";
                break;
            case LineSpinFadeLoader:
                indicatorName = "LineSpinFadeLoaderIndicator";
                break;
            case TriangleSkewSpin:
                indicatorName = "TriangleSkewSpinIndicator";
                break;
            case Pacman:
                indicatorName = "PacmanIndicator";
                break;
            case BallGridBeat:
                indicatorName = "BallGridBeatIndicator";
                break;
            case SemiCircleSpin:
                indicatorName = "SemiCircleSpinIndicator";
                break;
            default:
                indicatorName = "BallPulseIndicator";
                break;
        }
        setIndicator(indicatorName);
    }


}
