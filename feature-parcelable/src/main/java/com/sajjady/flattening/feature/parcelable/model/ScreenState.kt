package com.sajjady.flattening.feature.parcelable.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class ScreenState : Parcelable {
    @Parcelize
    data object Loading : ScreenState()

    @Parcelize
    data class Content(val user: PUser) : ScreenState()

    @Parcelize
    data class Error(val message: String) : ScreenState()
}
