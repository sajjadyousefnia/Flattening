package com.sajjady.flattening.feature.parcelable.model

import android.os.Parcel
import android.os.Parcelable

data class LegacyUser(
    val id: Long,
    val name: String,
    val email: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(email)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<LegacyUser> {
        override fun createFromParcel(parcel: Parcel): LegacyUser = LegacyUser(parcel)
        override fun newArray(size: Int): Array<LegacyUser?> = arrayOfNulls(size)
    }
}
