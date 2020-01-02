package com.ty.dropdowndemo

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ty.DropBean
import com.ty.adapter.MyAdapter
import com.ty.dropdowndemo.view.MyDividerItemDecoration
import com.ty.listener.OnMenuClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var city_index: Int = 0
    private var sex_index: Int = 0
    private var age_index: Int = 0
    internal var items: MutableList<Array<String>> = ArrayList()
    val arr1 = arrayOf("全部城市", "北京", "上海", "广州", "深圳")
    val arr2 = arrayOf("性别", "男", "女")
    val arr3 = arrayOf("全部年龄", "10", "20", "30", "40", "50", "60", "70", "80", "90")
    private var recyclerView: RecyclerView? = null
    private var dropBeans1: ArrayList<DropBean>? = ArrayList()
    private var dropBeans2: ArrayList<DropBean>? = ArrayList()
    private var dropBeans3: ArrayList<DropBean>? = ArrayList()
    private var adapter1: MyAdapter<DropBean>? = null
    private var adapter2: MyAdapter<DropBean>? = null
    private var adapter3: MyAdapter<DropBean>? = null

    internal val strings = arrayOf("选择城市", "选择性别", "选择年龄")

    var mDropDownView: View? = null
    var mShadowRl: RelativeLayout? = null
    var mPopupWindow: PopupWindow? = null
    var mDropRlv: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        initDropDownMenu()
    }

    private fun initData() {
        dropBeans1?.add(DropBean())
        dropBeans1?.add(DropBean())
        dropBeans1?.add(DropBean())

        dropBeans2?.add(DropBean())
        dropBeans2?.add(DropBean())
        dropBeans2?.add(DropBean())
        dropBeans2?.add(DropBean())


        dropBeans3?.add(DropBean())
        dropBeans3?.add(DropBean())
        dropBeans3?.add(DropBean())
        dropBeans3?.add(DropBean())
        dropBeans3?.add(DropBean())


    }

    private fun initDropDownMenu() {
        mDropDownMenu!!.setMenuCount(3)
        mDropDownMenu?.setDefaultMenuTitle(strings)
        mDropDownMenu?.onMenuClickListener = object : OnMenuClickListener {
            override fun onMenuClickListener(context: Context, index: Int) {
                setDropList(context, index)
            }
        }
    }

    private fun setDropList(context: Context, index: Int) {
        if (mDropDownView == null) {
            mDropDownView = LayoutInflater.from(this).inflate(R.layout.popupwindow_menu, null)
        }
        if (mPopupWindow == null) {
            mPopupWindow = PopupWindow(mDropDownView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false)
        }
        if (mShadowRl == null) {
            mShadowRl = mDropDownView!!.findViewById(R.id.rl_menu_shadow)
            mShadowRl!!.setBackgroundColor(Color.parseColor("#33000000"))
        }
        if (mDropRlv == null) {
            mDropRlv = mDropDownView!!.findViewById(R.id.mDropRlv)
        }
        mDropDownMenu?.setDropWindow(mPopupWindow!!, mShadowRl!!)

        mDropRlv!!.addItemDecoration(MyDividerItemDecoration(this, null, 0, 15, 15, false))
        mDropRlv!!.layoutManager = LinearLayoutManager(this)
        if (adapter1 == null) {
            adapter1 = MyAdapter(R.layout.menu_list_item, dropBeans1)
        }
        mDropRlv!!.adapter = adapter1
        adapter1!!.setOnItemClickListener { adapter, view, position ->
            //            Toast.makeText(this, position, Toast.LENGTH_LONG).show()
            setSelect(position, adapter.data as List<DropBean>)
            mPopupWindow!!.dismiss()
        }

        when (index) {
            0 -> {
                adapter1?.setNewData(dropBeans1)
            }
            1 -> {
                adapter1?.setNewData(dropBeans2)
            }
            2 -> {
                adapter1?.setNewData(dropBeans3)
            }
            else -> {
            }
        }
    }


    private fun setSelect(pos: Int, data: List<DropBean>) {
        for (dropBean in data) {
            dropBean.isSelect = false
        }
        data[pos].isSelect = true
    }


}
