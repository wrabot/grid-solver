package com.wrabot.solver.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class SelectImageViewModel(application: Application) : AndroidViewModel(application) {
    private var recentsDirectory = application.getFileStreamPath("recents").apply { mkdir() }

    var recents by mutableStateOf(recentsDirectory.listFiles().orEmpty().toList())
        private set

    fun addRecent(bitmap: Bitmap) {
        val id = recents.lastOrNull()?.name?.toIntOrNull()?.inc() ?: 0
        val file = File(recentsDirectory, id.toString().padStart(8, '0'))
        viewModelScope.launch(Dispatchers.IO) {
            file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        }
        recents = recents + file
    }

    fun removeRecent(file: File) {
        recents = recents - file
        file.delete()
    }
}
