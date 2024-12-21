package com.aleksandrgenrihs.sovcombanktest.data

import android.content.Context
import com.aleksandrgenrihs.sovcombanktest.R
import com.aleksandrgenrihs.sovcombanktest.data.mapper.ResendSmsCodeResponseMapper
import com.aleksandrgenrihs.sovcombanktest.data.mapper.SmsCodeRequestMapper
import com.aleksandrgenrihs.sovcombanktest.data.mapper.SmsSendResponseMapper
import com.aleksandrgenrihs.sovcombanktest.domain.SmsCodeRepository
import com.aleksandrgenrihs.sovcombanktest.domain.model.ResendSmsCodeResponse
import com.aleksandrgenrihs.sovcombanktest.domain.model.SmsSendResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class SmsCodeRepositoryImpl @Inject constructor(
    private val service: ApiService,
    private val sharedPref: SharedPref,
    private val resendSmsCodeMapper: ResendSmsCodeResponseMapper,
    private val smsCodeResultMapper: SmsCodeRequestMapper,
    private val smsSendMapper: SmsSendResponseMapper,
    @ApplicationContext private val context: Context
) : SmsCodeRepository {

    override suspend fun setCanResendIn(duration: Duration) {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + (duration - 1.seconds).inWholeMilliseconds
        sharedPref.saveTime(endTime)
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

    /**
     * Запрос на смс, в ответе с сервера должны получить время ожидания повторного запросаи длина кода из смс.
     * Сейчас ставим вручную  время 60 сек и длину кода 6 символов
     */
    override suspend fun requestSmsCode(): Result<SmsSendResponse> {
        return runCatching {
            /**
             * Используется, когда известен BASE_URL
             */
//            val response = service.resendSmsCode()
//            if (response.isSuccessful) {
//                val body = response.body()
//                    ?: throw NetworkException(context.getString(R.string.unknownError))
//                smsSendMapper.map(body)
//            } else {
//                throw NetworkException(context.getString(R.string.unknownError))
//            }

            /**
             * Используется, когда BASE_URL не известен
             */
            delay(2000)
            SmsSendResponse(
                canResendIn = 60.seconds,
                codeLength = 6
            )
        }
    }

    override suspend fun sendSmsCode(code: Int): Result<ResendSmsCodeResponse> {
        return runCatching {
            /**
             * Используется, когда известен BASE_URL
             */
//            val request = smsCodeResultMapper.map(SmsCodeRequest(code))
//            val response = service.sendSmsCode(request)
//            if (response.isSuccessful) {
//                val body = response.body()
//                    ?: throw NetworkException(context.getString(R.string.unknownError))
//                resendSmsCodeMapper.map(body)
//            } else {
//                throw NetworkException(context.getString(R.string.unknownError))
//
//            }
            /**
             * Используется, когда BASE_URL не известен
             */
            delay(1000)
            if (code == 123456) {
                ResendSmsCodeResponse(
                    success = true,
                )
            } else
                ResendSmsCodeResponse(
                    success = false,
                    message = context.getString(R.string.incorrectSmsCode)
                )
        }
    }
}