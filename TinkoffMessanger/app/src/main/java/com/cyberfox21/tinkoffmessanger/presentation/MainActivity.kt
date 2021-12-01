package com.cyberfox21.tinkoffmessanger.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.cyberfox21.tinkoffmessanger.App
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.ActivityMainBinding
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.ChannelsFragment
import com.cyberfox21.tinkoffmessanger.presentation.fragments.people.PeopleFragment
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.ProfileFragment

class MainActivity : AppCompatActivity(), NavigationHolder {

    val component by lazy {
        (application as App).component
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavigationListeners()
    }

    private fun setBottomNavigationListeners() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            onNavigationItemSelected(it)
            true
        }
    }

    private fun onNavigationItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.channels -> {
                ChannelsFragment.newInstance()
                navigateFragment(
                    ChannelsFragment.newInstance(),
                    ChannelsFragment.CHANNELS_FRAGMENT_NAME
                )
            }
            R.id.people -> {
                navigateFragment(
                    PeopleFragment.newInstance(),
                    PeopleFragment.PEOPLE_FRAGMENT_NAME
                )
            }
            R.id.profile -> {
                navigateFragment(
                    ProfileFragment.newInstanceYour(),
                    ProfileFragment.PROFILE_FRAGMENT_NAME
                )
            }
            else -> throw RuntimeException("Unknown menu item $item")
        }
    }

    override fun showNavigation() {
        binding.bottomNavigationView.isVisible = true
    }

    override fun hideNavigation() {
        binding.bottomNavigationView.isVisible = false
    }

    override fun startFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment, tag)
            .addToBackStack(tag)
            .commit()
        hideNavigation()
    }

    private fun navigateFragment(fragment: Fragment, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            if (supportFragmentManager.backStackEntryCount > 0)
                supportFragmentManager.popBackStack()

            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, fragment, tag)

                .commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount >= 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }

}
