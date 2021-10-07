package com.example.fridge_manger2
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fridge_manger2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var onboardingscreen :SharedPreferences//lateinit:確定變數會被初始化 只能被用在會改變的屬性上
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        //讓Onboarding只顯示一次
        onboardingscreen=getSharedPreferences("onboardingscreen", MODE_PRIVATE)
        var isfirsttime:Boolean=onboardingscreen.getBoolean("firsttime",true)
        if(isfirsttime)
        {
            var editor:SharedPreferences.Editor=onboardingscreen.edit()
            editor.putBoolean("firsttime",false)
            editor.commit()

            var intent:Intent= Intent(this,Onboarding::class.java)
            startActivity(intent)
            finish()
        }
    }
    //make customize app bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    //this function trigger alert dialog for appbar icons
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //trigger filter dialog by clicking filter icon on app bar
        val filterDialog = AlertDialog.Builder(this)
            .setPositiveButton("確定") { _, _ -> Toast.makeText(this, "篩選成功", Toast.LENGTH_SHORT).show() }
            .setNegativeButton("取消") { _, _ -> Toast.makeText(this, "已取消篩選", Toast.LENGTH_SHORT).show() }
            .setView(R.layout.filter_dialog)
            .create()
        when (item.itemId) {
            R.id.miFilter -> filterDialog.show()
        }
        return true
    }
}