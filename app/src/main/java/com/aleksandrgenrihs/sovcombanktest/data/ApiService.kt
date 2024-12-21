package com.aleksandrgenrihs.sovcombanktest.data

import com.aleksandrgenrihs.sovcombanktest.data.model.OtpVerifyResponse
import com.aleksandrgenrihs.sovcombanktest.data.model.OtpRequest
import com.aleksandrgenrihs.sovcombanktest.data.model.OtpResendResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    /**
     * Отправка  кода из otp
     */
    @POST("v1/reg/otp")
    suspend fun otpVerify(@Body request: OtpRequest): Response<OtpVerifyResponse>

    /**
     * Запрос на повторную отправку otp
     */
    @POST("v1/reg/otpresend")
    suspend fun otpResend(): Response<OtpResendResponse>
}