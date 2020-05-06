package com.ty.dropdowndemo

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ty.dropdowndemo.bean.DropBean
import com.ty.dropdowndemo.view.MyAdapter
import com.ty.dropdowndemo.view.MyDividerItemDecoration
import com.ty.listener.OnMenuClickListener
import kotlinx.android.synthetic.main.activity_dropmenu.*

class DropMenuActivity : AppCompatActivity() {
    val strings = arrayOf("选择城市选择城市", "选择性别", "选择年龄","价格趋势", "价格趋势")
    val arr1 = arrayOf("全部城市", "北京", "上海", "广州", "深圳","杭州")
    val arr2 = arrayOf("性别", "男", "女")
    val arr3 = arrayOf("全部年龄", "10", "20", "30", "40", "50", "60", "70", "80", "90")

    private var dropBeans1: ArrayList<DropBean>? = ArrayList()
    private var dropBeans2: ArrayList<DropBean>? = ArrayList()
    private var dropBeans3: ArrayList<DropBean>? = ArrayList()


    private var mDropDownView: View? = null
    var mShadowRl: RelativeLayout? = null
    var mDropRlv: RecyclerView? = null
    private var dropAdapter: MyAdapter<DropBean>? = null

    var option1: String? = null
    var option2: String? = null
    var option3: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dropmenu)
        initData()
        initDropDownMenu()

    }

    private fun initData() {
        for (name in arr1) {
            dropBeans1?.add(DropBean(name))
        }
        for (name in arr2) {
            dropBeans2?.add(DropBean(name))
        }
        for (name in arr3) {
            dropBeans3?.add(DropBean(name))
        }
//        Log.d("getJsonFromAssets" , getJsonFromAssets("drop.json"))
    }

    private fun initDropDownMenu() {
        mDropDownMenu?.initMenu(strings, false)
        mDropDownMenu?.onMenuClickListener = object : OnMenuClickListener {
            override fun onMenuClickListener(context: Context, index: Int) {
                setDropList(context, index)
            }
        }
    }

    fun setDropList(context: Context, index: Int) {

        if (mDropDownView == null) {
            mDropDownView = LayoutInflater.from(this).inflate(R.layout.popupwindow_menu, null)
        }
        if (mShadowRl == null) {
            mShadowRl = mDropDownView!!.findViewById(R.id.rl_menu_shadow)
            mShadowRl!!.setBackgroundColor(Color.parseColor("#11000000"))
            mShadowRl?.setOnClickListener{mDropDownMenu.dismiss()}
        }
        if (mDropRlv == null) {
            mDropRlv = mDropDownView!!.findViewById(R.id.mDropRlv)
        }
        mDropDownMenu?.setDropWindow(mDropDownView!!)
        if (mDropRlv!!.itemDecorationCount == 0) {
            mDropRlv!!.addItemDecoration(MyDividerItemDecoration(this, null, 1, 15, 15, true))
        }
        mDropRlv!!.layoutManager = LinearLayoutManager(this)
        if (dropAdapter == null) {
            dropAdapter = MyAdapter(R.layout.menu_list_item, dropBeans1)
        }
        mDropRlv!!.adapter = dropAdapter


        when (index) {
            0 -> {
                dropAdapter?.setNewData(dropBeans1)
            }
            1 -> {
                dropAdapter?.setNewData(dropBeans2)
            }
            2 -> {
                dropAdapter?.setNewData(dropBeans3)
            }
            else -> {
                dropAdapter?.setNewData(dropBeans1)
            }
        }

        //下拉列表点击事件
        dropAdapter!!.setOnItemClickListener { adapter, view, position ->
            (adapter.data[position] as DropBean).name.let { mDropDownMenu.setCurrentTitle(index, it!!) }
            when (index) {
                0 -> option1 = (adapter.data[position] as DropBean).name
                1 -> option2 = (adapter.data[position] as DropBean).name
                2 -> option3 = (adapter.data[position] as DropBean).name
            }
            setSelect(position, adapter.data as List<DropBean>)
//            mPopupWindow!!.dismiss()
            mDropDownMenu.dismiss()
            Toast.makeText(DropMenuActivity@ this, "$option1 $option2 $option3", Toast.LENGTH_LONG).show()
        }
    }

    private fun setSelect(pos: Int, data: List<DropBean>) {
        for (dropBean in data) {
            dropBean.isSelect = false
        }
        data[pos].isSelect = true
    }
}
