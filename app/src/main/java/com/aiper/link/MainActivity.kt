package com.aiper.link

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // base
        Log.e("TAG","base : ${com.aiper.base.MyClass.NAME}")
        Log.e("TAG","common-ui : ${com.aiper.ui.MyClass.NAME}")

        // business
        Log.e("TAG","customer : ${com.aiper.customer.MyClass.NAME}")
        Log.e("TAG","device-common : ${com.aiper.device.common.MyClass.NAME}")
        Log.e("TAG","device-hydrocomm : ${com.aiper.device.hydrocomm.MyClass.NAME}")
        Log.e("TAG","device-i : ${com.aiper.device.i.MyClass.NAME}")
        Log.e("TAG","device-r : ${com.aiper.device.r.MyClass.NAME}")
        Log.e("TAG","device-s : ${com.aiper.device.s.MyClass.NAME}")
        Log.e("TAG","device-x : ${com.aiper.device.x.MyClass.NAME}")
        Log.e("TAG","mine : ${com.aiper.mine.MyClass.NAME}")
        Log.e("TAG","sobot-chat : ${com.aiper.sobot.chat.MyClass.NAME}")

        // common-service
        Log.e("TAG","database : ${com.aiper.database.MyClass.NAME}")
        Log.e("TAG","device-api : ${com.aiper.device.api.MyClass.NAME}")

        // features
        Log.e("TAG","weather : ${com.aiper.weather.MyClass.NAME}")
        Log.e("TAG","ymodem : ${com.bw.yml.MyClass.NAME}")

    }
}