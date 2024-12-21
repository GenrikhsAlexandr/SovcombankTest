package com.aleksandrgenrihs.sovcombanktest.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleksandrgenrihs.sovcombanktest.domain.SmsCodeInteractor
import com.aleksandrgenrihs.sovcombanktest.domain.model.ResendSmsCodeResponse
import com.aleksandrgenrihs.sovcombanktest.utils.NetworkException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SmsCodeViewModel
@Inject constructor(private val interactor: SmsCodeInteractor) : ViewModel() {

    private var currentInput = ""
    private var job: Job? = null
    private var result: ResendSmsCodeResponse? = null
    var viewState by mutableStateOf(SmsCodeUiState())

    init {
        viewModelScope.launch {
            sendRequestSmsCode()
        }
    }

    fun sendRequestSmsCode() {
        viewModelScope.launch {
            viewState = viewState.copy(
                loading = true
            )
            try {
                if (!interactor.canSendRequest()) {
                    return@launch
                }
                val response = interactor.requestSmsCode()
                    .onSuccess {
                        viewState = viewState.copy(
                            codeLength = it.codeLength,
                            canResend = false,
                        )
                    }
                    .getOrThrow()
                setCanResendIn(response.canResendIn)
            } catch (e: NetworkException) {
                viewState = viewState.copy(
                    errorText = e.message,
                    isError = true,
                    loading = false
                )
            }
        }
    }

    private suspend fun setCanResendIn(canResendIn: Duration) {
        try {
            interactor.setCanResendIn(canResendIn)
            startTimerIfNeeded()
            viewState = viewState.copy(isError = false)
        } catch (e: NetworkException) {
            viewState = viewState.copy(
                isError = true,
                errorText = e.message,
            )
        } finally {
            viewState = viewState.copy(loading = false)
        }
    }

    private suspend fun startTimerIfNeeded() {
        val secondsLeft = interactor.getCanResendInSeconds()
        updateResendButtonState(secondsLeft)
        startTimer(secondsLeft)
    }

    private fun startTimer(secondsLeft: Int) {
        if (secondsLeft <= 0) {
            return
        }

        viewModelScope.launch {
            var secondsLeftInternal = secondsLeft
            while (isActive && secondsLeftInternal >= 0) {

                updateResendButtonState(secondsLeftInternal)

                delay(1.seconds)
                secondsLeftInternal--
            }
        }
    }

    private fun updateResendButtonState(secondsLeft: Int) {
        viewState = if (secondsLeft <= 0) {
            viewState.copy(canResend = true)
        } else {
            viewState.copy(
                secondsLeft = secondsLeft,
                canResend = false
            )
        }
    }

    fun onInputChange(newValue: String) {
        currentInput = newValue.replace(" ", "")
        viewState = viewState.copy(
            userInput = newValue,
            incorrectCodeText = null,
            canVerify = validateInput(),
        )
        sendSmsCode()
    }

    private fun sendSmsCode() {
        if (job?.isActive == true) {
            return
        }

        job = viewModelScope.launch {
            val isValid = validateInput()
            if (!isValid) return@launch

            viewState = viewState.copy(loading = true)
            try {
                result = interactor.sendSmsCode(currentInput).getOrThrow()
                val success = result?.success ?: false
                val message = result?.message ?: ""
                viewState = viewState.copy(
                    incorrectCodeText = if (success) null else message,
                )
            } catch (e: Exception) {
                viewState = viewState.copy(
                    isError = true,
                    errorText = e.message
                )

            } finally {
                viewState = viewState.copy(loading = false)
                job = null
            }
        }
    }

    private fun validateInput(): Boolean {
        return currentInput.length == viewState.codeLength
    }
}