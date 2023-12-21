package com.swe.buddybud.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.swe.buddybud.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoardFilteredFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardFilteredFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BoardFilteredFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArticleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoardFilteredFragment newInstance(String param1, String param2) {
        BoardFilteredFragment fragment = new BoardFilteredFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_board_filtered, container, false);

        ImageButton scrap_1_Button = rootView.findViewById(R.id.scrap_1_Button);
        final TextView[] scrap_1_Count = {rootView.findViewById(R.id.scrap_1_Count)};

        String scrap_1_Count_Text = scrap_1_Count[0].getText().toString();
        final int[] scrap_1_Count_int = {Integer.parseInt(scrap_1_Count_Text)};

        final boolean[] isScrapped_1 = { false };
        scrap_1_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isScrapped_1[0]) {
                    scrap_1_Button.setImageResource(R.drawable.scrapping);
                    scrap_1_Count_int[0]--;
                    scrap_1_Count[0].setText(String.valueOf(scrap_1_Count_int[0]));
                    isScrapped_1[0] = false;
                } else {
                    scrap_1_Button.setImageResource(R.drawable.scrapped);
                    scrap_1_Count_int[0]++;
                    scrap_1_Count[0].setText(String.valueOf(scrap_1_Count_int[0]));
                    isScrapped_1[0] = true;
                }
            }
        });

        ImageButton like_1_Button = rootView.findViewById(R.id.like_1_Button);
        final TextView[] like_1_Count = {rootView.findViewById(R.id.like_1_Count)};

        String like_1_Count_Text = like_1_Count[0].getText().toString();
        final int[] like_1_Count_int = {Integer.parseInt(like_1_Count_Text)};

        final boolean[] isLiked_1 = { false };
        like_1_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked_1[0]) {
                    like_1_Button.setImageResource(R.drawable.liking);
                    like_1_Count_int[0]--;
                    like_1_Count[0].setText(String.valueOf(like_1_Count_int[0]));
                    isLiked_1[0] = false;
                } else {
                    like_1_Button.setImageResource(R.drawable.liked);
                    like_1_Count_int[0]++;
                    like_1_Count[0].setText(String.valueOf(like_1_Count_int[0]));
                    isLiked_1[0] = true;
                }
            }
        });

        ImageButton translate_1_Button = rootView.findViewById(R.id.translate_1_Button);
        TextView post_1_Title = rootView.findViewById(R.id.post_1_Title);

        final boolean[] isTranslated_1 = { false };
        translate_1_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTranslated_1[0]) {
                    post_1_Title.setText("[채용/모집] [연장] 성균관대학교 SW융합대학...");
                    isTranslated_1[0] = false;
                } else {
                    post_1_Title.setText("[Recruit] [Extended] SKKU College of SW...");
                    isTranslated_1[0] = true;
                }
            }
        });

        ImageButton scrap_2_Button = rootView.findViewById(R.id.scrap_2_Button);
        final TextView[] scrap_2_Count = {rootView.findViewById(R.id.scrap_2_Count)};

        String scrap_2_Count_Text = scrap_2_Count[0].getText().toString();
        final int[] scrap_2_Count_int = {Integer.parseInt(scrap_2_Count_Text)};

        final boolean[] isScrapped_2 = { false };
        scrap_2_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isScrapped_2[0]) {
                    scrap_2_Button.setImageResource(R.drawable.scrapping);
                    scrap_2_Count_int[0]--;
                    scrap_2_Count[0].setText(String.valueOf(scrap_2_Count_int[0]));
                    isScrapped_2[0] = false;
                } else {
                    scrap_2_Button.setImageResource(R.drawable.scrapped);
                    scrap_2_Count_int[0]++;
                    scrap_2_Count[0].setText(String.valueOf(scrap_2_Count_int[0]));
                    isScrapped_2[0] = true;
                }
            }
        });

        ImageButton like_2_Button = rootView.findViewById(R.id.like_2_Button);
        final TextView[] like_2_Count = {rootView.findViewById(R.id.like_2_Count)};

        String like_2_Count_Text = like_2_Count[0].getText().toString();
        final int[] like_2_Count_int = {Integer.parseInt(like_2_Count_Text)};

        final boolean[] isLiked_2 = { true };
        like_2_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked_2[0]) {
                    like_2_Button.setImageResource(R.drawable.liking);
                    like_2_Count_int[0]--;
                    like_2_Count[0].setText(String.valueOf(like_2_Count_int[0]));
                    isLiked_2[0] = false;
                } else {
                    like_2_Button.setImageResource(R.drawable.liked);
                    like_2_Count_int[0]++;
                    like_2_Count[0].setText(String.valueOf(like_2_Count_int[0]));
                    isLiked_2[0] = true;
                }
            }
        });

        ImageButton translate_2_Button = rootView.findViewById(R.id.translate_2_Button);
        TextView post_2_Title = rootView.findViewById(R.id.post_2_Title);

        final boolean[] isTranslated_2 = { false };
        translate_2_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTranslated_2[0]) {
                    post_2_Title.setText("\uD83D\uDE4B일대일 그룹 대학생 영어회화");
                    isTranslated_2[0] = false;
                } else {
                    post_2_Title.setText("\uD83D\uDE4BOne-on-One Group College Student Eng...");
                    isTranslated_2[0] = true;
                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView frame_1 = view.findViewById(R.id.frame_1_Shadow);
        frame_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new BoardDetailFragment();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.board_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ImageView filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new BoardDetailFragment();

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.board_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}