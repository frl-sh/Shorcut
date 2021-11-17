package sharif.feryal.shortcut.task.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comic(
    val number: Int,
    val title: String,
    val imageUrl: String,
    val date: Date,
    val alt: String,
    val transcript: String
) : Parcelable

@Parcelize
data class Date(
    val year: String,
    val month: String,
    val day: String
) : Parcelable