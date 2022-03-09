package dev.matyaqubov.pinterest.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.matyaqubov.pinterest.R
import dev.matyaqubov.pinterest.ui.fragment.HomeFragment
import dev.matyaqubov.pinterest.ui.fragment.MessageFragment
import dev.matyaqubov.pinterest.ui.fragment.ProfileFragment
import dev.matyaqubov.pinterest.ui.fragment.SearchFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val homeFragment= HomeFragment()
        val profileFragment=ProfileFragment()
        val messageFragement= MessageFragment()
        val searchFragment= SearchFragment()
        setCurrentFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homeFragment->setCurrentFragment(homeFragment)
                R.id.searchFragment->setCurrentFragment(searchFragment)
                R.id.messageFragment->setCurrentFragment(messageFragement)
                R.id.profileFragment->setCurrentFragment(profileFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    }
}