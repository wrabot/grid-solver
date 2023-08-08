package com.wrabot.solver.ui

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wrabot.solver.R
import com.wrabot.solver.data.Recents
import com.wrabot.solver.game.Game
import kotlinx.coroutines.flow.map

@Preview
@Composable
fun SelectImageScreenPreview() = SelectImageScreen({}) {}

@Composable
fun SelectImageScreen(onSelectGame: (Game) -> Unit, onSelectImage: (Bitmap) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        val contentResolver = LocalContext.current.contentResolver
        val selectImage = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                onSelectImage(contentResolver.getBitmap(uri))
            }
        }
        val items by remember { Recents.ids.map { it.reversed() } }.collectAsState(emptyList())
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items.size, { items[it] }) { index ->
                val game by Recents.get(items[index]).collectAsState()
                game?.also {
                    @OptIn(ExperimentalFoundationApi::class)
                    Text(
                        text = it.toString(),
                        modifier = Modifier.combinedClickable(
                            onClick = { onSelectGame(it) },
                            onLongClick = { Recents.remove(it) }
                        ),
                        fontSize = 14.sp,
                        fontFamily = fontFamily
                    )
                } ?: CircularProgressIndicator()
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
