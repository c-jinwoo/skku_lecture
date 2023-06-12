/*
 *  Lecture     : Mobile App Programming Lab
 *  Task        : Programming Assignment 1
 *  File        : WordListAdapter.kt
 *  Date        : 2023-04-10
 *  SID         : 2020315798
 *  Name        : Choi Jin Woo
 */

package edu.skku.cs.pa1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class WordListAdapter(val data:ArrayList<WordList>, val context: Context) : BaseAdapter() {
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
        val view = layoutInflater.inflate(R.layout.wordlist_layout, null)

        val word = data[p0].word.uppercase()
        val resultIntArray = data[p0].resultIntArray
        val textViewList = listOf(R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5)

        for ((idx, text) in textViewList.withIndex()) {
            val textView = view.findViewById<TextView>(text)
            textView.text = word[idx].toString()

            if(resultIntArray[idx] == 2){                   // Green
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.background_strike))
                textView.setTextColor(ContextCompat.getColor(context, R.color.text_strike))
            }
            else if(resultIntArray[idx] == 1){              // Yellow
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.background_ball))
                textView.setTextColor(ContextCompat.getColor(context, R.color.text_ball))
            }
            else{                                   // Gray
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.background_out))
                textView.setTextColor(ContextCompat.getColor(context, R.color.text_out))
            }
        }

        return view
    }
}