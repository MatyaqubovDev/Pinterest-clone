package dev.matyaqubov.pinterest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import dev.matyaqubov.pinterest.R

class SearchFragment : Fragment() {
    private lateinit var et_search: EditText
    private lateinit var tv_cancel: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return initViews(inflater.inflate(R.layout.fragment_search, container, false))
    }

    private fun initViews(view: View): View {
        et_search = view.findViewById(R.id.et_search)
        tv_cancel = view.findViewById(R.id.tv_cancel)

        et_search.addTextChangedListener {
            tv_cancel.visibility = View.VISIBLE
            et_search.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_camera_alt_24, 0);
            val text = it.toString()

        }


        return view
    }


}