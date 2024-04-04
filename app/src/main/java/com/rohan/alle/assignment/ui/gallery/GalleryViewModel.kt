package com.rohan.alle.assignment.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohan.alle.assignment.data.model.state.DataState
import com.rohan.alle.assignment.data.model.state.ErrorType
import com.rohan.alle.assignment.data.repository.ScreenshotWorkerRepository
import com.rohan.alle.assignment.utils.Constants
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.lang.Exception

/***
 * Created by Rohan
 * ViewModel for GalleryScreen
 */
class GalleryViewModel(private val repository: ScreenshotWorkerRepository) : ViewModel() {

    private val _screenshots = MutableLiveData<DataState<List<String>>>()
    val screenshots : LiveData<DataState<List<String>>>
        get() = _screenshots

    private val _selectedFile = MutableLiveData<String>()
    val selectedFile : LiveData<String>
        get() = _selectedFile

    private var screenshotsJob : Deferred<List<String>>? = null

    fun cancel() {
        screenshotsJob?.cancel()
    }

    fun setSelectedFile(file: String) {
        _selectedFile.postValue(file)
    }

    fun getScreenshots() {
        viewModelScope.launch {
            try {
                _screenshots.postValue(DataState.Loading)
                val result = getAllScreenshotFilePath()
                if(result.isEmpty()) {
                    _screenshots.postValue(DataState.Error(Constants.NO_SCREENSHOTS_FOUND))
                } else {
                    _screenshots.postValue(DataState.Success(result))
                }
            } catch (e: Exception) {
                _screenshots.postValue(DataState.Error(e.message ?: Constants.SOMETHING_WENT_WRONG))
            }
        }
    }

    private suspend fun getAllScreenshotFilePath() : List<String> {
        runBlocking {
            if(screenshotsJob!= null) {
                screenshotsJob?.cancel()
            }

            screenshotsJob = async {
                repository.getScreenshots()
            }
        }

        return screenshotsJob!!.await()
    }

    fun wasPermissionDenied() : Boolean {
        return _screenshots.value is DataState.Error && (_screenshots.value as DataState.Error).type == ErrorType.PERMISSION_DISABLED_ERROR
    }

    fun setPermissionError(error: String, permissionError: ErrorType = ErrorType.PERMISSION_ERROR) {
        _screenshots.postValue(DataState.Error(error, permissionError))
    }

}