package edu.skku.map.week6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    companion object{
        const val EXT_NAME = "extra_key_name"
        const val EXT_SID = "extra_key_student_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnNewActivity = findViewById<Button>(R.id.buttonNewActivity)
        btnNewActivity.setOnClickListener{
            val intent = Intent(this, NothingActivity::class.java).apply{
                putExtra(EXT_NAME, "ChoiJinWoo")
                putExtra(EXT_SID, 2020315798)
            }
            startActivity(intent)
        }
    }
}