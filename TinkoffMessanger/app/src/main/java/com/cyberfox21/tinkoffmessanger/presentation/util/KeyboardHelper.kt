package com.cyberfox21.tinkoffmessanger.presentation.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardHelper {

    const val EMPTY_FLAGS = 0

    fun hideKeyboard(activity: Activity?) {
        activity?.currentFocus?.let { view ->
            val imm =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, EMPTY_FLAGS)
        }
    }

    fun showKeyboard(view: View, activity: Activity?) {
        activity?.currentFocus?.let {
            val imm =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        }
    }
}