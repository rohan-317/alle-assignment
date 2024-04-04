package com.rohan.alle.assignment.data.repository

/***
 * Created by Rohan
 * Repository for Screenshots
 */
interface ScreenshotWorkerRepository {

    suspend fun getScreenshots() : List<String>

}