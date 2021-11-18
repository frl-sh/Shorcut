package sharif.feryal.shortcut.task.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import sharif.feryal.shortcut.task.R
import sharif.feryal.shortcut.task.core.base.BaseFragment
import sharif.feryal.shortcut.task.core.extension.openUrl
import sharif.feryal.shortcut.task.core.imageproccess.ImageLoader
import sharif.feryal.shortcut.task.core.models.LoadableData
import sharif.feryal.shortcut.task.databinding.ScreenComicBrowserBinding
import sharif.feryal.shortcut.task.domain.Comic
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
            isNextEnabled.subscribe { isNextAvailable ->
                binding.nextComicButton.isEnabled = isNextAvailable
            }

            isPreviousEnabled.subscribe { isPrevAvailable ->
                binding.prevComicButton.isEnabled = isPrevAvailable
            }

            dataStatus.subscribe { status ->
                with(binding) {
                    comicLoading.isVisible = status is LoadableData.Loading
                    comicImage.isInvisible = status !is LoadableData.Loaded
                    (status as? LoadableData.Loaded)?.data?.let { comic ->
                        setComicData(comic)
                    }
                }
            }

            failure.subscribe {
                Snackbar.make(
                    binding.root,
                    it.throwable.message ?: getString(R.string.failure_message),
                    LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun ScreenComicBrowserBinding.setComicData(comic: Comic) {
        comicTitle.text = comic.title
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
            findNavController().navigate(
                R.id.comicDescriptionScreen,
                ComicDescriptionScreenArgs(comic).toBundle()
            )
        }
        comicShareButton.setOnClickListener {
            requireContext().openUrl(
                getString(R.string.comic_website_url, comic.number)
            ).takeIf { intent -> intent == null }?.let {
                Snackbar.make(root, getString(R.string.intent_failure_message), LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}