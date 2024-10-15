package com.example.epolisplusapp.api

import com.example.epolisplusapp.models.BaseApiResponse
import com.example.epolisplusapp.models.auth.response.TokenDataResponse
import com.example.epolisplusapp.models.auth.request.CheckPhoneRequest
import com.example.epolisplusapp.models.auth.request.ForgotPasswordRequest
import com.example.epolisplusapp.models.auth.request.LoginRequest
import com.example.epolisplusapp.models.auth.request.RegisterRequest
import com.example.epolisplusapp.models.auth.request.ResendSmsRequest
import com.example.epolisplusapp.models.auth.request.ResetPasswordRequest
import com.example.epolisplusapp.models.auth.request.VerifyCodeRequest
import com.example.epolisplusapp.models.cabinet.request.AddCarRequest
import com.example.epolisplusapp.models.cabinet.request.CheckCarRequest
import com.example.epolisplusapp.models.cabinet.request.DeleteCarRequest
import com.example.epolisplusapp.models.cabinet.request.DeleteProfileRequest
import com.example.epolisplusapp.models.cabinet.request.UpdateProfileRequest
import com.example.epolisplusapp.models.cabinet.response.AddUserCarResponse
import com.example.epolisplusapp.models.dopuslugi.LitroDiscountItems
import com.example.epolisplusapp.models.emergency.EmergencyServiceResponse
import com.example.epolisplusapp.models.partners.GetPartnersResponse
import com.example.epolisplusapp.models.profile.UserProfileData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MainApi {
    interface ApiService {
        @POST(ApiConstance.POST_USER_SIGN_IN)
        suspend fun login(@Body loginRequest: LoginRequest): BaseApiResponse<TokenDataResponse>

        @POST(ApiConstance.POST_USER_SIGH_UP)
        suspend fun signUp(@Body request: RegisterRequest): BaseApiResponse<Any>

        @POST(ApiConstance.POST_USER_CHECK_AUTH)
        suspend fun checkPhone(@Body request: CheckPhoneRequest): BaseApiResponse<Boolean>

        @POST(ApiConstance.POST_USER_RESEND_SMS)
        suspend fun resendSms(@Body request: ResendSmsRequest): BaseApiResponse<Any>

        @POST(ApiConstance.POST_USER_FORGOT_PASSWORD)
        suspend fun forgotPassword(@Body request: ForgotPasswordRequest): BaseApiResponse<Any>

        @POST(ApiConstance.POST_USER_CONFIRM_ACCOUNT)
        suspend fun verifyCode(@Body request: VerifyCodeRequest): BaseApiResponse<TokenDataResponse>

        @POST(ApiConstance.POST_USER_RESET_PASSWORD)
        suspend fun resetPassword(@Body request: ResetPasswordRequest): BaseApiResponse<Any>

        // ----------------------------------------------------------------------------------\\
        @GET(ApiConstance.GET_PARTHERS)
        suspend fun getPartners(): BaseApiResponse<List<GetPartnersResponse>>

        @GET(ApiConstance.GET_USER_PROFILE)
        suspend fun getUserProfile(@Header("Authorization") token: String): BaseApiResponse<UserProfileData>

        @POST(ApiConstance.POST_USER_DELETE_MY_CAR)
        suspend fun deleteCar(
            @Header("Authorization") token: String,
            @Body request: DeleteCarRequest
        ): BaseApiResponse<Any>

        @POST(ApiConstance.GET_VEHICLE_INFORMATION)
        suspend fun checkCar(
            @Header("Authorization") token: String,
            @Body request: CheckCarRequest
        ): BaseApiResponse<AddUserCarResponse>

        @POST(ApiConstance.POST_USER_ADD_CAR)
        suspend fun addCar(
            @Header("Authorization") token: String,
            @Body request: AddCarRequest
        ): BaseApiResponse<Any>

        @POST(ApiConstance.POST_USER_UPDATE_PROFILE)
        suspend fun updateProfile(
            @Header("Authorization") token: String,
            @Body request: UpdateProfileRequest
        ): BaseApiResponse<Any>

        @POST(ApiConstance.POST_USER_DELETE_PROFILE)
        suspend fun deleteProfile(
            @Header("Authorization") token: String,
            @Body request: DeleteProfileRequest
        ): BaseApiResponse<Any>

        @GET(ApiConstance.GET_EMERGENCY_SERVICE)
        suspend fun getEmergency(@Header("Authorization") token: String): BaseApiResponse<EmergencyServiceResponse>

        @GET(ApiConstance.GET_LITRO_CALCULATOR)
        suspend fun litroCalculator(@Header("Authorization") token: String): BaseApiResponse<LitroDiscountItems>

    }
}




