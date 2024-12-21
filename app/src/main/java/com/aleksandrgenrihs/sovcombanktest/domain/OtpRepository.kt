package com.aleksandrgenrihs.sovcombanktest.domain

import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpVerify
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpInfo
import kotlin.time.Duration

interface OtpRepository {
    /**
     * Определяем, как долго пользователь должен ждать перед отправкой нового письма с подтверждением
     */
    suspend fun setCanResendIn(duration: Duration, codeLength:Int)

    /**
     * @return true, если пользователь может отправить запрос на новое сообщение с кодом
     */
    suspend fun canSendRequest(): Boolean

    /**
     * @return оставшееся время в секундах, которое пользователь должен ждать перед отправкой нового письма с кодом
     */
    suspend fun getCanSendRequestInSeconds(): Int

    /**
     * @return  количество символов в коде подтверждения
     */
    suspend fun getCodeLength(): Int

    /**
     * Запрос на отправку кода подтверждения пользователю
     */
    suspend fun otpRequest(): Result<OtpInfo>

    /**
     *  Верификация пользователя по коду
     */
    suspend fun otpVerify(code: String): Result<OtpVerify>
}