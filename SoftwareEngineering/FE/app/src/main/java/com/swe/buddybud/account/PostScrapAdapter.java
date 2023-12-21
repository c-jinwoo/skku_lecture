package com.swe.buddybud.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.swe.buddybud.R;

import java.util.List;

public class PostScrapAdapter extends RecyclerView.Adapter<PostScrapAdapter.ViewHolder> {

    private final List<PostScrapData> items;
    private final OnScrapItemClickListener listener;

    // Interface for handling item clicks
    public interface OnScrapItemClickListener {
        void onScrapClick(PostScrapData scrapData);
    }

    // Adapter constructor
    public PostScrapAdapter(List<PostScrapData> items, OnScrapItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userid, date, title, content, thumbs_up_num, comment_num;
        private PostScrapData item; // Field to hold the current item

        public ViewHolder(View view, final OnScrapItemClickListener listener) {
            super(view);
            userid = view.findViewById(R.id.userid);
            date = view.findViewById(R.id.date);
            title = view.findViewById(R.id.title);
            content = view.findViewById(R.id.content);
            thumbs_up_num = view.findViewById(R.id.thumbsUpNumber);
            comment_num = view.findViewById(R.id.commentNumber);

            view.setOnClickListener(v -> {
                if (listener != null && item != null) {
                    listener.onScrapClick(item);
                }
            });
        }

        public void setItem(PostScrapData item) {
            this.item = item;
            userid.setText(item.getUserid());
            date.setText(item.getDate());
            title.setText(item.getTitle());
            content.setText(limitString(item.getContent(), 100)); // Limit content to 100 characters
            thumbs_up_num.setText(String.valueOf(item.getThumbsUpNumber()));
            comment_num.setText(String.valueOf(item.getCommentNumber()));
        }

        private String limitString(String str, int limit) {
            if (str == null || str.length() <= limit) {
                return str;
            }
            return str.substring(0, limit) + "...";
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.account_post_or_scrap, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PostScrapData item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
