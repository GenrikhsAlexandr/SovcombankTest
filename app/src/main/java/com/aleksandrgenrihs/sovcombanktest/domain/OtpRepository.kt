package com.aleksandrgenrihs.sovcombanktest.domain

import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpInfo
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpVerify

interface OtpRepository {

    /**
     * @return true, если пользователь может отправить запрос на новое сообщение с кодом
     */
    suspend fun canSendRequest(): Boolean

    /**
     * @return оставшееся время в секундах, которое пользователь должен ждать перед отправкой нового письма с кодом
     */
    suspend fun getCanSendRequestInSeconds(currentTime:Long = System.currentTimeMillis()): Int

    /**
     * @return  количество символов в коде подтверждения
     */
    suspend fun getCodeLength(): Int

    /**
     * Запрос на отправку кода подтверждения пользователю
     */
    suspend fun otpResend(): Result<OtpInfo>

    /**
     *  Верификация пользователя по коду
     */
    suspend fun otpVerify(code: String): Result<OtpVerify>
}