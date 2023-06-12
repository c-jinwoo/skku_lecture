package edu.skku.cs.pa2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

class MazeListAdapter(val items: List<MapList>, val context: Context) : BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(p0: Int): Any {
        return items.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.maze_entry, null)
        val tv_mazeName = view.findViewById<TextView>(R.id.textViewMazeName)
        val tv_mazeSize = view.findViewById<TextView>(R.id.textViewMazeSize)
        val btn_start = view.findViewById<Button>(R.id.buttonMazeStart)

        tv_mazeName.text = items[p0].name.toString()
        tv_mazeSize.text = items[p0].size.toString()

        btn_start.setOnClickListener {
            var intent = Intent(context, MazeActivity::class.java).apply{
                putExtra(MazeSelectionActivity.MAP_NAME, tv_mazeName.text.toString())
            }
            context.startActivity(intent)
        }

        return view
    }

}