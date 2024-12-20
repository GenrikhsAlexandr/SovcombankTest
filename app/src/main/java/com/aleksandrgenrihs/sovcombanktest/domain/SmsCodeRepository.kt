package com.aleksandrgenrihs.sovcombanktest.domain

import com.aleksandrgenrihs.sovcombanktest.domain.model.SmsSendResult
import kotlin.time.Duration

interface SmsCodeRepository {
    /**
     * Определите, как долго пользователь должен ждать перед отправкой нового письма с подтверждением
     */
    suspend fun setCanResendIn(duration: Duration)

    /**
     * @return true, если пользователь может отправить новое сообщение с кодом
     */
    suspend fun canSendRequest(): Boolean

    /**
     * @return оставшееся время в секундах, которое пользователь должен ждать перед отправкой нового письма с кодом
     */
    suspend fun getCanSendRequestInSeconds(): Int

    /**
     * Отправить код подтверждения пользователю
     */
    suspend fun requestSmsCode(): Result<SmsSendResult>

    /**
     *  Верификация пользователя по коду смс
     */
    suspend fun sendSmsCode(code: Int): Result<Unit>
}