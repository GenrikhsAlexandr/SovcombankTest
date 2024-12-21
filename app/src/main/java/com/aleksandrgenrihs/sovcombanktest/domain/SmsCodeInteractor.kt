package com.aleksandrgenrihs.sovcombanktest.domain

import com.aleksandrgenrihs.sovcombanktest.domain.model.ResendSmsCodeResponse
import com.aleksandrgenrihs.sovcombanktest.domain.model.SmsSendResponse
import javax.inject.Inject
import kotlin.time.Duration

class SmsCodeInteractor
@Inject constructor(
    private val repository: SmsCodeRepository,
) {
    suspend fun setCanResendIn(canResendIn: Duration) = repository.setCanResendIn(canResendIn)

    suspend fun canSendRequest(): Boolean = repository.canSendRequest()

    suspend fun requestSmsCode(): Result<SmsSendResponse> {
        return runCatching { repository.requestSmsCode().getOrThrow() }
    }

    suspend fun getCanResendInSeconds(): Int = repository.getCanSendRequestInSeconds()

    suspend fun sendSmsCode(code: String): Result<ResendSmsCodeResponse> {
        return runCatching { repository.sendSmsCode(code = code.toInt()).getOrThrow() }
    }
}