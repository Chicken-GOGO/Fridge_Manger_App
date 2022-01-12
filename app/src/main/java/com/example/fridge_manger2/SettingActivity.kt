package com.example.fridge_manger2


import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SettingActivity : AppCompatActivity() {
    val db = Firebase.firestore
    var user_id = "mrZp5QeXB11QlRSk9mx9"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        //呼叫所有 dialog
        show_add_fridge_dialog()
        show_delete_fridge_dialog()
        show_line_dialog()
        start_design_activity()
        show_change_fridge_dialog()

        //setting icon button on app bar
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            actionBar.title = "設定"
//            actionBar.setDisplayHomeAsUpEnabled(true)
//        }
    }

    //需預先建立以 user id 編號的文件，否則會造成輸入資料失敗

    //新增共享冰箱，EditTextDialog
    private fun show_add_fridge_dialog() {
        val add_button = findViewById<TextView>(R.id.fridge_dialog_textview1)
        add_button.setOnClickListener {

            val addFridgeDialog = AlertDialog.Builder(this,R.style.AlertDialogTheme)
            val dialogLayout = layoutInflater.inflate(R.layout.dialog_add_fridge, null)
            var editText_id = dialogLayout.findViewById<EditText>(R.id.editText_id)
            var editText_name = dialogLayout.findViewById<EditText>(R.id.editText_name)

            addFridgeDialog
                .setTitle("輸入以下資訊以新增冰箱")
                .setView(dialogLayout)
                .setPositiveButton("確定") { _, _ ->
                    var added_id = editText_id.text.toString()
                    var added_name = editText_name.text.toString()
                    //add new element in to db (in nested map format)
                    db.collection("users").document("mrZp5QeXB11QlRSk9mx9")
                        .update(
                            mapOf(
                                "fridges.$added_id" to "$added_name"
                            )
                        )
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(this, "冰箱 $added_name 新增成功", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "冰箱 $added_name 新增失敗}", Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("取消") { _, _ ->
                    Toast.makeText(this, "取消輸入", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }

    //取消共享冰箱，MultiChoiceDialog
    private fun show_delete_fridge_dialog() {
        val edit_fridges_button = findViewById<TextView>(R.id.fridge_dialog_textview2)
        edit_fridges_button.setOnClickListener {

            //add button and get fridges data form db
            var fridges: Map<*, *>
            var fridges_names: Array<String>

            val docRef = db.collection("users").document(user_id)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        fridges = document.data?.get("fridges") as Map<*, *>
                        fridges_names =
                            (fridges as Map<*, *>).values.map { it.toString() }.toTypedArray()
                        Log.d(ContentValues.TAG, "DocumentSnapshot data: ${fridges}")

                        //set dialog
                        val fridgeNameDialog = AlertDialog.Builder(this,R.style.AlertDialogTheme)
                        val selected_index = ArrayList<Int>()


                        fridgeNameDialog
                            .setTitle("勾選將取消共享的冰箱")
                            .setMultiChoiceItems(fridges_names, null) { dialog, which, isChecked ->
                                if (isChecked) {
                                    selected_index.add(which)
//                                    val selected_name =  fridges_names.filterIndexed{ index, _ -> index in selected_index }
//                                    Toast.makeText(this, "您選擇了 $selected_name", Toast.LENGTH_SHORT).show()
                                } else if (selected_index.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    selected_index.remove(which)
                                }
                            }
                            .setNegativeButton("離開") { _, _ ->
                                Toast.makeText(
                                    this,
                                    "未取消共享冰箱",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .setPositiveButton("取消共享") { _, _ ->
                                //delete fridge form (users/$user_id/fridges-key)
                                val delete_fridges = hashMapOf<String, Any>()
                                var fridges_ids = fridges.keys.map { it.toString() }.toTypedArray()
                                val selected_ids = fridges_ids.filterIndexed { index, _ -> index in selected_index }
                                for (id in selected_ids) {
                                    delete_fridges.put("fridges.$id", FieldValue.delete())
                                    docRef.update(delete_fridges)
                                        .addOnSuccessListener {
                                            Log.d(
                                                ContentValues.TAG,
                                                "$delete_fridges DocumentSnapshot successfully deleted!"
                                            )
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(
                                                ContentValues.TAG,
                                                "Error deleting document",
                                                e
                                            )
                                        }
                                }
                                docRef.update(delete_fridges)
                                    .addOnFailureListener { e ->
                                        Log.w(
                                            ContentValues.TAG,
                                            "Error deleting document",
                                            e
                                        )
                                    }
                                    .addOnSuccessListener {
                                        Log.d(
                                            ContentValues.TAG,
                                            "$delete_fridges DocumentSnapshot successfully deleted!"
                                        )
                                        Toast.makeText(this, "取消共享冰箱成功", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            .create()
                            .show()
                    } else {
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
    }

    //綁定 line 帳號
    private fun show_line_dialog() {
        val add_button = findViewById<TextView>(R.id.line_dialog_textview)
        add_button.setOnClickListener {
            val lineDialog = AlertDialog.Builder(this,R.style.AlertDialogTheme)
            val dialogLayout = layoutInflater.inflate(R.layout.dialog_line, null)

            lineDialog
                .setView(dialogLayout)
                .setTitle("加入冰箱管家 LINE")
                .setNegativeButton("退出") { _, _ ->
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }

    //跳轉設定層板頁面
    private fun start_design_activity() {
        val button = findViewById<TextView>(R.id.fridge_setting_textview)
        button.setOnClickListener {
            val intent = Intent(this, DesignActivity::class.java)
            startActivity(intent)
        }
    }

    //切換冰箱介面
    private fun show_change_fridge_dialog() {
        val button = findViewById<TextView>(R.id.fridge_change_textview)
        button.setOnClickListener {
            //add button and get fridges data form db
            var fridges: Map<*, *>
            var fridges_names: Array<String>
            var fridges_ids: Array<String>

            val docRef = db.collection("users").document(user_id)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        fridges = document.data?.get("fridges") as Map<*, *>
                        fridges_names = (fridges as Map<*, *>).values.map { it.toString() }.toTypedArray()
                        fridges_ids = (fridges as Map<*, *>).keys.map { it.toString() }.toTypedArray()
                        Log.d(ContentValues.TAG, "DocumentSnapshot data: ${fridges}")

                        //set dialog
                        val singleChoiceDialog = AlertDialog.Builder(this,R.style.AlertDialogTheme)
                            .setTitle("選取要切換的冰箱")
                            .setSingleChoiceItems(fridges_names, 0) { _, i ->
                                Toast.makeText(this, "你選擇了 ${fridges_names[i]} 冰箱", Toast.LENGTH_SHORT).show()
                                Log.d(ContentValues.TAG, "choose fridge ID: ${fridges_ids[i]}")
                            }
                            .setPositiveButton("確定") { _, i ->
                                Toast.makeText(this, "切換冰箱成功", Toast.LENGTH_SHORT).show()
//
                            }
                            .setNegativeButton("取消") { _, _ ->
                                Toast.makeText(this, "取消切換冰箱", Toast.LENGTH_SHORT).show()
                            }
                            .create()
                            .show()
                    }
                    else {
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
    }
}
