package com.swe.buddybud.home;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public RecycleViewDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // 첫 번째 아이템에는 상단 간격을 추가하고 싶으면 아래 주석을 해제
        //if (parent.getChildLayoutPosition(view) == 0) {
        //    outRect.top = space;
        //}
    }
}