package com.aleksandrgenrihs.sovcombanktest.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage

class SmsBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private var onCodeReceived: ((String) -> Unit)? = null

        fun setOnCodeReceivedListener(listener: (String) -> Unit) {
            onCodeReceived = listener
        }

        fun removeOnCodeReceivedListener() {
            onCodeReceived = null
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            val items: Array<SmsMessage> = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (item in items) {
                val messageBody = item.messageBody
                val code = extractCodeFromSms(messageBody)
                if (code.isNotEmpty()) {
                    onCodeReceived?.invoke(code)
                }
            }
        }
    }

    private fun extractCodeFromSms(message: String): String {
        val regex = Regex("\\d{4,6}")
        return regex.find(message)?.value ?: ""
    }
}