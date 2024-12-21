package com.aleksandrgenrihs.sovcombanktest.presentation

data class OtpUiState(
    val code: String = "",
    val correctCode: Boolean = true,
    val secondsLeft: Int = 0,
    val canResend: Boolean = true,
    val canVerify: Boolean = false,
    val userInput: String = "",
    val loading: Boolean = false,
    val codeLength: Int = 0,
    val isError: Boolean = false
)