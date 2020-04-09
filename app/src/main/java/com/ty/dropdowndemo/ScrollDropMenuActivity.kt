package com.ty.dropdowndemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_scroll_drop_menu.*

class ScrollDropMenuActivity : AppCompatActivity() {
    val strings = arrayOf("选择城市", "选择性别", "选择年龄", "选择年龄","选择年龄")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_drop_menu)


        initDropDownMenu()
    }

    fun initDropDownMenu(){
        mDropDownMenu1?.setDefaultStr(strings)
    }
}
