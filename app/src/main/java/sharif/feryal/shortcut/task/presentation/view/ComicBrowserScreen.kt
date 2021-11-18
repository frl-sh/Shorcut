package sharif.feryal.shortcut.task.presentation.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isInvisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import sharif.feryal.shortcut.task.R
import sharif.feryal.shortcut.task.core.base.BaseFragment
import sharif.feryal.shortcut.task.core.extension.sendUrl
import sharif.feryal.shortcut.task.core.imageproccess.ImageLoader
import sharif.feryal.shortcut.task.core.models.LoadableData
import sharif.feryal.shortcut.task.databinding.ScreenComicBrowserBinding
import sharif.feryal.shortcut.task.domain.Comic
import sharif.feryal.shortcut.task.presentation.viewmodel.ComicBrowserViewModel
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.*


class ComicBrowserScreen : BaseFragment() {
    private var _binding: ScreenComicBrowserBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val job = Job()
    private val viewScope = CoroutineScope(Dispatchers.Main + job)

    private val viewModel: ComicBrowserViewModel by viewModel()

    private var snackbar: Snackbar? = null

    private var textDebounceJob: Job? = null
    private val comicSearchTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(editable: Editable) {
            textDebounceJob?.cancel()
            editable.toString().toIntOrNull()?.let { comicNumberQuery ->
                textDebounceJob = viewScope.launch {
                    delay(SearchDebounceDelay)
                    viewModel.searchQueryRequested(comicNumberQuery)
                }
            }
        }
    }

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
                clearSearchFocus()
            }

            prevComicButton.setOnClickListener {
                viewModel.previousComicRequested()
                clearSearchFocus()
            }

            comicBrowserToolbar.toolbarSearchText.addTextChangedListener(comicSearchTextWatcher)
        }
    }

    private fun observeComicChanges() {
        with(viewModel) {
            isNextEnabled.subscribe { isNextAvailable ->
                binding.nextComicButton.isEnabled = isNextAvailable
            }

            isPreviousEnabled.subscribe { isPrevAvailable ->
                binding.prevComicButton.isEnabled = isPrevAvailable
            }

            dataStatus.subscribe { status ->
                with(binding) {
                    comicLoading.isInvisible = status !is LoadableData.Loading
                    (status as? LoadableData.Loaded)?.data?.let { comic ->
                        snackbar?.dismiss()
                        setComicData(comic)
                    }
                }
            }

            failure.subscribe { failure ->
                val retryObject = failure.retry
                val messageDuration = if (retryObject == null) LENGTH_SHORT else LENGTH_INDEFINITE
                snackbar = Snackbar.make(
                    binding.root,
                    failure.throwable.message ?: getString(R.string.failure_message),
                    messageDuration
                ).apply {
                    retryObject?.let { retry ->
                        setAction(getString(R.string.retry)) {
                            viewModel.retryRequested(retry.comicNumber)
                        }
                    }
                    show()
                }
            }
        }
    }

    private fun ScreenComicBrowserBinding.setComicData(comic: Comic) {
        comicTitle.text = comic.title
        comicNumber.text = getString(R.string.comic_number, comic.number)
        comicDate.text = getString(
            R.string.comic_date,
            comic.date.month,
            comic.date.day,
            comic.date.year
        )
        comicTranscript.text = comic.transcript

        ImageLoader.loadImage(comicImage, comic.imageUrl)

        setComicListeners(comic)
    }

    private fun ScreenComicBrowserBinding.setComicListeners(comic: Comic) {
        comicImage.setOnClickListener {
            clearSearchFocus()
            findNavController().navigate(
                R.id.comicDescriptionScreen,
                ComicDescriptionScreenArgs(comic).toBundle()
            )
        }
        comicShareButton.setOnClickListener {
            clearSearchFocus()
            requireContext().sendUrl(
                getString(R.string.comic_website_url, comic.number)
            ).takeIf { intent -> intent == null }?.let {
                Snackbar.make(root, getString(R.string.intent_failure_message), LENGTH_SHORT).show()
            }
        }
    }

    private fun clearSearchFocus() {
        binding.comicBrowserToolbar.toolbarSearchText.clearFocus()
        view?.let {
            getSystemService(
                requireContext(),
                InputMethodManager::class.java
            )?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.comicBrowserToolbar.toolbarSearchText.removeTextChangedListener(
            comicSearchTextWatcher
        )
        snackbar?.dismiss()
        textDebounceJob?.cancel()
        job.cancel()
        _binding = null
    }

    private companion object {
        const val SearchDebounceDelay = 500L
    }
}