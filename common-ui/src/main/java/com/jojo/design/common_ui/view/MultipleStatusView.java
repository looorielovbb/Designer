package com.jojo.design.common_ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jojo.design.common_ui.R;

import java.util.ArrayList;

/**
 * 描述：  一个方便在多种状态切换的view:包含loading_view，error_view，net_error，content_view，empty_view
 */
@SuppressWarnings("unused")
public class MultipleStatusView extends RelativeLayout {
    public static final int STATUS_CONTENT = 0x00;
    public static final int STATUS_LOADING = 0x01;
    public static final int STATUS_EMPTY = 0x02;
    public static final int STATUS_ERROR = 0x03;
    public static final int STATUS_NO_NETWORK = 0x04;
    private static final String TAG = "MultipleStatusView";
    private static final LayoutParams DEFAULT_LAYOUT_PARAMS =
            new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    private static final int NULL_RESOURCE_ID = -1;
    private final ArrayList<Integer> mOtherIds = new ArrayList<>();
    private View mEmptyView;
    private View mErrorView;
    private View mLoadingView;
    private View mNoNetworkView;
    private View mContentView;
    private final int mEmptyViewResId;
    private final int mErrorViewResId;
    private final int mLoadingViewResId;
    private final int mNoNetworkViewResId;
    private final int mContentViewResId;
    private int mViewStatus;
    private LayoutInflater mInflater;
    private OnClickListener mOnRetryClickListener;
    private final SparseArray<View> mViews = new SparseArray<>();

    public MultipleStatusView(Context context) {
        this(context, null);
    }

    public MultipleStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.multipleStatusView, defStyleAttr, 0);
        mEmptyViewResId = a.getResourceId(R.styleable.multipleStatusView_emptyView, R.layout.empty_view);
        mErrorViewResId = a.getResourceId(R.styleable.multipleStatusView_errorView, R.layout.error_view);
        mLoadingViewResId = a.getResourceId(R.styleable.multipleStatusView_loadingView, R.layout.loading_view);
        mNoNetworkViewResId = a.getResourceId(R.styleable.multipleStatusView_noNetworkView, R.layout.no_network_view);
        mContentViewResId = a.getResourceId(R.styleable.multipleStatusView_contentView, NULL_RESOURCE_ID);
        a.recycle();
        mInflater = LayoutInflater.from(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        showContent();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear(mEmptyView, mLoadingView, mErrorView, mNoNetworkView);
        mOtherIds.clear();
        if (null != mOnRetryClickListener) {
            mOnRetryClickListener = null;
        }
        mInflater = null;
    }

    /**
     * 获取当前状态
     */
    public int getViewStatus() {
        return mViewStatus;
    }

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    public void setOnRetryClickListener(OnClickListener onRetryClickListener) {
        this.mOnRetryClickListener = onRetryClickListener;
    }

    /**
     * 显示空视图
     */
    public final void showEmpty() {
        showEmpty(mEmptyViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示空视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showEmpty(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showEmpty(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示空视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showEmpty(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Empty view is null!");
        mViewStatus = STATUS_EMPTY;
        if (null == mEmptyView) {
            mEmptyView = view;
            View emptyRetryView = mEmptyView.findViewById(R.id.error_view);
            //设置点击重试的监听-需在视图中指定一个控件，设置其id为empty_retry_view才能生效
            if (null != mOnRetryClickListener && null != emptyRetryView) {
                emptyRetryView.setOnClickListener(mOnRetryClickListener);
            }
            mOtherIds.add(mEmptyView.getId());
            addView(mEmptyView, 0, layoutParams);
        }
        showViewById(mEmptyView.getId());
    }

    /**
     * 通过viewId获取控件,减少findViewById的次数
     */
    public <T extends View> T getView(View mConvertView, int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 自定义icon 和Text 显示多种类型的空视图
     */
    public final void showEmpty(int imgSrcId, String text) {
        View view = inflateView(mEmptyViewResId);
        ImageView ivEmptyImg = getView(view, R.id.iv_empty_img);
        TextView tvEmptyTxt = getView(view, R.id.tv_empty_txt);
        if (imgSrcId != 0) {
            ivEmptyImg.setImageResource(imgSrcId);
        }
        if (!TextUtils.isEmpty(text)) {
            tvEmptyTxt.setText(text);
        }
        checkNull(view, "Empty view is null!");
        mViewStatus = STATUS_EMPTY;
        if (null == mEmptyView) {
            mEmptyView = view;
            View emptyRetryView = mEmptyView.findViewById(R.id.ll_empty);
            //设置点击重试的监听-需在视图中指定一个控件，设置其id为empty_retry_view才能生效
            if (null != mOnRetryClickListener && null != emptyRetryView) {
                emptyRetryView.setOnClickListener(mOnRetryClickListener);
            }
            mOtherIds.add(mEmptyView.getId());
            addView(mEmptyView, 0, DEFAULT_LAYOUT_PARAMS);
        }
        showViewById(mEmptyView.getId());
    }

    /**
     * 显示错误视图
     */
    public final void showError() {
        showError(mErrorViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示错误视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showError(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showError(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示错误视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showError(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Error view is null!");
        mViewStatus = STATUS_ERROR;
        if (null == mErrorView) {
            mErrorView = view;
            View errorRetryView = mErrorView.findViewById(R.id.error_view);
            if (null != mOnRetryClickListener && null != errorRetryView) {
                errorRetryView.setOnClickListener(mOnRetryClickListener);
            }
            mOtherIds.add(mErrorView.getId());
            addView(mErrorView, 0, layoutParams);
        }
        showViewById(mErrorView.getId());
    }

    /**
     * 显示加载中视图
     */
    public final void showLoading() {
        showLoading(mLoadingViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示加载中视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showLoading(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showLoading(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示加载中视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showLoading(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "Loading view is null!");
        mViewStatus = STATUS_LOADING;
        if (null == mLoadingView) {
            mLoadingView = view;
            mOtherIds.add(mLoadingView.getId());
            addView(mLoadingView, 0, layoutParams);
        }
        showViewById(mLoadingView.getId());
    }

    /**
     * 显示无网络视图
     */
    public final void showNoNetwork() {
        showNoNetwork(mNoNetworkViewResId, DEFAULT_LAYOUT_PARAMS);
    }

    /**
     * 显示无网络视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    public final void showNoNetwork(int layoutId, ViewGroup.LayoutParams layoutParams) {
        showNoNetwork(inflateView(layoutId), layoutParams);
    }

    /**
     * 显示无网络视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    public final void showNoNetwork(View view, ViewGroup.LayoutParams layoutParams) {
        checkNull(view, "No network view is null!");
        mViewStatus = STATUS_NO_NETWORK;
        if (null == mNoNetworkView) {
            mNoNetworkView = view;
            View noNetworkRetryView = mNoNetworkView.findViewById(R.id.no_network_view);
            if (null != mOnRetryClickListener && null != noNetworkRetryView) {
                noNetworkRetryView.setOnClickListener(mOnRetryClickListener);
            }
            mOtherIds.add(mNoNetworkView.getId());
            addView(mNoNetworkView, 0, layoutParams);
        }
        showViewById(mNoNetworkView.getId());
    }

    /**
     * 显示内容视图
     */
    public final void showContent() {
        mViewStatus = STATUS_CONTENT;
        if (null == mContentView && mContentViewResId != NULL_RESOURCE_ID) {
            mContentView = mInflater.inflate(mContentViewResId, null);
            addView(mContentView, 0, DEFAULT_LAYOUT_PARAMS);
        }
        showContentView();
    }

    private void showContentView() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            view.setVisibility(mOtherIds.contains(view.getId()) ? View.GONE : View.VISIBLE);
        }
    }

    private View inflateView(int layoutId) {
        return mInflater.inflate(layoutId, null);
    }

    private void showViewById(int viewId) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            view.setVisibility(view.getId() == viewId ? View.VISIBLE : View.GONE);
        }
    }

    private void checkNull(Object object, String hint) {
        if (null == object) {
            throw new NullPointerException(hint);
        }
    }

    private void clear(View... views) {
        if (null == views) {
            return;
        }
        try {
            for (View view : views) {
                if (null != view) {
                    removeView(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}