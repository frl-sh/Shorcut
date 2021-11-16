package sharif.feryal.shortcut.task.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import sharif.feryal.shortcut.task.R
import sharif.feryal.shortcut.task.core.base.BaseFragment
import sharif.feryal.shortcut.task.core.models.DataStatus
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeComicChanges()
        setClickListeners()
    }

    private fun setClickListeners() {
        with(binding) {
            nextComicButton.setOnClickListener {
                viewModel.nextComicRequested()
            }

            prevComicButton.setOnClickListener {
                viewModel.previousComicRequested()
            }
        }
    }

    private fun observeComicChanges() {
        with(viewModel) {
            comic.subscribe { comic ->
                binding.comicTitle.text = comic.title
            }

            isNextEnabled.subscribe { isNextAvailable ->
                binding.nextComicButton.isEnabled = isNextAvailable
            }

            isPreviousEnabled.subscribe { isPrevAvailable ->
                binding.prevComicButton.isEnabled = isPrevAvailable
            }

            dataStatus.subscribe { status ->
                with(binding) {
                    comicLoading.isVisible = status is DataStatus.Loading
                    if (status is DataStatus.Failed) {
                        Snackbar.make(root, getString(R.string.failure_message), LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}