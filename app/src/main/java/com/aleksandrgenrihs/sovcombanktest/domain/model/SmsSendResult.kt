package com.aleksandrgenrihs.sovcombanktest.domain.model

import kotlin.time.Duration

data class SmsSendResult(
    val canResendIn: Duration,
    val codeLength: Int
)
