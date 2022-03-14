package dev.matyaqubov.pinterest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import dev.matyaqubov.pinterest.R
import dev.matyaqubov.pinterest.adapter.ChatAdapter

class MessageFragment : Fragment() {
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
    : View {

        return initViews(inflater.inflate(R.layout.fragment_message, container, false))
    }

    private fun initViews(view: View): View {
        var icon=view.findViewById<ImageView>(R.id.icon)
        viewPager2=view.findViewById(R.id.viewPager)
        tabLayout=view.findViewById(R.id.tab_layout)
        chatAdapter= ChatAdapter(childFragmentManager,lifecycle)
        viewPager2.adapter=chatAdapter

        tabLayout.addTab(tabLayout.newTab().setText("Updates"))
        tabLayout.addTab(tabLayout.newTab().setText("Message"))

        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem=tab!!.position
                if (tab.position == 1){
                    icon.visibility = View.GONE
                }else{
                    icon.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })


        return view
    }

}