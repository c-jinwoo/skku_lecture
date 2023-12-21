package com.swe.buddybud.willow;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.swe.buddybud.R;
import com.swe.buddybud.common.RetrofitClient;
import com.swe.buddybud.user.LoginData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SentWillow_Adapter extends RecyclerView.Adapter<SentWillow_Adapter.ViewHolder> implements Filterable {
    private ArrayList<SentWillowData> mData;
    private ArrayList<SentWillowData> unFilteredData;
    private WillowManageInterface parentActivity;

    public void setmData(ArrayList<SentWillowData> mData) {
        this.mData = mData;
        this.unFilteredData = mData;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if(charString.isEmpty()){
                    mData = unFilteredData;
                } else {
                    ArrayList<SentWillowData> filterTempData = new ArrayList<>();
                    for(SentWillowData dat : unFilteredData){
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
                mData = (ArrayList<SentWillowData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView nameText;
        Button cancelBtn;
        int receiveUserNo;
        private WillowApiService willowApiService = RetrofitClient.getService(WillowApiService.class);

        public ViewHolder(View v) {
            super(v) ;
            nameText = (TextView)v.findViewById(R.id.sent_willow_id_txt);
            profileImage = (CircleImageView) v.findViewById(R.id.sent_willow_profile_img);
            cancelBtn = (Button) v.findViewById(R.id.request_cancel_btn);

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //make post body
                    Map<String, String> fields = new HashMap<>();
                    Gson gson = new Gson();

                    fields.put("receiver_no", String.valueOf(receiveUserNo));
                    fields.put("sender_no", String.valueOf(LoginData.getLoginUserNo()));

                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(fields));

                    //post deleteWillow
                    Call<WillowApiData> call = willowApiService.deleteWillow(requestBody);
                    call.enqueue(new Callback<WillowApiData>() {
                        @Override
                        public void onResponse(Call<WillowApiData> call, Response<WillowApiData> response) {
                            WillowApiData userResponse = response.body();


                            // User Info Insert 성공시
                            if(userResponse.getDeleteWillowResult()){
                                parentActivity.refreshWillowList();
                            }
                            else {
                                Toast.makeText(view.getContext(),"cancel Request Network Error", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        @Override
                        public void onFailure(Call<WillowApiData> call, Throwable t) {
                            Toast.makeText(view.getContext(),"cancel Request Network Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }
            });
        }
    }

    SentWillow_Adapter(ArrayList<SentWillowData> list, WillowManageInterface parent){
        this.mData = list;
        this.unFilteredData = list;
        this.parentActivity = parent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sentwillow_recycleview_item, parent, false);
        return new ViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.profileImage.setImageResource(mData.get(position).getImgResId());
        holder.nameText.setText(mData.get(position).getUserId());
        holder.receiveUserNo = mData.get(position).getUserNo();
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
