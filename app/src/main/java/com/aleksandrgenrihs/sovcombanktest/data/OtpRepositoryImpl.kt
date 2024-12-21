package com.aleksandrgenrihs.sovcombanktest.data

import com.aleksandrgenrihs.sovcombanktest.R
import com.aleksandrgenrihs.sovcombanktest.data.mapper.OtpRequestMapper
import com.aleksandrgenrihs.sovcombanktest.data.mapper.OtpVerifyResponseMapper
import com.aleksandrgenrihs.sovcombanktest.data.mapper.OtpResendResponseMapper
import com.aleksandrgenrihs.sovcombanktest.domain.OtpRepository
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpCode
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpVerify
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpInfo
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class OtpRepositoryImpl @Inject constructor(
    private val service: ApiService,
    private val sharedPref: SharedPref,
    private val otpVerifyMapper: OtpVerifyResponseMapper,
    private val otpRequestMapper: OtpRequestMapper,
    private val otpResendMapper: OtpResendResponseMapper,
) : OtpRepository {

    override suspend fun setCanResendIn(duration: Duration, codeLength:Int) {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + (duration - 1.seconds).inWholeMilliseconds
        sharedPref.saveTime(endTime)
        sharedPref.saveCodeLength(codeLength)
    }

    override suspend fun canSendRequest(): Boolean {
        return getCanSendRequestInSeconds() <= 0
    }

    override suspend fun getCanSendRequestInSeconds(): Int {
        val currentTime = System.currentTimeMillis()
        val endTime = sharedPref.getTime()
        val remainingSeconds = (endTime - currentTime) / 1000
        return if (remainingSeconds > 0) remainingSeconds.toInt() else 0
    }

    override suspend fun getCodeLength(): Int =  sharedPref.getCodeLength()

    /**
     * Запрос на otp, в ответе с сервера должны получить время ожидания повторного запросаи длина кода.
     * Сейчас ставим вручную  время 60 сек и длину кода 6 символов
     *
     * Реальный код закоментирован так как [BASE_URL] не известен.
     * Используются моки пока beckend не готов.
     */
    override suspend fun otpRequest(): Result<OtpInfo> {
        return runCatching {
//            val response = service.otpResend()
//            if (response.isSuccessful) {
//                val body = response.body() ?: error("Body is null")
//                otpResendMapper.map(body)
//            } else {
//                error("Unknown error")
//            }

            delay(2000)
            OtpInfo(
                canResendIn = 60.seconds,
                codeLength = 6
            )
        }
    }


    /**
     * Запрос на otp, в ответе с сервера должны получить время ожидания повторного запросаи длина кода.
     * Сейчас ставим вручную  время 60 сек и длину кода 6 символов
     *
     * Реальный код закоментирован так как [BASE_URL] не известен.
     * Используются моки пока beckend не готов.
     */
    override suspend fun otpVerify(code: String): Result<OtpVerify> {
        return runCatching {
//            val request = otpRequestMapper.map(OtpCode(code))
//            val response = service.otpVerify(request)
//            if (response.isSuccessful) {
//                val body = response.body() ?: error("Body is null")
//                otpVerifyMapper.map(body)
//            } else {
//                error("Unknown error")
//            }

            delay(1000)
            if (code == "123456") {
                OtpVerify(
                    success = true,
                )
            } else
                OtpVerify(
                    success = false,
                )
        }
    }
}