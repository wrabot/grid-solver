package com.wrabot.solver.ui

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wrabot.solver.R
import com.wrabot.solver.ui.theme.GridSolverTheme
import com.wrabot.tools.compose.BackStack
import com.wrabot.tools.compose.CrossSlide

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFlow() = GridSolverTheme {
    Surface(Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val backStack by remember { mutableStateOf(BackStack<State>(State.SelectImage)) }
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                navigationIcon = {
                    if (backStack.hasBack()) {
                        IconButton(onClick = { backStack.back() }) { Icon(Icons.Filled.ArrowBack, null) }
                    }
                }
            )
            BackHandler(backStack.hasBack()) { backStack.back() }
            CrossSlide(backStack.current) { state ->
                when (state) {
                    State.SelectImage -> SelectImageScreen { backStack.next(State.Recognition(it)) }
                    is State.Recognition -> RecognitionScreen(state.bitmap) { backStack.current = State.Solve(it) }
                    is State.Solve -> SolveScreen(state.grid)
                }
            }
        }
    }
}
