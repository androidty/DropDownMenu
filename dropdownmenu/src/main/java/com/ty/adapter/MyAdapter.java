package com.ty.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.ty.dropdownmenu.R;

import java.util.List;

/**
 * @author ty
 * @date 2019/11/22.
 * GitHub：
 * email：
 * description：
 */
public class MyAdapter<DropBean> extends CommonAdapter<DropBean>{
    int[] heights ;
    public MyAdapter(int layoutResId, @Nullable List<DropBean> data) {
        super(layoutResId, data);
        heights = new int[data.size()];
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DropBean item) {
        helper.setText(R.id.tv_menu_item,"dsaklfj");
        helper.addOnClickListener(R.id.mmm);
    }


}
