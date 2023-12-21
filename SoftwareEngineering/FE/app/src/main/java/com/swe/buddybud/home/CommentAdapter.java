package com.swe.buddybud.home;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.swe.buddybud.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CommentData> commentDataList;
    private static final int TYPE_COMMENT = 0;
    private static final int TYPE_REPLY = 1;
    public CommentAdapter(List<CommentData> commentDataList) {
        this.commentDataList = commentDataList;
    }

    public void setDataList(List<CommentData> commentDataList) {
        this.commentDataList = commentDataList;
    }

    @Override
    public int getItemViewType(int position) {
        CommentData item = commentDataList.get(position);
        if (item.getReplyToCommentId() != item.getCommentId()) {
            return TYPE_REPLY;
        } else {
            return TYPE_COMMENT;
        }
    }

    // 댓글 뷰 홀더
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        ImageView imageThumbsUp, imageThumbsDown, imageTranslation, imageRecomment;
        public TextView nickname, date, content, thumbsUpNumber, thumbsDownNumber;

        public CommentViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.commentProfileImage);
            nickname = itemView.findViewById(R.id.commentNickname);
            date = itemView.findViewById(R.id.commentDate);
            content = itemView.findViewById(R.id.commentText);
            thumbsUpNumber = itemView.findViewById(R.id.commentThumbsUpNumber);
            thumbsDownNumber = itemView.findViewById(R.id.feedCommentNumber);
            imageThumbsUp = itemView.findViewById(R.id.imageThumbsUp);
            imageThumbsDown = itemView.findViewById(R.id.imageThumbsDown);
            imageTranslation = itemView.findViewById(R.id.imageTranslation);
            imageRecomment = itemView.findViewById(R.id.imageRecomment);
        }
    }

    // 대댓글 뷰 홀더
    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        ImageView imageThumbsUp, imageThumbsDown, imageTranslation, imageRecomment;
        public TextView nickname, date, content, thumbsUpNumber, thumbsDownNumber, replyNickname;

        public ReplyViewHolder(View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.commentProfileImage);
            nickname = itemView.findViewById(R.id.commentNickname);
            replyNickname = itemView.findViewById(R.id.commentReplyNickname);
            date = itemView.findViewById(R.id.commentDate);
            content = itemView.findViewById(R.id.commentText);
            thumbsUpNumber = itemView.findViewById(R.id.commentThumbsUpNumber);
            thumbsDownNumber = itemView.findViewById(R.id.feedCommentNumber);
            imageThumbsUp = itemView.findViewById(R.id.imageThumbsUp);
            imageThumbsDown = itemView.findViewById(R.id.imageThumbsDown);
            imageTranslation = itemView.findViewById(R.id.imageTranslation);
            imageRecomment = itemView.findViewById(R.id.imageRecomment);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_COMMENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_reply, parent, false);
            return new ReplyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentData commentData = commentDataList.get(position);

        if (holder.getItemViewType() == TYPE_COMMENT) {
            CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
            commentViewHolder.profileImage.setImageResource(commentData.getProfileImageId());;
            commentViewHolder.nickname.setText(commentData.getNickname());;
            commentViewHolder.date.setText(commentData.getDate());;
            commentViewHolder.content.setText(commentData.getContent());;
            commentViewHolder.thumbsUpNumber.setText(String.valueOf(commentData.getThumbsUpNumber()));;
            commentViewHolder.thumbsDownNumber.setText(String.valueOf(commentData.getThumbsDownNumber()));;

            // 댓글 좋아요 상태 설정
            boolean isLiked = commentData.isLiked();
            commentViewHolder.imageThumbsUp.setColorFilter(isLiked ? Color.parseColor("#94DEF7") : Color.parseColor("#A4A4A4"));
            commentViewHolder.thumbsUpNumber.setText(String.valueOf(commentData.getThumbsUpNumber()));

            // 댓글 좋아요 버튼 클릭 리스너
            commentViewHolder.imageThumbsUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 상태 변경
                    commentData.setLiked(!commentData.isLiked());
                    if (commentData.isLiked()) {
                        commentData.incrementLikeCount();
                    } else {
                        commentData.decrementLikeCount();
                    }
                    // 좋아요 상태가 변경되었는지 확인
                    if (!commentData.getInitialIsThumbsUpClicked().equals(commentData.getIsThumbsUpClicked())) {
                        commentData.setLikeStatusChanged(true);
                    } else {
                        commentData.setLikeStatusChanged(false);
                    }

                    // UI 업데이트
                    commentViewHolder.imageThumbsUp.setColorFilter(commentData.isLiked() ? Color.parseColor("#94DEF7") : Color.parseColor("#A4A4A4"));
                    commentViewHolder.thumbsUpNumber.setText(String.valueOf(commentData.getThumbsUpNumber()));
                }
            });

            // 댓글 싫어요 상태 설정
            boolean isHated = commentData.isHated();
            commentViewHolder.imageThumbsDown.setColorFilter(isHated ? Color.parseColor("#94DEF7") : Color.parseColor("#A4A4A4"));
            commentViewHolder.thumbsDownNumber.setText(String.valueOf(commentData.getThumbsDownNumber()));

            // 댓글 싫어요 버튼 클릭 리스너
            commentViewHolder.imageThumbsDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 상태 변경
                    commentData.setHated(!commentData.isHated());
                    if (commentData.isHated()) {
                        commentData.incrementHateCount();
                    } else {
                        commentData.decrementHateCount();
                    }
                    // 싫어요 상태가 변경되었는지 확인
                    if (!commentData.getInitialIsThumbsDownClicked().equals(commentData.getIsThumbsDownClicked())) {
                        commentData.setHateStatusChanged(true);
                    } else {
                        commentData.setHateStatusChanged(false);
                    }

                    // UI 업데이트
                    commentViewHolder.imageThumbsDown.setColorFilter(commentData.isHated() ? Color.parseColor("#94DEF7") : Color.parseColor("#A4A4A4"));
                    commentViewHolder.thumbsDownNumber.setText(String.valueOf(commentData.getThumbsDownNumber()));
                }
            });

            // 댓글 번역 아이콘을 눌렀을 경우
            commentViewHolder.imageTranslation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentData data = commentDataList.get(holder.getAdapterPosition());

                    data.setTranslated(!data.isTranslated());

                    if (data.isTranslated()) {
                        Translator translator = new Translator();
                        translator.detectAndTranslate(data.getContent(), new Translator.TranslationCallback() {
                            @Override
                            public void onTranslationDone(String translatedText) {
                                // 번역된 텍스트 처리
                                commentViewHolder.content.setText(translatedText);
                            }
                            @Override
                            public void onTranslationError(Exception e) {
                                // 에러 처리
                                Log.e("Translation", "Error during translation", e);
                            }
                        });
                        commentViewHolder.imageTranslation.setColorFilter(Color.parseColor("#94DEF7"));
                    } else {
                        commentViewHolder.content.setText(data.getContent());
                        commentViewHolder.imageTranslation.setColorFilter(Color.parseColor("#A4A4A4"));
                    }
                }
            });

            // reply 이미지를 눌렀을 경우
            commentViewHolder.imageRecomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = holder.itemView.getContext();
                    if (context instanceof FeedDetailActivity) {
                        FeedDetailActivity activity = (FeedDetailActivity) context;
                        activity.enableReplyMode(commentData.getReplyToCommentId(), commentData.getNickname());
                    }
                }
            });


        } else {
            ReplyViewHolder replyViewHolder = (ReplyViewHolder) holder;
            replyViewHolder.profileImage.setImageResource(commentData.getProfileImageId());;
            replyViewHolder.nickname.setText(commentData.getNickname());;
            replyViewHolder.replyNickname.setText(commentData.getReplyToNickname());
            replyViewHolder.date.setText(commentData.getDate());;
            replyViewHolder.content.setText(commentData.getContent());;
            replyViewHolder.thumbsUpNumber.setText(String.valueOf(commentData.getThumbsUpNumber()));;
            replyViewHolder.thumbsDownNumber.setText(String.valueOf(commentData.getThumbsDownNumber()));;

            // 대댓글 좋아요 상태 설정
            boolean isLiked = commentData.isLiked();
            replyViewHolder.imageThumbsUp.setColorFilter(isLiked ? Color.parseColor("#94DEF7") : Color.parseColor("#A4A4A4"));
            replyViewHolder.thumbsUpNumber.setText(String.valueOf(commentData.getThumbsUpNumber()));

            // 대댓글 좋아요 버튼 클릭 리스너
            replyViewHolder.imageThumbsUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 상태 변경
                    commentData.setLiked(!commentData.isLiked());
                    if (commentData.isLiked()) {
                        commentData.incrementLikeCount();
                    } else {
                        commentData.decrementLikeCount();
                    }
                    // 좋아요 상태가 변경되었는지 확인
                    if (!commentData.getInitialIsThumbsUpClicked().equals(commentData.getIsThumbsUpClicked())) {
                        commentData.setLikeStatusChanged(true);
                    } else {
                        commentData.setLikeStatusChanged(false);
                    }

                    // UI 업데이트
                    replyViewHolder.imageThumbsUp.setColorFilter(commentData.isLiked() ? Color.parseColor("#94DEF7") : Color.parseColor("#A4A4A4"));
                    replyViewHolder.thumbsUpNumber.setText(String.valueOf(commentData.getThumbsUpNumber()));
                }
            });

            // 대댓글 싫어요 상태 설정
            boolean isHated = commentData.isHated();
            replyViewHolder.imageThumbsDown.setColorFilter(isHated ? Color.parseColor("#94DEF7") : Color.parseColor("#A4A4A4"));
            replyViewHolder.thumbsDownNumber.setText(String.valueOf(commentData.getThumbsDownNumber()));

            // 대댓글 싫어요 버튼 클릭 리스너
            replyViewHolder.imageThumbsDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 상태 변경
                    commentData.setHated(!commentData.isHated());
                    if (commentData.isHated()) {
                        commentData.incrementHateCount();
                    } else {
                        commentData.decrementHateCount();
                    }
                    // 싫어요 상태가 변경되었는지 확인
                    if (!commentData.getInitialIsThumbsDownClicked().equals(commentData.getIsThumbsDownClicked())) {
                        commentData.setHateStatusChanged(true);
                    } else {
                        commentData.setHateStatusChanged(false);
                    }

                    // UI 업데이트
                    replyViewHolder.imageThumbsDown.setColorFilter(commentData.isHated() ? Color.parseColor("#94DEF7") : Color.parseColor("#A4A4A4"));
                    replyViewHolder.thumbsDownNumber.setText(String.valueOf(commentData.getThumbsDownNumber()));
                }
            });

            // 대댓글 번역 아이콘을 눌렀을 경우
            replyViewHolder.imageTranslation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentData data = commentDataList.get(holder.getAdapterPosition());

                    data.setTranslated(!data.isTranslated());

                    if (data.isTranslated()) {
                        Translator translator = new Translator();
                        translator.detectAndTranslate(data.getContent(), new Translator.TranslationCallback() {
                            @Override
                            public void onTranslationDone(String translatedText) {
                                // 번역된 텍스트 처리
                                replyViewHolder.content.setText(translatedText);
                            }
                            @Override
                            public void onTranslationError(Exception e) {
                                // 에러 처리
                                Log.e("Translation", "Error during translation", e);
                            }
                        });
                        replyViewHolder.imageTranslation.setColorFilter(Color.parseColor("#94DEF7"));
                    } else {
                        replyViewHolder.content.setText(data.getContent());
                        replyViewHolder.imageTranslation.setColorFilter(Color.parseColor("#A4A4A4"));
                    }
                }
            });

            // reply 이미지를 눌렀을 경우
            replyViewHolder.imageRecomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = holder.itemView.getContext();
                    if (context instanceof FeedDetailActivity) {
                        FeedDetailActivity activity = (FeedDetailActivity) context;
                        activity.enableReplyMode(commentData.getReplyToCommentId(), commentData.getNickname());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return commentDataList.size();
    }
}
