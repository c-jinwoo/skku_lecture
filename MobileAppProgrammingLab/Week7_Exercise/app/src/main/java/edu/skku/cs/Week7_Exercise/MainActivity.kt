package edu.skku.cs.Week7_Exercise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    companion object{
        const val EXT_TIME = "extra_key_time"
        const val EXT_DESC = "extra_key_desc"
    }

    override fun onResume() {
        super.onResume()
        val editTextTime = findViewById<EditText>(R.id.editTextTime)
        val editTextDesc = findViewById<EditText>(R.id.editTextDesc)
        editTextTime.text.clear()
        editTextDesc.text.clear()

        Log.v(localClassName, "@@@onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.v(localClassName, "@@@onStart")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.v(localClassName, "@@@onCreate")

        val editTextTime = findViewById<EditText>(R.id.editTextTime)
        val editTextDesc = findViewById<EditText>(R.id.editTextDesc)
        val buttonStart = findViewById<Button>(R.id.buttonStart)

        buttonStart.setOnClickListener {
            var time = editTextTime.text.toString()
            var desc = editTextDesc.text.toString()

            val intent = Intent(this, SelectActivity::class.java).apply{
                putExtra(EXT_TIME, time)
                putExtra(EXT_DESC, desc)
            }
            startActivity(intent)
        }
    }
}