package com.aleksandrgenrihs.sovcombanktest.data.mapper

import com.aleksandrgenrihs.sovcombanktest.utils.Mapper
import com.aleksandrgenrihs.sovcombanktest.data.model.OtpVerifyResponse
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpVerify
import javax.inject.Inject

class OtpVerifyResponseMapper
@Inject constructor() : Mapper<OtpVerifyResponse, OtpVerify> {
    override fun map(input: OtpVerifyResponse): OtpVerify {
        return OtpVerify(
            success = input.success,
        )
    }
}