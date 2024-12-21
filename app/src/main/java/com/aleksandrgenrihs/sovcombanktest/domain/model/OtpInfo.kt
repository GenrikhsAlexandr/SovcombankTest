package com.aleksandrgenrihs.sovcombanktest.domain.model

import kotlin.time.Duration

data class OtpInfo(
    val canResendIn: Duration,
    val codeLength: Int
)