package com.jojo.design.common_base.adapter.rv;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Created by zhy on 16/4/9.
 * RecyclerView的CommonAdapter 正常的CommonAdapter
 * 备注：lv包下的是ListView装配时使用的通用Adapter，使用见lv包下的CommonAdapterListView的备注信息
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public CommonAdapter(Context context) {
        super(context);
        mContext = context;
        mInflater = LayoutInflater.from(context);
//        mLayoutId = layoutId;
//        mDatas = datas;
        //返回：MultiItemTypeAdapter
        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return itemLayoutId();
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract int itemLayoutId();

    protected abstract void convert(ViewHolder holder, T t, int position);

    /**
     * 设置适配器的数据，添加数据
     * @param dataList 数据
     */
    public void update(@NonNull List<T> dataList) {
        mDatas.addAll(dataList);
        notifyItemRangeChanged(0,dataList.size(),null);
    }

    /**
     * 设置适配器数据
     * @param dataList 数据
     * @param isClear  是否需要清空list然后在加载数据
     */
    public void update(@NonNull List<T> dataList, Boolean isClear) {
        if (isClear) {
            clear();
        }
        mDatas.addAll(dataList);
        notifyItemRangeChanged(0,dataList.size(),null);
    }

    /**
     * 单条刷新
     * @param dataList 数据
     * @param isClear  是否需要清空list然后在加载数据
     */
    public void update(List<T> dataList, Boolean isClear, int position) {
        if (isClear) {
            clear();
        }
        if (dataList != null) {
            mDatas.addAll(dataList);
        }
        notifyItemChanged(position);
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
            this.mDatas.remove(position - 1);
            // 界面移除
            notifyItemRemoved(position - 1);
            notifyItemRangeChanged(0, getItemCount());
//            notifyDataSetChanged();
            // 刷新位置
//            if (position != (mDatas.size())) { // 如果移除的是最后一个，忽略
//                notifyItemRangeChanged(position - 1, getItemCount() - position + 1);
//            }
        }
    }

    public List<T> getDataList() {
        return mDatas;
    }

    /**
     * 清空集合，设置适配器数据
     *
     * @param list 数据
     */
    public void setDataList(Collection<T> list) {
        this.mDatas.clear();
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }
}
