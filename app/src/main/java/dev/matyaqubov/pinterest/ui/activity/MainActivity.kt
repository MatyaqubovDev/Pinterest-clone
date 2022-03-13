package dev.matyaqubov.pinterest.ui.activity

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.matyaqubov.pinterest.R
import dev.matyaqubov.pinterest.service.model.SearchResultsItem
import dev.matyaqubov.pinterest.ui.fragment.*

class MainActivity : AppCompatActivity(), SearchFragment.SendData {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val messageFragement = MessageFragment()
        val searchFragment = SearchFragment()

        setCurrentFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> setCurrentFragment(homeFragment)
                R.id.searchFragment -> setCurrentFragment(searchFragment)
                R.id.messageFragment -> setCurrentFragment(messageFragement)
                R.id.profileFragment -> setCurrentFragment(profileFragment)
            }
            true
        }

    }

    fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            addToBackStack("back")
            commit()
        }

    }



    override fun sendPhoto(photo:SearchResultsItem, word: String, page:Int) {
        val detailsFragment = DetailsFragment()
        detailsFragment.receivedData(photo, word,page)
        setCurrentFragment(detailsFragment)
        }



}