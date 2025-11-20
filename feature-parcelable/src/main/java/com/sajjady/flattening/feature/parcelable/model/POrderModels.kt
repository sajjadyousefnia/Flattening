package com.sajjady.flattening.feature.parcelable.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.TypeParceler
import kotlinx.parcelize.RawValue
import java.util.Date

@Parcelize
data class PAddress(
    val city: String,
    val street: String
) : Parcelable

@Parcelize
data class POrderItem(
    val id: Long,
    val title: String,
    val quantity: Int
) : Parcelable

@Parcelize
data class POrder(
    val id: Long,
    val user: PUser,
    val items: List<POrderItem>,
    val meta: Map<String, String> = emptyMap()
) : Parcelable

@Parcelize
data class AnalyticsEvent(
    val name: String,
    @IgnoredOnParcel
    val createdAt: Long = System.currentTimeMillis(),
    val attributes: @RawValue Map<String, Any?> = emptyMap()
) : Parcelable

object DateParceler : Parceler<Date> {
    override fun create(parcel: android.os.Parcel): Date =
        Date(parcel.readLong())

    override fun Date.write(parcel: android.os.Parcel, flags: Int) {
        parcel.writeLong(time)
    }
}

@Parcelize
@TypeParceler<Date, DateParceler>
data class EventWithDate(
    val id: Long,
    val title: String,
    val time: Date
) : Parcelable
