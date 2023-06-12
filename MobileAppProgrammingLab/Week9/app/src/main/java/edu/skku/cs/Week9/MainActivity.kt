package edu.skku.cs.Week9 // please take care of the package name T.T

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val textView = findViewById<TextView>(R.id.textView)
        button.setOnClickListener {
            textView.text = getCurrentTimeString() // button pressed

            // 동시에 진행 안됨
            //Thread.sleep(5000)
            //textView.text = getCurrentTimeString() // after 5000 ms

            // UI쓰레드가 아닌 새로운 쓰레드로 진행 안됨
            /*
            val runnable = Runnable{
                Thread.sleep(5000)
                textView.text = getCurrentTimeString() // after 5000 ms
            }
            Thread(runnable).start() // start new thread
            */

            // Coroutine으로 진행
            CoroutineScope(Dispatchers.Main).launch(){
                textView.text = getCurrentTimeString() // button pressed
                textView.text = longTask().await() // wait 5000 ms then change
            }
        }
    }

    fun getCurrentTimeString(): String{
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(time)
    }

    fun longTask() : Deferred<String> = CoroutineScope(Dispatchers.IO).async{
        Thread.sleep(5000)
        getCurrentTimeString() // Just value to return result of async. Not return ***.
    }
}