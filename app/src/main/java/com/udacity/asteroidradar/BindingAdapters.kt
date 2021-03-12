package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.main.AsteroidListAdapter
import com.udacity.asteroidradar.repository.AsteroidRadarApiStatus

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("statusIconContentDescription")
fun bindAsteroidStatusImageContentDescription(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.contentDescription =
            imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.contentDescription =
            imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("pictureOfDayImage")
fun bindPictureOfDayImage(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    if (pictureOfDay != null) {
        imageView.loadImage(pictureOfDay.url)
    }
}

@BindingAdapter("pictureOfDayImageContentDescription")
fun bindPictureOfDayImageContentDescription(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    if (pictureOfDay != null) {
        imageView.contentDescription = imageView.context.getString(
            R.string.nasa_picture_of_day_content_description_format,
            pictureOfDay.title
        )
    } else {
        imageView.contentDescription =
            imageView.context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("listData")
fun RecyclerView.bindRecyclerView(data: List<Asteroid>?) {
    val adapter = adapter as AsteroidListAdapter
    adapter.submitList(data)
}

@BindingAdapter("asteroidApiStatus")
fun ProgressBar.bindStatus(status: AsteroidRadarApiStatus?) {
    when (status) {
        AsteroidRadarApiStatus.LOADING -> {
            visibility = View.VISIBLE
        }
        AsteroidRadarApiStatus.ERROR -> {
            visibility = View.GONE
        }
        AsteroidRadarApiStatus.DONE -> {
            visibility = View.GONE
        }
    }
}