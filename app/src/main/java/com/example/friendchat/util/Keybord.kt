package com.example.friendchat.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun setupUI(view: View, activity: AppCompatActivity) {
    if (view !is androidx.appcompat.widget.AppCompatEditText) {
        view.setOnTouchListener { _, _ ->
            hideSoftKeyboard(activity)
            false
        }
    }

    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val innerView = view.getChildAt(i)
            setupUI(innerView, activity)
        }
    }
}

private fun hideSoftKeyboard(activity: AppCompatActivity) {
    val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusView = activity.currentFocus
    if (currentFocusView != null) {
        inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
    }
}