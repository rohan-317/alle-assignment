package com.rohan.alle.assignment.ui.gallery

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.rohan.alle.assignment.data.model.state.DataState
import com.rohan.alle.assignment.data.model.state.ErrorType
import com.rohan.alle.assignment.ui.common.RepeatOnLifecycleEffect
import com.rohan.alle.assignment.ui.gallery.views.HandleScreenshotsErrorView
import com.rohan.alle.assignment.ui.gallery.views.LoaderView
import com.rohan.alle.assignment.ui.gallery.views.ScreenshotsSuccessView
import com.rohan.alle.assignment.utils.Constants
import com.rohan.alle.assignment.utils.Utils
import com.rohan.alle.assignment.utils.findActivity
import org.koin.androidx.compose.koinViewModel

/***
 * Created by Rohan
 * Gallery Screen
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GalleryScreen(
    modifier : Modifier = Modifier,
    galleryViewModel: GalleryViewModel = koinViewModel()) {

    val readPermissionState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(READ_MEDIA_IMAGES)
        } else {
            listOf(READ_EXTERNAL_STORAGE)
        }
    )

    val context = LocalContext.current.findActivity()

    val requestReadPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted
            galleryViewModel.getScreenshots()
        } else {
            // Handle permission denial
            galleryViewModel.setPermissionError(Constants.READ_PERMISSION_MUST_ERROR_MSG, ErrorType.PERMISSION_DISABLED_ERROR)
        }
    }

    LaunchedEffect(readPermissionState) {
        if (!readPermissionState.permissions[0].status.isGranted && readPermissionState.permissions[0].status.shouldShowRationale) {
            // Show rationale if needed
            galleryViewModel.setPermissionError(Constants.READ_PERMISSION_ERROR_MSG)
        } else {
            requestReadImagePermission(requestReadPermissionLauncher)
        }
    }

    RepeatOnLifecycleEffect(action = {
        if(galleryViewModel.wasPermissionDenied() && !readPermissionState.permissions[0].status.shouldShowRationale) {
            requestReadImagePermission(requestReadPermissionLauncher)
        }
    })

    galleryViewModel.screenshots.observeAsState().value?.let { dataState ->
        when (dataState) {
            is DataState.Loading -> {
                LoaderView()
            }

            is DataState.Success -> {
                ScreenshotsSuccessView(galleryViewModel, dataState)
            }

            is DataState.Error -> {
                HandleScreenshotsErrorView(dataState) {
                    when (dataState.type) {
                        ErrorType.PERMISSION_ERROR -> {
                            requestReadImagePermission(requestReadPermissionLauncher)
                        }

                        ErrorType.PERMISSION_DISABLED_ERROR -> {
                            Utils.openAppSettings(context)
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}

private fun requestReadImagePermission(requestReadPermissionLauncher: ManagedActivityResultLauncher<String, Boolean>) {
    requestReadPermissionLauncher.launch(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            READ_MEDIA_IMAGES
        } else {
            READ_EXTERNAL_STORAGE
        }
    )
}

@Preview
@Composable
fun PreviewGalleryScreen() {
    GalleryScreen()
}