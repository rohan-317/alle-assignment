package com.rohan.alle.assignment.data.repositoryImpl

import android.os.Environment
import androidx.compose.ui.util.fastMap
import com.rohan.alle.assignment.data.repository.ScreenshotWorkerRepository
import com.rohan.alle.assignment.utils.Constants
import java.io.File

/***
 * Created by Rohan
 * Repository implementation for ScreenshotWorkerRepository
 */
class ScreenshotWorkerRepositoryImpl : ScreenshotWorkerRepository {

    override suspend fun getScreenshots() : List<String> {
        val pictures =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val screenshots = File(pictures, Constants.FOLDER_SCREENSHOTS)

        if (!screenshots.exists()) {
            screenshots.mkdirs()
        }

        return (screenshots.listFiles()?.filter {
            it.extension == Constants.PNG || it.extension == Constants.JPG || it.extension == Constants.JPEG || it.extension == Constants.WEBP
        } ?: emptyList()).fastMap { it.absolutePath }
    }

}