package com.aleksandrgenrihs.sovcombanktest.data.model

import javax.inject.Inject

interface ClockProvider {
    fun now(): Long
}

class SystemClockProvider @Inject constructor() : ClockProvider {
    override fun now(): Long = System.currentTimeMillis()
}
