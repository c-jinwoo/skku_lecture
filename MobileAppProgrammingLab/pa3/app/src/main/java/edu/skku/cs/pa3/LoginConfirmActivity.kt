package edu.skku.cs.pa3

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.kakao.sdk.user.UserApiClient
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class LoginConfirmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_confirm)

        val userId = intent.getStringExtra(MainActivity.USER_ID).toString()
        val userName = intent.getStringExtra(MainActivity.USER_NICKNAME).toString()
        val userProfileImg = intent.getStringExtra(MainActivity.USER_PROFILE).toString()

        var imageViewBg = findViewById<ImageView>(R.id.imageViewBg)
        var imageViewProfile = findViewById<ImageView>(R.id.imageViewProfile)
        var textViewName = findViewById<TextView>(R.id.textViewNickname)
        var btnYes = findViewById<Button>(R.id.buttonYes)
        var btnNo = findViewById<Button>(R.id.buttonNo)

        textViewName.text = userName
        imageViewBg.setColorFilter(ContextCompat.getColor(this, R.color.dark_overlay), PorterDuff.Mode.SRC_ATOP);
        Glide.with(this)
            .load(userProfileImg)
            .into(imageViewProfile)

        btnYes.setOnClickListener{
            session_userId = userId
            val selectUserTask = SelectUser()
            selectUserTask.execute("http://$AWS_URL/selectUser.php")
        }
        btnNo.setOnClickListener {
            finish()
        }
    }

    inner class SelectUser : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            val serverURL: String? = params[0]
            val postParameters: String = "userId=$session_userId"

            try {
                val url = URL(serverURL)
                val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.readTimeout = 5000
                httpURLConnection.connectTimeout = 5000
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.connect()

                val outputStream: OutputStream = httpURLConnection.outputStream
                outputStream.write(postParameters.toByteArray(charset("UTF-8")))
                outputStream.flush()
                outputStream.close()

                val responseStatusCode: Int = httpURLConnection.responseCode
                val inputStream: InputStream
                inputStream = if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    httpURLConnection.inputStream
                }
                else {
                    httpURLConnection.errorStream
                }

                val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
                val bufferedReader = BufferedReader(inputStreamReader)
                val sb = StringBuilder()
                var line: String? = null

                while (bufferedReader.readLine().also({ line = it }) != null) {
                    sb.append(line)
                }
                bufferedReader.close();
                return sb.toString();
            }
            catch (e: Exception) {
                return "MySQL : Select User Error" + e.message
            }
        }

        override fun onPostExecute(result: String?) {
            // 유저가 없는 경우
            val jsonArray = JSONArray(result)

            if(jsonArray.length() == 0){
                val insertUserTask = InsertUser()
                insertUserTask.execute("http://$AWS_URL/insertUser.php", DEFAULT_HOMEPAGE_URL)
                homePageUrl = DEFAULT_HOMEPAGE_URL
            }
            // 유저가 있는 경우
            else{
                val jsonObject = jsonArray.getJSONObject(0)
                val res = Gson().fromJson(jsonObject.toString(), Db_homepage::class.java).homepage
                homePageUrl = getFullUrlWithProtocol(res)

                // 홈페이지 설정 후 다음 Activity로
                val intent = Intent(this@LoginConfirmActivity, ContentActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    inner class InsertUser : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            val serverURL: String? = params[0]
            val homepage: String? = params[1]
            val postParameters: String = "userId=$session_userId&homepage=$homepage"

            try {
                val url = URL(serverURL)
                val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.readTimeout = 5000
                httpURLConnection.connectTimeout = 5000
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.connect()

                val outputStream: OutputStream = httpURLConnection.outputStream
                outputStream.write(postParameters.toByteArray(charset("UTF-8")))
                outputStream.flush()
                outputStream.close()

                val responseStatusCode: Int = httpURLConnection.responseCode
                val inputStream: InputStream
                inputStream = if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    httpURLConnection.inputStream
                }
                else {
                    httpURLConnection.errorStream
                }

                val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
                val bufferedReader = BufferedReader(inputStreamReader)
                val sb = StringBuilder()
                var line: String? = null

                while (bufferedReader.readLine().also({ line = it }) != null) {
                    sb.append(line)
                }
                bufferedReader.close();

                return sb.toString();

            }
            catch (e: Exception) {
                return "MySQL : Insert User Error" + e.message
            }
        }

        override fun onPostExecute(result: String?) {
            val intent = Intent(this@LoginConfirmActivity, ContentActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}