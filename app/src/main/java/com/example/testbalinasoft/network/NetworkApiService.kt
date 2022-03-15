package com.example.testbalinasoft.network

import android.util.Log
import com.example.testbalinasoft.data.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://junior.balinasoft.com/"

private val client = OkHttpClient.Builder()
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .addInterceptor(
        HttpLoggingInterceptor { message ->
            Log.d("Main", message)
        }.apply { level = HttpLoggingInterceptor.Level.BODY }
    )
    .build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface NetworkApiService {
    @POST("api/account/signup")
    suspend fun singUp(@Body login: LoginRequest): LoginResponse

    @POST("api/account/signin")
    suspend fun singIn(@Body login: LoginRequest): LoginResponse

    @GET("api/image")
    @Headers("accept: */*")
    suspend fun getImages(
        @Header("Access-Token") token: String,
        @Query("page") page: Int
    ): ImagesResponse

    @POST("api/image")
    @Headers("accept: */*")
    suspend fun postImage(
        @Header("Access-Token") token: String,
        @Body imageRequest: ImageRequest
    ): ImageResponse

    @DELETE("api/image/{id}")
    @Headers("accept: */*")
    suspend fun deleteImage(
        @Header("Access-Token") token: String,
        @Path("id") id: Int
    ): ImageResponse

    @GET("api/image/{imageId}/comment")
    @Headers("accept: */*")
    suspend fun getComments(
        @Header("Access-Token") token: String,
        @Path("imageId") imageId: Int,
        @Query("page") page: Int
    ): CommentsResponse

    @POST("api/image/{imageId}/comment")
    @Headers("accept: */*")
    suspend fun postComment(
        @Header("Access-Token") token: String,
        @Path("imageId") imageId: Int,
        @Body text: String
    ): CommentResponse

    @DELETE("api/image/{imageId}/comment/{commentId}")
    @Headers("accept: */*")
    suspend fun deleteComment(
        @Header("Access-Token") token: String,
        @Path("imageId") imageId: Int,
        @Path("commentId") commentId: Int
    ): CommentResponse
}

object NetworkApi {
    val retrofitService: NetworkApiService by lazy {
        retrofit.create(NetworkApiService::class.java)
    }
}