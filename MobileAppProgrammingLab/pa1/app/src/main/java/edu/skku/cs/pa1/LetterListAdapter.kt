/*
 *  Lecture     : Mobile App Programming Lab
 *  Task        : Programming Assignment 1
 *  File        : LetterListAdapter.kt
 *  Date        : 2023-04-10
 *  SID         : 2020315798
 *  Name        : Choi Jin Woo
 */

package edu.skku.cs.pa1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class LetterListAdapter(val data:ArrayList<Char>, val type:Int, val context: Context) : BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): Any {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layoutInflater: LayoutInflater =
            context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
            ) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.letterlist_layout, null)

        // Set text
        val textView = view.findViewById<TextView>(R.id.textViewLetter)
        textView.text = data[p0].toString().uppercase()

        // Set color
        if(type == 0) {             // Out
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.background_out))
            textView.setTextColor(ContextCompat.getColor(context, R.color.text_out))
        }
        else if(type == 1) {        // Ball
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.background_ball))
            textView.setTextColor(ContextCompat.getColor(context, R.color.text_ball))
        }
        else{                       // Strike
            textView.setBackgroundColor(ContextCompat.getColor(context, R.color.background_strike))
            textView.setTextColor(ContextCompat.getColor(context, R.color.text_strike))
        }

        return view
    }

}