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
import androidx.compose.ui.unit.dp
import com.wrabot.solver.R
import com.wrabot.solver.ui.theme.GridStyle
import grids.Grid

@Composable
fun SolveScreen(grid: Grid) {
    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        var text by remember { mutableStateOf(grid.toString()) }
        Text(text, Modifier.weight(1f), style = GridStyle)
        Button(onClick = {
            grid.solve()
            text = grid.toString()
        }) {
            Text(stringResource(R.string.solve))
        }
    }
}
