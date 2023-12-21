package com.swe.buddybud.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swe.buddybud.R;

public class ScrapDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_detail);

        // Extract data from the intent
        Intent intent = getIntent();
        String userId = intent.getStringExtra("user_id");
        String date = intent.getStringExtra("date");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        int thumbsUpNumber = intent.getIntExtra("thumbs_up_num", 0);
        int commentNumber = intent.getIntExtra("comment_num", 0);

        // Set data to views
        TextView userIdTextView = findViewById(R.id.feedNickname);
        TextView dateTextView = findViewById(R.id.feedDate);
        TextView titleTextView = findViewById(R.id.feedTitle);
        TextView contentTextView = findViewById(R.id.content);
        TextView thumbsUpTextView = findViewById(R.id.feedThumbsUpNumber);
//        TextView commentTextView = findViewById(R.id.commentNumber);

        ImageView iconBack = findViewById(R.id.iconBack);
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the activity and return to the previous one
                finish();
            }
        });

        userIdTextView.setText(userId);
        dateTextView.setText(date);
        titleTextView.setText(title);
        contentTextView.setText(content);
        thumbsUpTextView.setText(String.valueOf(thumbsUpNumber));
//        commentTextView.setText(String.valueOf(commentNumber));

        // Add any other views that need to be updated
    }

}