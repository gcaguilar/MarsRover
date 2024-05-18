package com.gcaguilar.marsrover.presentation.selector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gcaguilar.marsrover.R
import com.gcaguilar.marsrover.presentation.file.FILE_MISSION
import com.gcaguilar.marsrover.presentation.manual.MANUAL_MISSION

const val MISSION_SELECTOR = "Mission selector"

@Composable
fun MissionSelectorScreen(
    navHostController: NavHostController,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.choose_mission_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Button(
            onClick = {
                navHostController.navigate(FILE_MISSION)
            },
            content = {
                Text(stringResource(R.string.form_file))
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navHostController.navigate(MANUAL_MISSION)
            },
            content = {
                Text(stringResource(R.string.manual_input))
            }
        )
    }
}