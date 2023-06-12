package edu.skku.cs.Week9_Exercise // please take care of the package name T.T

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val textView = findViewById<TextView>(R.id.textView)
        button.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch(){
                textView.text = longTask().await()
            }
        }
    }
    fun longTask() = CoroutineScope(Dispatchers.Default).async{
        var totalCount = 0
        var innerCount = 0
        val random = Random(System.currentTimeMillis())
        for(bigLoop in 1..100){
            for(smallLoop in 1..1_000_000){
                val x = random.nextDouble()
                val y = random.nextDouble()
                if(x*x + y*y <= 1.0) innerCount += 1
                totalCount += 1
            }
            val currentValue = innerCount.toDouble() / totalCount.toDouble() * 4.0f
            CoroutineScope(Dispatchers.Main).launch{
                val textView = findViewById<TextView>(R.id.textView)
                    textView.text = "Done ${bigLoop}%...\n" +
                            "current estimation: ${String.format("%.6f", currentValue)}"
            }
        }
        val lastValue = innerCount.toDouble() / totalCount.toDouble() * 4.0f

        "Done!\nEstimation: ${String.format("%.6f", lastValue)}"
    }
    /*
    fun longTask() = CoroutineScope(Dispatchers.Default).async{
        var totalCount = 0
        var innerCount = 0
        for(bigLoop in 1..100){
            for(smallLoop in 1..1_000_000){
                /*TODO*/
                /*TODO*/
                if(/*TODO*/) innerCount += 1
                totalCount += 1
            }
            val currentValue = /*TODO*/ / /*TODO*/ * 4.0f
            CoroutineScope(/*TODO*/).launch{
                val textView = /*TODO*/
                /*TODO*/ = "Done ${bigLoop}%...\n" +
                    "current estimation: ${String.format("%.6f", currentValue)}"
            }
        }
        val lastValue = /*TODO*/
            "Done!\nEstimation: ${/*TODO*/}"
    }
    */
}