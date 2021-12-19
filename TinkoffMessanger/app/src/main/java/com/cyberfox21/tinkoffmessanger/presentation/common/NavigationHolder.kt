package com.cyberfox21.tinkoffmessanger.presentation.common

import androidx.fragment.app.Fragment

interface NavigationHolder {
    fun showNavigation()
    fun hideNavigation()
    fun startFragment(fragment: Fragment, tag: String)
}
