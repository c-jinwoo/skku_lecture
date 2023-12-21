package com.swe.buddybud.account;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.swe.buddybud.R;
import com.swe.buddybud.common.RetrofitClient;
import com.swe.buddybud.user.LoginData;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountEditActivity extends AppCompatActivity {

    private EditText editUserId;
    private Spinner genderSpinner, languageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        editUserId = findViewById(R.id.editTextID);
        String userId = LoginData.getLoginUserId();
        editUserId.setText(userId);

        genderSpinner = findViewById(R.id.spinnerGender);
        languageSpinner = findViewById(R.id.spinnerLanguage);

        // Language 스피너 설정
        String[] languageArray = getResources().getStringArray(R.array.AccountLanguage);
        setSpinner(languageSpinner, languageArray, LoginData.getLoginUserLang());

        // Gender 스피너 설정
        String[] genderArray = getResources().getStringArray(R.array.AccountGender);
        setSpinner(genderSpinner, genderArray, LoginData.getLoginUserGender());

        Button doneButton = findViewById(R.id.buttonDone);

        ImageButton goBackButton = findViewById(R.id.go_back_button);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finish and go back to the previous activity
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernum = String.valueOf(LoginData.getLoginUserNo());
                String userid = editUserId.getText().toString();
                String language = languageSpinner.getSelectedItem().toString();
                String gender = genderSpinner.getSelectedItem().toString();

                UserAccountUpdate updateInfo = new UserAccountUpdate(userid, language, gender);
                updateUserAccount(usernum, updateInfo);
            }
        });
    }

    private void updateUserAccount(String usernum, UserAccountUpdate updateInfo) {
        AccountApiService service = RetrofitClient.getService(AccountApiService.class);
        Call<UpdateResponse> call = service.updateUserAccount(usernum, updateInfo);
        call.enqueue(new Callback<UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean updateResult = response.body().isUpdateResult();
                    if (updateResult) {
                        // Update was successful
                        LoginData.setLoginUserId(updateInfo.getUserid());
                        LoginData.setLoginUserLang(updateInfo.getLang());
                        LoginData.setLoginUserGender(updateInfo.getGender());
                        Toast.makeText(AccountEditActivity.this, "Account updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // Update failed
                        Toast.makeText(AccountEditActivity.this, "Failed to update account, code 2", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Response is not successful, inspect the error body
                    if (response.errorBody() != null) {
                        try {
                            String errorString = response.errorBody().string();
                            Log.e("API Error", "Error response: " + errorString);
                            Toast.makeText(AccountEditActivity.this, "Error: " + errorString, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Log.e("API Error", "Error parsing error response", e);
                        }
                    } else {
                        Toast.makeText(AccountEditActivity.this, "Failed to update account, code 1", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateResponse> call, Throwable t) {
//                Log.e("Network error", t.getMessage());
                Toast.makeText(AccountEditActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }

        });
    }


    private void setSpinner(Spinner spinner, String[] items, String defaultValue) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Set default value if not null
        if (defaultValue != null) {
            int spinnerPosition = adapter.getPosition(defaultValue);
            spinner.setSelection(spinnerPosition);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


}
