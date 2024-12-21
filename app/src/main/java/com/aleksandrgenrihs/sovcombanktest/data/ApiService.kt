package com.aleksandrgenrihs.sovcombanktest.data

import com.aleksandrgenrihs.sovcombanktest.data.model.ResendSmsCodeResponseNetwork
import com.aleksandrgenrihs.sovcombanktest.data.model.SmsCodeRequestNetwork
import com.aleksandrgenrihs.sovcombanktest.data.model.SmsSendResponseNetwork
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    /**
     * Отправка  кода из смс
     */
    @POST("v1/reg/otp")
    suspend fun sendSmsCode(@Body request: SmsCodeRequestNetwork): Response<ResendSmsCodeResponseNetwork>

    /**
     * Запрос на повторную отправку смс
     */
    @POST("v1/reg/otpresend")
    suspend fun resendSmsCode(): Response<SmsSendResponseNetwork>


}