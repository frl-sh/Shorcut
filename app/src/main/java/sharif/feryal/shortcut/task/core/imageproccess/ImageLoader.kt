package sharif.feryal.shortcut.task.core.imageproccess

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

object ImageLoader {

    fun loadImage(
        imageView: ImageView,
        imageURI: String,
    ) {
        Glide.with(imageView.context)
            .load(Uri.parse(imageURI))
            .into(imageView)
    }
}