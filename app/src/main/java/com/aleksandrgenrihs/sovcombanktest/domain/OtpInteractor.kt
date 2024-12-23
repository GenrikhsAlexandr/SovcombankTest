package com.aleksandrgenrihs.sovcombanktest.domain

import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpVerify
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpInfo
import javax.inject.Inject

class OtpInteractor
@Inject constructor(
    private val repository: OtpRepository,
) {
    suspend fun canSendRequest(): Boolean = repository.canSendRequest()

    suspend fun otpResend(): Result<OtpInfo> = repository.otpResend()


    suspend fun getCanResendInSeconds(): Int = repository.getCanSendRequestInSeconds()

    suspend fun getCodeLength(): Int = repository.getCodeLength()

    suspend fun otpVerify(code: String): Result<OtpVerify> =
        repository.otpVerify(code = code)
}