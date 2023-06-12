package edu.skku.cs.pa3

import android.graphics.Bitmap
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.codeplayon.custombottobar.CustomBottomNavView
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class ContentActivity : AppCompatActivity() {
    lateinit var webView : WebView
    private lateinit var blockFragment: BlockFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        // 초기 뷰 설정
        var webViewWrapper = findViewById<ConstraintLayout>(R.id.webViewWrapper)
        var frameWrapper = findViewById<ConstraintLayout>(R.id.frameWrapper)
        webViewWrapper.visibility = View.VISIBLE
        frameWrapper.visibility = View.INVISIBLE
        blockFragment = BlockFragment()

        // 상단 바 설정
        var btn_top_home = findViewById<ImageButton>(R.id.btn_home)
        var btn_top_add = findViewById<ImageButton>(R.id.btn_add)
        var btn_top_back = findViewById<ImageButton>(R.id.btn_back)
        var btn_top_forward = findViewById<ImageButton>(R.id.btn_forward)
        var toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        var addressBar = findViewById<TextView>(R.id.addressBar)
        addressBar.setOnEditorActionListener { v, actionId, event ->                    // 주소창 키보드 완료 버튼 이벤트
            if(actionId == EditorInfo.IME_ACTION_DONE){
                proceedPage(v.text.toString())
            }
            return@setOnEditorActionListener false
        }

        addressBar.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->           // 주소창 엔터 버튼 이벤트
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                proceedPage(addressBar.text.toString())
                return@OnKeyListener true
            }
            false
        })
        setSupportActionBar(toolbar)
        setTitle("")

        // 하단바 설정
        var btmNavView = findViewById<CustomBottomNavView>(R.id.customBottomBar)
        btmNavView.inflateMenu(R.menu.menu_btm_nav)

        // 웹뷰 설정
        webView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                addressBar.text = webView.url
            }
        }

        // 버튼 클릭 설정
        btn_top_home.setOnClickListener{
            proceedPage(homePageUrl)
        }
        btn_top_add.setOnClickListener{
            addFavoritePage(addressBar.text.toString())
        }
        btn_top_back.setOnClickListener{
            if(webView.canGoBack()) {
                webView.goBack()
            }
        }
        btn_top_forward.setOnClickListener{
            if(webView.canGoForward()) {
                webView.goForward()
            }
        }

        // Fragment 핸들러 및 하단바 연동
        var btmNaviBar = findViewById(R.id.customBottomBar) as CustomBottomNavView
        btmNaviBar.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.action_home -> {
                        webViewWrapper.visibility = View.VISIBLE
                        frameWrapper.visibility = View.INVISIBLE
                    }
                    R.id.action_favorite -> {
                        supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, FavoriteFragment()).commit()
                        setTitle("Favorite Pages")
                        webViewWrapper.visibility = View.INVISIBLE
                        frameWrapper.visibility = View.VISIBLE
                    }
                    R.id.action_block -> {
                        supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, blockFragment).commit()
                        setTitle("Blocked Pages")
                        webViewWrapper.visibility = View.INVISIBLE
                        frameWrapper.visibility = View.VISIBLE
                    }
                    R.id.action_setting -> {
                        supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, SettingFragment()).commit()
                        setTitle("Settings")
                        webViewWrapper.visibility = View.INVISIBLE
                        frameWrapper.visibility = View.VISIBLE
                    }
                }
                true
            }
        }

        // Fragment 초기화 및 진행
        supportFragmentManager.beginTransaction().replace(R.id.mainFrameLayout, blockFragment).commit()
        frameWrapper.visibility = View.INVISIBLE
        webViewWrapper.visibility = View.VISIBLE
        proceedPage(homePageUrl)
    }

    // 페이지 이동 관련
    fun proceedPage(input: String) {
        var url = getFullUrlWithProtocol(input)
        var domain = getDomainFromUrl(url).toString()
        var requestUrl = if(isIpAddress(domain)){
            VIRUSTOTAL_IPREQUEST_URLPREFIX + domain
        }
        else{
            VIRUSTOTAL_DOMATINREQUEST_URLPREFIX + domain
        }

        val client = OkHttpClient()
        val request = try {
            Request.Builder()
                .url(requestUrl)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-apikey", VIRUSTOTAL_APIKEY)
                .build()
        }
        catch (e: Exception) {
            null
        }

        if(request != null) {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        val data = response.body!!.string()
                        val res = Gson().fromJson(data, ApiResponse::class.java)
                        val lastAnalysisResults = res.data.attributes.last_analysis_results
                        val filteredResults = lastAnalysisResults.filter { (_, result) ->
                            result.category != "harmless" && result.category != "undetected"
                        }.keys.toList()

                        CoroutineScope(Dispatchers.Main).launch {
                            if(filteredResults.isNotEmpty()){
                                val builder = AlertDialog.Builder(this@ContentActivity)
                                builder.setMessage("Malicious Source Detected. Continue anyway?")
                                    .setPositiveButton("Yes") { dialog, _ ->
                                        // 확인 버튼을 클릭했을 때 수행할 동작
                                        webView.loadUrl(url)
                                        dialog.dismiss()
                                    }
                                    .setNegativeButton("no") { dialog, _ ->
                                        // BlockFragment 인스턴스 가져오기
                                        val blockAdapter =
                                            blockFragment?.blockUrlListView?.adapter as? BlockAdapter

                                        if(blockAdapter?.containsUrl(url) == false) {
                                            blockAdapter?.addItem(DataBlockUrl(url, filteredResults))
                                        }
                                        dialog.dismiss()
                                    }
                                    .show()
                            }
                            else{
                                webView.loadUrl(url)
                            }
                        }
                    }
                }
            })
        }
        else{
            Log.e("ERROR", "Virustotal Request Exception")
        }
    }

    // 즐겨찾기 추가용 Virustotal 조회
    private fun addFavoritePage(input: String) {
        var requestUrl = ""
        var url = getFullUrlWithProtocol(input).toString()
        var domain = getDomainFromUrl(url).toString()

        if(isIpAddress(domain)){
            requestUrl = VIRUSTOTAL_IPREQUEST_URLPREFIX + domain
        }
        else{
            requestUrl = VIRUSTOTAL_DOMATINREQUEST_URLPREFIX + domain
        }

        val client = OkHttpClient()
        val request = try {
            Request.Builder()
                .url(requestUrl)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-apikey", VIRUSTOTAL_APIKEY)
                .build()
        }
        catch (e: Exception) {
            null
        }

        if(request != null) {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        val data = response.body!!.string()
                        val res = Gson().fromJson(data, ApiResponse::class.java)
                        val lastAnalysisResults = res.data.attributes.last_analysis_results
                        val filteredResults = lastAnalysisResults.filter { (_, result) ->
                            result.category != "harmless" && result.category != "undetected"
                        }.keys.toList()

                        if(filteredResults.isEmpty()){
                            val insertFavorite = InsertFavorite()
                            insertFavorite.execute("http://$AWS_URL/insertFavorite.php", url)
                        }
                        else{
                            CoroutineScope(Dispatchers.Main).launch {
                                val builder = AlertDialog.Builder(this@ContentActivity)
                                builder.setMessage("Malicious Source Detected. Continue anyway?")
                                    .setPositiveButton("Yes") { dialog, _ ->
                                        // 확인 버튼을 클릭했을 때 수행할 동작
                                        val insertFavorite = InsertFavorite()
                                        insertFavorite.execute("http://$AWS_URL/insertFavorite.php", url)
                                        dialog.dismiss()
                                    }
                                    .setNegativeButton("no") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .show()
                            }
                        }
                    }
                }
            })
        }
        else{
            Log.e("ERROR", "Virustotal Request Exception")
        }
    }

    // DB : 즐겨찾기 사이트 추가
    inner class InsertFavorite: AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            val serverURL: String? = params[0]
            val page: String? = params[1]
            val postParameters: String = "userId=$session_userId&page=$page"

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

                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        this@ContentActivity,
                        "Successfully added to favorites!",
                        Toast.LENGTH_LONG
                    ).show()
                }

                return sb.toString();
            }
            catch (e: Exception) {
                return "MySQL : Insert Favorite Error" + e.message
            }
        }
    }
}