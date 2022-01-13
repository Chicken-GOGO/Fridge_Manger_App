package com.example.fridge_manger2
import android.content.ContentValues
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog


class FilterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        val btn: Button = findViewById(R.id.button)
        var editText_search =findViewById<EditText>(R.id.editText_search)

        btn.setOnClickListener {
            var search_name = editText_search.text.toString()
            Toast.makeText(this, "篩選成功", Toast.LENGTH_SHORT).show()
        }
    }

}
        //declare filter dialog
//        fun show_filter_dialog(){
//            val filterDialog = AlertDialog.Builder(this,R.style.AlertDialogTheme)
//            val dialogLayout = layoutInflater.inflate(R.layout.filter_dialog, null)
//
//            var chip_group_date= dialogLayout.findViewById<EditText>(R.id.chip_group_date)
//            var chip_group_category= dialogLayout.findViewById<EditText>(R.id.chip_group_category)
//
//            filterDialog
//                .setPositiveButton("確定") { _, _ ->
//                    var search_name = editText_search.text.toString()
//                    Toast.makeText(this, "篩選成功 $search_name", Toast.LENGTH_SHORT).show()
//                    Log.d(ContentValues.TAG, "DocumentSnapshot data: $search_name")
//                    Log.d(ContentValues.TAG, "inputTestText is empty? ${search_name == ""}")
//                }
//                .setNegativeButton("取消") { _, _ ->
//                    Toast.makeText(this, "已取消篩選", Toast.LENGTH_SHORT).show()
//                }
//                .setView(R.layout.filter_dialog)
//                .create()
//                .show()
//        }
