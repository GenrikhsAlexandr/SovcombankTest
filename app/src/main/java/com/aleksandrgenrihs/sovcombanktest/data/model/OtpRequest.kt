package com.aleksandrgenrihs.sovcombanktest.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OtpRequest(
    val code: String,
)