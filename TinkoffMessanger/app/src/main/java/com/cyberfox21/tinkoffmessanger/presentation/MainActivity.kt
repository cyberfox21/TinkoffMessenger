package com.cyberfox21.tinkoffmessanger.presentation

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.cyberfox21.tinkoffmessanger.R
import com.cyberfox21.tinkoffmessanger.databinding.ActivityMainBinding
import com.cyberfox21.tinkoffmessanger.presentation.enums.ProfileMode
import com.cyberfox21.tinkoffmessanger.presentation.fragments.channels.ChannelsFragment
import com.cyberfox21.tinkoffmessanger.presentation.fragments.chat.ChatFragment
import com.cyberfox21.tinkoffmessanger.presentation.fragments.people.PeopleFragment
import com.cyberfox21.tinkoffmessanger.presentation.fragments.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavigationListeners()
    }

    private fun setBottomNavigationListeners() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.channels -> {
                    ChannelsFragment.newInstance()
                }
                R.id.people -> {
                    PeopleFragment.newInstance()
                }
                R.id.profile -> {
                    ProfileFragment.newInstance(ProfileMode.YOUR)
                }
                else -> throw RuntimeException("Unknown menu item ${it.itemId}")
            }
            changeFragment(fragment)
            true
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(
                fm: FragmentManager,
                f: Fragment,
                v: View,
                savedInstanceState: Bundle?
            ) {
                TransitionManager.beginDelayedTransition(
                    binding.root,
                    Slide(Gravity.BOTTOM).excludeTarget(R.id.main_fragment_container, true)
                )
                binding.bottomNavigationView.visibility = when (f) {
                    is ChatFragment -> View.GONE
                    is ProfileFragment -> {
                        if (f.screenMode == ProfileMode.STRANGER) View.GONE
                        else View.VISIBLE
                    }
                    else -> View.VISIBLE
                }
            }
        }, false)
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment)
            .commit()
    }

}
