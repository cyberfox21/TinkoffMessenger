package com.cyberfox21.tinkoffmessanger.presentation.common

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

    private lateinit var fragments: Map<MainFragments, Fragment>
    private lateinit var activeFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFragments()
        setBottomNavigationListeners()
    }

    private fun initFragments() {
        fragments = mapOf(
            MainFragments.CHANNELS to ChannelsFragment.newInstance(),
            MainFragments.PEOPLE to PeopleFragment.newInstance(),
            MainFragments.PROFILE to ProfileFragment.newInstanceYour()
        )
        fragments.entries.reversed().forEach { pair ->
            supportFragmentManager.beginTransaction()
                .addToBackStack(pair.key.tag)
                .commit()
        }
        activeFragment = fragments.getValue(MainFragments.CHANNELS)
    }

    private fun setBottomNavigationListeners() {
        binding.bottomNavigationView.setOnItemSelectedListener { onNavigationItemSelected(it) }
    }

    private fun onNavigationItemSelected(item: MenuItem): Boolean {
        val key = when (item.itemId) {
            R.id.channels -> MainFragments.CHANNELS
            R.id.people -> MainFragments.PEOPLE
            R.id.profile -> MainFragments.PROFILE
            else -> throw RuntimeException("Unknown menu item $item")
        }
        val fragment = fragments.getValue(key)
        navigateFragment(fragment, key.tag)
        activeFragment = fragment
        return true
    }

    override fun showNavigation() {
        binding.bottomNavigationView.isVisible = true
    }

    override fun hideNavigation() {
        binding.bottomNavigationView.isVisible = false
    }


    override fun startFragment(fragment: Fragment, oldTag: String, newFragmentTag: String) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(oldTag)
            .add(R.id.main_fragment_container, fragment, newFragmentTag)
            .commit()
        activeFragment = fragment
        hideNavigation()
    }

    private fun navigateFragment(fragment: Fragment, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .add(R.id.main_fragment_container, fragment, tag)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .hide(activeFragment)
                .show(fragment)
                .commit()
        }
    }

    override fun onBackPressed() {
        if (fragments.containsValue(activeFragment)) finish()
        else if (supportFragmentManager.backStackEntryCount >= 1) {
            val index = supportFragmentManager.backStackEntryCount - 1
            val backStackEntry = supportFragmentManager.getBackStackEntryAt(index)
            val name = backStackEntry.name ?: EMPTY_FRAGMENT_TAG
            val key = MainFragments.values().filter { it.tag == name }[0]
            super.onBackPressed()
            if (fragments.containsKey(key)) {
                showNavigation()
                fragments[key]?.let { activeFragment = it }
            }
        }
    }

    companion object {
        const val EMPTY_FRAGMENT_TAG = ""
    }
}
