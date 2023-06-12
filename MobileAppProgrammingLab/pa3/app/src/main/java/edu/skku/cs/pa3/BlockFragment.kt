package edu.skku.cs.pa3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

class BlockFragment : Fragment() {
    lateinit var blockUrlListView: ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_block, container, false)
        val blockAdapter = BlockAdapter(blockUrlList, requireContext())

        blockUrlListView = view.findViewById(R.id.blockUrlListView)
        blockUrlListView.adapter = blockAdapter

        return view
    }
}