package edu.skku.cs.Week7

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<Button>(R.id.buttonBrowse)

        btnStart.setOnClickListener {
            // Explicit Intent
            // val webIntent = Intent(Intent.ACTION_VIEW)
            // startActivity(webIntent)
            val urlEditText = findViewById<EditText>(R.id.editTextUrl)
            val uri = Uri.parse("https://" + urlEditText.text.toString())
            val webIntent = Intent(Intent.ACTION_VIEW, uri)
            webIntent.apply{
                putExtra("","")
            }
            startActivity(webIntent)
        }
    }
}