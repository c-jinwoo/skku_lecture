package edu.skku.cs.pa2

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class MazeAdapter(val context: Context, val typeArr: Array<Int>, val pos: Pos) : BaseAdapter() {
    override fun getCount(): Int {
        return cell_cnt * cell_cnt
    }

    override fun getItem(p0: Int): Any {
        return 0
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.maze_cell, p2, false)

        // 너비 및 높이 설정
        val dp = size / cell_cnt
        val gap = (dpToPx(size) - dpToPx(dp) * cell_cnt) / cell_cnt             // 오차 계산
        val lp_wrapper = view.layoutParams
        lp_wrapper.width = dpToPx(dp) + gap
        lp_wrapper.height = dpToPx(dp) + gap
        view.layoutParams = lp_wrapper

        // 셀 타입 설정
        val img_bg = view.findViewById<ImageView>(R.id.imageViewbg)
        val lp_cell = img_bg.layoutParams as ViewGroup.MarginLayoutParams

        if(typeArr[p0] and mask_up != 0){
            lp_cell.topMargin = margin
        }
        if(typeArr[p0] and mask_left != 0){
            lp_cell.leftMargin = margin
        }
        if(typeArr[p0] and mask_down != 0){
            lp_cell.bottomMargin = margin
        }
        if(typeArr[p0] and mask_right != 0){
            lp_cell.rightMargin = margin
        }
        img_bg.layoutParams = lp_cell

        // 이미지 설정
        val img_src = view.findViewById<ImageView>(R.id.imageViewSrc)

        if(p0 == pos.user){                                 // 유저 좌표에 user 이미지
            img_src.setImageResource(R.drawable.user)
            img_src.rotation = pos.user_rot?.times(90f) ?: 0f
        }
        else if(p0 == cell_cnt * cell_cnt - 1){             // 마지막 좌표에 goal 이미지
            img_src.setImageResource(R.drawable.goal)
        }
        else if(p0 == pos.hint){                            // 힌트 좌표에 hint 이미지
            img_src.setImageResource(R.drawable.hint)
        }
        else{                                               // 나머지는 숨김
            img_src.visibility = View.GONE
        }

        return view
    }

    private fun dpToPx(dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}





