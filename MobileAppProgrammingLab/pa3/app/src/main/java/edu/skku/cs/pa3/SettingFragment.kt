package edu.skku.cs.pa3

import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class SettingFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_setting, container, false)
        var editTextUrl = view.findViewById<EditText>(R.id.editTextUrl)
        var btnOk = view.findViewById<Button>(R.id.buttonOk)

        // 초기 설정 및 클릭 시 설정
        editTextUrl.text = Editable.Factory.getInstance().newEditable(homePageUrl)
        btnOk.setOnClickListener {
            var url = editTextUrl.text.toString()
            homePageUrl = getFullUrlWithProtocol(url)
            editTextUrl.text = Editable.Factory.getInstance().newEditable(homePageUrl)

            val updateUserHomepageTask = UpdateUserHomepage()
            updateUserHomepageTask.execute("http://$AWS_URL/updateUserHomepage.php", homePageUrl)
        }

        return view
    }

    // 사용자 시작페이지 설정
    inner class UpdateUserHomepage : AsyncTask<String, Void, String>() {
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
                } else {
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
                return "MySQL : Update User Homepage Error" + e.message
            }
        }
        override fun onPostExecute(result: String?) {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(
                    requireContext(),
                    "Successfully changed homepage!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}