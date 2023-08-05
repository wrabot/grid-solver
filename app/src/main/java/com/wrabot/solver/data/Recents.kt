package com.wrabot.solver.data

import android.content.Context
import com.wrabot.solver.game.Game
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import java.io.File

object Recents {
    private val mutableRecents = MutableStateFlow(emptyList<String>())
    val ids: Flow<List<String>> = mutableRecents

    fun init(context: Context) {
        recentsDirectory = context.getFileStreamPath("recents").apply { mkdir() }
        val files = recentsDirectory.listFiles().orEmpty().toList()
        mutableRecents.value = files.sortedBy { it.lastModified() }.map { it.name }
    }

    fun get(id: String) = cache.getOrPut(id) { load(id) }

    fun add(game: Game) {
        save(game)
        mutableRecents.value = mutableRecents.value.filter { it != game.id } + game.id
    }

    fun remove(game: Game) {
        mutableRecents.value = mutableRecents.value - game.id
        File(recentsDirectory, game.id).delete()
    }

    private lateinit var recentsDirectory: File
    private val cache = mutableMapOf<String, StateFlow<Game?>>()

    @OptIn(ExperimentalSerializationApi::class)
    private val cbor = Cbor {}

    @OptIn(ExperimentalSerializationApi::class)
    private fun load(id: String) = MutableStateFlow<Game?>(null).apply {
        CoroutineScope(Dispatchers.IO).launch {
            value = File(recentsDirectory, id).inputStream().use { cbor.decodeFromByteArray(it.readBytes()) }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun save(game: Game) {
        CoroutineScope(Dispatchers.IO).launch {
            File(recentsDirectory, game.id).outputStream().use { it.write(cbor.encodeToByteArray(game)) }
        }
    }
}
