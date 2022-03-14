package com.example.testbalinasoft.repository

import com.example.testbalinasoft.data.*
import com.example.testbalinasoft.network.NetworkApi
import java.lang.Exception
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val myDao: MyDao
) {
    private lateinit var token: String

    suspend fun postLogin(loginRequest: LoginRequest, isRegistered: Boolean): Int {
        try {
            val response = if (isRegistered) {
                NetworkApi.retrofitService.singIn(loginRequest)
            } else {
                NetworkApi.retrofitService.singUp(loginRequest)
            }
            val users = myDao.getUsers()
            val user = response.user
            token = user.token
            if (!users.isNullOrEmpty()) {
                if (users[0].userId != user.userId) {
                    myDao.clearUsers()
                    myDao.clearImages()
                }
            }
            myDao.insertUser(user)
            return response.status
        } catch (e: Exception) {
            return e.message!!.filter { it.isDigit() }.toInt()
        }
    }

    suspend fun getImages(): Int {
        val response = NetworkApi.retrofitService.getImages(token, 0)

        try {
            val images = response.images
            for (image in images) {
                myDao.insertImage(image)
            }
            return response.status
        } catch (e: Exception) {
            return e.message!!.filter { it.isDigit() }.toInt()
        }
    }

    suspend fun deleteImage(image: Image): Int {
        val response = NetworkApi.retrofitService.deleteImage(token, image.id)
        try {
            myDao.deleteImage(image)
            return response.status
        } catch (e: Exception) {
            return e.message!!.filter { it.isDigit() }.toInt()
        }
    }

    suspend fun getComments(imageId: Int): Int {
        val response = NetworkApi.retrofitService.getComments(token, imageId, 0)

        try {
            val comments = response.comments
            for (comment in comments) {
                val commentToRoom = Comment(comment.id, comment.date, comment.text, imageId)
                myDao.insertComment(commentToRoom)
            }
            return response.status
        } catch (e: Exception) {
            return e.message!!.filter { it.isDigit() }.toInt()
        }
    }

    suspend fun postComment(imageId: Int, text: String): Int {
        val response = NetworkApi.retrofitService.postComment(token, imageId, text)

        try {
            val comment = response.comment
            val commentToRoom = Comment(comment.id, comment.date, comment.text, imageId)
            myDao.insertComment(commentToRoom)
            return response.status
        } catch (e: Exception) {
            return e.message!!.filter { it.isDigit() }.toInt()
        }
    }

    suspend fun deleteComment(comment: Comment): Int {
        val response =
            NetworkApi.retrofitService.deleteComment(token, comment.imageId, comment.id)

        try {
            myDao.deleteComment(comment)
            return response.status
        } catch (e: Exception) {
            return e.message!!.filter { it.isDigit() }.toInt()
        }
    }

    suspend fun postImage(imageRequest: ImageRequest): Int {
        val response = NetworkApi.retrofitService.postImage(token, imageRequest)

        try {
            val image = response.image
            myDao.insertImage(image)
            return response.status
        } catch (e: Exception) {
            return e.message!!.filter { it.isDigit() }.toInt()
        }
    }
}