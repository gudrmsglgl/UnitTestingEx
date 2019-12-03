package com.fastival.unittestex.extension

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun AppCompatActivity.hideKeyboard(){
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    if (imm != null && currentFocus != null) {
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG) {

    val snack = Snackbar.make(this, message, length)
    snack.show()

}

fun String.removeWhiteSpace(): String {
    var cStr = this.replace("\n","")
    cStr = cStr.replace(" ","")
    return cStr
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>){
    observe(lifecycleOwner, object : Observer<T>{
        override fun onChanged(t: T) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}