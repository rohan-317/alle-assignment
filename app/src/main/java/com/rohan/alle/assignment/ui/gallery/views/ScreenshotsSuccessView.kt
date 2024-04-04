package com.rohan.alle.assignment.ui.gallery.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.rohan.alle.assignment.data.model.state.DataState
import com.rohan.alle.assignment.ui.gallery.GalleryViewModel
import com.rohan.alle.assignment.utils.animateScrollAndCentralizeItem
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/***
 * Created by Rohan
 * Success view for GalleryScreen
 */
@Composable
internal fun ScreenshotsSuccessView(
    galleryViewModel: GalleryViewModel,
    dataState: DataState.Success<List<String>>
) {
    val state = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val model = ImageRequest.Builder(LocalContext.current)
            .data(galleryViewModel.selectedFile.observeAsState().value?.let { File(it) })
            .size(Size.ORIGINAL)
            .build()

        AsyncImage(
            model = model,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.None
        )

        BoxWithConstraints(
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                state = state
            ) {
                itemsIndexed(dataState.data, key = { index, item ->
                    return@itemsIndexed item
                }) { index, item ->

                    val isSelected by remember {
                        getIsSelected(state, index)
                    }

                    val coroutineScope = rememberCoroutineScope()

                    Layout(
                        content = {
                            GalleryPreviewItem(
                                filePath = item,
                                state = state,
                                galleryViewModel = galleryViewModel,
                                isSelected = isSelected,
                                onClick = {
                                    coroutineScope.launch {
                                        state.animateScrollAndCentralizeItem(index)
                                    }
                                }
                            )
                        },
                        measurePolicy = { measurables, constraints ->
                            measureResult(
                                measurables,
                                constraints,
                                maxWidth.roundToPx(),
                                index,
                                dataState.data.lastIndex,
                                this
                            )
                        }
                    )
                }
            }
        }

    }
}

private fun measureResult(
    measurable: List<Measurable>,
    constraints: Constraints,
    maxWidthInPx: Int,
    index: Int,
    lastIndex: Int,
    measureScope: MeasureScope
): MeasureResult {
    // I'm assuming you'll declaring just one root
    // composable in the content function above
    // so it's measuring just the Box
    val placeable = measurable.first().measure(constraints)
    // Box width
    val itemWidth = placeable.width
    // Calculating the space for the first and last item
    val startSpace =
        if (index == 0) (maxWidthInPx - itemWidth) / 2 else 0
    // Calculating the space for the last item
    val endSpace =
        if (index == lastIndex) (maxWidthInPx - itemWidth) / 2 else 0
    // The width of the box + extra space
    val width = startSpace + placeable.width + endSpace
    return measureScope.layout(width, placeable.height) {
        // Placing the Box in the right X position
        val x = if (index == 0) startSpace else 0
        placeable.place(x, 0)
    }
}


private fun getIsSelected(
    state: LazyListState,
    index: Int
) = derivedStateOf {
    val layoutInfo = state.layoutInfo
    val visibleItemsInfo = layoutInfo.visibleItemsInfo
    val itemInfo = visibleItemsInfo.firstOrNull { it.index == index }

    itemInfo?.let {
        val delta = it.size / 2 //use your custom logic
        val center = state.layoutInfo.viewportEndOffset / 2
        val childCenter = it.offset + it.size / 2
        val target = childCenter - center
        if (target in -delta..delta) return@derivedStateOf true
    }
    false
}