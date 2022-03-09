package dev.matyaqubov.pinterest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.matyaqubov.pinterest.R

class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initViews(inflater.inflate(R.layout.fragment_profile, container, false))
    }

    private fun initViews(view: View): View {
        return view
    }


}