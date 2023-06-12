package edu.skku.map.week6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class NothingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nothing)

        val name = getIntent().getStringExtra(MainActivity.EXT_NAME)
        val sid = intent.getIntExtra(MainActivity.EXT_SID, -1)

        Toast.makeText(
            applicationContext,
            "Welcome, ${name}(${sid})!",
            Toast.LENGTH_SHORT
        ).show()
    }
}