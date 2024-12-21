package com.aleksandrgenrihs.sovcombanktest.data.mapper

import com.aleksandrgenrihs.sovcombanktest.utils.Mapper
import com.aleksandrgenrihs.sovcombanktest.data.model.OtpResendResponse
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpInfo
import javax.inject.Inject

class OtpResendResponseMapper
@Inject constructor() : Mapper<OtpResendResponse, OtpInfo> {
    override fun map(input: OtpResendResponse): OtpInfo {
        return OtpInfo(
            canResendIn = input.canResendIn,
            codeLength = input.codeLength
        )
    }
}