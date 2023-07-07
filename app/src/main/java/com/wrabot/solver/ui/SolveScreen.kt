package com.wrabot.solver.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wrabot.solver.R
import com.wrabot.solver.ui.theme.GridStyle

@Composable
fun SolveScreen(grid: Grid, viewModel: SolveViewModel = viewModel()) {
    LaunchedEffect(grid) { viewModel.grid = grid }
    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(viewModel.grid?.toString().orEmpty(), Modifier.weight(1f), style = GridStyle)
        Button(onClick = { viewModel.solve() }) {
            Text(stringResource(R.string.solve))
        }
    }
}
