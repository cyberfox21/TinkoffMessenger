package com.cyberfox21.tinkoffmessanger.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Channel(val name: String, val listOfTopics: List<Topic>) : Parcelable
