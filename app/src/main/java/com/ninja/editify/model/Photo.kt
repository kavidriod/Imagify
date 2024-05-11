package com.ninja.editify.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val name: String,
    val imageUrl: String,
    val imageTwoUrl: String,
    val description: String
) : Parcelable
