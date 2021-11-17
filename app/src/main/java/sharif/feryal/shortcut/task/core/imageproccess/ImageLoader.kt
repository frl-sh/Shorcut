package sharif.feryal.shortcut.task.core.imageproccess

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

@Suppress("SwallowedException", "LongParameterList", "TooManyFunctions")
object ImageLoader {

    @SuppressLint("CheckResult")
    fun loadImage(
        imageView: ImageView,
        imageURI: String,
    ) {
        Glide.with(imageView.context)
            .load(Uri.parse(imageURI))
            .into(imageView)
    }
}