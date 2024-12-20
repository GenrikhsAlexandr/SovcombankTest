package com.aleksandrgenrihs.sovcombanktest.data

import android.content.Context
import com.aleksandrgenrihs.sovcombanktest.R
import com.aleksandrgenrihs.sovcombanktest.domain.SmsCodeRepository
import com.aleksandrgenrihs.sovcombanktest.domain.model.SmsCodeRequest
import com.aleksandrgenrihs.sovcombanktest.domain.model.SmsSendResult
import com.aleksandrgenrihs.sovcombanktest.utils.NetworkException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class SmsCodeRepositoryImpl @Inject constructor(
    private val service: ApiService,
    private val sharedPref: SharedPref,
    @ApplicationContext private val context: Context
) : SmsCodeRepository {

    override suspend fun setCanResendIn(duration: Duration) {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + (duration + 1.seconds).inWholeMilliseconds
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
    override suspend fun requestSmsCode(): Result<SmsSendResult> {
        return runCatching {
            val response = service.resendSmsCode()
            if (response.isSuccessful) {
                SmsSendResult(
                    canResendIn = 60.seconds,
                    codeLength = 6
                )
            } else {
                throw NetworkException(context.getString(R.string.unknownError))
            }
        }
    }

    override suspend fun sendSmsCode(code: Int): Result<Unit> {
        return runCatching {
            val response = service.sendSmsCode(SmsCodeRequest(code))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                throw NetworkException(context.getString(R.string.incorrectSmsCode))
            }
        }
    }
}