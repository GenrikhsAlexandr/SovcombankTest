package com.aleksandrgenrihs.sovcombanktest.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import com.aleksandrgenrihs.sovcombanktest.BuildConfig

/**
 * BroadcastReceiver для получения смс с кодом
 */

class SmsBroadcastReceiver : BroadcastReceiver() {

    /**
     * Слушатель для получения кода из смс
     */
    companion object {
        private var onCodeReceived: ((String) -> Unit)? = null

        fun setOnCodeReceivedListener(listener: (String) -> Unit) {
            onCodeReceived = listener
        }

        fun removeOnCodeReceivedListener() {
            onCodeReceived = null
        }
    }

    /**
     * senderNumber -  номера отправителя
     * allowedSenders - разрешенные отправители
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        messages.forEach { processSmsMessage(it) }
    }

    private fun processSmsMessage(message: SmsMessage) {
        val senderNumber = message.originatingAddress ?: return
        if (!isSenderAllowed(senderNumber)) return

        val code = extractCodeFromSms(message.messageBody)
        if (code.isNotEmpty()) {
            onCodeReceived?.invoke(code)
        }
    }

    private fun isSenderAllowed(senderNumber: String): Boolean {
        val allowedSenders = BuildConfig.ALLOWED_SENDERS.split(",")
        return allowedSenders.contains(senderNumber)
    }

    /**
     *  Извлечения кода из смс
     * в regex задаем шаблон для поиска кода из 4-6 цифр
     */
    private fun extractCodeFromSms(message: String): String {
        val regex = Regex("\\d{4,6}")
        return regex.find(message)?.value ?: ""
    }
}
