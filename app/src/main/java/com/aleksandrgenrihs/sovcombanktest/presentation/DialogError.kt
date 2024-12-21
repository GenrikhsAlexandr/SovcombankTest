package com.aleksandrgenrihs.sovcombanktest.presentation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.aleksandrgenrihs.sovcombanktest.R
import com.aleksandrgenrihs.sovcombanktest.ui.theme.Blue
import com.aleksandrgenrihs.sovcombanktest.ui.theme.SovcombankTestTheme

@Composable
fun DialogError(
    onDismiss: () -> Unit,
    text: String
) {
    AlertDialog(
        shape = ShapeDefaults.Small,
        title = {
            Text(
                text = stringResource(R.string.errorTitle),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(
                    text = stringResource(id = R.string.close).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 12.sp,
                    color = Blue,
                    textAlign = TextAlign.End
                )
            }
        }
    )
}

@Preview(widthDp = 300, heightDp = 400)
@Composable
private fun DialogInfoPreview() {
    SovcombankTestTheme() {
        DialogError(
            onDismiss = {},
            text = stringResource(R.string.unknownError)
        )
    }
}