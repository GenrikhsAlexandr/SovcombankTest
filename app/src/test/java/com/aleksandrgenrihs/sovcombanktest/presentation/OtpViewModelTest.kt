package com.aleksandrgenrihs.sovcombanktest.presentation

import com.aleksandrgenrihs.sovcombanktest.domain.OtpInteractor
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpInfo
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpVerify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.time.Duration.Companion.seconds

class OtpViewModelTest {

    private lateinit var viewModel: OtpViewModel
    private val interactor: OtpInteractor = mock()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = OtpViewModel(interactor)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN call otpResend on success THEN update viewState canResend`() =
        runTest {
            whenever(interactor.canSendRequest()).thenReturn(true)
            whenever(interactor.otpResend()).thenReturn(Result.success(OtpInfo(60.seconds, 6)))
            whenever(interactor.getCanResendInSeconds()).thenReturn(60)

            runCurrent()

            verify(interactor).otpResend()
            assertFalse(viewModel.viewState.canResend)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN call otpResend fails THEN update viewState isError AND canResend`() =
        runTest {
            whenever(interactor.canSendRequest()).thenReturn(true)
            whenever(interactor.otpResend()).thenAnswer {
                throw Exception("Request failed")
            }

            runCurrent()

            verify(interactor).otpResend()
            assertTrue(viewModel.viewState.isError)
            assertTrue(viewModel.viewState.canResend)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN call startTimerIfNeeded THEN update viewState secondsLeft AND codeLength  AND canResend`() =
        runTest {
            whenever(interactor.canSendRequest()).thenReturn(true)
            whenever(interactor.getCanResendInSeconds()).thenReturn(3)
            whenever(interactor.getCodeLength()).thenReturn(6)

            runCurrent()

            assertEquals(6, viewModel.viewState.codeLength)
            assertEquals(3, viewModel.viewState.secondsLeft)
            assertFalse(viewModel.viewState.canResend)
        }

    @Test
    fun `WHEN call onInputChange with short code THEN update viewState canVerify = false`() {
        val shortInput = "123"

        viewModel.onInputChange(shortInput)

        assertEquals(shortInput, viewModel.viewState.userInput)
        assertFalse(viewModel.viewState.canVerify)
    }

    @Test
    fun `WHEN call onInputChange with exactly 6 digits THEN update viewState = true`() {
        val validInput = "123456"

        viewModel.viewState = viewModel.viewState.copy(codeLength = 6)

        viewModel.onInputChange(validInput)

        assertEquals(validInput, viewModel.viewState.userInput)
        assertTrue(viewModel.viewState.canVerify)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN code is correct THEN  update viewState correctCode`() =
        runTest {
            val validInput = "123456"

            whenever(interactor.otpVerify(validInput)).thenReturn(
                Result.success(OtpVerify(success = true))
            )

            viewModel.viewState = viewModel.viewState.copy(
                codeLength = 6,
                canVerify = true
            )

            viewModel.onInputChange(validInput)

            runCurrent()

            verify(interactor).otpVerify(validInput)
            assertTrue(viewModel.viewState.correctCode)
            assertTrue(viewModel.viewState.canVerify)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN code is invalid THEN  updates viewState correctCode = false`() =
        runTest {
            val validInput = "123456"

            whenever(interactor.otpVerify(validInput)).thenReturn(
                Result.success(OtpVerify(success = false))
            )

            viewModel.viewState = viewModel.viewState.copy(
                codeLength = 6,
                canVerify = true
            )

            viewModel.onInputChange(validInput)

            runCurrent()

            verify(interactor).otpVerify(validInput)
            assertFalse(viewModel.viewState.correctCode)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN call otpVerify  fails THEN update viewState isError`() =
        runTest {
            val validInput = "123456"

            whenever(interactor.canSendRequest()).thenReturn(true)
            whenever(interactor.getCanResendInSeconds()).thenReturn(3)
            whenever(interactor.getCodeLength()).thenReturn(6)
            whenever(interactor.otpVerify(validInput)).thenAnswer {
                throw Exception("Request failed")
            }

            viewModel.viewState = viewModel.viewState.copy(
                codeLength = 6,
                canVerify = true
            )

            viewModel.onInputChange(validInput)

            runCurrent()

            verify(interactor).otpVerify(validInput)
            assertTrue(viewModel.viewState.isError)
        }
}
