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
        if (intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            val items: Array<SmsMessage> = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (item in items) {
                val senderNumber: String? = item.originatingAddress
                val allowedSendersString = BuildConfig.ALLOWED_SENDERS
                val allowedSenders = allowedSendersString.split(",")

                if (senderNumber != null && allowedSenders.contains(senderNumber)) {
                    val messageBody = item.messageBody
                    val code = extractCodeFromSms(messageBody)

                    if (code.isNotEmpty()) {
                        onCodeReceived?.invoke(code)
                    }
                }
            }
        }
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