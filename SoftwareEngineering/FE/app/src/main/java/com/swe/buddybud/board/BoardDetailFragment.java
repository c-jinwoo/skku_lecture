package com.swe.buddybud.board;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.swe.buddybud.R;
import com.swe.buddybud.common.RetrofitClient;
import com.swe.buddybud.home.CommentAdapter;
import com.swe.buddybud.home.CommentData;
import com.swe.buddybud.home.FeedDetailActivity;
import com.swe.buddybud.home.RecycleViewDecoration;
import com.swe.buddybud.home.Translator;
import com.swe.buddybud.user.LoginData;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardDetailFragment extends Fragment {
    private boolean isLiked;
    private int likeCount;
    private boolean isScrap;
    private int scrapCount;
    private boolean isTranslated = false;
    private boolean initialIsLiked;
    private boolean initialIsScrap;
    private int boardId;
    JsonObject boardDetails = null;
    public int getBoardId() {
        return boardId;
    }

    public boolean getIsLiked() {
        return isLiked;
    }

    public boolean isLikeStatusChanged() {
        return initialIsLiked != isLiked;
    }

    public boolean getIsScrap() {
        return isScrap;
    }

    public boolean isScrapStatusChanged() {
        return initialIsScrap != isScrap;
    }

    public BoardDetailFragment () {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_board_detail, container, false);

        int feedId = -1;
        // 번들에서 feed_id 얻기
        Bundle bundle = getArguments();
        if (bundle != null) {
            feedId = bundle.getInt("feed_id", -1); // 기본값으로 -1을 사용
        }

        BoardApiService boardApiServices = RetrofitClient.getService(BoardApiService.class);
        Call<JsonObject> call = boardApiServices.getBoardDetails("Notice", LoginData.getLoginUserNo(), feedId);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    boardDetails = response.body();

                    Log.d("test", boardDetails.get("title").getAsString());
                    CircleImageView profileImageView = rootView.findViewById(R.id.feedProfileImage);
                    TextView nicknameTextView = rootView.findViewById(R.id.feedNickname);
                    TextView dateTextView = rootView.findViewById(R.id.feedDate);
                    TextView titleTextView = rootView.findViewById(R.id.feedTitle);
                    TextView contentTextView = rootView.findViewById(R.id.feedContent);
                    ImageView thumbsUpImageView = rootView.findViewById(R.id.imageThumbsUp);
                    TextView thumbsUpNumberTextView = rootView.findViewById(R.id.feedThumbsUpNumber);
                    ImageView scrapImageView = rootView.findViewById(R.id.imageScrap);
                    TextView scrapNumberTextView = rootView.findViewById(R.id.feedScrapNumber);
                    ImageView translationImageView = rootView.findViewById(R.id.imageTranslation);

                    //임시
                    profileImageView.setImageResource(R.drawable.logo_profile);
                    nicknameTextView.setText(boardDetails.get("post_user_id").getAsString());
                    dateTextView.setText(boardDetails.get("created_at").getAsString());
                    titleTextView.setText(boardDetails.get("title").getAsString());
                    contentTextView.setText(boardDetails.get("content").getAsString());
                    thumbsUpNumberTextView.setText(String.valueOf(boardDetails.get("like_num").getAsInt()));
                    scrapNumberTextView.setText(String.valueOf(boardDetails.get("scrap_num").getAsInt()));

                    // 초기 좋아요 상태 설정
                    isLiked = "Y".equals(boardDetails.get("like_yn").getAsString());
                    likeCount = boardDetails.get("like_num").getAsInt();
                    initialIsLiked = "Y".equals(boardDetails.get("like_yn").getAsString());
                    boardId = boardDetails.get("post_no").getAsInt();

                    // 초기 스크랩 상태 설정
                    isScrap = "Y".equals(boardDetails.get("scrap_yn").getAsString());
                    scrapCount = boardDetails.get("scrap_num").getAsInt();
                    initialIsScrap = "Y".equals(boardDetails.get("scrap_yn").getAsString());

                    if (isLiked) {
                        thumbsUpImageView.setColorFilter(Color.parseColor("#94DEF7"));
                    } else {
                        thumbsUpImageView.setColorFilter(Color.parseColor("#A4A4A4"));
                    }
                    thumbsUpNumberTextView.setText(String.valueOf(likeCount));

                    if (isScrap) {
                        scrapImageView.setColorFilter(Color.parseColor("#94DEF7"));
                    } else {
                        scrapImageView.setColorFilter(Color.parseColor("#A4A4A4"));
                    }
                    scrapNumberTextView.setText(String.valueOf(scrapCount));
                } else {
                    Log.d("FeedDetail", "Server error");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("FeedDetail", "Network error: " + t.getMessage());
            }
        });

        ImageView thumbsUpImageView = rootView.findViewById(R.id.imageThumbsUp);
        TextView thumbsUpNumberTextView = rootView.findViewById(R.id.feedThumbsUpNumber);
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

        ImageView scrapImageView = rootView.findViewById(R.id.imageScrap);
        TextView scrapNumberTextView = rootView.findViewById(R.id.feedScrapNumber);
        // 스크랩 버튼 클릭 리스너 설정
        scrapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 좋아요 상태 변경
                isScrap = !isScrap;
                scrapCount = isScrap ? scrapCount + 1 : scrapCount - 1;

                // UI 업데이트
                scrapImageView.setColorFilter(isScrap ? Color.parseColor("#94DEF7") : Color.parseColor("#A4A4A4"));
                scrapNumberTextView.setText(String.valueOf(scrapCount));

            }
        });

        TextView titleTextView = rootView.findViewById(R.id.feedTitle);
        TextView contentTextView = rootView.findViewById(R.id.feedContent);
        ImageView translationImageView = rootView.findViewById(R.id.imageTranslation);
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

        ImageView imageLink = rootView.findViewById(R.id.imageLink);
        // 링크 아이콘을 눌렀을 경우
        imageLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView iconBack = rootView.findViewById(R.id.iconBack);
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 좋아요 상태가 변경된 경우 확인
                if ("Y".equals(boardDetails.get("like_yn").getAsString()) != isLiked) {
                    updateLikeStatusOnServer();
                }
                // 스크랩 상태가 변경된 경우 확인
                if ("Y".equals(boardDetails.get("scrap_yn").getAsString()) != isScrap) {
                    updateScrapStatusOnServer();
                }

                // 이전 Fragment로 돌아가기
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        return rootView;
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

    private void updateScrapStatusOnServer() {
        String scrapYN = isScrap ? "Y" : "N";
        int userId = LoginData.getLoginUserNo();
        int boardId = boardDetails.get("post_no").getAsInt();

        BoardApiService service = RetrofitClient.getService(BoardApiService.class);
        Call<JsonObject> call = service.updateScrap(scrapYN, userId, boardId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("FeedDetailActivity", "Board scrap status updated successfully");
                } else {
                    Log.e("FeedDetailActivity", "Server error occurred while updating board scrap status");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("FeedDetailActivity", "Network error: " + t.getMessage());
            }
        });
    }


//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        ImageButton back_1_Button = view.findViewById(R.id.back_1_Button);
//        back_1_Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment = new BoardFragment();
//
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.board_detail_container, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });
//    }
}