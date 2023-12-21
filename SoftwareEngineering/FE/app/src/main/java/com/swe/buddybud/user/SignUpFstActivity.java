package com.swe.buddybud.user;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.swe.buddybud.R;
import com.swe.buddybud.common.RetrofitClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFstActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_fst);

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextSchool = findViewById(R.id.editTextSchool);
        Button btnNext = findViewById(R.id.buttonNext);
        UserApiService userApiService = RetrofitClient.getService(UserApiService.class);

        // Next 버튼 클릭 시 : SignUpSndActivity
        btnNext.setOnClickListener(v -> {
            enablebOjects(false);
            String emailText = editTextEmail.getText().toString();
            String passwordText = editTextPassword.getText().toString();
            String schoolText = editTextSchool.getText().toString();

            // 이메일, 비밀번호, 학교정보 비어 있는 경우
            if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText) || TextUtils.isEmpty(schoolText)) {
                enablebOjects(true);
                Toast.makeText(getApplicationContext(), "Please fill the forms", Toast.LENGTH_SHORT).show();
                return;
            }

            // Email 유효성 확인
            if (!isValidEmail(emailText)) {
                enablebOjects(true);
                Toast.makeText(getApplicationContext(), "Not a valid Email address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Email 중복 확인
            Call<UserApiData> call = userApiService.userEmailDuplCheck(emailText);
            call.enqueue(new Callback<UserApiData>() {
                @Override
                public void onResponse(Call<UserApiData> call, Response<UserApiData> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        enablebOjects(true);
                        UserApiData userResponse = response.body();

                        // 이메일 중복시
                        if(userResponse.getIsUserEmailDupl()){
                            Toast.makeText(getApplicationContext(),"Duplicated Email", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 이메일 중복 아닐시
                        else{
                            Intent intent = new Intent(getApplicationContext(), SignUpSndActivity.class);
                            intent.putExtra("email", emailText);
                            intent.putExtra("password", passwordText);
                            intent.putExtra("school", schoolText);
                            startActivity(intent);
                        }
                    }
                    else {
                        enablebOjects(true);
                        Toast.makeText(getApplicationContext(),"Network Error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                @Override
                public void onFailure(Call<UserApiData> call, Throwable t) {
                    enablebOjects(true);
                    Toast.makeText(getApplicationContext(),"Network Error", Toast.LENGTH_SHORT).show();
                    return;
                }
            });
        });
    }

    // 이메일 유효성 확인
    public boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    // 객체 활성/비활성
    public void enablebOjects(boolean bool){
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        EditText editTextSchool = findViewById(R.id.editTextSchool);
        Button btnNext = findViewById(R.id.buttonNext);

        editTextEmail.setEnabled(bool);
        editTextPassword.setEnabled(bool);
        editTextSchool.setEnabled(bool);
        btnNext.setEnabled(bool);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            // 선택한 이미지 파일 이름을 EditText에 표시
            String imageName = getRealPathFromURI(selectedImageUri);
            String[] imageNameArray = imageName.split("/");
        }
    }

    // URI에서 실제 파일 경로 가져오기
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null){
            return null;
        }

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();

        return result;
    }
}