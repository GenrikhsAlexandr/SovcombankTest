package com.aleksandrgenrihs.sovcombanktest.data.mapper

import com.aleksandrgenrihs.sovcombanktest.Mapper
import com.aleksandrgenrihs.sovcombanktest.data.model.SmsSendResponseNetwork
import com.aleksandrgenrihs.sovcombanktest.domain.model.SmsSendResponse
import javax.inject.Inject

class SmsSendResponseMapper
@Inject constructor() : Mapper<SmsSendResponseNetwork, SmsSendResponse> {
    override fun map(input: SmsSendResponseNetwork): SmsSendResponse {
        return SmsSendResponse(
            canResendIn = input.canResendIn,
            codeLength = input.codeLength
        )
    }
}