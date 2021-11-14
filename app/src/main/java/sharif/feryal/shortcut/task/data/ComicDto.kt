package sharif.feryal.shortcut.task.data

import com.google.gson.annotations.SerializedName

data class ComicDto(
    @SerializedName("num") val number: Int,
    @SerializedName("title") val title: String,
    @SerializedName("img") val imageUrl: String,
    @SerializedName("year") val year: String,
    @SerializedName("month") val month: String,
    @SerializedName("day") val day: String
)