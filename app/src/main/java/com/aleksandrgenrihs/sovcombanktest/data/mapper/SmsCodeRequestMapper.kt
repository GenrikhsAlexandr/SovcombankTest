package com.aleksandrgenrihs.sovcombanktest.data.mapper

import com.aleksandrgenrihs.sovcombanktest.Mapper
import com.aleksandrgenrihs.sovcombanktest.data.model.SmsCodeRequestNetwork
import com.aleksandrgenrihs.sovcombanktest.domain.model.SmsCodeRequest
import javax.inject.Inject

class SmsCodeRequestMapper
@Inject constructor() : Mapper<SmsCodeRequest, SmsCodeRequestNetwork> {
    override fun map(input: SmsCodeRequest): SmsCodeRequestNetwork {
        return SmsCodeRequestNetwork(
            code = input.code
        )
    }
}