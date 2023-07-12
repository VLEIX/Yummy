package com.frantun.yummy.other

import android.content.Intent
import android.view.View
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

fun Fragment.navigateTo(navDirections: NavDirections) {
    findNavController().navigate(navDirections)
}

fun Fragment.navigateTo(navDirections: NavDirections, navigatorExtras: Navigator.Extras) {
    findNavController().navigate(navDirections, navigatorExtras)
}

fun Fragment.navigateTo(intent: Intent) {
    requireActivity().startActivity(intent)
}

fun Fragment.popBackStack() {
    findNavController().popBackStack()
}

fun Fragment.navigateUp() {
    findNavController().navigateUp()
}

fun Fragment.finish() {
    requireActivity().finish()
}

fun Fragment.finishAfterTransition() {
    requireActivity().finishAfterTransition()
}

fun Fragment.showKeyboard(view: View) {
    requireActivity().showKeyboard(view)
}

fun Fragment.hideKeyboard() {
    requireActivity().hideKeyboard()
}

fun Fragment.showSnackbar(text: String) {
    view?.let { Snackbar.make(it, text, Snackbar.LENGTH_LONG).show() }
}

fun Fragment.showSnackbar(text: String, @ColorInt colorInt: Int) {
    view?.let {
        with(Snackbar.make(it, text, Snackbar.LENGTH_LONG)) {
            setBackgroundTint(colorInt)
            show()
        }
    }
}
