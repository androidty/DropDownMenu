package com.ty.dropdowndemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toDropMenu(view: View) {
        startActivity(Intent(this, DropMenuActivity::class.java))
    }

    fun toDropScrollMenu(view: View) {
        startActivity(Intent(this, ScrollDropMenuActivity::class.java))
    }
}
