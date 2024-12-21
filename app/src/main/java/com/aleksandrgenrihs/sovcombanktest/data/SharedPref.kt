package com.aleksandrgenrihs.sovcombanktest.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref
@Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            "AppPref",
            Context.MODE_PRIVATE
        )
    }

    /**
     * Сохраняем время, когда можно будет отправить запрос повторно
     */
    fun saveTime(endTime: Long) {
        preferences.edit()
            .putLong(
                "time", endTime
            )
            .apply()
    }

    /**
     * Сохраняем время, когда можно будет отправить запрос повторно
     */
    fun saveCodeLength(codeLength: Int) {
        preferences.edit()
            .putInt(
                "code", codeLength
            )
            .apply()
    }

    fun getTime(): Long =
        preferences.getLong("time", 0)

    fun getCodeLength(): Int =
        preferences.getInt("code", 0)
}