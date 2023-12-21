package com.swe.buddybud.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.fragment.app.DialogFragment;

import com.google.gson.JsonObject;
import com.swe.buddybud.R;
import com.swe.buddybud.common.RetrofitClient;
import com.swe.buddybud.willow.WillowApiData;
import com.swe.buddybud.willow.WillowApiService;
import com.swe.buddybud.user.LoginData;

import java.util.List;

public class CustomDialog extends DialogFragment {
    public interface CustomDialogCallback {
        void onWillowRequestSent(boolean success, boolean sendWillowSuccess);
    }

    private CustomDialogCallback callback;

    public void setCustomDialogCallback(CustomDialogCallback callback) {
        this.callback = callback;
    }
    private static final String ARG_NICKNAME = "nickname";
    private static final String ARG_PROFILE_IMAGE_ID = "profileImageId";
    private static final String ARG_ID = "user_id";

    public static CustomDialog newInstance(String nickname, int profileImageId, int user_id) {
        CustomDialog fragment = new CustomDialog();
        Bundle args = new Bundle();
        args.putString(ARG_NICKNAME, nickname);
        args.putInt(ARG_PROFILE_IMAGE_ID, profileImageId);
        args.putInt(ARG_ID, user_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (getArguments() != null) {
            String nickname = getArguments().getString(ARG_NICKNAME);
            int profileImageId = getArguments().getInt(ARG_PROFILE_IMAGE_ID);
            int user_id = getArguments().getInt(ARG_ID);

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_willow_request, null);

            CircleImageView profileImageView = dialogView.findViewById(R.id.dialog_profile_image);
            TextView nicknameTextView = dialogView.findViewById(R.id.dialog_nickname);
            TextView cancel = dialogView.findViewById(R.id.dialog_cancel);
            TextView send = dialogView.findViewById(R.id.dialog_send);

            profileImageView.setImageResource(profileImageId);
            nicknameTextView.setText(nickname);

            // 취소를 누를 경우
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            // 전송을 누를 경우
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CustomDialog", nickname + "에게 Send");
                    // 요청 데이터 준비
                    JsonObject requestBodyJson = new JsonObject();
                    requestBodyJson.addProperty("sender_no", LoginData.getLoginUserNo());
                    requestBodyJson.addProperty("receiver_no", user_id);

                    // Retrofit을 사용하여 서비스 인스턴스 가져오기
                    WillowApiService service = RetrofitClient.getService(WillowApiService.class);

                    // JSON을 RequestBody로 변환
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson.toString());

                    // API 호출
                    Call<WillowApiData> call = service.sendWillow(requestBody);
                    call.enqueue(new Callback<WillowApiData>() {
                        @Override
                        public void onResponse(Call<WillowApiData> call, Response<WillowApiData> response) {
                            WillowApiData userResponse = response.body();
                            if(callback != null) {
                                callback.onWillowRequestSent(response.isSuccessful(), userResponse.getSendWillowRequestResult());
                            }
                        }

                        @Override
                        public void onFailure(Call<WillowApiData> call, Throwable t) {
                            // 네트워크 오류 처리
                            Log.e("CustomDialog", "Network error: " + t.getMessage());
                        }
                    });

                    dismiss(); //
                }
            });

            builder.setView(dialogView);
        }

        Dialog dialog = builder.create();

        int dpValue = 300; // 300dp
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int pixelValue = (int) (dpValue * displayMetrics.density); // 픽셀 값으로 변환

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Window window = dialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(window.getAttributes());
                    layoutParams.width = pixelValue;
                    window.setAttributes(layoutParams);
                }
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }
}
