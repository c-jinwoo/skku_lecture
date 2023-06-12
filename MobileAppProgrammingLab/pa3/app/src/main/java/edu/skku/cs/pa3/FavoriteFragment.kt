package edu.skku.cs.pa3

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.codeplayon.custombottobar.CustomBottomNavView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class FavoriteFragment : Fragment() {
    lateinit var favoriteUrlListView: ListView
    lateinit var favoriteAdapter: ArrayAdapter<String>

    override fun onResume() {
        super.onResume()
        val selectFavorite = SelectFavorite()
        selectFavorite.execute("http://$AWS_URL/selectFavorite.php")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_favorite, container, false)
        favoriteAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        favoriteUrlListView = view.findViewById(R.id.favoriteUrlListView)
        favoriteUrlListView.adapter = favoriteAdapter

        // 즐겨찾기 리스트 주소 클릭 시 이벤트
        favoriteUrlListView.setOnItemClickListener { parent, view, position, id ->
            val activity = activity as ContentActivity
            val webViewWrapper = activity.findViewById<ConstraintLayout>(R.id.webViewWrapper)
            val frameWrapper = activity.findViewById<ConstraintLayout>(R.id.frameWrapper)
            val bottomNavigationView = activity.findViewById<CustomBottomNavView>(R.id.customBottomBar)
            val webView = activity.findViewById<WebView>(R.id.webView)

            bottomNavigationView.selectedItemId = R.id.action_home              // Home 버튼 활성화
            favoriteAdapter.getItem(position)?.let { activity.proceedPage(it) } // webView 주소 이동
            webViewWrapper.visibility = View.VISIBLE                            // webView 보이기
            frameWrapper.visibility = View.INVISIBLE                            // Fragment 숨기기
        }
        return view
    }

    // DB : 즐겨찾기 목록 조회용
    inner class SelectFavorite : AsyncTask<String, Void, String>() {
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
                return "MySQL : Select Favorite Error" + e.message
            }
        }
        override fun onPostExecute(result: String?) {
            val jsonArray = JSONArray(result)
            val resultList = ArrayList<String>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val fpage = jsonObject.getString("fpage")
                resultList.add(getFullUrlWithProtocol(fpage))
            }

            // 어댑터에 새로운 주소 추가
            CoroutineScope(Dispatchers.Main).launch {
                favoriteAdapter.clear()
                favoriteAdapter.addAll(resultList)
                favoriteAdapter.notifyDataSetChanged()
            }
        }
    }
}