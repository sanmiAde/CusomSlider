package com.sanmi.cusomslider

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sanmi_custom_slider.addListener { color ->
            Toast.makeText(this, "$color", Toast.LENGTH_SHORT).show()
        }


        button.setOnClickListener {
            sanmi_custom_slider.removeListener()
        }

    }
}
