package com.rohan.alle.assignment.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/***
 * Created by Rohan
 * Utility functions for the app
 */
object Utils {
    fun openAppSettings(context: Context) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
        context.startActivity(intent)
    }
}
