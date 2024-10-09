package com.example.epolisplusapp.api

import com.example.epolisplusapp.models.BaseResponse
import com.example.epolisplusapp.models.auth.LoginRequest
import com.example.epolisplusapp.models.auth.LoginResponse
import com.example.epolisplusapp.models.auth.CheckPhoneRequest
import com.example.epolisplusapp.models.auth.CheckPhoneResponse
import com.example.epolisplusapp.models.auth.ForgotPasswordRequest
import com.example.epolisplusapp.models.auth.ForgotPasswordResponse
import com.example.epolisplusapp.models.auth.RegisterRequest
import com.example.epolisplusapp.models.auth.RegisterResponse
import com.example.epolisplusapp.models.auth.ResendSmsRequest
import com.example.epolisplusapp.models.auth.ResendSmsResponse
import com.example.epolisplusapp.models.auth.ResetPasswordRequest
import com.example.epolisplusapp.models.auth.ResetPasswordResponse
import com.example.epolisplusapp.models.auth.VerifyCodeRequest
import com.example.epolisplusapp.models.auth.VerifyCodeResponse
import com.example.epolisplusapp.models.cabinet.AddCarRequest
import com.example.epolisplusapp.models.cabinet.AddCarResponse
import com.example.epolisplusapp.models.cabinet.DeleteCarRequest
import com.example.epolisplusapp.models.cabinet.DeleteCarResponse
import com.example.epolisplusapp.models.cabinet.CheckCarRequest
import com.example.epolisplusapp.models.cabinet.DeleteProfileResponse
import com.example.epolisplusapp.models.cabinet.DeleteProfileRequest
import com.example.epolisplusapp.models.cabinet.UpdateProfileRequest
import com.example.epolisplusapp.models.cabinet.UpdateProfileResponse
import com.example.epolisplusapp.models.cabinet.VehicleInformationResponse
import com.example.epolisplusapp.models.dopuslugi.DopUslugiResponse
import com.example.epolisplusapp.models.extra.ExtraResponse
import com.example.epolisplusapp.models.partners.PartnersResponse
import com.example.epolisplusapp.models.profile.UserProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MainApi {
    interface ApiService {
        @POST("v1/user/sign-in")
        suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

                @POST("/v1/user/sign-up")
        suspend fun signUp(@Body request: RegisterRequest): RegisterResponse

        @POST("v3/user/check-auth")
        suspend fun checkPhone(@Body request: CheckPhoneRequest): CheckPhoneResponse

                @POST("/v1/user/resend-sms")
        suspend fun resendSms(@Body request: ResendSmsRequest): ResendSmsResponse

                @POST("/v1/user/forgot-password")
        suspend fun forgotPassword(@Body request: ForgotPasswordRequest): ForgotPasswordResponse

        @POST("/v1/user/confirm-account")
        suspend fun verifyCode(@Body request: VerifyCodeRequest): VerifyCodeResponse

                @POST("v1/user/reset-password")
        suspend fun resetPassword(@Body request: ResetPasswordRequest): ResetPasswordResponse

        // ----------------------------------------------------------------------------------\\
        @GET("/v1/references/get-partners")
        suspend fun getPartners(): PartnersResponse

        @GET("/v1/user/profile")
        suspend fun getUserProfile(@Header("Authorization") token: String): UserProfileResponse

        @POST("/v1/user/delete-my-car")
        suspend fun deleteCar(
            @Header("Authorization") token: String,
            @Body request: DeleteCarRequest
        ): DeleteCarResponse

        @POST(ApiConstanta.GET_VEHICLE_INFORMATION)
        suspend fun checkCar(
            @Header("Authorization") token: String,
            @Body request: CheckCarRequest
        ): BaseResponse<VehicleInformationResponse>

        @POST("/v1/user/add-user-car")
        suspend fun addCar(
            @Header("Authorization") token: String,
            @Body request: AddCarRequest
        ): AddCarResponse

        @POST("/v2/user/update-profile")
        suspend fun updateProfile(
            @Header("Authorization") token: String,
            @Body request: UpdateProfileRequest
        ): UpdateProfileResponse

        @POST("/v1/user/delete-phone-test")
        suspend fun deleteProfile(
            @Header("Authorization") token: String,
            @Body request: DeleteProfileRequest
        ): DeleteProfileResponse

        @GET("v1/litro/emergency-services")
        suspend fun getEmergency(@Header("Authorization")token: String):ExtraResponse

        @GET("v2/litro/litro-calculator")
        suspend fun getDopUslugi(@Header("Authorization")token: String):DopUslugiResponse

    }
}




