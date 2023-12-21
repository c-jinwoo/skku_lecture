package com.swe.buddybud.willow;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class IncomingWillow_Adapter extends RecyclerView.Adapter<IncomingWillow_Adapter.ViewHolder>{
    private ArrayList<IncomingWillowData> mData;
    private WillowManageInterface parentfrag;
    private WillowApiService willowApiService = RetrofitClient.getService(WillowApiService.class);

    public void setmData(ArrayList<IncomingWillowData> mData) {
        this.mData = mData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int sentUserNo;
        CircleImageView profileImage;
        TextView nameText;
        TextView infoText;
        Button acceptBtn;
        Button rejectBtn;

        public ViewHolder(View v) {
            super(v) ;
            nameText = (TextView)v.findViewById(R.id.sent_willow_id_txt);
            infoText = (TextView)v.findViewById(R.id.incoming_info_txt);
            profileImage = (CircleImageView) v.findViewById(R.id.sent_willow_profile_img);
            acceptBtn = (Button) v.findViewById(R.id.request_accept_btn);
            rejectBtn = (Button) v.findViewById(R.id.request_cancel_btn);
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO : acceptWillow ,notifyDataSetChanged()
                    //make post body
                    Map<String, String> fields = new HashMap<>();
                    Gson gson = new Gson();

                    fields.put("receiver_no", String.valueOf(LoginData.getLoginUserNo()));
                    fields.put("sender_no", String.valueOf(sentUserNo));

                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(fields));

                    //post acceptwillow
                    Call<WillowApiData> call = willowApiService.acceptWillow(requestBody);
                    call.enqueue(new Callback<WillowApiData>() {
                        @Override
                        public void onResponse(Call<WillowApiData> call, Response<WillowApiData> response) {
                            WillowApiData userResponse = response.body();

                            // User Info Insert 성공시
                            if(userResponse.getAcceptWillowResult()){
                                parentfrag.refreshWillowList();
                            }
                            else {
                                Toast.makeText(view.getContext(),"acceptWillow Network Error", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        @Override
                        public void onFailure(Call<WillowApiData> call, Throwable t) {
                            Toast.makeText(view.getContext(),"acceptWillow Network Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                    /*
                    int idx = getAdapterPosition();
                    Context context = v.getContext();
                    Resources resources = context.getResources();
                    int resourceId = resources.getIdentifier(nameText.getText().toString().toLowerCase(), "drawable",  context.getPackageName());
                    if(resourceId <= 0) resourceId = R.drawable.profile;
                    MyWillowsData newWillow = new MyWillowsData(nameText.getText().toString(), null,"", resourceId);
                    parentfrag.onAddWillow(newWillow);
                    if(idx>=0) {
                        mData.remove(idx);
                        notifyItemRemoved(idx);
                    }

                     */
                }
            });

            rejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO : deleteWillow -> get receivedWillow -> notifyDataSetChanged
                    //make post body
                    Map<String, String> fields = new HashMap<>();
                    Gson gson = new Gson();

                    fields.put("receiver_no", String.valueOf(LoginData.getLoginUserNo()));
                    fields.put("sender_no", String.valueOf(sentUserNo));

                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(fields));

                    //post acceptwillow
                    Call<WillowApiData> call = willowApiService.deleteWillow(requestBody);
                    call.enqueue(new Callback<WillowApiData>() {
                        @Override
                        public void onResponse(Call<WillowApiData> call, Response<WillowApiData> response) {
                            WillowApiData userResponse = response.body();

                            // User Info Insert 성공시
                            if(userResponse.getDeleteWillowResult()){
                                parentfrag.refreshWillowList();
                            }
                            else {
                                Toast.makeText(view.getContext(),"rejectWillow Network Error", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        @Override
                        public void onFailure(Call<WillowApiData> call, Throwable t) {
                            Toast.makeText(view.getContext(),"rejectWillow Network Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                    /*
                    int idx = getAdapterPosition();
                    mData.remove(idx);
                    notifyItemRemoved(idx);

                     */
                }
            });
        }
    }

    IncomingWillow_Adapter(ArrayList<IncomingWillowData> list, WillowManageInterface parentfrag){
        this.mData = list;
        this.parentfrag = parentfrag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.incomingwillow_recycleview_item, parent, false);
        //IncomingWillow_Adapter.ViewHolder vh = new IncomingWillow_Adapter.ViewHolder(view);
        return new ViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Log.d("IncomingWillowAdapter","Element" + position + "set" + mData.toString());

        //IncomingWillowData data = mData.get(position);
        holder.nameText.setText(mData.get(position).getUserId());
        holder.profileImage.setImageResource(mData.get(position).getImgResId());
        holder.infoText.setText(mData.get(position).getDept() + "\n" + mData.get(position).getGender());
        holder.sentUserNo = mData.get(position).getUserNo();
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
