package com.swe.buddybud.home;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.swe.buddybud.R;
import com.swe.buddybud.board.BoardApiService;
import com.swe.buddybud.common.RetrofitClient;
import com.swe.buddybud.user.LoginData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private List<CommentData> commentDataList;
    private boolean isLiked;
    private int likeCount;
    private boolean isCommented;
    private int commentCount;
    private boolean isTranslated = false;
    private boolean isReplyMode = false;
    private int replyToCommentId = -1;
    private String replyToNickname = null;
    JsonObject boardDetails;
    JsonObject commentObject;

    // 대댓글 모드 활성화 메서드
    public void enableReplyMode(int commentId, String nickname) {
        this.isReplyMode = true;
        this.replyToCommentId = commentId;
        this.replyToNickname = nickname;

        EditText commentEditText = findViewById(R.id.commentEditText);
        commentEditText.setHint("@ " + nickname + " write reply");
        commentEditText.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(commentEditText, InputMethodManager.SHOW_IMPLICIT);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        TextView titleTextView = findViewById(R.id.feedTitle);
        TextView contentTextView = findViewById(R.id.feedContent);
        ImageView translationImageView = findViewById(R.id.imageTranslation);
        EditText commentEditText = findViewById(R.id.commentEditText);
        ImageView sendCommentButton = findViewById(R.id.sendCommentButton);

        int feedId = getIntent().getIntExtra("feed_id", -1);
        // 댓글 / 대댓글 recyclerView
        recyclerView = findViewById(R.id.commentRecyclerView);

        // 어댑터 설정 및 갱신
        commentDataList = new ArrayList<>();
        adapter = new CommentAdapter(commentDataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(FeedDetailActivity.this));
        recyclerView.addItemDecoration(new RecycleViewDecoration(15));

        // Api 호출
        loadBoardDetails("SNS", LoginData.getLoginUserNo(), feedId);

        ImageView thumbsUpImageView = findViewById(R.id.imageThumbsUp);
        TextView thumbsUpNumberTextView = findViewById(R.id.feedThumbsUpNumber);

        // 좋아요 버튼 클릭 리스너 설정
        thumbsUpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 좋아요 상태 변경
                isLiked = !isLiked;
                likeCount = isLiked ? likeCount + 1 : likeCount - 1;

                // UI 업데이트
                thumbsUpImageView.setColorFilter(isLiked ? Color.parseColor("#94DEF7") : Color.parseColor("#A4A4A4"));
                thumbsUpNumberTextView.setText(String.valueOf(likeCount));

            }
        });

        // 번역 버튼을 눌렀을 경우
        translationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTranslated = !isTranslated;

                if (isTranslated) {
                    Translator translator = new Translator();
                    translator.detectAndTranslate(boardDetails.get("title").getAsString(), new Translator.TranslationCallback() {
                        @Override
                        public void onTranslationDone(String translatedText) {
                            // 번역된 텍스트 처리
                            titleTextView.setText(translatedText);
                        }
                        @Override
                        public void onTranslationError(Exception e) {
                            // 에러 처리
                            Log.e("Translation", "Error during translation", e);
                        }
                    });
                    translator.detectAndTranslate(boardDetails.get("content").getAsString(), new Translator.TranslationCallback() {
                        @Override
                        public void onTranslationDone(String translatedText) {
                            // 번역된 텍스트 처리
                            contentTextView.setText(translatedText);
                        }
                        @Override
                        public void onTranslationError(Exception e) {
                            // 에러 처리
                            Log.e("Translation", "Error during translation", e);
                        }
                    });
                    translationImageView.setColorFilter(Color.parseColor("#94DEF7"));
                } else {
                    titleTextView.setText(boardDetails.get("title").getAsString());
                    contentTextView.setText(boardDetails.get("content").getAsString());
                    translationImageView.setColorFilter(Color.parseColor("#A4A4A4"));
                }
            }
        });

        // 댓글 달기
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentEditText.getText().toString();
                if (!commentText.isEmpty()) {
                    // 댓글 데이터 생성
                    JsonObject commentObject = new JsonObject();
                    commentObject.addProperty("content", commentText);
                    commentObject.addProperty("post_no", boardDetails.get("post_no").getAsInt());
                    commentObject.addProperty("user_no", LoginData.getLoginUserNo());
                    // 만약 대댓글이라면 해당 부모 댓글의 ID를 지정
                    // commentObject.addProperty("parent_comment_no", isReplyMode ? replyToCommentId : JsonNull.INSTANCE);

                    // parent_comment_no는 일단 null로 설정
                    commentObject.addProperty("parent_comment_no", replyToCommentId);

                    // JSON을 RequestBody로 변환
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), commentObject.toString());

                    // API 호출
                    BoardApiService service = RetrofitClient.getService(BoardApiService.class);
                    Call<JsonObject> call = service.insertComment(requestBody);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                // 댓글 작성 성공 처리
                                Log.d("FeedDetailActivity", "Comment posted successfully");
                                // UI 업데이트 및 댓글 입력창 초기화
                                resetCommentInput();
                                loadBoardDetails("SNS", LoginData.getLoginUserNo(), feedId);
                            } else {
                                // 서버 오류 처리
                                Log.e("FeedDetailActivity", "Server error occurred while posting comment");
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            // 네트워크 오류 처리
                            Log.e("FeedDetailActivity", "Network error: " + t.getMessage());
                        }
                    });
                }
            }
            private void resetCommentInput() {
                isReplyMode = false;
                replyToCommentId = -1;
                replyToNickname = null;
                commentEditText.setHint("");
                commentEditText.setText("");

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
            }
        });

        // EditText 포커스 리스너 추가
        commentEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 대댓글 모드 해제 및 힌트 초기화
                    isReplyMode = false;
                    replyToCommentId = -1;
                    replyToNickname = null;
                    commentEditText.setHint("");
                    commentEditText.setText("");
                }
            }
        });

        // 뒤로가기 아이콘을 눌렀을 경우
        // onBackPressed()도 생각하기
        ImageView iconBack = findViewById(R.id.iconBack);
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Y".equals(boardDetails.get("like_yn").getAsString()) != isLiked) {
                    // 상태 업데이트 API 호출
                    updateLikeStatusOnServer();
                }
                updateAllCommentLikeStatusOnServer();
                updateAllCommentHateStatusOnServer();
                finish();
            }
        });
    }

    public void onBackPressed() {
        // 좋아요 상태가 변경된 경우 확인
        if ("Y".equals(boardDetails.get("like_yn").getAsString()) != isLiked) {
            // 상태 업데이트 API 호출
            updateLikeStatusOnServer();
        }
        updateAllCommentLikeStatusOnServer();
        updateAllCommentHateStatusOnServer();
        // 기본 뒤로가기 동작 수행
        super.onBackPressed();
    }

    private void updateAllCommentLikeStatusOnServer() {
        for (CommentData comment : commentDataList) {
            if (comment.isLikeStatusChanged()) {
                updateCommentLikeStatusOnServer(comment);
            }
        }
    }

    private void updateAllCommentHateStatusOnServer() {
        for (CommentData comment : commentDataList) {
            if (comment.isHateStatusChanged()) {
                updateCommentHateStatusOnServer(comment);
            }
        }
    }

    private void updateCommentLikeStatusOnServer(CommentData comment) {
        String likeYN = comment.isLiked() ? "Y" : "N";
        int userId = LoginData.getLoginUserNo();
        int commentId = comment.getCommentId();

        BoardApiService service = RetrofitClient.getService(BoardApiService.class);
        Call<JsonObject> call = service.updateCommentLike(likeYN, userId, commentId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("FeedDetailActivity", "Comment like status updated successfully");
                } else {
                    Log.e("FeedDetailActivity", "Server error occurred while updating comment like status");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("FeedDetailActivity", "Network error: " + t.getMessage());
            }
        });
    }

    private void updateCommentHateStatusOnServer(CommentData comment) {
        String hateYN = comment.isHated() ? "Y" : "N";
        int userId = LoginData.getLoginUserNo();
        int commentId = comment.getCommentId();

        BoardApiService service = RetrofitClient.getService(BoardApiService.class);
        Log.d("test", hateYN);
        Call<JsonObject> call = service.updateCommentHate(hateYN, userId, commentId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("FeedDetailActivity", "Comment hate status updated successfully");
                } else {
                    Log.e("FeedDetailActivity", "Server error occurred while updating comment hate status");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("FeedDetailActivity", "Network error: " + t.getMessage());
            }
        });
    }

    private void updateLikeStatusOnServer() {
        String likeYN = isLiked ? "Y" : "N";
        int userId = LoginData.getLoginUserNo();
        int boardId = boardDetails.get("post_no").getAsInt();

        BoardApiService service = RetrofitClient.getService(BoardApiService.class);
        Call<JsonObject> call = service.updateBoardLike(likeYN, userId, boardId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("FeedDetailActivity", "Board like status updated successfully");
                } else {
                    Log.e("FeedDetailActivity", "Server error occurred while updating board like status");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("FeedDetailActivity", "Network error: " + t.getMessage());
            }
        });
    }

    private FeedData findFeedById(int feedId) {
        List<FeedData> feedList = DataManager.getInstance().getFeedList();
        for (FeedData feed : feedList) {
            if (feed.getId() == feedId) {
                return feed;
            }
        }
        return null;
    }

    private void loadBoardDetails(String boardType, Integer userId, Integer boardId) {
        BoardApiService boardApiServices = RetrofitClient.getService(BoardApiService.class);
        Call<JsonObject> call = boardApiServices.getBoardDetails(boardType, userId, boardId);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    List<CommentData> newCommentDataList = new ArrayList<>();
                    boardDetails = response.body();
                    // 피드 UI 업데이트
                    updateUIWithBoardDetails(boardDetails);
                    commentDataList.clear();
                    // 댓글 데이터 추출 및 변환
                    JsonArray commentsJsonArray = boardDetails.getAsJsonArray("comments");
                    for (JsonElement commentElement : commentsJsonArray) {
                        commentObject = commentElement.getAsJsonObject();

                        CommentData commentData = new CommentData(
                                commentObject.get("comment_no").getAsInt(),
                                R.drawable.logo_profile, // 프로필 이미지
                                commentObject.get("user_id").getAsString(),
                                commentObject.get("created_at").getAsString(),
                                commentObject.get("content").getAsString(),
                                commentObject.get("comment_like_num").getAsInt(), // thumbsUpNumber
                                commentObject.get("comment_hate_num").getAsInt(), // thumbsDownNumber
                                commentObject.get("parent_comment_no").getAsInt(), // replyToCommentId
                                commentObject.get("parent_comment_user_id").getAsString(),  // replyToNickname
                                commentObject.get("comment_like_yn").getAsString(),
                                commentObject.get("comment_hate_yn").getAsString()
                        );
                        commentDataList.add(commentData);
                    }
                    commentDataList.addAll(newCommentDataList);
                    adapter.setDataList(commentDataList);
                    adapter.notifyDataSetChanged();
                    
                } else {
                    Log.d("FeedDetail", "Server error");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("FeedDetail", "Network error: " + t.getMessage());
            }
        });
    }

    private void updateUIWithBoardDetails(JsonObject boardDetails) {
        CircleImageView profileImageView = findViewById(R.id.feedProfileImage);
        TextView nicknameTextView = findViewById(R.id.feedNickname);
        TextView dateTextView = findViewById(R.id.feedDate);
        TextView titleTextView = findViewById(R.id.feedTitle);
        TextView contentTextView = findViewById(R.id.feedContent);
        ImageView thumbsUpImageView = findViewById(R.id.imageThumbsUp);
        TextView thumbsUpNumberTextView = findViewById(R.id.feedThumbsUpNumber);
        ImageView commentImageView = findViewById(R.id.imageComment);
        TextView commentNumberTextView = findViewById(R.id.feedCommentNumber);
        TextView numOfComments = findViewById(R.id.numOfComments);

        // 임시
        profileImageView.setImageResource(R.drawable.logo_profile);
        nicknameTextView.setText(boardDetails.get("post_user_id").getAsString());
        dateTextView.setText(boardDetails.get("created_at").getAsString());
        titleTextView.setText(boardDetails.get("title").getAsString());
        contentTextView.setText(boardDetails.get("content").getAsString());
        numOfComments.setText("Comments(" + String.valueOf(boardDetails.get("comment_num").getAsInt()) + ")");


        // 초기 좋아요 상태 설정
        isLiked = "Y".equals(boardDetails.get("like_yn").getAsString());
        likeCount = boardDetails.get("like_num").getAsInt();

        if (isLiked) {
            thumbsUpImageView.setColorFilter(Color.parseColor("#94DEF7"));
        } else {
            thumbsUpImageView.setColorFilter(Color.parseColor("#A4A4A4"));
        }
        thumbsUpNumberTextView.setText(String.valueOf(likeCount));

        // 초기 댓글 아이콘 색상 설정
        isCommented = "Y".equals(boardDetails.get("comment_yn").getAsString());
        commentCount = boardDetails.get("comment_num").getAsInt();

        // 댓글 아이콘 상태 설정
        if (isCommented) {
            commentImageView.setColorFilter(Color.parseColor("#94DEF7"));
        } else {
            commentImageView.setColorFilter(Color.parseColor("#A4A4A4"));
        }
        commentNumberTextView.setText(String.valueOf(commentCount));

        Log.d("test", String.valueOf(isCommented));
    }
}