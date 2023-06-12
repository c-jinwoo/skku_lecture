package edu.skku.cs.Week12

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.io.IOException

class MainActivity : AppCompatActivity() {
    /* Fill your Lambda URL here */
    val URL = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var tv1 = findViewById<TextView>(R.id.textView)
        var btn = findViewById<Button>(R.id.button)

        btn.setOnClickListener {
            val okHttpClient = OkHttpClient()
            val new_url= Uri.parse(URL).buildUpon().appendQueryParameter("name", "cjw").build().toString()
            val request = Request.Builder().url(new_url).build()

            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) { }

                override fun onResponse(call: Call, response: Response) {
                    var resp = response.body!!.string()
                    Log.d("RESPONSE", resp)
                    if (resp != null) {
                        CoroutineScope(Dispatchers.Main).launch {
                            tv1.setText(resp)
                        }
                    }
                }
            })
        }
    }
}