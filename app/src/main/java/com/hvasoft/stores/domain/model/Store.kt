package com.hvasoft.stores.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Store(
    val id: Long = 0,
    var name: String = "",
    var phone: String = "",
    var website: String = "",
    var photoUrl: String = "",
    var isFavorite: Boolean = false
) : Parcelable