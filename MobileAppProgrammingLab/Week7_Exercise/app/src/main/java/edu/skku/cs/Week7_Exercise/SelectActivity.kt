package edu.skku.cs.Week7_Exercise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import android.widget.TextView

class SelectActivity : AppCompatActivity() {
    override fun onRestart() {
        super.onRestart()
        this.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        val time = getIntent().getStringExtra(MainActivity.EXT_TIME)
        val desc = intent.getStringExtra(MainActivity.EXT_DESC)

        val textView = findViewById<TextView>(R.id.textView3)
        textView.setText("Do you want to set alarm on time ${time} with descripttion '${desc}'?")

        // Button Click Listener
        val btnOk = findViewById<Button>(R.id.buttonOk)
        val btnCancel = findViewById<Button>(R.id.buttonCancel)

        btnOk.setOnClickListener{
            val hr = time.toString().split(":")[0]
            val mn = time.toString().split(":")[1]
            val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply{
                putExtra(AlarmClock.EXTRA_MESSAGE, desc.toString())
                putExtra(AlarmClock.EXTRA_HOUR, hr.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, mn.toInt())
            }
            startActivity(intent)
        }

        btnCancel.setOnClickListener{
            finish()
        }

    }
}