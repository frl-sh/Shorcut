package sharif.feryal.shortcut.task.core.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData

abstract class BaseFragment : Fragment() {

    fun <T> LiveData<T>.subscribe(block: (T) -> Unit) {
        observe(viewLifecycleOwner, {
            block(it)
        })
    }
}