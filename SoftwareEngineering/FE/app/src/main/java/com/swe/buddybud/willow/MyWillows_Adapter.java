package com.swe.buddybud.willow;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swe.buddybud.R;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyWillows_Adapter extends RecyclerView.Adapter<MyWillows_Adapter.ViewHolder> implements Filterable {
    public int addData(MyWillowsData data) {
        if(this.mData.add(data))
            return mData.size()-1;
        else return 0;
    }

    public void setmData(ArrayList<MyWillowsData> mData) {
        this.mData = mData;
    }

    private ArrayList<MyWillowsData> mData;
    private ArrayList<MyWillowsData> unFilteredData;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if(charString.isEmpty()){
                    mData = unFilteredData;
                } else {
                    ArrayList<MyWillowsData> filterTempData = new ArrayList<>();
                    for(MyWillowsData dat : unFilteredData){
                        if(dat.getUserId().toLowerCase().contains(charString.toLowerCase())){
                            filterTempData.add(dat);
                        }
                    }
                    mData = filterTempData;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mData;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mData = (ArrayList<MyWillowsData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView nameText;
        TextView lastMsgText;
        TextView lastTimeText;

        Context context;
        int opponentNo;
        String opponentId;

        ViewHolder(View v) {
            super(v);
            context = v.getContext();
            nameText = (TextView) v.findViewById(R.id.willow_id_txt);
            lastMsgText = (TextView)v.findViewById(R.id.willow_lastmsg_txt);
            lastTimeText = (TextView)v.findViewById(R.id.willow_lastmsg_time);
            profileImage = (CircleImageView) v.findViewById(R.id.willow_profile_img);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WillowChatActivity.class);
                    intent.putExtra("OPPONENT_ID",opponentId);
                    intent.putExtra("OPPONENT_NO",opponentNo);
                    context.startActivity(intent);
                }
            });
        }
    }

    MyWillows_Adapter(ArrayList<MyWillowsData> list){
        mData = list;
        unFilteredData = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Context context = parent.getContext() ;
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.willowlist_recycleview_item, parent, false);
        //MyWillows_Adapter.ViewHolder vh = new MyWillows_Adapter.ViewHolder(view) ;
        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //MyWillowsData data = mData.get(position);
        holder.nameText.setText(mData.get(position).getUserId());
        holder.profileImage.setImageResource(mData.get(position).getImgResId());
        try{
            holder.lastTimeText.setText(mData.get(position).getLastMsgTime().format(DateTimeFormatter.ofPattern("yy/MM-dd HH:mm")));
            holder.lastMsgText.setText(mData.get(position).getLastMsg());
        } catch(DateTimeParseException e) {
            holder.lastTimeText.setText("");
            holder.lastMsgText.setText("");
        } catch(NullPointerException e ){
            holder.lastTimeText.setText("");
            holder.lastMsgText.setText("");
        }
        holder.opponentId = mData.get(position).getUserId();
        holder.opponentNo = mData.get(position).getUserNo();

    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
