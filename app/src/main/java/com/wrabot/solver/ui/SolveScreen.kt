package com.wrabot.solver.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wrabot.solver.R
import com.wrabot.solver.games.Game

@Composable
fun SolveScreen(game: Game) {
    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        var text by remember { mutableStateOf(game.toString()) }
        Text(
            text, Modifier.weight(1f), style = TextStyle.Default.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 30.sp,
                letterSpacing = 10.sp,
            )
        )
        Button(onClick = {
            game.solve()
            text = game.toString()
        }) {
            Text(stringResource(R.string.solve))
        }
    }
}
