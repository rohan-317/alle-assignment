package com.rohan.alle.assignment.data.model.state

/***
 * Created by Rohan
 * A simple [DataState] sealed class for handling data state.
 */
sealed class DataState<out T> {

    data class Success<out T>(val data: T) : DataState<T>()

    class Error(val message: String, val type: ErrorType = ErrorType.GENERAL) : DataState<Nothing>()

    object Loading: DataState<Nothing>()

}

enum class ErrorType {
    PERMISSION_DISABLED_ERROR,
    PERMISSION_ERROR,
    GENERAL
}