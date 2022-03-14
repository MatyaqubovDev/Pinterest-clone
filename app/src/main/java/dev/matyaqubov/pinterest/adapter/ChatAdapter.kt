package dev.matyaqubov.pinterest.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.matyaqubov.pinterest.ui.fragment.ChatMessageFragment
import dev.matyaqubov.pinterest.ui.fragment.ChatUpdateFragment

class ChatAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return ChatUpdateFragment()
        }
        return ChatMessageFragment()
    }
}