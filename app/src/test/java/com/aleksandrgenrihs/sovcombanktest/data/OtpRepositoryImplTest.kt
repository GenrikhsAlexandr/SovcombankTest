package com.aleksandrgenrihs.sovcombanktest.data

import com.aleksandrgenrihs.sovcombanktest.data.mapper.OtpRequestMapper
import com.aleksandrgenrihs.sovcombanktest.data.mapper.OtpResendResponseMapper
import com.aleksandrgenrihs.sovcombanktest.data.mapper.OtpVerifyResponseMapper
import com.aleksandrgenrihs.sovcombanktest.data.model.OtpRequest
import com.aleksandrgenrihs.sovcombanktest.data.model.OtpResendResponse
import com.aleksandrgenrihs.sovcombanktest.data.model.OtpVerifyResponse
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpCode
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpInfo
import com.aleksandrgenrihs.sovcombanktest.domain.model.OtpVerify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import kotlin.time.Duration.Companion.milliseconds

class OtpRepositoryImplTest {

    private lateinit var repository: OtpRepositoryImpl
    private val service: ApiService = mock()
    private val sharedPref: SharedPref = mock()
    private val otpVerifyMapper: OtpVerifyResponseMapper = mock()
    private val otpRequestMapper: OtpRequestMapper = mock()
    private val otpResendMapper: OtpResendResponseMapper = mock()

    @BeforeEach
    fun setUp() {
        repository = OtpRepositoryImpl(
            service,
            sharedPref,
            otpVerifyMapper,
            otpRequestMapper,
            otpResendMapper
        )
    }

    @Test
    fun `WHEN call getCanSendRequestInSeconds AND currentTime less than endTime THEN returns difference seconds`() =
        runTest {
            val currentTime = 100_000L
            whenever(sharedPref.getTime()).thenReturn(currentTime + 10_000L)

            val result = repository.getCanSendRequestInSeconds(currentTime)

            assertEquals(10, result)
        }

    @Test
    fun `WHEN call getCanSendRequestInSeconds AND time currentTime greater than endTime THEN returns 0 seconds`() =
        runTest {
            val currentTime = 100_000L
            whenever(sharedPref.getTime()).thenReturn(currentTime - 10_000L)

            val result = repository.getCanSendRequestInSeconds(currentTime)

            assertEquals(0, result)
        }


    @Test
    fun `WHEN send otp THEN return OtpInfo`() = runTest {
        whenever(service.otpResend()).thenReturn(
            OtpResendResponse(
                canResendIn = 100.milliseconds,
                codeLength = 6
            )
        )
        whenever(otpResendMapper.map(any())).thenReturn(
            OtpInfo(
                canResendIn = 100.milliseconds,
                codeLength = 6
            )
        )

        val result = repository.otpResend().getOrNull()

        assertEquals(
            OtpInfo(
                canResendIn = 100.milliseconds,
                codeLength = 6
            ), result
        )

        verify(service).otpResend()
        verify(otpResendMapper).map(
            OtpResendResponse(
                canResendIn = 100.milliseconds,
                codeLength = 6
            )
        )
        verify(sharedPref).saveTime(100)
        verify(sharedPref).saveCodeLength(6)
        verifyNoMoreInteractions(service)
    }

    @Test
    fun `WHEN otp resend fails THEN return failure`() = runTest {
        whenever(service.otpResend()).thenAnswer {
            throw Exception("Request failed")
        }
        whenever(otpResendMapper.map(any())).thenReturn(
            OtpInfo(
                canResendIn = 100.milliseconds,
                codeLength = 6
            )
        )

        val result = repository.otpResend()

        assertTrue(result.isFailure)

        verify(service).otpResend()
        verify(otpResendMapper, never()).map(any())
        verify(sharedPref, never()).saveTime(any())
        verify(sharedPref, never()).saveCodeLength(any())
        verifyNoMoreInteractions(service, otpResendMapper, sharedPref)
    }

    @Test
    fun `WHEN send code THEN return success`() = runTest {
        whenever(
            service.otpVerify(
                OtpRequest(
                    code = "123456"
                )
            )
        ).thenReturn(
            OtpVerifyResponse(
                success = true
            )
        )
        whenever(otpVerifyMapper.map(any())).thenReturn(
            OtpVerify(
                success = true
            )
        )

        whenever(otpRequestMapper.map(any())).thenReturn(
            OtpRequest(
                code = "123456"
            )
        )

        val result = repository.otpVerify(code = "123456").getOrNull()

        assertEquals(
            OtpVerify(
                success = true
            ), result
        )

        verify(service).otpVerify(
            OtpRequest(
                code = "123456"
            )
        )
        verify(otpVerifyMapper).map(
            OtpVerifyResponse(
                success = true
            )
        )
        verify(otpRequestMapper).map(
            OtpCode(
                code = "123456"
            )
        )
        verifyNoMoreInteractions(service, otpVerifyMapper, otpRequestMapper)
    }

    @Test
    fun `WHEN verify request fails THEN return failure`() = runTest {
        whenever(
            service.otpVerify(
                OtpRequest(
                    code = "123456"
                )
            )
        ).thenAnswer {
            throw Exception("Request failed")
        }
        whenever(otpVerifyMapper.map(any())).thenReturn(
            OtpVerify(
                success = false
            )
        )

        whenever(otpRequestMapper.map(any())).thenReturn(
            OtpRequest(
                code = "123456"
            )
        )

        val result = repository.otpVerify(code = "123456")

        assertTrue(result.isFailure)

        verify(service).otpVerify(OtpRequest(code = "123456"))
        verify(otpVerifyMapper, never()).map(any())
        verify(otpRequestMapper).map(any())
        verifyNoMoreInteractions(service, otpVerifyMapper, otpRequestMapper)
    }

    @Test
    fun `WHEN call getCodeLength THEN return codeLength from sharedPref`() = runTest {
        val codeLength = 6
        whenever(sharedPref.getCodeLength()).thenReturn(codeLength)

        val result = repository.getCodeLength()

        assertEquals(codeLength, result)
        verify(sharedPref).getCodeLength()
        verifyNoMoreInteractions(sharedPref)
    }
}