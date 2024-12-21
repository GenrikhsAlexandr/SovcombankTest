package com.aleksandrgenrihs.sovcombanktest.presentation

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aleksandrgenrihs.sovcombanktest.R
import com.aleksandrgenrihs.sovcombanktest.receiver.SmsBroadcastReceiver
import com.aleksandrgenrihs.sovcombanktest.ui.theme.Black
import com.aleksandrgenrihs.sovcombanktest.ui.theme.Blue
import com.aleksandrgenrihs.sovcombanktest.ui.theme.Gray500
import com.aleksandrgenrihs.sovcombanktest.ui.theme.NavigationColor
import com.aleksandrgenrihs.sovcombanktest.ui.theme.Red
import com.aleksandrgenrihs.sovcombanktest.ui.theme.SovcombankTestTheme

@Composable
fun SmsCodeScreen(
    viewModel: SmsCodeViewModel = hiltViewModel()
) {
    val viewState = viewModel.viewState
    val context = LocalContext.current

    DisposableEffect(Unit) {
        SmsBroadcastReceiver.setOnCodeReceivedListener { code ->
            viewModel.onInputChange(code)
        }
        onDispose {
            SmsBroadcastReceiver.removeOnCodeReceivedListener()
        }
    }

    Content(
        viewState = viewState,
        onClickBack = { (context as? Activity)?.finish() },//так как некуда возращаться, поэтому при нажатии приложение закроется
        onClickResend = viewModel::sendRequestSmsCode,
        userInput = viewState.userInput,
        onInputChange = { viewModel.onInputChange(it) },
    )
    if (viewState.isError) {
        viewState.errorText?.let {
            DialogError(
                onDismiss = { (context as? Activity)?.finish() },
                text = it
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    viewState: SmsCodeUiState,
    onClickBack: () -> Unit = {},
    onClickResend: () -> Unit = {},
    userInput: String,
    onInputChange: (String) -> Unit = {},
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.appbarTitle),
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            tint = NavigationColor,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            if (viewState.loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .wrapContentSize()
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.title),
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(modifier = Modifier.height(164.dp))

                CodeInputTextField(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    errorText = viewState.incorrectCodeText,
                    onInputChange = onInputChange,
                    codeLength = viewState.codeLength,
                    input = userInput
                )
            }
            TextButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .width(278.dp)
                    .wrapContentHeight(),
                enabled = viewState.canResend && !viewState.loading,
                onClick = onClickResend
            ) {
                Text(
                    text = if (viewState.canResend) stringResource(id = R.string.resend)
                    else stringResource(
                        id = R.string.blockResend,
                        viewState.secondsLeft
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    color = if (viewState.canResend) Black else Blue,
                )
            }
        }
    }
}

@Composable
private fun CodeInputTextField(
    modifier: Modifier = Modifier,
    input: String,
    errorText: String? = null,
    codeLength: Int,
    onInputChange: (String) -> Unit = {},
    onImeGoAction: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    val isError = errorText != null

    val targetTextColor = if (isError) {
        Red
    } else {
        LocalContentColor.current
    }

    val textStyle = MaterialTheme.typography.titleLarge.copy(
        color = targetTextColor,
        fontSize = 28.sp,
        lineHeight = 30.sp
    )

    BasicTextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .then(modifier),
        value = input,
        onValueChange = { newValue ->
            val filteredValue = newValue.filter { it.isDigit() }
            if (filteredValue.length <= codeLength) {
                onInputChange(filteredValue)
            }
        },
        textStyle = textStyle,
        cursorBrush = SolidColor(Color.Transparent),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Go
        ),
        keyboardActions = KeyboardActions(onGo = { onImeGoAction() }),
        maxLines = 1,
        singleLine = true,
        decorationBox = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                ) {
                    for (i in 0 until codeLength) {
                        Box(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .weight(1f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = input.getOrNull(i)?.toString() ?: "",
                                style = textStyle
                            )
                            Box(
                                modifier = Modifier
                                    .width(36.dp)
                                    .height(2.dp)
                                    .background(
                                        if (isError) Red
                                        else Gray500
                                    )
                                    .align(Alignment.BottomCenter)
                            )
                        }
                    }
                }

                if (isError) {
                    if (errorText != null) {
                        Text(
                            text = errorText,
                            style = MaterialTheme.typography.labelLarge,
                            color = Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@PreviewLightDark
@Composable
private fun AuthEmailVerificationCodeScreenPreview() {
    SovcombankTestTheme() {
        Content(
            viewState = SmsCodeUiState(
                errorText = "Error",
                userInput = ""
            ),
            userInput = "",
            onClickBack = {},
            onClickResend = {},
            onInputChange = {}
        )
    }
}