package edu.skku.cs.pa3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import java.sql.DriverManager

class MainActivity : AppCompatActivity() {
    companion object{
        const val USER_ID = "user_id_value"
        const val USER_NICKNAME = "user_nickname_value"
        const val USER_PROFILE = "user_profile_value"
    }
    private var userClick = false

    override fun onResume(){
        super.onResume()
        userClick = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        KakaoSdk.init(this, KAKAO_APPKEY)

        var btnKakaoLogin = findViewById<ImageButton>(R.id.kakao_login_button)

        btnKakaoLogin.setOnClickListener {
            if(!userClick) {
                userClick = true                                                                                 // 연속클릭 방지용
                val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                    if (error != null) {
                        Log.e("ERROR", "Kakao User Authentication Fail")
                    }
                    else if (token != null) {
                        proceedActivity()
                    }
                }

                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {                             // 카카오톡 로그인
                    UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                        if (error != null) {                                                                     // 로그인 실패
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                return@loginWithKakaoTalk
                            }
                            UserApiClient.instance.loginWithKakaoAccount(
                                this,
                                callback = callback
                            )
                        }
                        else if (token != null) {                                                                // 로그인 성공
                            proceedActivity()
                        }
                    }
                }
                else {                                                                                          // 카카오 이메일 로그인
                    UserApiClient.instance.loginWithKakaoAccount(
                        this,
                        callback = callback
                    )
                }
            }
        }
    }

    private fun proceedActivity(){
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("ERROR", "Kakao User Information Fail")
            }
            else if (user != null) {
                val intent = Intent(this, LoginConfirmActivity::class.java).apply{
                    putExtra(USER_ID, "${user.id}")
                    putExtra(USER_NICKNAME, "${user.kakaoAccount?.profile?.nickname}")
                    putExtra(USER_PROFILE, "${user.kakaoAccount?.profile?.profileImageUrl}")
                }
                startActivity(intent)
            }
        }
    }
}
