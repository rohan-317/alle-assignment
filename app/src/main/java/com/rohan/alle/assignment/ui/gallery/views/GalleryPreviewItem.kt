package com.rohan.alle.assignment.ui.gallery.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Dimension
import com.rohan.alle.assignment.ui.gallery.GalleryViewModel
import com.rohan.alle.assignment.utils.normalizedItemPosition
import java.io.File
import kotlin.math.absoluteValue

/***
 * Created by Rohan
 * Gallery Preview Item
 */
@Composable
internal fun GalleryPreviewItem(
    filePath: String,
    state: LazyListState,
    galleryViewModel: GalleryViewModel,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {

    LaunchedEffect(isSelected) {
        if (isSelected) {
            galleryViewModel.setSelectedFile(filePath)
        }
    }

    Box(modifier = Modifier
        .padding(5.dp)
        .width(40.dp)
        .height(70.dp)
        .graphicsLayer {
            val x = 1 - (state.layoutInfo.normalizedItemPosition(filePath).absoluteValue * 0.15F)
            val y = 1 - (state.layoutInfo.normalizedItemPosition(filePath).absoluteValue * 0.35F)
            val alphaValue = 1 - (state.layoutInfo.normalizedItemPosition(filePath).absoluteValue * 0.55F)
            alpha = alphaValue
            scaleX = x
            scaleY = y
        }
        .clickable {
            onClick()
        }
    ) {
        
        //Image
        val model = ImageRequest.Builder(LocalContext.current)
            .data(File(filePath))
            .size(Dimension(40), Dimension(70))
            .build()

        AsyncImage(
            model = model,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}

@Preview
@Composable
fun PreviewGalleryPreviewItem() {
//    GalleryPreviewItem()
}

