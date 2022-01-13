package com.example.fridge_manger2
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.security.Timestamp
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var onboardingscreen :SharedPreferences//lateinit:確定變數會被初始化 只能被用在會改變的屬性上
    class Food(val iconname: String) {
        var x=0f
        var y=0f
        var id=0
        var class_big=""
        var class_small=""
        var due_date=""
        var note=""
        var foodname=""
        var amount=""

    }
//    data class Food(){
//    val name: String? = null
//    val amount: Number? = null
//    val class_big: String?=null
//    val class_small: String?=null
//    val put_in_date: Timestamp? = null
//    val due_date: Timestamp? = null
//    val note: String? = null
//    val icon: String? = null
//    val x: Number? = null
//    val y: Number? = null
//
//    }

    private var xToSubtract = 0f
    private var yToSubtract = 0f
    var mode = 0
    //connect to firebase
    val db = Firebase.firestore

    companion object
    {
        val mode_none=0
        val mode_move=1

    }

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
        //declare add button
        val button: FloatingActionButton = findViewById(R.id.fab)
        button.setOnClickListener {
            showPopup(button)
        }

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


    private fun showPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.add_food)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem?->

            val text1=findViewById<TextView>(R.id.test)
            var big=""
            var small=""
            when (item!!.itemId) {
                R.id.cooking,R.id.dairy,R.id.delicatessen,R.id.dessert,R.id.drink
                ,R.id.fruit,R.id.grains,R.id.meat,R.id.protein,R.id.vegetable-> {
                        big=item.title.toString()
                }

            }

            text1.text=big
            true
        })
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem?->

            val text1 = findViewById<TextView>(R.id.test)
            val text2 = findViewById<TextView>(R.id.test2)
            var big = ""
            var small = ""

            when (item!!.itemId) {
                R.id.cooking, R.id.dairy, R.id.delicatessen, R.id.dessert, R.id.drink, R.id.fruit, R.id.grains, R.id.meat, R.id.protein, R.id.vegetable -> {
                    big = item.title.toString()
                }
                R.id.baozi, R.id.dish, R.id.dumpling, R.id.lunch_box,R.id.noodles,R.id.raw_dumpling,R.id.rice_ball,R.id.soup,
                R.id.chili,R.id.jam, R.id.kimchi,R.id.olive_oil,R.id.soy_sauce,
                R.id.cheese,R.id.milk,R.id.yogurt,
                R.id.apple_pie,R.id.birthday_cake,R.id.biscuit,R.id.candy,R.id.chocolate_bar,R.id.dount,R.id.ice_bar,R.id.ice_cream,R.id.panna_cotta,R.id.pudding,
                R.id.beer,R.id.bottle_drink,R.id.canned_drink,R.id.hand_shake,R.id.juice,R.id.soy_milk,R.id.water,
                R.id.apple,R.id.banana,R.id.cherry,R.id.lemon,R.id.mango,R.id.orange,R.id.peach,R.id.pear,R.id.pineapple,R.id.strawberry,R.id.watermelon,R.id.grape,R.id.guava,
                R.id.bread,R.id.corn,R.id.croissant,R.id.raw_rice,R.id.rice,R.id.sweet_potato,R.id.toast,
                R.id.beef,R.id.chicken,R.id.fish,R.id.mutton,R.id.pork,
                R.id.egg,R.id.tofu,R.id.sweet_pepper,R.id.broccoli,R.id.cabbage,R.id.carrot,R.id.lettuce,R.id.onion,R.id.potato,
                R.id.butter-> {
                    small = item.title.toString()
                    val ref = findViewById(R.id.constraintLayout) as ConstraintLayout
                    val icon= ImageView(this)
                    icon.setImageResource(R.drawable.chocolate_bar)
                    icon.layoutParams = LinearLayout.LayoutParams(150, 150)
                    icon.x = 400f
                    icon.y = 300f
                    ref.addView(icon)
                    val new_food=Food("123")
                    new_food.class_big=big
                    new_food.class_small=small
                    SetListener(new_food,icon)
                }
            }

            text1.text = big
            text2.text = small

            true
        })

        popup.show()
    }


    //from origin addfood activity
    @SuppressLint("ClickableViewAccessibility")
    fun  SetListener(food: Food, icon:ImageView)
    {
        icon.setOnTouchListener(
            object: View.OnTouchListener{
                override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                    when(event!!.action and MotionEvent.ACTION_MASK)
                    {
                        MotionEvent.ACTION_DOWN -> {    //第一根手指按下時觸發
                            mode = mode_move
                            down(view, event)
                        }
                        MotionEvent.ACTION_MOVE -> {    //螢幕上觸控點滑動時觸發
                            move(view, event)
                        }
                        MotionEvent.ACTION_UP -> {  //最後一根手指離開螢幕時觸發
                            up(view, event,food)

                        }
                    }
                    return true
                }

            })
    }

    fun down(view: View?, event: MotionEvent) {
        xToSubtract = event.rawX - view!!.x
        yToSubtract = event.rawY - view.y
    }
    fun move(view: View?, event: MotionEvent) {
        when (mode) {
            mode_move -> {
                view!!.x = event.rawX - xToSubtract
                view.y = event.rawY - yToSubtract
            }
            mode_none -> {
            }
        }
    }

    //待處理的bug:如果拖曳時間比較久食物不會跳到冰箱裡 拖到垃圾桶上半部不會刪除
    fun up(view: View?, event: MotionEvent, food: Food){
        val fridge=findViewById<ImageView>(R.id.refrigerator)
        val trashcan=findViewById<ImageView>(R.id.trashcan)
        when (mode) {
            mode_move -> {
                val reHeight=fridge.height/2   //移到冰箱外面自動跳到的高度位置
                val reWidth=fridge.width/2 //移到冰箱外面自動跳到的寬度位置

                if(view!!.x>(trashcan.left)&&view.x<(trashcan.right)&&
                    view.y>(trashcan.top)&&view.y<(trashcan.bottom))//拖到垃圾筒的範圍就刪除食物
                { // remove food
                    (view.getParent() as ViewGroup).removeView(view)
//                    db.collection("testbyalicia").document("${food.id}")
//                        .delete()
                }
                else
                {
                    val x = if (view.x<fridge.left||view.x>fridge.right) //往右||往左移出冰箱外
                        (reWidth).toFloat()
                    else
                        event.rawX - xToSubtract
                    val y = if (view.y<fridge.top||view.y>fridge.bottom) //往下||往上移出冰箱外
                        (reHeight).toFloat()
                    else event.rawY - yToSubtract
                    view.x = x
                    view.y = y

                    //更新食物資訊
                    food.x=view.x
                    food.y=view.y
//                    db.collection("testbyalicia").document("${food.id}")
//                        .update(mapOf(
//                            "x" to "${food.x}","y" to "${food.y}"
//                        ))
                    editfood(food)//跳出食物資訊表格
                }

            }
            mode_none -> {
            }
        }
    }


    fun editfood(food: Food){
        val DetailDialog = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
        val dialogLayout = layoutInflater.inflate(R.layout.food_detail_dialog,null)
        val namefromdialog=dialogLayout.findViewById<EditText>(R.id.nameinput)
        val amountfromdialog=dialogLayout.findViewById<EditText>(R.id.amount_input)
        val notefromdialog=dialogLayout.findViewById<EditText>(R.id.noteinput)

        //show today
        val date=dialogLayout.findViewById<Button>(R.id.expired_date)
        val currenttime= Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        date.text= df.format(currenttime)


        val docRef = db.collection("testbyalicia").document("${food.id}")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
//                    food.class_big=document.data?.get("class_big").toString()
//                    food.class_small=document.data?.get("class_small").toString()
                    dialogLayout.findViewById<TextView>(R.id.category_big).text=food.class_big
                    dialogLayout.findViewById<TextView>(R.id.category_small).text=food.class_small

                } else {
                    Toast.makeText(this, "No such document", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "get failed with${exception}", Toast.LENGTH_SHORT).show()
            }



        DetailDialog
            .setView(dialogLayout)
            .setPositiveButton("確定" ){ _, _ ->
                food.foodname=namefromdialog.text.toString()
                food.amount=amountfromdialog.text.toString()
                food.note=notefromdialog.text.toString()
                //upadte
//                db.collection("testbyalicia").document("${food.id}")
//                    .update(mapOf(
//                        "foodname" to "${food.foodname}","amount" to "${food.amount}","note" to "${food.note}",
//                        "due_date" to "${food.due_date}"
//                    ))
//                    .addOnSuccessListener { documentReference ->
//                        Toast.makeText(this, "${food.foodname} 新增成功", Toast.LENGTH_SHORT).show()
//
//                    }
//                    .addOnFailureListener { e ->
//                        Toast.makeText(this, "${food.foodname} 新增失敗", Toast.LENGTH_SHORT).show()
//                    }
            }
            .setNegativeButton("取消") { _, _ -> }
            .create()
        DetailDialog.show()

        date.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.show(supportFragmentManager, "tag");//show calendar
            //set time from user
            datePicker.addOnPositiveButtonClickListener {
                val currenttime=datePicker.selection
                val df = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                date.text= df.format(currenttime)
                food.due_date=date.text.toString()

            }
        }

    }





}




