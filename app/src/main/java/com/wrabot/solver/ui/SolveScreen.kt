package com.wrabot.solver.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FirstPage
import androidx.compose.material.icons.filled.LastPage
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wrabot.solver.R
import com.wrabot.solver.game.Game
import com.wrabot.solver.game.Takuzu
import com.wrabot.solver.grid.Grid
import com.wrabot.solver.grid.GridStack

@Preview(showBackground = true)
@Composable
fun SolveScreenPreview() = SolveScreen(Takuzu(GridStack(Grid(10, 10, List(100) {
    '0' + (Math.random() * 2).toInt()
}))))

@Composable
fun SolveScreen(game: Game) {
    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        var text by remember { mutableStateOf(game.toString()) }
        Text(
            text, Modifier.weight(1f), style = TextStyle.Default.copy(
                fontFamily = FontFamily(listOf(Font(R.font.source_code_pro_regular))),
                fontSize = 20.sp,
            )
        )
        with(game.stack) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                IconButton(onClick = { undoAll() }, enabled = hasUndo()) {
                    Icon(Icons.Default.FirstPage, stringResource(R.string.stack_start))
                }
                IconButton(onClick = { undo() }, enabled = hasUndo()) {
                    Icon(Icons.Default.NavigateBefore, stringResource(R.string.stack_previous))
                }
                IconButton(onClick = { redo() }, enabled = hasRedo()) {
                    Icon(Icons.Default.NavigateNext, stringResource(R.string.stack_next))
                }
                IconButton(onClick = { redoAll() }, enabled = hasRedo()) {
                    Icon(Icons.Default.LastPage, stringResource(R.string.stack_end))
                }
            }
        }
        Spacer(Modifier.size(16.dp))
        Button(onClick = {
            game.solve()
            text = game.toString()
        }) {
            Text(stringResource(R.string.solve))
        }
    }
}
