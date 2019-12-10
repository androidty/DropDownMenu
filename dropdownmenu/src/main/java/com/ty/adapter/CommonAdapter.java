package com.ty.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author ty
 * @date 2019/11/22.
 * GitHub：
 * email：
 * description：
 */
public abstract class CommonAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    public CommonAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }
}
