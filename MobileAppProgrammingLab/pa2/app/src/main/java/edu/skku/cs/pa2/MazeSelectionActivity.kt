package edu.skku.cs.pa2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class MazeSelectionActivity : AppCompatActivity() {
    companion object{
        const val MAP_NAME = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mazeselection)

        // 상단 유저이름 설정
        var textViewUserName = findViewById<TextView>(R.id.textViewUserName)
        textViewUserName.text = intent.getStringExtra(MainActivity.USER_NAME)

        // 요청 설정
        val client = OkHttpClient()
        val host = "http://121.169.12.99:10099/maps"
        val req = Request.Builder().url(host).build()

        client.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use{
                    if(!response.isSuccessful) throw IOException("Unexpected code $response")

                    val data = response.body!!.string()
                    val typeToken = object : TypeToken<List<MapList>>() {}.type
                    val mapList = Gson().fromJson<List<MapList>>(data, typeToken)

                    CoroutineScope(Dispatchers.Main).launch {
                        val listAdapter = MazeListAdapter(mapList, this@MazeSelectionActivity)
                        var listView = findViewById<ListView>(R.id.mazeListView)
                        listView.adapter = listAdapter
                    }
                }
            }
        })
    }
}