package com.aleksandrgenrihs.sovcombanktest.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ResendSmsCodeResponseNetwork(
    val success: Boolean,
    val message: String? = null
)
