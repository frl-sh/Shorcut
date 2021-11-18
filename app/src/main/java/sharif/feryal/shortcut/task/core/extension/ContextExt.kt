package sharif.feryal.shortcut.task.core.extension

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openUrl(url: String): Intent? {
    return runCatching {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }.getOrNull()
}