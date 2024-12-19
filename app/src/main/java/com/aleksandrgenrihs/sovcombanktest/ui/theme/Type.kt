package com.aleksandrgenrihs.sovcombanktest.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.aleksandrgenrihs.sovcombanktest.R

// Set of Material typography styles to start with
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("Roboto")
val fontFamily = FontFamily(
    Font(
        googleFont = fontName,
        fontProvider = provider,
        style = FontStyle.Normal
    )
)

val Typography = Typography(
    titleSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = W700,
        color = Black,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center
    ),
    titleMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = W500,
        color = Black,
        fontSize = 20.sp,
        lineHeight = 23.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center
    ),
    titleLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = W700,
        color = Black,
        fontSize = 22.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center
    ),
  bodyMedium = TextStyle(
      fontFamily = fontFamily,
      fontWeight = W600,
      color = LabelColor,
      fontSize = 16.sp,
      lineHeight = 21.sp,
      letterSpacing = 0.sp,
  ),
    bodySmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = W400,
        color = SecondaryColor,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.sp,
    ),
)