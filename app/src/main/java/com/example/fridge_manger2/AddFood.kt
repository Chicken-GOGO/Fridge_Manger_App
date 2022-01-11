package com.example.fridge_manger2

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*



class AddFood : AppCompatActivity() {

    class FoodInfo(val iconname: String) {
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
        setContentView(R.layout.activity_add_food)
        var i=0

        food_category().food.forEach{
            val foodicon:ImageView=findViewById(food_category().iconid[i])
            SetListener(it,foodicon)
            it.id=i+1
            //建立食物的文件 預計會把這些程式搬家

            db.collection("testbyalicia").document("${it.id}")
                .set(food_category().food[i])
            i++
        }

    }
    //listener section start
    @SuppressLint("ClickableViewAccessibility")
    fun  SetListener(food: FoodInfo,icon:ImageView)
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

    //待處理的bug:如果拖曳時間比較久食物不會跳到冰箱裡
    fun up(view: View?, event: MotionEvent, food:FoodInfo){
        val fridge=findViewById<ImageView>(R.id.refrigerator)
        val trashcan=findViewById<ImageView>(R.id.trashcan)
        when (mode) {
            mode_move -> {
                val reHeight=fridge.height/2   //移到冰箱外面自動跳到的高度位置
                val reWidth=fridge.width/2 //移到冰箱外面自動跳到的寬度位置

                if(view!!.x>(trashcan.left-5)&&view.x<(trashcan.right+5)&&
                    view.y>(trashcan.top-5)&&view.y<(trashcan.bottom+5))//拖到垃圾筒的範圍就刪除食物
                {
                    (view.getParent() as ViewGroup).removeView(view)
                    db.collection("testbyalicia").document("${food.id}")
                        .delete()
                }
                else
                {
                val x = if (view!!.x<fridge.left||view.x>fridge.right) //往右||往左移出冰箱外
                    (reWidth).toFloat()
                else
                    event.rawX - xToSubtract
                val y = if (view!!.y<fridge.top||view.y>fridge.bottom) //往下||往上移出冰箱外
                    (reHeight).toFloat()
                else event.rawY - yToSubtract
                view.x = x
                view.y = y

                //更新食物資訊
                food.x=view.x
                food.y=view.y
                    db.collection("testbyalicia").document("${food.id}")
                        .update(mapOf(
                            "x" to "${food.x}","y" to "${food.y}"
                        ))
                    editfood(food)//跳出食物資訊表格
                }

            }
            mode_none -> {
            }
        }
    }

    //listener section end
    fun editfood(food:FoodInfo){
        val DetailDialog = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme)
        val dialogLayout = layoutInflater.inflate(R.layout.food_detail_dialog,null)
        var namefromdialog=dialogLayout.findViewById<EditText>(R.id.nameinput)
        var amountfromdialog=dialogLayout.findViewById<EditText>(R.id.amount_input)
        var notefromdialog=dialogLayout.findViewById<EditText>(R.id.noteinput)

        //show today
        val date=dialogLayout.findViewById<Button>(R.id.expired_date)
        val currenttime=Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        date.text= df.format(currenttime)

            DetailDialog
                .setView(dialogLayout)
                .setPositiveButton("確定" ){ _, _ ->
                food.foodname=namefromdialog.text.toString()
                food.amount=amountfromdialog.text.toString()
                food.note=notefromdialog.text.toString()
                    //val info: TextView =findViewById(R.id.test)
                    db.collection("testbyalicia").document("${food.id}")
                        .update(mapOf(
                            "foodname" to "${food.foodname}","amount" to "${food.amount}","note" to "${food.note}",
                            "due_date" to "${food.due_date}"
                        ))
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(this, "${food.foodname} 新增成功", Toast.LENGTH_SHORT).show()
                            //info.text="${addname}"
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "${food.foodname} 新增失敗", Toast.LENGTH_SHORT).show()
                        }
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

    fun showcalendar(view:View)
    {


    }



    }

