package sharif.feryal.shortcut.task.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel
import sharif.feryal.shortcut.task.core.base.BaseFragment
import sharif.feryal.shortcut.task.databinding.ScreenComicBrowserBinding
import sharif.feryal.shortcut.task.presentation.viewmodel.ComicBrowserViewModel

class ComicBrowserScreen : BaseFragment() {
    private var _binding: ScreenComicBrowserBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel: ComicBrowserViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenComicBrowserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}