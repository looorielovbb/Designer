package com.jojo.design.common_base.adapter.rv;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhy on 16/4/9.
 * 支持多种ItemViewType的Adapter：每种Item类型对应一个ItemViewDelegete
 * 对于多中item_view_type的处理参考：<a href="https://github.com/sockeqwe/AdapterDelegates">AdapterDelegates</a> ，有极高的扩展性。
 */
public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected List<T> mDatas = new ArrayList<>();

    protected ItemViewDelegateManager<T> mItemViewDelegateManager;
    protected OnItemClickListener mOnItemClickListener;

    public MultiItemTypeAdapter(Context mContext) {
        this.mContext = mContext;
        mItemViewDelegateManager = new ItemViewDelegateManager<T>();
    }

    public MultiItemTypeAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
        mItemViewDelegateManager = new ItemViewDelegateManager<T>();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewDelegate<T> itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder, holder.getConvertView());
        setListener(parent, holder, viewType);
        return holder;
    }

    public void onViewHolderCreated(ViewHolder holder, View itemView) {

    }

    public void convert(ViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getBindingAdapterPosition());
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                int position = viewHolder.getBindingAdapterPosition();
                mOnItemClickListener.onItemClick(v, viewHolder, position);
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(v -> {
            if (mOnItemClickListener != null) {
                int position = viewHolder.getBindingAdapterPosition();
                return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
            }
            return false;
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        convert(holder, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public List<T> getDatas() {
        return mDatas;
    }

    public MultiItemTypeAdapter<T> addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter<T> addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 设置适配器的数据，添加数据
     *
     * @param dataList 数据
     */
    public void update(@NonNull List<T> dataList) {
        mDatas.addAll(dataList);
        notifyItemRangeChanged(0,dataList.size(),null);
    }

    /**
     * 设置适配器数据
     *
     * @param dataList 数据集
     * @param isClear  是否需要清空list然后在加载数据
     */
    public void update(List<T> dataList, Boolean isClear) {
        if (isClear) {
            clear();
        }
        if (dataList != null) {
            mDatas.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除集合数据
     */
    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (mDatas != null && mDatas.size() > 0) {
            // 数据移除
            this.mDatas.remove(position);
            // 界面移除
            notifyItemRemoved(position - 1);
            // 刷新位置
            if (position != (mDatas.size())) { // 如果移除的是最后一个，忽略
                notifyItemRangeChanged(position - 1, getItemCount() - position);
            }
        }
    }

    public List<T> getDataList() {
        return mDatas;
    }

    /**
     * 清空集合，设置适配器数据
     *
     * @param list
     */
    public void setDataList(Collection<T> list) {
        this.mDatas.clear();
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }
}
