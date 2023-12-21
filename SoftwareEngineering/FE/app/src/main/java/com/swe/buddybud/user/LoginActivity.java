package com.swe.buddybud.user;

import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.gson.Gson;
import com.swe.buddybud.common.ContentActivity;
import com.swe.buddybud.R;
import com.swe.buddybud.common.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // 화면 복귀 시 박스 초기화
    @Override
    protected void onResume() {
        super.onResume();

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        CheckBox checkBoxRemember = findViewById(R.id.checkBoxRememberMe);

        editTextEmail.setText(LoginData.isLoginRemember() ? LoginData.getLoginEmail() : "");
        editTextPassword.setText("");
        checkBoxRemember.setChecked(LoginData.isLoginRemember());
    }

    @Override
    protected void onPause() {
        super.onPause();

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        LoginData.setLoginEmail(editTextEmail.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CheckBox checkBoxRemember = findViewById(R.id.checkBoxRememberMe);
        Button btnSignIn = findViewById(R.id.buttonSignIn);
        TextView textViewSignUp = findViewById(R.id.textViewSignUp);

        // Remember me 체크 박스 클릭시
        checkBoxRemember.setOnClickListener(view -> LoginData.setLoginRemember(checkBoxRemember.isChecked()));

        // Sign In 버튼 클릭 시 : ContentActivity
        btnSignIn.setOnClickListener(v -> {
            enableObjects(false);
            EditText editTextEmail = findViewById(R.id.editTextEmail);
            EditText editTextPassword = findViewById(R.id.editTextPassword);

            String emailText = editTextEmail.getText().toString();
            String passwordText = editTextPassword.getText().toString();

            if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)) {
                enableObjects(true);
                Toast.makeText(getApplicationContext(), "Please fill the forms", Toast.LENGTH_SHORT).show();
            }
            else {
                Map<String, String> fields = new HashMap<>();
                Gson gson = new Gson();

                fields.put("email", emailText);
                fields.put("password", passwordText);

                UserApiService userApiService = RetrofitClient.getService(UserApiService.class);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(fields));

                Call<UserApiData> call = userApiService.userLogin(requestBody);
                call.enqueue(new Callback<UserApiData>() {
                    @Override
                    public void onResponse(Call<UserApiData> call, Response<UserApiData> response) {
                        UserApiData userResponse = response.body();

                        // 패스워드 매치 시
                        if (userResponse.getIsUserLoginSucceed()) {
                            LoginData.setLoginUserNo(userResponse.getLoginUserNo());
                            LoginData.setLoginUserId(userResponse.getLoginUserId());
                            LoginData.setLoginUserLang(userResponse.getLoginUserLang());
                            LoginData.setLoginUserGender(userResponse.getLoginUserGender());

                            enableObjects(true);
                            Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                            startActivity(intent);
                        }
                        else {
                            enableObjects(true);
                            Toast.makeText(getApplicationContext(), "Incorrect Login Info", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<UserApiData> call, Throwable t) {
                        enableObjects(true);
                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        });

        // Sign Up 버튼 클릭시 : SignUpFstActivity
        textViewSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignUpFstActivity.class);
            startActivity(intent);
        });
    }

    // 객체 활성/비활성
    public void enableObjects(boolean bool) {
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        CheckBox checkBoxRemember = findViewById(R.id.checkBoxRememberMe);
        Button btnSignIn = findViewById(R.id.buttonSignIn);
        TextView textViewSignUp = findViewById(R.id.textViewSignUp);

        editTextEmail.setEnabled(bool);
        editTextPassword.setEnabled(bool);
        checkBoxRemember.setEnabled(bool);
        btnSignIn.setEnabled(bool);
        textViewSignUp.setEnabled(bool);
    }
}