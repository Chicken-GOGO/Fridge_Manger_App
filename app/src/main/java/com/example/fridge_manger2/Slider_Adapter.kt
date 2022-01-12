package com.example.fridge_manger2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter

class Slider_Adapter: PagerAdapter{

    private val context:Context

    private lateinit var layoutinflater:LayoutInflater

    constructor(context: Context) : super() {
        this.context = context
    }

    //content
    private val images:ArrayList<Int> = arrayListOf(R.drawable.interactive_manage_fridge,R.drawable.design_fridge,
        R.drawable.many_fridge,R.drawable.family_shared)
    private val features:ArrayList<Int> = arrayListOf(R.string.feature1,R.string.feature2,R.string.feature3,R.string.feature4)
    private val descriptions:ArrayList<Int> = arrayListOf(R.string.descript1,R.string.descript2,R.string.descript3,R.string.descript4)


    override fun getCount(): Int {
        return features.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutinflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)as LayoutInflater
        val view:View=layoutinflater.inflate(R.layout.slides_layout,container,false)

        val image:ImageView=view.findViewById(R.id.slider_image)
        val feature:TextView=view.findViewById(R.id.feature)
        val descripton:TextView=view.findViewById(R.id.descript)

        image.setImageResource(images[position])
        feature.setText(features[position])
        descripton.setText(descriptions[position])

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}