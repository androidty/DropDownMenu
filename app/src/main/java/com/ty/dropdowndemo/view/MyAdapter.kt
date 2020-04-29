package com.ty.dropdowndemo.view

import com.chad.library.adapter.base.BaseViewHolder
import com.ty.adapter.CommonAdapter
import com.ty.dropdowndemo.bean.DropBean
import com.ty.dropdownmenu.R

/**
 * @author ty
 * @date 2019/11/22.
 * GitHub：
 * email：
 * description：
 */
class MyAdapter<Item: DropBean>(layoutResId: Int, data: List<DropBean>?) : CommonAdapter<DropBean>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder, item: DropBean) {
        helper.setText(R.id.tv_menu_item, item.name)
        helper.setGone(R.id.iv_menu_select, item.isSelect!!)
    }


}
