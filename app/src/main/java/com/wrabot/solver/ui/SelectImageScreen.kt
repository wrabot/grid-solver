package com.wrabot.solver.ui

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.wrabot.solver.R

@Preview
@Composable
fun SelectImageScreenPreview() = SelectImageScreen {}

@Composable
fun SelectImageScreen(viewModel: SelectImageViewModel = viewModel(), onSelect: (Bitmap) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        val contentResolver = LocalContext.current.contentResolver
        val selectImage = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val bitmap = contentResolver.getBitmap(uri)
                viewModel.addRecent(bitmap)
                onSelect(bitmap)
            }
        }
        LazyVerticalGrid(GridCells.Fixed(2), Modifier.fillMaxWidth().weight(1f)) {
            items(viewModel.recents.size) { index ->
                val file = viewModel.recents.run { get(lastIndex - index) }
                @OptIn(ExperimentalFoundationApi::class)
                AsyncImage(file, null, Modifier.padding(8.dp).combinedClickable(
                    onClick = { onSelect(file.inputStream().use { BitmapFactory.decodeStream(it) }) },
                    onLongClick = { viewModel.removeRecent(file) }
                ))
            }
        }
        Button(
            modifier = Modifier.padding(16.dp).align(CenterHorizontally),
            onClick = { selectImage.launch(imageRequest) }
        ) {
            Text(stringResource(R.string.select_image))
        }
    }
}

private val imageRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)

@Suppress("DEPRECATION")
private fun ContentResolver.getBitmap(uri: Uri) = MediaStore.Images.Media.getBitmap(this, uri)
