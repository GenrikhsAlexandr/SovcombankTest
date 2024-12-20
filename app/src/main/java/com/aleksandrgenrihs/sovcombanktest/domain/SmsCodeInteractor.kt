package com.aleksandrgenrihs.sovcombanktest.domain

import javax.inject.Inject

class SmsCodeInteractor
@Inject constructor(
    private val repository: SmsCodeRepository,
) {
    /**
     * @return true если запрос отправлен, false если отправить не возможно или произошла ошибка
     */
    suspend fun sendRequestSmsCode(): Result<Boolean> {
        return runCatching {
            if (repository.canSendRequest()) {
                val sendResult = repository.requestSmsCode().getOrThrow()
                repository.setCanResendIn(sendResult.canResendIn)
                true
            } else {
                false
            }
        }
    }

    suspend fun getCanResendInSeconds(): Int = repository.getCanSendRequestInSeconds()

    suspend fun sendSmsCode(code: String): Result<Unit> {
        return runCatching { repository.sendSmsCode(code = code.toInt()).getOrThrow() }
    }
}