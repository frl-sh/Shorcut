package sharif.feryal.shortcut.task.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import sharif.feryal.shortcut.task.data.ComicDto

interface ComicApiService {

    @GET("info.0.json")
    fun getCurrentComic(): Call<ComicDto>

    @GET("{num}/info.0.json")
    fun getComicByNumber(@Path("num") num: Int): Call<ComicDto>
}