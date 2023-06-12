package edu.skku.cs.pa2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity() {
    companion object{
        const val USER_NAME = ""
    }

    override fun onResume() {
        super.onResume()

        var editText = findViewById<EditText>(R.id.editText)
        editText.text.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 컴포넌트
        var editText = findViewById<EditText>(R.id.editText)
        var button = findViewById<Button>(R.id.button)

        // 요청 설정
        val client = OkHttpClient()
        val loginHost = "http://121.169.12.99:10099/users"
        val mediaType = "application/json; charset=utf-8".toMediaType()

        // SIGN IN 클릭 시
        button.setOnClickListener {
            val inputName = editText.text.toString()
            val json = Gson().toJson(DataLoginRequest(inputName))
            val req = Request.Builder().url(loginHost).post(json.toString().toRequestBody(mediaType)).build()

            client.newCall(req).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use{
                        if(!response.isSuccessful) throw IOException("Unexpected code $response")

                        val data = response.body!!.string()
                        val result = Gson().fromJson(data, DataLoginResponse::class.java)

                        // 로그인 성공 시
                        if(result.success == true){
                            CoroutineScope(Dispatchers.Main).launch{
                                var intent = Intent(applicationContext, MazeSelectionActivity::class.java).apply{
                                    putExtra(USER_NAME, inputName)
                                }
                                startActivity(intent)
                            }
                        }
                        // 로그인 실패 시
                        else{
                            CoroutineScope(Dispatchers.Main).launch{
                                Toast.makeText(
                                    this@MainActivity,
                                    "Wrong User Name",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            })
        }
    }
}