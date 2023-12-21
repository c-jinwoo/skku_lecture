package com.swe.buddybud.willow;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.swe.buddybud.R;
import com.swe.buddybud.common.RetrofitClient;
import com.swe.buddybud.user.LoginData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WillowFragment extends Fragment implements TextWatcher, View.OnClickListener, WillowManageInterface {
    public TextView seeAllText;
    public TextView emptyIncomingText;
    public TextView emptyMyWillowText;
    public ImageView totalEmptyImageView;
    public EditText willowSearchEditText;
    public ImageButton searchBtn;
    public ImageButton sentWillowBtn;
    public RecyclerView incomingWillowRcView;
    public RecyclerView myWillowsRcView;
    public IncomingWillow_Adapter incomingWillowAdapter;
    public MyWillows_Adapter myWillowsAdapter;
    public ArrayList<IncomingWillowData> incomingWillows = new ArrayList<>();
    public ArrayList<IncomingWillowData> incomingWillowItems = new ArrayList<>();
    public ArrayList<MyWillowsData> myWillowsItems = new ArrayList<>();

    protected RecyclerView.LayoutManager iclayoutManager;
    protected RecyclerView.LayoutManager mwlayoutManager;
    private WillowApiService willowApiService = RetrofitClient.getService(WillowApiService.class);
    private boolean incomingExpanded = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incomingExpanded = false;
        /* TEST DATA
        IncomingWillowData woosik = new IncomingWillowData("Woosik12","software","male", R.drawable.woosik);
        IncomingWillowData marvelyvly = new IncomingWillowData("marvelyvly","Chemistry","female",  R.drawable.marvelyvly);
        incomingWillows.add(woosik);
        incomingWillows.add(marvelyvly);
        MyWillowsData realisshoman = new MyWillowsData("realisshoman",LocalDateTime.of(2023, 1, 23, 07, 18),"살바도르 달리, 반고흐 같이 Picasso in my body. Man I'm freakin artist", R.drawable.realisshoman);
        MyWillowsData nakedbibi = new MyWillowsData("nakedbibi", LocalDateTime.of(2022, 12, 25, 11, 24),"well, thank you have a nice day", R.drawable.nakedbibi);
        MyWillowsData calmdownman = new MyWillowsData("calmdownman", LocalDateTime.of(2022, 9, 3, 1, 11),"Wow! thank you for your support", R.drawable.calmdownman);
        myWillowsItems.add(realisshoman);
        myWillowsItems.add(nakedbibi);
        myWillowsItems.add(calmdownman);
         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_willow, container,false);

        totalEmptyImageView = view.findViewById(R.id.total_empty_img);
        totalEmptyImageView.setVisibility(View.INVISIBLE);

        emptyIncomingText = view.findViewById(R.id.incoming_empty_textview);
        emptyIncomingText.setVisibility(View.GONE);

        emptyMyWillowText = view.findViewById(R.id.mywillow_empty_textview);
        emptyMyWillowText.setVisibility(View.GONE);

        incomingWillowRcView = view.findViewById(R.id.incoming_willow_view);
        myWillowsRcView = view.findViewById(R.id.my_willows_view);

        seeAllText = view.findViewById(R.id.seeall_text);
        seeAllText.setOnClickListener(this);

        sentWillowBtn = view.findViewById(R.id.sent_willow_btn);
        sentWillowBtn.setOnClickListener(this);

        searchBtn = view.findViewById(R.id.sent_willow_search_btn);
        searchBtn.setOnClickListener(this);

        willowSearchEditText = view.findViewById(R.id.willowsearch_text);
        willowSearchEditText.setVisibility(View.GONE);
        willowSearchEditText.addTextChangedListener(this);

        iclayoutManager = new LinearLayoutManager(getActivity());
        mwlayoutManager = new LinearLayoutManager(getActivity());

        incomingWillowAdapter = new IncomingWillow_Adapter(incomingWillowItems, this);
        myWillowsAdapter = new MyWillows_Adapter(myWillowsItems);

        incomingWillowRcView.setLayoutManager(iclayoutManager);
        myWillowsRcView.setLayoutManager(mwlayoutManager);

        incomingWillowRcView.setAdapter(incomingWillowAdapter);
        myWillowsRcView.setAdapter(myWillowsAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshWillowList();
    }

    @Override
    public void onClick(View view) {
        Log.w("test","click " + view.getId());
        switch(view.getId()){
            case R.id.seeall_text:{
                incomingWillowItems = incomingWillows;
                incomingWillowAdapter.setmData(incomingWillowItems);
                view.setVisibility(View.INVISIBLE);
                incomingWillowRcView.invalidate();
                incomingWillowAdapter.notifyDataSetChanged();
                break;
            }
            case R.id.sent_willow_btn:{
                Context context = view.getContext();
                Intent intent = new Intent(context, SentWillowActivity.class);
                context.startActivity(intent);
                break;
            }

            case R.id.sent_willow_search_btn:{
                if(willowSearchEditText.getVisibility() == View.VISIBLE)
                    willowSearchEditText.setVisibility(View.GONE);
                else
                    willowSearchEditText.setVisibility(View.VISIBLE);
                break;
            }

        }
    }

    @Override
    public void refreshWillowList() {
        getIncomingWillow();
        getMyWillows();
    }

    private void getIncomingWillow(){
        incomingWillows.clear();
        Call<List<WillowApiData>> call = willowApiService.receivedWillow(LoginData.getLoginUserNo());
        call.enqueue(new Callback<List<WillowApiData>>() {
            @Override
            public void onResponse(Call<List<WillowApiData>> call, Response<List<WillowApiData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<WillowApiData> userResponse = response.body();
                    for(WillowApiData res : userResponse){
                        Resources resources = getResources();
                        int resourceId = R.drawable.profile;
                        if(res.getSender_no()!=LoginData.getLoginUserNo()) {
                            int foundProfileImg = resources.getIdentifier(res.getSender_id().toLowerCase(), "drawable", getActivity().getPackageName());
                            if (foundProfileImg > 0) resourceId = foundProfileImg;
                        }
                        incomingWillows.add(new IncomingWillowData(res.getSender_id(),res.getSender_no(),res.getSender_school(),res.getSender_gender(), resourceId));
                    }
                    incomingWillowItems.clear();
                    if(!incomingExpanded && incomingWillows.size()>1) {
                        incomingWillowItems.add(incomingWillows.get(0));
                        seeAllText.setVisibility(View.VISIBLE);
                    } else {
                        for(IncomingWillowData willow:incomingWillows) {
                            incomingWillowItems.add(willow);
                        }
                    }
                    if(incomingWillowItems.size()==0){
                        emptyIncomingText.setVisibility(View.VISIBLE);
                        if(myWillowsItems.size()==0) totalEmptyImageView.setVisibility(View.VISIBLE);
                    } else {
                        emptyIncomingText.setVisibility(View.GONE);
                        totalEmptyImageView.setVisibility(View.INVISIBLE);
                    }

                    incomingWillowAdapter.notifyDataSetChanged();

                    return;
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(),"receivedWillow Network Error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<List<WillowApiData>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(),"receivedWillow Network Error", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void getMyWillows(){
        myWillowsItems.clear();
        //TEST : getMyWillows
        Call<List<WillowApiData>> call = willowApiService.getMyWillows(LoginData.getLoginUserNo());
        call.enqueue(new Callback<List<WillowApiData>>() {
            @Override
            public void onResponse(Call<List<WillowApiData>> call, Response<List<WillowApiData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<WillowApiData> userResponse = response.body();
                    for(WillowApiData res : userResponse){
                        Resources resources = getResources();
                        int resourceId = R.drawable.profile;
                        if(res.getSender_no()!=LoginData.getLoginUserNo()) {
                            int foundProfileImg = resources.getIdentifier(res.getUid().toLowerCase(), "drawable", getActivity().getPackageName());
                            if (foundProfileImg > 0) resourceId = foundProfileImg;
                        }
                        try {
                            myWillowsItems.add(new MyWillowsData(res.getUid(), res.getUser_no(), LocalDateTime.parse(res.getCreated_at(), DateTimeFormatter.ISO_LOCAL_DATE_TIME), res.getLatest_message(), resourceId));
                        } catch (DateTimeParseException e){
                            myWillowsItems.add(new MyWillowsData(res.getUid(), res.getUser_no(), null, "", resourceId));
                        }
                    }
                    if(myWillowsItems.size()==0){
                        emptyMyWillowText.setVisibility(View.VISIBLE);
                        if(incomingWillowItems.size()==0) totalEmptyImageView.setVisibility(View.VISIBLE);
                    } else {
                        emptyMyWillowText.setVisibility(View.GONE);
                        totalEmptyImageView.setVisibility(View.INVISIBLE);
                    }
                    myWillowsAdapter.notifyDataSetChanged();
                    return;
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(),"getMyWillows Network Error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<List<WillowApiData>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(),"getMyWillows Network Error", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        myWillowsAdapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}