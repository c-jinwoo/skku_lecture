package edu.skku.cs.pa3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView

class BlockAdapter(val data: ArrayList<DataBlockUrl>, val context: Context) : BaseAdapter() {
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
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_blockurl, null)

        val textViewName = view.findViewById<TextView>(R.id.blockUrlTextView)
        textViewName.text = data[p0].url

        val engineList = data[p0].engineList
        val layoutEngineList = view.findViewById<LinearLayout>(R.id.layoutEngineList)
        layoutEngineList.removeAllViews()

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        for (engine in engineList) {
            val textViewEngine = TextView(context)
            textViewEngine.text = engine
            textViewEngine.layoutParams = layoutParams
            layoutEngineList.addView(textViewEngine)
        }

        return view
    }

    fun addItem(blockUrl: DataBlockUrl) {
        data.add(blockUrl)
        notifyDataSetChanged()
    }

    fun containsUrl(url: String): Boolean {
        return data.any { it.url == url }
    }
}