package com.hing.githubuser.view.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hing.githubuser.R

fun ImageView.loadImage(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.ic_terrain)
        .error(R.drawable.ic_terrain)
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}
