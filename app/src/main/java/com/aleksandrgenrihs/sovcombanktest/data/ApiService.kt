package com.aleksandrgenrihs.sovcombanktest.data

import com.aleksandrgenrihs.sovcombanktest.domain.model.SmsCodeRequest
import com.aleksandrgenrihs.sovcombanktest.domain.model.SmsSendResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    /**
     * Отправка  кода из смс
     */
    @POST("v1/reg/otp")
    suspend fun sendSmsCode(@Body request: SmsCodeRequest): Response<Unit>

    /**
     * Запрос на повторную отправку смс
     */
    @POST("v1/reg/otpresend")
    suspend fun resendSmsCode(): Response<SmsSendResult>


}