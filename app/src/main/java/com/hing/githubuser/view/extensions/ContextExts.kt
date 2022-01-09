package com.hing.githubuser.view.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(@StringRes messagesRes: Int) {
    Toast.makeText(this, getString(messagesRes), Toast.LENGTH_SHORT).show()
}
