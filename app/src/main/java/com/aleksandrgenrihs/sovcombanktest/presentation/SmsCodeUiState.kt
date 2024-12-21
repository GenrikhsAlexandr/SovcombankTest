package com.aleksandrgenrihs.sovcombanktest.presentation

data class SmsCodeUiState(
    val code: String = "",
    val errorText: String? = null,
    val incorrectCodeText: String? = null,
    val secondsLeft: Int = 0,
    val canResend: Boolean = true,
    val canVerify: Boolean = false,
    val userInput: String = "",
    val loading: Boolean = false,
    val codeLength: Int = 0,
    val isError: Boolean = false
)