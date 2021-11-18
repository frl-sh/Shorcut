package sharif.feryal.shortcut.task.core.extension

import android.content.Context
import android.content.Intent

fun Context.sendUrl(url: String): Intent? {
    return runCatching {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
            startActivity(Intent.createChooser(this, "Choose one to share"))
        }
    }.getOrNull()
}