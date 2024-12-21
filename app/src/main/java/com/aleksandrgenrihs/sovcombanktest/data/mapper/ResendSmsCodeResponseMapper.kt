package com.aleksandrgenrihs.sovcombanktest.data.mapper

import com.aleksandrgenrihs.sovcombanktest.Mapper
import com.aleksandrgenrihs.sovcombanktest.data.model.ResendSmsCodeResponseNetwork
import com.aleksandrgenrihs.sovcombanktest.domain.model.ResendSmsCodeResponse
import javax.inject.Inject

class ResendSmsCodeResponseMapper
@Inject constructor() : Mapper<ResendSmsCodeResponseNetwork, ResendSmsCodeResponse> {
    override fun map(input: ResendSmsCodeResponseNetwork): ResendSmsCodeResponse {
        return ResendSmsCodeResponse(
            success = input.success,
            message = input.message
        )
    }
}