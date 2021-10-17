package com.example.fridge_manger2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator

class Onboarding : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val springDotsIndicator = findViewById<SpringDotsIndicator>(R.id.spring_dots_indicator)
        val viewPager = findViewById<ViewPager>(R.id.slider)
        val adapter = Slider_Adapter(this)
        viewPager.adapter = adapter
        springDotsIndicator.setViewPager(viewPager)
    }
    fun skip(view: View) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}
