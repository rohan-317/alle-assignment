package com.rohan.alle.assignment.ui.gallery.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rohan.alle.assignment.R
import com.rohan.alle.assignment.data.model.state.DataState
import com.rohan.alle.assignment.data.model.state.ErrorType
import com.rohan.alle.assignment.utils.Utils
import com.rohan.alle.assignment.utils.findActivity

/***
 * Created by Rohan
 * Error view for GalleryScreen
 */
@Composable
internal fun HandleScreenshotsErrorView(dataState: DataState.Error, onGivePermissionClick : () -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = dataState.message,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            textAlign = TextAlign.Center
        )

        if (dataState.type == ErrorType.PERMISSION_ERROR || dataState.type == ErrorType.PERMISSION_DISABLED_ERROR) {
            Button(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    onGivePermissionClick()
                }
            ) {
                Text(text = stringResource(id = R.string.give_permission))
            }
        }
    }
}