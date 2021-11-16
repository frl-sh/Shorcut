package sharif.feryal.shortcut.task.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import sharif.feryal.shortcut.task.core.models.DataStatus
import sharif.feryal.shortcut.task.core.models.Either
import sharif.feryal.shortcut.task.domain.Comic
import sharif.feryal.shortcut.task.domain.Date
import sharif.feryal.shortcut.task.domain.interactor.GetComicUseCase
import sharif.feryal.shortcut.task.util.CoroutineTestRule
import sharif.feryal.shortcut.task.util.testCoroutineDispatcher

@ExperimentalCoroutinesApi
class ComicBrowserViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @MockK
    private lateinit var comicUseCase: GetComicUseCase

    private val anyNumber = 0
    private val anyDate = Date("", "", "")
    private val mockComic = Comic(
        anyNumber,
        "anyTitle",
        "anyImageUrl",
        anyDate
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    private val viewModel: ComicBrowserViewModel by lazy {
        ComicBrowserViewModel(comicUseCase)
    }

    @Test
    fun `when get currentComic, then data-status should be loading`() =
        coroutineTestRule.testDispatcher.runBlockingTest {
            val successResponse = Either.Success(mockComic)
            coEvery { comicUseCase.getComic() } coAnswers {
                delay(500)
                successResponse
            }

            assert(viewModel.dataStatus.value is DataStatus.Loading)
        }

    @Test
    fun `when get currentComic failed, then data-status should be failed`() {
        val error = Throwable()
        val failureResponse = Either.Failure(error)
        coEvery { comicUseCase.getComic() } coAnswers { failureResponse }

        assert(viewModel.dataStatus.value is DataStatus.Failed)
    }

    @Test
    fun `when get currentComic successfully, then Comic live-data should be updated`() {
        val successResponse = Either.Success(mockComic)
        coEvery { comicUseCase.getComic() } coAnswers { successResponse }

        assertEquals(mockComic, viewModel.comic.value)
    }
}