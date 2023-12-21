package com.swe.buddybud.willow;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swe.buddybud.R;
import com.swe.buddybud.home.Translator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class WillowChat_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int LEFT_CONTENT = 0;
    public static final int RIGHT_CONTENT = 1;
    private ArrayList<ChatData> mData;

    private String userid;

    WillowChat_Adapter(ArrayList<ChatData> list, String userid){
        mData = list;
        this.userid = userid;
    }

    public class ReceivingViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImg;
        TextView name;
        TextView message;
        TextView sendTime;
        ImageButton translateBtn;
        ImageView image;
        boolean translated = false;
        String translatedString;
        String originalString;

        ReceivingViewHolder(View v) {
            super(v);
            profileImg = v.findViewById(R.id.sent_willow_profile_img);
            name = v.findViewById(R.id.receiving_sender_txt);
            message = v.findViewById(R.id.receiving_message_txt);
            sendTime = v.findViewById(R.id.receiving_time_text);
            translateBtn = v.findViewById(R.id.receiving_translate_btn);
            image = v.findViewById(R.id.receiving_message_img);
            translateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(message.getText()==null || message.getText().length()<1) return;
                    if(translated){
                        message.setText(originalString);
                        translateBtn.setColorFilter(Color.parseColor("#A4A4A4"));
                    } else {
                        if(translatedString!=null && translatedString.length()>0) {
                            translateBtn.setColorFilter(Color.parseColor("#94DEF7"));
                            message.setText(translatedString);
                        } else {
                            Translator translator = new Translator();
                            translator.detectAndTranslate(message.getText().toString(), new Translator.TranslationCallback() {
                                @Override
                                public void onTranslationDone(String translatedText) {
                                    translatedString = translatedText;
                                    message.setText(translatedString);
                                    translateBtn.setColorFilter(Color.parseColor("#94DEF7"));
                                }
                                @Override
                                public void onTranslationError(Exception e) {
                                    Log.e("Translation", "Error during translation", e);
                                }
                            });
                        }
                    }
                    translated = !translated;


                }
            });
        }
    }

    public class SendingViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView sendTime;
        ImageView image;
        SendingViewHolder(View v) {
            super(v) ;
            message = v.findViewById(R.id.sending_message_txt);
            sendTime = v.findViewById(R.id.sending_time_text);
            image = v.findViewById(R.id.sending_message_img);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.get(position).getName().equals(userid)){
            return RIGHT_CONTENT;
        } else {
            return LEFT_CONTENT;
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType == LEFT_CONTENT)
        {
            view = inflater.inflate(R.layout.receiving_message, parent, false);
            return new ReceivingViewHolder(view);
        }
        else
        {
            view = inflater.inflate(R.layout.sending_message, parent, false);
            return new SendingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ReceivingViewHolder){
            if(mData.get(position).getMessage() == null || mData.get(position).getMessage().length()<1) {
                ((ReceivingViewHolder) holder).message.setText("");
                ((ReceivingViewHolder) holder).translatedString = "";
            }
            else {
                ((ReceivingViewHolder) holder).message.setText(mData.get(position).getMessage());
                ((ReceivingViewHolder) holder).originalString = mData.get(position).getMessage();
            }
            ((ReceivingViewHolder) holder).name.setText(mData.get(position).getName());
            ((ReceivingViewHolder) holder).sendTime.setText(mData.get(position).getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm")));
            ((ReceivingViewHolder) holder).profileImg.setImageResource(mData.get(position).getImgResId());
            if(mData.get(position).getImageURI() != null) {
                ((ReceivingViewHolder) holder).image.setImageURI(mData.get(position).getImageURI());
                ((ReceivingViewHolder) holder).translateBtn.setVisibility(View.INVISIBLE);
            }
        } else if(holder instanceof SendingViewHolder){
            if(mData.get(position).getMessage() == null || mData.get(position).getMessage().length()<1)
                ((SendingViewHolder) holder).message.setText("");
            else ((SendingViewHolder) holder).message.setText(mData.get(position).getMessage());
            ((SendingViewHolder) holder).sendTime.setText(mData.get(position).getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm")));
            if(mData.get(position).getImageURI() != null) ((SendingViewHolder) holder).image.setImageURI(mData.get(position).getImageURI());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
