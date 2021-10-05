package com.cyberfox21.homework1

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(val name: String, val photo: Bitmap) : Parcelable