package com.udacity.asteroidradar

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

fun ImageView.loadImage(url: String) {
    val circularProgressDrawable = CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 60f
        setColorSchemeColors(
            ContextCompat.getColor(context, R.color.colorAccent)
        )
        start()
    }

    Picasso
        .Builder(context)
        //.indicatorsEnabled(true)
        .build()
        .load(url)
        .fit()
        .centerCrop()
        .placeholder(circularProgressDrawable)
        .error(R.drawable.ic_broken_image)
        .into(this)
}

fun String.toDate(
    dateFormat: String = "yyyy-MM-dd HH:mm:ss",
    timeZone: TimeZone = TimeZone.getDefault()
): Date? {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}