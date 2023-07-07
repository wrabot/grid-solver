package com.wrabot.solver.ui

import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.wrabot.solver.R

@Composable
fun SelectImageScreen(onSelect: (Bitmap) -> Unit) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        val contentResolver = LocalContext.current.contentResolver
        val selectImage = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                @Suppress("DEPRECATION")
                onSelect(MediaStore.Images.Media.getBitmap(contentResolver, uri))
            }
        }
        Button(onClick = { selectImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
            Text(stringResource(R.string.select_image))
        }
    }
}
