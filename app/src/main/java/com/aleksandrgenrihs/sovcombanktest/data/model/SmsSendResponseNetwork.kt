package com.aleksandrgenrihs.sovcombanktest.data.model

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class SmsSendResponseNetwork(
    val canResendIn: Duration,
    val codeLength: Int
)
