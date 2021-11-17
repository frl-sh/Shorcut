package sharif.feryal.shortcut.task.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import sharif.feryal.shortcut.task.databinding.ScreenComicDescriptionBinding

class ComicDescriptionScreen: BottomSheetDialogFragment() {
    private var _binding: ScreenComicDescriptionBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val args: ComicDescriptionScreenArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenComicDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            comicDescriptionTitle.text = args.comic.title
            comicDescriptionContent.text = args.comic.alt
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}