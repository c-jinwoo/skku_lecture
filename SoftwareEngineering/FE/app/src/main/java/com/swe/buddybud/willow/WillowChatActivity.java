package com.swe.buddybud.willow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swe.buddybud.R;
import com.swe.buddybud.common.RetrofitClient;
import com.swe.buddybud.user.LoginData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WillowChatActivity extends AppCompatActivity {
    private String opponentID = null;
    private int opponentNo;
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private ArrayList<ChatData> mData = new ArrayList<>();
    private RecyclerView chatRecyclerView;
    private RecyclerView imageRecyclerView;
    private WillowChat_Adapter chatAdapter;
    private TextView titleText;
    private ImageButton backBtn;
    private ImageButton addImgBtn;
    private ImageButton sendChatBtn;
    private EditText chatEditText;
    private ArrayList<Uri> selectedImageUris = new ArrayList<>();
    private ImageUploadAdapter imageUploadAdapter;

    private WillowApiService willowApiService = RetrofitClient.getService(WillowApiService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_willow_chat);

        opponentID = getIntent().getStringExtra("OPPONENT_ID");
        opponentNo = getIntent().getIntExtra("OPPONENT_NO",0);
        //if(userID == null) userID = "user1";

        titleText =findViewById(R.id.chat_title_txt);
        titleText.setText(opponentID);
        chatRecyclerView = findViewById(R.id.chat_recyclerView);
        imageRecyclerView = findViewById(R.id.chat_image_recycler_view);
        chatAdapter = new WillowChat_Adapter(mData, LoginData.getLoginUserId());
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        chatRecyclerView.setAdapter(chatAdapter);
        getData(LoginData.getLoginUserNo());

        imageUploadAdapter = new ImageUploadAdapter(selectedImageUris);
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageRecyclerView.setAdapter(imageUploadAdapter);

        backBtn = findViewById(R.id.chat_back_btn);
        addImgBtn = findViewById(R.id.add_image_btn);
        sendChatBtn = findViewById(R.id.send_chat_btn);
        chatEditText = findViewById(R.id.chat_edittext);

        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {finish();}
        });

        addImgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            }
        });

        sendChatBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(selectedImageUris.size() > 1) {
                    for(Uri imageUri : selectedImageUris){
                        mData.add(new ChatData(LocalDateTime.now(), "","user1",R.drawable.profile, imageUri));
                    }
                    selectedImageUris.clear();
                    imageUploadAdapter.notifyDataSetChanged();
                }
                if(chatEditText.getText().length()<1) return;
                Map<String, String> fields = new HashMap<>();
                Gson gson = new Gson();

                fields.put("sender_no", String.valueOf(LoginData.getLoginUserNo()));
                fields.put("receiver_no", String.valueOf(opponentNo));
                fields.put("content", chatEditText.getText().toString());
                sendChat(gson.toJson(fields));
                /*
                data.add(new ChatData(LocalTime.now(), chatEditText.getText().toString(),"user1",R.drawable.profile, null, ""));
                chatAdapter.notifyItemInserted(data.size() - 1);
                 */
                chatEditText.setText("");
                chatRecyclerView.scrollToPosition(mData.size() - 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();

                        try {
                            // 내부 저장소에 파일 복사
                            File file = createImageFile();
                            copyUriToFile(imageUri, file);

                            // 내부 저장소 URI를 사용
                            Uri internalUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
                            selectedImageUris.add(internalUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (data.getData() != null) {
                    Uri imageUri = data.getData();

                    try {
                        // 내부 저장소에 파일 복사
                        File file = createImageFile();
                        copyUriToFile(imageUri, file);

                        // 내부 저장소 URI를 사용
                        Uri internalUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
                        selectedImageUris.add(internalUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                imageUploadAdapter.notifyDataSetChanged();
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    private void copyUriToFile(Uri uri, File destFile) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = getContentResolver().openInputStream(uri);
            os = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    private void sendChat(String chat){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), chat);

        Call<WillowApiData> call = willowApiService.sendChat(requestBody);
        call.enqueue(new Callback<WillowApiData>() {
            @Override
            public void onResponse(Call<WillowApiData> call, Response<WillowApiData> response) {
                WillowApiData userResponse = response.body();

                // User Info Insert 성공시
                if(userResponse.getSendChatResult()){
                    getData(LoginData.getLoginUserNo());
                }
                else {
                    Toast.makeText(getApplicationContext(),"sendChat Network Error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<WillowApiData> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"sendChat Network Error", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void getData(int willow_no){

        //fetch mData
        Call<List<WillowApiData>> call = willowApiService.getAllChat(willow_no,opponentNo);
        call.enqueue(new Callback<List<WillowApiData>>() {
            @Override
            public void onResponse(Call<List<WillowApiData>> call, Response<List<WillowApiData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<WillowApiData> userResponse = response.body();
                    //ArrayList<ChatData> newData = new ArrayList<>();
                    for(WillowApiData res : userResponse){
                        Resources resources = getResources();
                        int resourceId = R.drawable.profile;
                        if(res.getSender_no()!=LoginData.getLoginUserNo()) {
                            int opp_profile = resources.getIdentifier(opponentID.toLowerCase(), "drawable", getPackageName());
                            if (opp_profile > 0) resourceId = opp_profile;
                        }
                        ChatData dat = new ChatData(
                                LocalDateTime.parse(res.getCreated_at(), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                                res.getContent(),
                                LoginData.getLoginUserNo() == res.getSender_no() ? LoginData.getLoginUserId() : opponentID,
                                resourceId,
                                null);
                        if(!mData.contains(dat)){
                            mData.add(dat);
                            chatAdapter.notifyItemInserted(mData.size()-1);
                        }
                    }
                    return;
                }
                else {
                    Toast.makeText(getApplicationContext(),"getChat Network Error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<List<WillowApiData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"getChat Network Error", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        /* TEST DATA
        mData.add(new ChatData(LocalTime.of(7, 25), "안녕하세요 전 빈지노에요","realisshoman",R.drawable.realisshoman, null, "Hello, I'm Beenzino."));
        mData.add(new ChatData(LocalTime.of(9, 30), "Thank you for coming to the festival. I’m enjoying the song haha.","user1",R.drawable.profile, null, "축제 와주셔서 감사합니다 노래 잘 듣고 있습니다 ㅎㅎ."));
        mData.add(new ChatData(LocalTime.of(13, 01), "안녕하세요 전 빈지노에요","realisshoman",R.drawable.realisshoman, null, "Hello, I'm Beenzino."));
        mData.add(new ChatData(LocalTime.of(15, 22), "Thank you for accepting Willow.","user1",R.drawable.profile, null, "윌로우 받아주셔서 감사합니다"));
        mData.add(new ChatData(LocalTime.of(23, 11),
                "살바도르 달리, 반고흐 같이 Picasso in my body. Man I'm freakin artist \n"+
                        "달리 반 피카소를 보며 난 자랐어 \n"+
                        "나도 물감을 짰고 난 여기까지 왔어 And you can tell me nothing",
                "realisshoman", R.drawable.realisshoman, null,
                "Like Salvador Dali and Van Gogh, Picasso in my body. Man I'm freakin artist\n" +
                "I grew up watching Dali and Picasso\n" +
                "I also applied paint and I came this far And you can tell me nothing"));
         */
    }
}