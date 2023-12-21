package com.swe.buddybud.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swe.buddybud.R;
import com.swe.buddybud.common.RetrofitClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpSndActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_snd);

        EditText editTextId = findViewById(R.id.editTextID);
        EditText editTextDob = findViewById(R.id.editTextDob);
        Spinner spinnerLanguage = findViewById(R.id.spinnerLanguage);
        Spinner spinnerGender = findViewById(R.id.spinnerGender);
        UserApiService userApiService = RetrofitClient.getService(UserApiService.class);

        // Language 스피너 설정
        String[] languageArray = getResources().getStringArray(R.array.Language);
        setSpinner(spinnerLanguage, languageArray);

        // Gender 스피너 설정
        String[] genderArray = getResources().getStringArray(R.array.Gender);
        setSpinner(spinnerGender, genderArray);

        // SignUp 버튼 클릭 시
        Button btnSignUp = findViewById(R.id.buttonSignUp);
        btnSignUp.setOnClickListener(v -> {
            enableObjects(false);

            String idText = editTextId.getText().toString();
            String dobText = editTextDob.getText().toString();
            String languageText = spinnerLanguage.getSelectedItem().toString();
            String genderText = spinnerGender.getSelectedItem().toString();

            // ID 비어 있는 경우
            if (TextUtils.isEmpty(idText) || TextUtils.isEmpty(dobText) || languageText.equals("Language") || genderText.equals("Gender")) {
                enableObjects(true);
                Toast.makeText(getApplicationContext(), "Please fill the forms", Toast.LENGTH_SHORT).show();
                return;
            }

            // 날짜 유효성 검사
            if(!isValidDate(dobText)){
                enableObjects(true);
                Toast.makeText(getApplicationContext(), "Not a valid DOB", Toast.LENGTH_SHORT).show();
                return;
            }

            // ID 중복 확인
            Call<UserApiData> call = userApiService.userIdDuplCheck(idText);
            call.enqueue(new Callback<UserApiData>() {
                @Override
                public void onResponse(Call<UserApiData> call, Response<UserApiData> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserApiData userResponse = response.body();

                        // 아이디 중복시
                        if(userResponse.getIsUserIdDupl()){
                            enableObjects(true);
                            Toast.makeText(getApplicationContext(),"Duplicated ID", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 아이디 중복 아닐시
                        else{
                            Intent prevIntent = getIntent();

                            Map<String, String> fields = new HashMap<>();
                            Gson gson = new Gson();

                            fields.put("email", prevIntent.getStringExtra("email"));
                            fields.put("password", prevIntent.getStringExtra("password"));
                            fields.put("school", prevIntent.getStringExtra("school"));
                            fields.put("id", idText);
                            fields.put("dob", dobText);
                            fields.put("lang", languageText);
                            fields.put("gender", genderText);

                            insertUserData(gson.toJson(fields));
                        }
                    }
                    else {
                        enableObjects(true);
                        Toast.makeText(getApplicationContext(),"Network Error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                @Override
                public void onFailure(Call<UserApiData> call, Throwable t) {
                    enableObjects(true);
                    Toast.makeText(getApplicationContext(),"Network Error", Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        });
    }

    private void insertUserData(String info){
        Button btnSignUp = findViewById(R.id.buttonSignUp);
        UserApiService userApiService = RetrofitClient.getService(UserApiService.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), info);

        Call<UserApiData> call = userApiService.insertUserInfo(requestBody);
        call.enqueue(new Callback<UserApiData>() {
            @Override
            public void onResponse(Call<UserApiData> call, Response<UserApiData> response) {
                UserApiData userResponse = response.body();
                enableObjects(true);

                // User Info Insert 성공시
                if(userResponse.getInsertUserInfoResult()){
                    Toast.makeText(getApplicationContext(),"Join Succeeded", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Network Error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<UserApiData> call, Throwable t) {
                enableObjects(true);
                Toast.makeText(getApplicationContext(),"Network Error", Toast.LENGTH_SHORT).show();
                return;
            }
        });

    }

    // 객체 활성/비활성
    public void enableObjects(boolean bool){
        EditText editTextId = findViewById(R.id.editTextID);
        EditText editTextDob = findViewById(R.id.editTextDob);
        Spinner spinnerLanguage = findViewById(R.id.spinnerLanguage);
        Spinner spinnerGender = findViewById(R.id.spinnerGender);

        editTextId.setEnabled(bool);
        editTextDob.setEnabled(bool);
        spinnerLanguage.setEnabled(bool);
        spinnerGender.setEnabled(bool);
    }

    // 날짜 유효성 확인
    public static boolean isValidDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(dateString);
            return true;
        }
        catch (ParseException e) {
            return false;
        }
    }

    // 스피너 설정
    private void setSpinner(Spinner spinner, String[] array) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line) {
            @Override
            public int getCount() {
                return super.getCount() - 1;        // 힌트 옵션 제외
            }
        };

        for (int i = 0; i < array.length; i++) {
            adapter.add(array[i]);
        }

        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());   // 초기값을 힌트로 설정
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 클릭한 아이템의 텍스트 색상 변경
                TextView selectedTextView = (TextView) selectedItemView;
                selectedTextView.setTextColor(Color.parseColor("#A4A4A4"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView){}
        });
    }

}