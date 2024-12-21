package com.aleksandrgenrihs.sovcombanktest.domain.model

import kotlin.time.Duration

data class SmsSendResponse(
    val canResendIn: Duration,
    val codeLength: Int
)
