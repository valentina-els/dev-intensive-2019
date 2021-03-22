package ru.skillbranch.devintensive.extensions
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager

    fun Activity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) { val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0) }
    }

    fun Activity.isKeyboardOpen():Boolean{
        val softKeyboardHeight = 100
        val rect = Rect()

        val rootView : View = window.decorView.rootView
        rootView.getWindowVisibleDisplayFrame(rect)

        val dm = rootView.resources.displayMetrics
        val heightDiff = rootView.bottom - rect.bottom
        return heightDiff > softKeyboardHeight * dm.density
    }

    fun Activity.isKeyboardClosed():Boolean{

        val softKeyboardHeight = 100
        val rect = Rect()

        val rootView : View = window.decorView.rootView

        rootView.getWindowVisibleDisplayFrame(rect)

        val dm = rootView.resources.displayMetrics
        val heightDiff = rootView.bottom - rect.bottom
        return heightDiff < softKeyboardHeight * dm.density
    }
