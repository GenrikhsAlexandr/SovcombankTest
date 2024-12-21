package com.aleksandrgenrihs.sovcombanktest.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SmsCodeRequestNetwork(
    val code: Int,
)