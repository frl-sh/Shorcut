package sharif.feryal.shortcut.task.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
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

    @RelaxedMockK
    private lateinit var comicUseCase: GetComicUseCase

    private val anyNumber = 100
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

    private fun simulateSettingCurrentComic(currentComicNumber: Int, maxNumber: Int) {
        coEvery { comicUseCase.getComic(any()) } returnsMany listOf(
            // First api call will set comics max number
            Either.Success(mockComic.copy(number = maxNumber)),
            // any second api call will return this currentComicNumber as its number(it's just
            // for mocking)
            Either.Success(mockComic.copy(number = currentComicNumber))
        )

        // We couldn't call next here but at first next is disable
        viewModel.previousComicRequested()
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

    @Test
    fun `when previous Comic is requested, then data-status should be loading`() {
        coEvery { comicUseCase.getComic() } coAnswers {
            Either.Success(mockComic)
        }
        val prevNumber = mockComic.number - 1
        coEvery { comicUseCase.getComic(prevNumber) } coAnswers {
            delay(500)
            val previousComic = mockComic.copy(prevNumber)
            Either.Success(previousComic)
        }

        viewModel.previousComicRequested()

        assert(viewModel.dataStatus.value is DataStatus.Loading)
    }

    @Test
    fun `when previous Comic is requested, and it failed, then data-status should be failed`() {
        coEvery { comicUseCase.getComic() } coAnswers {
            Either.Success(mockComic)
        }
        val prevNumber = mockComic.number - 1
        val error = Throwable()
        coEvery { comicUseCase.getComic(prevNumber) } coAnswers {
            Either.Failure(error)
        }

        viewModel.previousComicRequested()

        assert(viewModel.dataStatus.value is DataStatus.Failed)
    }

    @Test
    fun `when get previous Comic successfully, then Comic live-data should be updated`() {
        coEvery { comicUseCase.getComic() } coAnswers {
            Either.Success(mockComic)
        }
        val prevNumber = mockComic.number - 1
        val previousComic = mockComic.copy(prevNumber)
        coEvery { comicUseCase.getComic(prevNumber) } coAnswers {
            Either.Success(previousComic)
        }

        viewModel.previousComicRequested()

        assertEquals(previousComic, viewModel.comic.value)
    }

    @Test
    fun `when it's already the first comic, then request previous comic should not call api`() {
        val firstComicNum = 0
        coEvery { comicUseCase.getComic() } coAnswers {
            Either.Success(mockComic.copy(number = firstComicNum))
        }

        viewModel.previousComicRequested()

        coVerify(timeout = 0) { comicUseCase.getComic(any()) }
    }

    @Test
    fun `when next Comic is requested, then data-status should be loading`() {
        val currentNumber = 2
        simulateSettingCurrentComic(currentComicNumber = currentNumber, maxNumber = 5)
        val nextComic = mockComic.copy(number = currentNumber + 1)
        coEvery { comicUseCase.getComic(any()) } coAnswers {
            delay(500)
            Either.Success(nextComic)
        }

        viewModel.nextComicRequested()

        assert(viewModel.dataStatus.value is DataStatus.Loading)
    }

    @Test
    fun `when next Comic is requested, and it failed, then data-status should be failed`() {
        val currentNumber = 2
        simulateSettingCurrentComic(currentComicNumber = currentNumber, maxNumber = 5)
        val nextComic = mockComic.copy(number = currentNumber + 1)
        val error = Throwable()
        coEvery { comicUseCase.getComic(any()) } coAnswers {
            Either.Failure(error)
        }

        viewModel.nextComicRequested()

        assert(viewModel.dataStatus.value is DataStatus.Failed)
    }

    @Test
    fun `when get next Comic successfully, then Comic live-data should be updated`() {
        val currentNumber = 2
        simulateSettingCurrentComic(currentComicNumber = currentNumber, maxNumber = 5)
        val nextComic = mockComic.copy(number = currentNumber + 1)
        coEvery { comicUseCase.getComic(any()) } coAnswers {
            Either.Success(nextComic)
        }

        viewModel.nextComicRequested()

        assertEquals(nextComic, viewModel.comic.value)
    }

    @Test
    fun `when it's already the last comic, then request next comic should not call api`() {
        val currentNumber = 5
        simulateSettingCurrentComic(currentComicNumber = currentNumber, maxNumber = currentNumber)
        val nextComic = mockComic.copy(number = currentNumber + 1)
        coEvery { comicUseCase.getComic(any()) } coAnswers {
            Either.Success(nextComic)
        }

        viewModel.nextComicRequested()

        coVerify(timeout = 0) { comicUseCase.getComic(any()) }
    }
}