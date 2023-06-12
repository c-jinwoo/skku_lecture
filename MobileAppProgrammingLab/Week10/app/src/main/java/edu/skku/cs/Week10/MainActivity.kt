package edu.skku.cs.Week10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.button)
        val et = findViewById<EditText>(R.id.editText)

        val client = OkHttpClient()
        val host = "https://api.tvmaze.com"

        btn.setOnClickListener {
            val path = "/search/shows?q=" + et.text
            val req = Request.Builder().url(host+path).build()
            client.newCall(req).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use{
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        val data = response.body!!.string()

                        val typeToken = object : TypeToken<List<Show>>() {}.type
                        val id = Gson().fromJson<List<Show>>(data, typeToken)[0].show?.id

                        val path2 = "/shows/" + id.toString() + "/episodes"
                        val req = Request.Builder().url(host+path2).build()
                        client.newCall(req).enqueue(object : Callback{
                            override fun onFailure(call: Call, e: IOException) {
                                e.printStackTrace()
                            }

                            override fun onResponse(call: Call, response: Response) {
                                response.use{
                                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                                    val data = response.body!!.string()

                                    val typeToken = object : TypeToken<List<Episode>>() {}.type
                                    val episodeList = Gson().fromJson<List<Episode>>(data, typeToken)

                                    CoroutineScope(Dispatchers.Main).launch {
                                        val listAdapter = ListViewAdapter(this@MainActivity, episodeList)
                                        var listView = findViewById<ListView>(R.id.listView)
                                        listView.adapter = listAdapter
                                    }
                                }
                            }
                        })
                    }
                }
            })
        }
    }
}


/*
//ex3
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.button)
        val tv = findViewById<TextView>(R.id.textView)
        val tv2 = findViewById<TextView>(R.id.textView2)

        val client = OkHttpClient()
        val host = "https://reqres.in"
        val path = "/api/register"

        btn.setOnClickListener {
            tv.text = host + path
            val json = Gson().toJson(Register("eve.holt@reqres.in", "pistol"))
            val mediaType = "application/json; charset=utf-8".toMediaType()

            val req = Request.Builder().url(host + path).post(json.toString().toRequestBody(mediaType)).build()
            client.newCall(req).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    response.use{
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        val str = response.body!!.string()
                        val data = Gson().fromJson(str, RegisterResponse::class.java)
                        CoroutineScope(Dispatchers.Main).launch {
                            tv2.text = data.id.toString()
                        }
                    }
                }
            })
        }
    }
}
*/

/*
//ex2
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.button)
        val tv = findViewById<TextView>(R.id.textView)
        val tv2 = findViewById<TextView>(R.id.textView2)

        val client = OkHttpClient()
        val host = "https://reqres.in"
        val path = "/api/users?page=2"

        btn.setOnClickListener {
            tv.text = host + path
            val req = Request.Builder().url(host + path).build()
            client.newCall(req).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    response.use{
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        val str = response.body!!.string()
                        val data = Gson().fromJson(str, DataModel::class.java)
                        CoroutineScope(Dispatchers.Main).launch {
                            var concat : String = ""
                            for (i:Int in 0..data.data.size -1)
                                concat += " " + data.data[i].last_name
                            tv2.text = concat
                        }
                    }
                }
            })
        }
    }
}
*/
/*
//ex1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.button)
        val tv = findViewById<TextView>(R.id.textView)
        val tv2 = findViewById<TextView>(R.id.textView2)

        val client = OkHttpClient()
        val host = "https://reqres.in"
        val path = "/api/users?page=2"

        btn.setOnClickListener {
            tv.text = host + path
            val req = Request.Builder().url(host + path).build()
            client.newCall(req).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use{
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        val str = response.body!!.string()

                        CoroutineScope(Dispatchers.Main).launch {
                            tv2.text = str
                        }
                    }
                }

            })

        }

    }
}
*/