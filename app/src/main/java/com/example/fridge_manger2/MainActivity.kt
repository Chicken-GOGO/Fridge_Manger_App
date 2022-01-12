package com.example.fridge_manger2
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase




class MainActivity : AppCompatActivity() {
    lateinit var onboardingscreen :SharedPreferences//lateinit:確定變數會被初始化 只能被用在會改變的屬性上

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        //connect to firebase
        val db = Firebase.firestore


        //讓Onboarding只顯示一次
        onboardingscreen = getSharedPreferences("onboardingscreen", MODE_PRIVATE)
        var isfirsttime: Boolean = onboardingscreen.getBoolean("firsttime", true)
        if (isfirsttime) {
            var editor: SharedPreferences.Editor = onboardingscreen.edit()
            editor.putBoolean("firsttime", false)
            editor.commit()

            var intent: Intent = Intent(this, Onboarding::class.java)
            startActivity(intent)
            finish()
        }

        //Build bottom app bar

        //declare filter dialog
        val filterDialog = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setPositiveButton("確定") { _, _ ->
                Toast.makeText(this, "篩選成功", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消") { _, _ ->
                Toast.makeText(this, "已取消篩選", Toast.LENGTH_SHORT).show()
            }
            .setView(R.layout.filter_dialog)
            .create()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.filter -> {
                    filterDialog.show()
                }
                R.id.home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.setting -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
            return@setOnItemSelectedListener true
        }
    }
}




