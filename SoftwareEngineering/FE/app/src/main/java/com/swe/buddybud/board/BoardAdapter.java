package com.swe.buddybud.board;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.swe.buddybud.R;
import com.swe.buddybud.common.RetrofitClient;
import com.swe.buddybud.home.DataManager;
import com.swe.buddybud.home.FeedData;
import com.swe.buddybud.home.Translator;
import com.swe.buddybud.user.LoginData;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {

    private List<BoardData> dataList;

    public BoardAdapter(List<BoardData> dataList) {
        this.dataList = dataList;
    }

    public void setDataList(List<BoardData> dataList) {
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board, parent, false);
        return new BoardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        BoardData data = dataList.get(position);

        holder.feedProfileImage.setImageResource(data.getProfileImageId());
        holder.feedNickname.setText(data.getNickname());
        holder.feedDate.setText(data.getDate());
        holder.feedTitle.setText(data.getTitle());
        holder.feedContent.setText(data.getContent());
        holder.feedThumbsUpNumber.setText(String.valueOf(data.getThumbsUpNumber()));
        holder.feedScrapNumber.setText(String.valueOf(data.getScrapNumber()));

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

        // 스크랩 상태 설정
        boolean isScrap = data.isScrap();
        holder.imageScrap.setColorFilter(isScrap ? Color.parseColor("#94DEF7") : Color.parseColor("#A4A4A4"));
        holder.feedScrapNumber.setText(String.valueOf(data.getScrapNumber()));

        // 스크랩 버튼 클릭 리스너
        holder.imageScrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 상태 변경
                data.setScrap(!data.isScrap());
                if (data.isScrap()) {
                    data.incrementScrapCount();
                } else {
                    data.decrementScrapCount();
                }

                // UI 업데이트
                holder.imageScrap.setColorFilter(data.isScrap() ? Color.parseColor("#94DEF7") : Color.parseColor("#A4A4A4"));
                holder.feedScrapNumber.setText(String.valueOf(data.getScrapNumber()));
            }
        });

        // 제목과 내용 max line 설정
        holder.feedTitle.setMaxLines(1);
        holder.feedTitle.setEllipsize(TextUtils.TruncateAt.END);
        holder.feedContent.setMaxLines(4);
        holder.feedContent.setEllipsize(TextUtils.TruncateAt.END);

        // 번역 아이콘을 눌렀을 경우
        holder.imageTranslation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardData data = dataList.get(holder.getAdapterPosition());

                data.setTranslated(!data.isTranslated());

                if (data.isTranslated()) {
                    Translator translator = new Translator();
                    translator.detectAndTranslate(data.getTitle(), new Translator.TranslationCallback() {
                        @Override
                        public void onTranslationDone(String translatedText) {
                            holder.feedTitle.setText(translatedText);
                        }
                        @Override
                        public void onTranslationError(Exception e) {
                            Log.e("Translation", "Error during translation", e);
                        }
                    });
                    translator.detectAndTranslate(data.getContent(), new Translator.TranslationCallback() {
                        @Override
                        public void onTranslationDone(String translatedText) {
                            holder.feedContent.setText(translatedText);
                        }
                        @Override
                        public void onTranslationError(Exception e) {
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

        // 링크 아이콘을 눌렀을 경우
        holder.imageLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

        // 피드 아이템 클릭 리스너
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 변경된 좋아요 상태를 추적하고 필요한 경우 API 호출
                updateAllLikesAndScrapsBeforeNavigating();

                // BoardDetailFragment로 이동
                BoardDetailFragment boardDetailFragment = new BoardDetailFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("feed_id", data.getId());
                boardDetailFragment.setArguments(bundle);

                FragmentTransaction transaction = ((FragmentActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainFrameLayout, boardDetailFragment);
                transaction.addToBackStack(null); // 백 스택에 추가
                transaction.commit();
            }
        });
    }

    private void updateAllLikesAndScrapsBeforeNavigating() {
        for (BoardData data : dataList) {
            // 좋아요 상태가 변경된 경우 확인
            if (!data.getInitialIsThumbsUpClicked().equals(data.getIsThumbsUpClicked())) {
                String likeYN = data.getIsThumbsUpClicked();
                int userId = LoginData.getLoginUserNo();
                int boardId = data.getId();
                updateLikeStatus(likeYN, userId, boardId);
            }
            // 스크랩 상태가 변경된 경우 확인
            if (!data.getInitialIsScrapClicked().equals(data.getIsScrapClicked())) {
                String scrapYN = data.getIsScrapClicked();
                int userId = LoginData.getLoginUserNo();
                int boardId = data.getId();
                updateScrapStatus(scrapYN, userId, boardId);
            }
        }
    }
    private void updateLikeStatus(String likeYN, int userId, int boardId) {
        BoardApiService service = RetrofitClient.getService(BoardApiService.class);
        Call<JsonObject> call = service.updateBoardLike(likeYN, userId, boardId);
        Log.d("test",likeYN);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("BoardAdapter", "Board like status updated successfully");
                } else {
                    Log.e("BoardAdapter", "Server error occurred while updating board like status");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("BoardAdapter", "Network error: " + t.getMessage());
            }
        });
    }

    private void updateScrapStatus(String scrapYN, int userId, int boardId) {
        BoardApiService service = RetrofitClient.getService(BoardApiService.class);
        Call<JsonObject> call = service.updateScrap(scrapYN, userId, boardId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("BoardAdapter", "Board scrap status updated successfully");
                } else {
                    Log.e("BoardAdapter", "Server error occurred while updating board scrap status");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("BoardAdapter", "Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        CircleImageView feedProfileImage;
        ImageView imageThumbsUp, imageScrap, imageTranslation, imageLink;
        TextView feedNickname, feedDate, feedTitle, feedContent, feedThumbsUpNumber, feedScrapNumber;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            feedProfileImage = itemView.findViewById(R.id.feedProfileImage);
            imageThumbsUp = itemView.findViewById(R.id.imageThumbsUp);
            imageScrap = itemView.findViewById(R.id.imageScrap);
            imageTranslation = itemView.findViewById(R.id.imageTranslation);
            imageLink = itemView.findViewById(R.id.imageLink);
            feedNickname = itemView.findViewById(R.id.feedNickname);
            feedDate = itemView.findViewById(R.id.feedDate);
            feedTitle = itemView.findViewById(R.id.feedTitle);
            feedContent = itemView.findViewById(R.id.feedContent);
            feedThumbsUpNumber = itemView.findViewById(R.id.feedThumbsUpNumber);
            feedScrapNumber = itemView.findViewById(R.id.feedScrapNumber);
        }
    }

}
