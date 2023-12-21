package com.swe.buddybud.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.swe.buddybud.R;
import com.swe.buddybud.board.BoardApiService;
import com.swe.buddybud.common.RetrofitClient;
import com.swe.buddybud.user.LoginData;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    private List<FeedData> dataList;
    private boolean isCommented;
    private int commentCount;

    public FeedAdapter(List<FeedData> dataList) {
        this.dataList = dataList;
    }

    public void setDataList(List<FeedData> dataList) {
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        FeedData data = dataList.get(position);

        holder.feedProfileImage.setImageResource(data.getProfileImageId());
        holder.feedNickname.setText(data.getNickname());
        holder.feedDate.setText(data.getDate());
        holder.feedTitle.setText(data.getTitle());
        holder.feedContent.setText(data.getContent());
        holder.feedThumbsUpNumber.setText(String.valueOf(data.getThumbsUpNumber()));
        holder.feedCommentNumber.setText(String.valueOf(data.getCommentNumber()));

        // 좋아요 상태 설정
        boolean isLiked = data.isLiked();
        holder.imageThumbsUp.setColorFilter(isLiked ? Color.parseColor("#94DEF7") : Color.parseColor("#A4A4A4"));
        holder.feedThumbsUpNumber.setText(String.valueOf(data.getThumbsUpNumber()));

        // 좋아요 버튼 클릭 리스너
        holder.imageThumbsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 상태 변경
                data.setLiked(!data.isLiked());
                if (data.isLiked()) {
                    data.incrementLikeCount();
                } else {
                    data.decrementLikeCount();
                }

                // UI 업데이트
                holder.imageThumbsUp.setColorFilter(data.isLiked() ? Color.parseColor("#94DEF7") : Color.parseColor("#A4A4A4"));
                holder.feedThumbsUpNumber.setText(String.valueOf(data.getThumbsUpNumber()));
            }
        });

        // 댓글 아이콘 색상 설정
        isCommented = "Y".equals(data.isCommentWritten());
        commentCount = data.getCommentNumber();
        
        // 댓글 아이콘 상태 설정
        if (isCommented) {
            holder.imageComment.setColorFilter(Color.parseColor("#94DEF7"));
        } else {
            holder.imageComment.setColorFilter(Color.parseColor("#A4A4A4"));
        }
        holder.feedCommentNumber.setText(String.valueOf(data.getCommentNumber()));

        // 제목과 내용 max line 설정
        holder.feedTitle.setMaxLines(1);
        holder.feedTitle.setEllipsize(TextUtils.TruncateAt.END);
        holder.feedContent.setMaxLines(4);
        holder.feedContent.setEllipsize(TextUtils.TruncateAt.END);

        // 번역 아이콘을 눌렀을 경우
        holder.imageTranslation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedData data = dataList.get(holder.getAdapterPosition());

                data.setTranslated(!data.isTranslated());

                if (data.isTranslated()) {
                    Translator translator = new Translator();
                    translator.detectAndTranslate(data.getTitle(), new Translator.TranslationCallback() {
                        @Override
                        public void onTranslationDone(String translatedText) {
                            // 번역된 텍스트 처리
                            holder.feedTitle.setText(translatedText);
                        }
                        @Override
                        public void onTranslationError(Exception e) {
                            // 에러 처리
                            Log.e("Translation", "Error during translation", e);
                        }
                    });
                    translator.detectAndTranslate(data.getContent(), new Translator.TranslationCallback() {
                        @Override
                        public void onTranslationDone(String translatedText) {
                            // 번역된 텍스트 처리
                            holder.feedContent.setText(translatedText);
                        }
                        @Override
                        public void onTranslationError(Exception e) {
                            // 에러 처리
                            Log.e("Translation", "Error during translation", e);
                        }
                    });
                    holder.imageTranslation.setColorFilter(Color.parseColor("#94DEF7"));
                } else {
                    holder.feedTitle.setText(data.getTitle());
                    holder.feedContent.setText(data.getContent());
                    holder.imageTranslation.setColorFilter(Color.parseColor("#A4A4A4"));
                }
            }
        });

        // 프로필 사진을 눌렀을 경우
        holder.feedProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!data.getNickname().equals(LoginData.getLoginUserId())) {
                    Context context = v.getContext();
                    if (context instanceof AppCompatActivity) {
                        AppCompatActivity activity = (AppCompatActivity) context;
                        FragmentManager fragmentManager = activity.getSupportFragmentManager();
                        CustomDialog customDialog = CustomDialog.newInstance(data.getNickname(), data.getProfileImageId(), data.getUserId());

                        customDialog.setCustomDialogCallback(new CustomDialog.CustomDialogCallback() {
                            @Override
                            public void onWillowRequestSent(boolean success, boolean sendWillowSuccess) {
                                if (success) {
                                    if(sendWillowSuccess) {
                                        Toast.makeText(activity, "Send Willow request", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, "Willow request already sent", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        customDialog.show(fragmentManager, "customDialog");
                    }
                }
            }
        });

        // 피드 아이템 클릭 리스너
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 변경된 좋아요 상태를 추적하고 필요한 경우 API 호출
                updateAllLikesBeforeNavigating();

                // 상세 화면으로 이동
                Intent detailIntent = new Intent(view.getContext(), FeedDetailActivity.class);
                detailIntent.putExtra("feed_id", dataList.get(holder.getAdapterPosition()).getId());
                view.getContext().startActivity(detailIntent);
            }
        });
    }
    private void updateAllLikesBeforeNavigating() {
        for (FeedData data : dataList) {
            // 좋아요 상태가 변경된 경우 확인
            if (!data.getInitialIsThumbsUpClicked().equals(data.getIsThumbsUpClicked())) {
                String likeYN = data.getIsThumbsUpClicked();
                int userId = LoginData.getLoginUserNo();
                int boardId = data.getId();
                updateLikeStatus(likeYN, userId, boardId);
            }
        }
    }
    private void updateLikeStatus(String likeYN, int userId, int boardId) {
        BoardApiService service = RetrofitClient.getService(BoardApiService.class);
        Call<JsonObject> call = service.updateBoardLike(likeYN, userId, boardId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("FeedAdapter", "Board like status updated successfully");
                } else {
                    Log.e("FeedAdapter", "Server error occurred while updating board like status");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("FeedAdapter", "Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {
        CircleImageView feedProfileImage;
        ImageView imageThumbsUp, imageComment, imageTranslation;
        TextView feedNickname, feedDate, feedTitle, feedContent, feedThumbsUpNumber, feedCommentNumber;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            feedProfileImage = itemView.findViewById(R.id.feedProfileImage);
            imageThumbsUp = itemView.findViewById(R.id.imageThumbsUp);
            imageComment = itemView.findViewById(R.id.imageComment);
            imageTranslation = itemView.findViewById(R.id.imageTranslation);
            feedNickname = itemView.findViewById(R.id.feedNickname);
            feedDate = itemView.findViewById(R.id.feedDate);
            feedTitle = itemView.findViewById(R.id.feedTitle);
            feedContent = itemView.findViewById(R.id.feedContent);
            feedThumbsUpNumber = itemView.findViewById(R.id.feedThumbsUpNumber);
            feedCommentNumber = itemView.findViewById(R.id.feedCommentNumber);
        }
    }


}
