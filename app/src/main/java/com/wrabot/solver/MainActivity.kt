package com.wrabot.solver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.wrabot.solver.data.Recents
import com.wrabot.solver.ui.MainFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Recents.init(this)
        setContent { MainFlow() }
    }
}
