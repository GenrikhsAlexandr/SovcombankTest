package com.aleksandrgenrihs.sovcombanktest.data.mapper

import com.aleksandrgenrihs.sovcombanktest.utils.Mapper
import com.aleksandrgenrihs.sovcombanktest.data.model.OtpRequest
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpCode
import javax.inject.Inject

class OtpRequestMapper
@Inject constructor() : Mapper<OtpCode, OtpRequest> {
    override fun map(input: OtpCode): OtpRequest {
        return OtpRequest(
            code = input.code
        )
    }
}